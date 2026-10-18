package io.sinistral.proteus.server.http1;

import io.undertow.UndertowMessages;
import io.undertow.connector.ByteBufferPool;
import io.undertow.connector.PooledByteBuffer;
import io.undertow.conduits.ReadDataStreamSourceConduit;
import io.undertow.server.AbstractServerConnection;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.HttpUpgradeListener;
import io.undertow.server.SSLSessionInfo;
import io.undertow.server.protocol.http.PipeliningBufferingStreamSinkConduit;
import io.undertow.util.Headers;
import io.undertow.util.Methods;
import java.io.IOException;
import org.xnio.ChannelListener;
import org.xnio.IoUtils;
import org.xnio.OptionMap;
import org.xnio.StreamConnection;
import org.xnio.channels.StreamSinkChannel;
import org.xnio.conduits.AbstractStreamSinkConduit;
import org.xnio.conduits.StreamSinkConduit;

/**
 * Plain-TCP HTTP/1.x connection owned by the Proteus front end.
 *
 * <p>One {@link Http1ReadListener} parses requests; one {@link Http1ResponseConduit} is reused
 * across keep-alive responses. Request and response bodies use Undertow's fixed-length and
 * chunked conduits so handlers see the standard {@link HttpServerExchange} contract.
 *
 * <p>When a request arrives with further pipelined requests already buffered, and
 * {@code UndertowOptions.BUFFER_PIPELINED_DATA} is set, responses are written through Undertow's
 * {@link PipeliningBufferingStreamSinkConduit} so a pipelined batch leaves in one write. The
 * buffer is flushed whenever the connection would otherwise wait for input.
 */
public final class Http1ServerConnection extends AbstractServerConnection {

    private Http1ResponseConduit responseConduit;
    private Http1FixedLengthSinkConduit fixedLengthSinkConduit;
    private PipeliningBufferingStreamSinkConduit pipelineBuffer;
    private final boolean bufferPipelinedData;
    private final ReadDataStreamSourceConduit readDataSourceConduit;
    private Http1ReadListener readListener;
    private HttpUpgradeListener upgradeListener;
    private boolean connectHandled;

    /**
     * Creates a connection over an accepted channel.
     *
     * @param channel the accepted stream connection
     * @param bufferPool the application buffer pool
     * @param rootHandler the root handler
     * @param undertowOptions server options
     * @param bufferSize pooled buffer size
     */
    public Http1ServerConnection(
        StreamConnection channel,
        ByteBufferPool bufferPool,
        HttpHandler rootHandler,
        OptionMap undertowOptions,
        int bufferSize
    ) {
        super(channel, bufferPool, rootHandler, undertowOptions, bufferSize);
        this.responseConduit = new Http1ResponseConduit(channel.getSinkChannel().getConduit(), bufferPool, this);
        this.fixedLengthSinkConduit = new Http1FixedLengthSinkConduit(responseConduit);
        this.readDataSourceConduit = new ReadDataStreamSourceConduit(channel.getSourceChannel().getConduit(), this);
        this.bufferPipelinedData = undertowOptions.get(io.undertow.UndertowOptions.BUFFER_PIPELINED_DATA, false);
        addCloseListener(c -> {
            responseConduit.freeBuffers();
            if (pipelineBuffer != null) {
                try {
                    pipelineBuffer.truncateWrites();
                } catch (IOException ignored) {
                    // connection is closed; truncateWrites only returns the pooled buffer
                }
            }
        });
    }

    /**
     * Routes responses through a pipelining buffer from now on. Called before the response of a
     * request that has further pipelined requests behind it; no response is in flight.
     */
    void enablePipelineBuffer() {
        if (pipelineBuffer != null || !bufferPipelinedData) {
            return;
        }
        pipelineBuffer = new PipeliningBufferingStreamSinkConduit(getOriginalSinkConduit(), getByteBufferPool());
        responseConduit.freeBuffers();
        responseConduit = new Http1ResponseConduit(pipelineBuffer, getByteBufferPool(), this);
        fixedLengthSinkConduit = new Http1FixedLengthSinkConduit(responseConduit);
    }

    PipeliningBufferingStreamSinkConduit getPipelineBuffer() {
        return pipelineBuffer;
    }

    /**
     * Flushes buffered pipelined responses, then runs {@code then}. When the socket cannot take
     * everything, a write listener finishes the flush before {@code then} runs.
     */
    void flushPipelineBuffer(Runnable then) {
        try {
            if (pipelineBuffer.flushPipelinedData()) {
                then.run();
                return;
            }
            final StreamSinkChannel sink = channel.getSinkChannel();
            sink.getWriteSetter().set((ChannelListener<StreamSinkChannel>) c -> {
                try {
                    if (pipelineBuffer.flushPipelinedData()) {
                        c.getWriteSetter().set(null);
                        c.suspendWrites();
                        then.run();
                    }
                } catch (IOException e) {
                    io.undertow.UndertowLogger.REQUEST_IO_LOGGER.ioException(e);
                    IoUtils.safeClose(this);
                }
            });
            sink.resumeWrites();
        } catch (IOException e) {
            io.undertow.UndertowLogger.REQUEST_IO_LOGGER.ioException(e);
            IoUtils.safeClose(this);
        }
    }

    void setReadListener(Http1ReadListener readListener) {
        this.readListener = readListener;
    }

    Http1ResponseConduit getResponseConduit() {
        return responseConduit;
    }

    Http1FixedLengthSinkConduit getFixedLengthSinkConduit() {
        return fixedLengthSinkConduit;
    }

    ReadDataStreamSourceConduit getReadDataSourceConduit() {
        return readDataSourceConduit;
    }

    boolean isConnectHandled() {
        return connectHandled;
    }

    /** Pushes back bytes read past the end of the current request for the next pipelined one. */
    public void ungetRequestBytes(PooledByteBuffer unget) {
        if (getExtraBytes() == null) {
            setExtraBytes(unget);
            return;
        }
        PooledByteBuffer eb = getExtraBytes();
        java.nio.ByteBuffer b = eb.getBuffer();
        if (b.remaining() + unget.getBuffer().remaining() <= b.capacity()) {
            b.compact();
            b.put(unget.getBuffer());
            b.flip();
            unget.close();
        } else {
            // Rare: combine into one pooled buffer sized for both.
            PooledByteBuffer merged = getByteBufferPool().allocate();
            java.nio.ByteBuffer m = merged.getBuffer();
            m.put(b).put(unget.getBuffer()).flip();
            eb.close();
            unget.close();
            setExtraBytes(merged);
        }
    }

    @Override
    public HttpServerExchange sendOutOfBandResponse(HttpServerExchange exchange) {
        if (exchange == null || !io.undertow.server.protocol.http.HttpContinue.requiresContinueResponse(exchange)) {
            throw UndertowMessages.MESSAGES.outOfBandResponseOnlyAllowedFor100Continue();
        }
        final ConduitState state = resetChannel();
        HttpServerExchange newExchange = new HttpServerExchange(this);
        newExchange.setProtocol(exchange.getProtocol());
        newExchange.setRequestMethod(exchange.getRequestMethod());
        newExchange.getRequestHeaders().put(Headers.CONNECTION, Headers.KEEP_ALIVE.toString());
        newExchange.getRequestHeaders().put(Headers.CONTENT_LENGTH, 0);
        newExchange.setPersistent(true);
        io.undertow.server.Connectors.terminateRequest(newExchange);
        // Behind buffered pipelined responses the interim response must queue after them and then
        // be flushed, since the client may hold the body until it sees 100 Continue.
        final PipeliningBufferingStreamSinkConduit pb = pipelineBuffer;
        final StreamSinkConduit continueSink = pb == null ? getSinkChannel().getConduit() : new AbstractStreamSinkConduit<>(pb) {
            @Override
            public boolean flush() throws IOException {
                return pb.flushPipelinedData();
            }

            @Override
            public void terminateWrites() {
                // the pipeline buffer outlives the interim response; only the connection shuts it down
            }
        };
        newExchange.addResponseWrapper((factory, ex) -> {
            Http1ResponseConduit continueConduit = new Http1ResponseConduit(continueSink, getByteBufferPool(), this);
            continueConduit.reset(ex);
            ex.addExchangeCompleteListener((e, next) -> {
                continueConduit.freeBuffers();
                next.proceed();
            });
            Http1FixedLengthSinkConduit fixed = new Http1FixedLengthSinkConduit(continueConduit);
            fixed.reset(0, ex);
            return fixed;
        });
        newExchange.addExchangeCompleteListener((e, next) -> {
            restoreChannel(state);
            next.proceed();
        });
        return newExchange;
    }

    @Override
    public boolean isContinueResponseSupported() {
        return true;
    }

    @Override
    public void terminateRequestChannel(HttpServerExchange exchange) {
        if (!exchange.isPersistent()) {
            org.xnio.IoUtils.safeClose(getChannel().getSourceChannel());
        }
    }

    @Override
    public SSLSessionInfo getSslSessionInfo() {
        return null;
    }

    @Override
    public void setSslSessionInfo(SSLSessionInfo sessionInfo) {
        throw new UnsupportedOperationException("TLS is not supported by the Proteus HTTP/1.1 front end");
    }

    @Override
    protected StreamConnection upgradeChannel() {
        clearChannel();
        if (getExtraBytes() != null) {
            channel.getSourceChannel().setConduit(new ReadDataStreamSourceConduit(channel.getSourceChannel().getConduit(), this));
        }
        return channel;
    }

    @Override
    protected StreamSinkConduit getSinkConduit(HttpServerExchange exchange, StreamSinkConduit conduit) {
        if (exchange.getRequestMethod().equals(Methods.CONNECT) && !connectHandled) {
            exchange.setPersistent(false);
            exchange.getResponseHeaders().put(Headers.CONNECTION, "close");
        }
        return Http1TransferEncoding.createSinkConduit(exchange);
    }

    @Override
    protected boolean isUpgradeSupported() {
        return true;
    }

    @Override
    protected boolean isConnectSupported() {
        return false;
    }

    @Override
    protected void exchangeComplete(HttpServerExchange exchange) {
        // Flush once no further pipelined request is buffered: reads resume next and may wait.
        if (pipelineBuffer != null && (getExtraBytes() == null || exchange.isUpgrade() || !exchange.isPersistent())) {
            clearChannel();
            flushPipelineBuffer(() -> readListener.exchangeComplete(exchange));
        } else {
            readListener.exchangeComplete(exchange);
        }
    }

    HttpUpgradeListener getUpgradeListener() {
        return upgradeListener;
    }

    void setCurrentExchange(HttpServerExchange exchange) {
        this.current = exchange;
    }

    @Override
    protected void setUpgradeListener(HttpUpgradeListener upgradeListener) {
        this.upgradeListener = upgradeListener;
    }

    @Override
    protected void setConnectListener(HttpUpgradeListener connectListener) {
        this.upgradeListener = connectListener;
        this.connectHandled = true;
    }

    @Override
    protected void maxEntitySizeUpdated(HttpServerExchange exchange) {
        // Fixed-length request conduits read the limit from the exchange at creation; nothing to update.
    }

    @Override
    public String getTransportProtocol() {
        return "http/1.1";
    }

    @Override
    public boolean isRequestTrailerFieldsSupported() {
        return false;
    }
}
