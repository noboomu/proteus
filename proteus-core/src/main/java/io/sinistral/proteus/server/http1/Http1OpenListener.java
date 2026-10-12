package io.sinistral.proteus.server.http1;

import io.undertow.UndertowLogger;
import io.undertow.UndertowOptions;
import io.undertow.conduits.IdleTimeoutConduit;
import io.undertow.conduits.ReadTimeoutStreamSourceConduit;
import io.undertow.conduits.WriteTimeoutStreamSinkConduit;
import io.undertow.connector.ByteBufferPool;
import io.undertow.server.ConnectorStatistics;
import io.undertow.server.HttpHandler;
import io.undertow.server.OpenListener;
import io.undertow.server.ServerConnection;
import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.xnio.IoUtils;
import org.xnio.OptionMap;
import org.xnio.Options;
import org.xnio.StreamConnection;

/**
 * Accepts plain-TCP connections and serves HTTP/1.x with the Proteus codec.
 *
 * <p>Per connection: applies read/write/idle timeouts from the channel options, creates an
 * {@link Http1ServerConnection} with its {@link Http1ReadListener}, and resumes reads.
 */
public final class Http1OpenListener implements OpenListener {

    private final ByteBufferPool bufferPool;
    private final int bufferSize;
    private final Set<Http1ServerConnection> connections = ConcurrentHashMap.newKeySet();
    private volatile HttpHandler rootHandler;
    private volatile OptionMap undertowOptions;

    /**
     * Creates a listener.
     *
     * @param bufferPool buffer pool shared with the application
     * @param undertowOptions server options ({@link UndertowOptions})
     */
    public Http1OpenListener(ByteBufferPool bufferPool, OptionMap undertowOptions) {
        this.bufferPool = bufferPool;
        this.bufferSize = bufferPool.getBufferSize();
        this.undertowOptions = undertowOptions;
    }

    @Override
    public void handleEvent(StreamConnection channel) {
        if (UndertowLogger.REQUEST_LOGGER.isTraceEnabled()) {
            UndertowLogger.REQUEST_LOGGER.tracef("Opened connection with %s", channel.getPeerAddress());
        }
        try {
            Integer readTimeout = channel.getOption(Options.READ_TIMEOUT);
            Integer idle = undertowOptions.get(UndertowOptions.IDLE_TIMEOUT);
            if (idle != null) {
                IdleTimeoutConduit conduit = new IdleTimeoutConduit(channel);
                channel.getSourceChannel().setConduit(conduit);
                channel.getSinkChannel().setConduit(conduit);
            }
            if (readTimeout != null && readTimeout > 0) {
                channel.getSourceChannel().setConduit(new ReadTimeoutStreamSourceConduit(channel.getSourceChannel().getConduit(), channel, this));
            }
            Integer writeTimeout = channel.getOption(Options.WRITE_TIMEOUT);
            if (writeTimeout != null && writeTimeout > 0) {
                channel.getSinkChannel().setConduit(new WriteTimeoutStreamSinkConduit(channel.getSinkChannel().getConduit(), channel, this));
            }
        } catch (IOException e) {
            IoUtils.safeClose(channel);
            UndertowLogger.REQUEST_IO_LOGGER.ioException(e);
            return;
        } catch (Throwable t) {
            IoUtils.safeClose(channel);
            UndertowLogger.REQUEST_IO_LOGGER.handleUnexpectedFailure(t);
            return;
        }

        Http1ServerConnection connection = new Http1ServerConnection(channel, bufferPool, rootHandler, undertowOptions, bufferSize);
        Http1ReadListener readListener = new Http1ReadListener(connection);
        connections.add(connection);
        connection.addCloseListener(c -> connections.remove(connection));
        connection.setReadListener(readListener);
        channel.getSourceChannel().setReadListener(readListener);
        readListener.handleEvent(channel.getSourceChannel());
    }

    @Override
    public HttpHandler getRootHandler() {
        return rootHandler;
    }

    @Override
    public void setRootHandler(HttpHandler rootHandler) {
        this.rootHandler = rootHandler;
    }

    @Override
    public OptionMap getUndertowOptions() {
        return undertowOptions;
    }

    @Override
    public void setUndertowOptions(OptionMap undertowOptions) {
        if (undertowOptions == null) {
            throw new IllegalArgumentException("undertowOptions");
        }
        this.undertowOptions = undertowOptions;
    }

    @Override
    public ByteBufferPool getBufferPool() {
        return bufferPool;
    }

    @Override
    public ConnectorStatistics getConnectorStatistics() {
        return null;
    }

    @Override
    public void closeConnections() {
        for (ServerConnection c : connections) {
            IoUtils.safeClose(c);
        }
    }

    /** Number of currently open connections. */
    public int openConnections() {
        return connections.size();
    }
}
