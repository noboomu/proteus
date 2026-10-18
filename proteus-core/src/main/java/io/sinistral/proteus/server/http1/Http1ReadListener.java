package io.sinistral.proteus.server.http1;

import io.undertow.UndertowLogger;
import io.undertow.UndertowOptions;
import io.undertow.connector.PooledByteBuffer;
import io.undertow.server.Connectors;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.BadRequestException;
import io.undertow.util.ConnectionUtils;
import io.undertow.util.HeaderValues;
import io.undertow.util.Headers;
import io.undertow.util.Methods;
import io.undertow.util.Protocols;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import org.xnio.ChannelListener;
import org.xnio.ChannelListeners;
import org.xnio.IoUtils;
import org.xnio.StreamConnection;
import org.xnio.channels.StreamSinkChannel;
import org.xnio.conduits.ConduitStreamSourceChannel;

/**
 * Drives one {@link Http1ServerConnection}: reads bytes, parses request heads, hands each
 * {@link HttpServerExchange} to the root handler, and re-arms reads on completion for
 * keep-alive and pipelined requests.
 */
final class Http1ReadListener implements ChannelListener<ConduitStreamSourceChannel>, Runnable {

    private static final byte[] BAD_REQUEST =
        "HTTP/1.1 400 Bad Request\r\nContent-Length: 0\r\nConnection: close\r\n\r\n".getBytes(StandardCharsets.US_ASCII);

    private static final AtomicIntegerFieldUpdater<Http1ReadListener> REQUEST_STATE =
        AtomicIntegerFieldUpdater.newUpdater(Http1ReadListener.class, "requestState");

    private final Http1ServerConnection connection;
    private final Http1Parser parser;
    private final long maxEntitySize;
    private final boolean recordRequestStartTime;

    /** 0 = idle, 1 = request running, 2 = state transition in progress. */
    @SuppressWarnings("unused")
    private volatile int requestState;
    private HttpServerExchange exchange;

    Http1ReadListener(Http1ServerConnection connection) {
        this.connection = connection;
        this.parser = new Http1Parser(connection.getUndertowOptions());
        this.maxEntitySize = connection.getUndertowOptions().get(UndertowOptions.MAX_ENTITY_SIZE, UndertowOptions.DEFAULT_MAX_ENTITY_SIZE);
        this.recordRequestStartTime = connection.getUndertowOptions().get(UndertowOptions.RECORD_REQUEST_START_TIME, false);
    }

    private void newRequest() {
        parser.reset();
        connection.setCurrentExchange(null);
    }

    @Override
    public void run() {
        handleEvent(connection.getChannel().getSourceChannel());
    }

    @Override
    public void handleEvent(ConduitStreamSourceChannel channel) {
        while (REQUEST_STATE.get(this) != 0) {
            if (REQUEST_STATE.compareAndSet(this, 1, 2)) {
                try {
                    channel.suspendReads();
                } finally {
                    REQUEST_STATE.set(this, 1);
                }
                return;
            }
        }
        handleEventWithNoRunningRequest(channel);
    }

    private void handleEventWithNoRunningRequest(ConduitStreamSourceChannel channel) {
        PooledByteBuffer existing = connection.getExtraBytes();
        if ((existing == null && connection.getOriginalSourceConduit().isReadShutdown()) || connection.getOriginalSinkConduit().isWriteShutdown()) {
            IoUtils.safeClose(connection);
            channel.suspendReads();
            return;
        }
        final PooledByteBuffer pooled;
        try {
            pooled = existing == null ? connection.getByteBufferPool().allocate() : existing;
        } catch (IllegalStateException e) {
            IoUtils.safeClose(connection);
            channel.suspendReads();
            return;
        }
        final ByteBuffer buffer = pooled.getBuffer();
        boolean free = true;
        try {
            do {
                int res;
                if (existing == null) {
                    buffer.clear();
                    try {
                        res = channel.read(buffer);
                    } catch (IOException e) {
                        UndertowLogger.REQUEST_IO_LOGGER.debug("Error reading request", e);
                        IoUtils.safeClose(connection);
                        return;
                    }
                } else {
                    res = buffer.remaining();
                }
                if (res <= 0) {
                    handleFailedRead(channel, res);
                    return;
                }
                if (existing != null) {
                    existing = null;
                    connection.setExtraBytes(null);
                } else {
                    buffer.flip();
                }
                if (exchange == null) {
                    exchange = new HttpServerExchange(connection, maxEntitySize);
                }
                parser.parse(buffer, exchange);
                if (buffer.hasRemaining()) {
                    free = false;
                    connection.setExtraBytes(pooled);
                }
            } while (!parser.isComplete());

            connection.getOriginalSourceConduit().suspendReads();

            final HttpServerExchange ex = this.exchange;
            this.exchange = null;
            ex.setRequestScheme("http");
            REQUEST_STATE.set(this, 1);

            if (recordRequestStartTime) {
                Connectors.setRequestStartTime(ex);
            }

            HeaderValues host = ex.getRequestHeaders().get(Headers.HOST);
            if (host != null && host.size() > 1) {
                sendBadRequestAndClose(connection.getChannel(), "more than one Host header");
                return;
            }
            if (ex.getProtocol() == Protocols.HTTP_1_1 && (host == null || host.getFirst().isEmpty())) {
                sendBadRequestAndClose(connection.getChannel(), "no Host header in HTTP/1.1 request");
                return;
            }
            if (ex.getRequestMethod().equals(Methods.CONNECT)) {
                sendBadRequestAndClose(connection.getChannel(), "CONNECT is not supported");
                return;
            }
            if (!Connectors.areRequestHeadersValid(ex.getRequestHeaders())) {
                sendBadRequestAndClose(connection.getChannel(), "invalid request headers");
                return;
            }

            Http1TransferEncoding.setupRequest(ex);
            connection.setCurrentExchange(ex);
            Connectors.executeRootHandler(connection.getRootHandler(), ex);
        } catch (BadRequestException e) {
            sendBadRequestAndClose(connection.getChannel(), e.getMessage());
        } catch (Throwable t) {
            UndertowLogger.REQUEST_IO_LOGGER.debug("Failed to handle request", t);
            sendBadRequestAndClose(connection.getChannel(), t.toString());
        } finally {
            if (free) {
                pooled.close();
            }
        }
    }

    private void handleFailedRead(ConduitStreamSourceChannel channel, int res) {
        if (res == 0) {
            // About to wait for input: buffered pipelined responses must reach the client first,
            // or a client that withholds the rest of a partial request until it sees them stalls.
            if (connection.getPipelineBuffer() != null) {
                connection.flushPipelineBuffer(() -> {
                    channel.setReadListener(this);
                    channel.resumeReads();
                });
                return;
            }
            channel.setReadListener(this);
            channel.resumeReads();
        } else if (res == -1) {
            IoUtils.safeClose(connection);
        }
    }

    private void sendBadRequestAndClose(StreamConnection channel, String reason) {
        UndertowLogger.REQUEST_IO_LOGGER.debugf("Rejecting request from %s: %s", channel.getPeerAddress(), reason);
        if (exchange != null) {
            exchange = null;
        }
        channel.getSourceChannel().suspendReads();
        final ByteBuffer out = ByteBuffer.wrap(BAD_REQUEST);
        final StreamSinkChannel sink = channel.getSinkChannel();
        try {
            while (out.hasRemaining()) {
                if (sink.write(out) == 0) {
                    sink.getWriteSetter().set(ChannelListeners.flushingChannelListener(c -> IoUtils.safeClose(channel), (c, e) -> IoUtils.safeClose(channel)));
                    sink.resumeWrites();
                    return;
                }
            }
            sink.shutdownWrites();
            if (!sink.flush()) {
                sink.getWriteSetter().set(ChannelListeners.flushingChannelListener(c -> IoUtils.safeClose(channel), (c, e) -> IoUtils.safeClose(channel)));
                sink.resumeWrites();
                return;
            }
        } catch (IOException ignored) {
            // fall through to close
        }
        IoUtils.safeClose(channel);
    }

    /** Called by the connection when the exchange finishes; re-arms reads or closes. */
    void exchangeComplete(HttpServerExchange ex) {
        connection.clearChannel();
        connection.setCurrentExchange(null);
        if (ex.isPersistent() && !ex.isUpgrade()) {
            final StreamConnection channel = connection.getChannel();
            if (connection.getExtraBytes() == null) {
                if (ex.isInIoThread()) {
                    newRequest();
                    channel.getSourceChannel().setReadListener(this);
                    channel.getSourceChannel().resumeReads();
                    REQUEST_STATE.set(this, 0);
                } else {
                    while (true) {
                        if (connection.getOriginalSourceConduit().isReadShutdown() || connection.getOriginalSinkConduit().isWriteShutdown()) {
                            channel.getSourceChannel().suspendReads();
                            channel.getSinkChannel().suspendWrites();
                            IoUtils.safeClose(connection);
                            return;
                        }
                        if (REQUEST_STATE.compareAndSet(this, 1, 2)) {
                            try {
                                newRequest();
                                channel.getSourceChannel().setReadListener(this);
                                channel.getSourceChannel().resumeReads();
                            } finally {
                                REQUEST_STATE.set(this, 0);
                            }
                            break;
                        }
                    }
                }
            } else {
                // pipelined bytes already buffered: parse them on the I/O thread without a read
                if (ex.isInIoThread()) {
                    REQUEST_STATE.set(this, 0);
                    newRequest();
                    channel.getIoThread().execute(this);
                } else {
                    while (true) {
                        if (connection.getOriginalSinkConduit().isWriteShutdown()) {
                            channel.getSourceChannel().suspendReads();
                            channel.getSinkChannel().suspendWrites();
                            IoUtils.safeClose(connection);
                            return;
                        }
                        if (REQUEST_STATE.compareAndSet(this, 1, 2)) {
                            try {
                                newRequest();
                                channel.getSourceChannel().suspendReads();
                            } finally {
                                REQUEST_STATE.set(this, 0);
                            }
                            break;
                        }
                    }
                    Executor executor = ex.getDispatchExecutor();
                    if (executor == null) {
                        executor = ex.getConnection().getWorker();
                    }
                    executor.execute(this);
                }
            }
        } else if (!ex.isPersistent()) {
            if (connection.getExtraBytes() != null) {
                connection.getExtraBytes().close();
                connection.setExtraBytes(null);
            }
            ConnectionUtils.cleanClose(connection.getChannel(), connection);
        } else {
            // upgrade: flush, then hand the raw channel to the upgrade listener
            if (connection.getExtraBytes() != null) {
                connection.getChannel().getSourceChannel().setConduit(
                    new io.undertow.conduits.ReadDataStreamSourceConduit(connection.getChannel().getSourceChannel().getConduit(), connection));
            }
            try {
                if (!connection.getChannel().getSinkChannel().flush()) {
                    connection.getChannel().getSinkChannel().setWriteListener(ChannelListeners.flushingChannelListener(
                        c -> connection.getUpgradeListener().handleUpgrade(connection.getChannel(), ex),
                        (c, e) -> IoUtils.safeClose(connection)));
                    connection.getChannel().getSinkChannel().resumeWrites();
                    return;
                }
                connection.getUpgradeListener().handleUpgrade(connection.getChannel(), ex);
            } catch (IOException e) {
                UndertowLogger.REQUEST_IO_LOGGER.ioException(e);
                IoUtils.safeClose(connection);
            } catch (Throwable t) {
                UndertowLogger.REQUEST_IO_LOGGER.handleUnexpectedFailure(t);
                IoUtils.safeClose(connection);
            }
        }
    }
}
