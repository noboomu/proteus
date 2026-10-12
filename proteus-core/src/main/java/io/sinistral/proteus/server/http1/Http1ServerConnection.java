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
import io.undertow.util.Headers;
import io.undertow.util.Methods;
import org.xnio.OptionMap;
import org.xnio.StreamConnection;
import org.xnio.conduits.StreamSinkConduit;

/**
 * Plain-TCP HTTP/1.x connection owned by the Proteus front end.
 *
 * <p>One {@link Http1ReadListener} parses requests; one {@link Http1ResponseConduit} is reused
 * across keep-alive responses. Request and response bodies use Undertow's fixed-length and
 * chunked conduits so handlers see the standard {@link HttpServerExchange} contract.
 */
public final class Http1ServerConnection extends AbstractServerConnection {

    private final Http1ResponseConduit responseConduit;
    private final Http1FixedLengthSinkConduit fixedLengthSinkConduit;
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
        addCloseListener(c -> responseConduit.freeBuffers());
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
        newExchange.addResponseWrapper((factory, ex) -> {
            Http1ResponseConduit continueConduit = new Http1ResponseConduit(getSinkChannel().getConduit(), getByteBufferPool(), this);
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
        readListener.exchangeComplete(exchange);
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
