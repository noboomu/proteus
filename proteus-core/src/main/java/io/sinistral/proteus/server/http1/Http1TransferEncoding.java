package io.sinistral.proteus.server.http1;

import io.undertow.UndertowLogger;
import io.undertow.UndertowOptions;
import io.undertow.conduits.ChunkedStreamSinkConduit;
import io.undertow.conduits.ChunkedStreamSourceConduit;
import io.undertow.conduits.ConduitListener;
import io.undertow.conduits.FinishableStreamSinkConduit;
import io.undertow.conduits.FixedLengthStreamSourceConduit;
import io.undertow.conduits.HeadStreamSinkConduit;
import io.undertow.server.Connectors;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.DateUtils;
import io.undertow.util.HeaderMap;
import io.undertow.util.Headers;
import io.undertow.util.HttpString;
import io.undertow.util.Methods;
import io.undertow.util.StatusCodes;
import org.xnio.conduits.ConduitStreamSourceChannel;
import org.xnio.conduits.StreamSinkConduit;
import org.xnio.conduits.StreamSourceConduit;

/**
 * Wires request-body source conduits and response sink conduits for the Proteus front end,
 * with the same framing semantics as Undertow's {@code HttpTransferEncoding}.
 */
final class Http1TransferEncoding {

    private Http1TransferEncoding() {}

    /** Installs the request body conduit and decides persistence from the request headers. */
    static void setupRequest(HttpServerExchange exchange) {
        final HeaderMap h = exchange.getRequestHeaders();
        final String connectionHeader = h.getFirst(Headers.CONNECTION);
        final String transferEncoding = h.getLast(Headers.TRANSFER_ENCODING);
        final String contentLength = h.getFirst(Headers.CONTENT_LENGTH);
        final Http1ServerConnection connection = (Http1ServerConnection) exchange.getConnection();

        ConduitStreamSourceChannel source = connection.getChannel().getSourceChannel();
        source.setConduit(connection.getReadDataSourceConduit());

        boolean persistent = persistentConnection(exchange, connectionHeader);

        if (transferEncoding == null && contentLength == null) {
            Connectors.terminateRequest(exchange);
        } else if (transferEncoding != null && !Headers.IDENTITY.equalToString(transferEncoding)) {
            source.setConduit(new ChunkedStreamSourceConduit(source.getConduit(), exchange, chunkedDrainListener(exchange)));
        } else if (contentLength != null) {
            long len = parsePositiveLong(contentLength);
            if (len == 0L) {
                Connectors.terminateRequest(exchange);
            } else {
                source.setConduit(new FixedLengthStreamSourceConduit(source.getConduit(), len, fixedLengthDrainListener(exchange), exchange));
            }
        } else {
            // identity transfer-encoding without a length: body runs to close
            persistent = false;
        }

        exchange.setPersistent(persistent);

        if (!exchange.isRequestComplete() || connection.getExtraBytes() != null) {
            source.setReadListener(null);
            source.suspendReads();
        }
    }

    private static boolean persistentConnection(HttpServerExchange exchange, String connectionHeader) {
        if (exchange.isHttp11()) {
            return !(connectionHeader != null && Headers.CLOSE.equalToString(connectionHeader));
        }
        return exchange.isHttp10() && connectionHeader != null && Headers.KEEP_ALIVE.equalToString(connectionHeader);
    }

    /** Builds the response sink chain for the exchange on first write. */
    static StreamSinkConduit createSinkConduit(HttpServerExchange exchange) {
        DateUtils.addDateHeaderIfRequired(exchange);

        final boolean head = exchange.getRequestMethod().equals(Methods.HEAD);
        final Http1ServerConnection connection = (Http1ServerConnection) exchange.getConnection();
        final Http1ResponseConduit responseConduit = connection.getResponseConduit();
        responseConduit.reset(exchange);
        StreamSinkConduit channel = responseConduit;

        if (head) {
            channel = new HeadStreamSinkConduit(channel, terminateResponseListener(exchange));
        } else if (!Connectors.isEntityBodyAllowed(exchange)) {
            exchange.getResponseHeaders().remove(Headers.CONTENT_LENGTH);
            exchange.getResponseHeaders().remove(Headers.TRANSFER_ENCODING);
            return new HeadStreamSinkConduit(channel, terminateResponseListener(exchange));
        }

        final HeaderMap rh = exchange.getResponseHeaders();
        final String connectionHeader = rh.getFirst(Headers.CONNECTION);
        if (exchange.getStatusCode() == StatusCodes.EXPECTATION_FAILED) {
            exchange.setPersistent(false);
        }
        if (!exchange.isPersistent()) {
            rh.put(Headers.CONNECTION, Headers.CLOSE.toString());
        } else if (connectionHeader != null) {
            if (HttpString.tryFromString(connectionHeader).equals(Headers.CLOSE)) {
                exchange.setPersistent(false);
            }
        } else if (exchange.getConnection().getUndertowOptions().get(UndertowOptions.ALWAYS_SET_KEEP_ALIVE, true)) {
            rh.put(Headers.CONNECTION, Headers.KEEP_ALIVE.toString());
        }

        final String transferEncoding = rh.getLast(Headers.TRANSFER_ENCODING);
        if (transferEncoding == null) {
            final String contentLength = rh.getFirst(Headers.CONTENT_LENGTH);
            if (contentLength != null) {
                try {
                    long len = parsePositiveLong(contentLength);
                    if (head) {
                        return channel;
                    }
                    Http1FixedLengthSinkConduit fixed = connection.getFixedLengthSinkConduit();
                    fixed.reset(len, exchange);
                    return fixed;
                } catch (NumberFormatException e) {
                    rh.remove(Headers.CONTENT_LENGTH);
                }
            }
        } else {
            rh.remove(Headers.CONTENT_LENGTH);
        }
        return handleResponseConduit(exchange, head, channel, rh, terminateResponseListener(exchange), transferEncoding);
    }

    private static StreamSinkConduit handleResponseConduit(
        HttpServerExchange exchange, boolean head, StreamSinkConduit channel, HeaderMap rh,
        ConduitListener<StreamSinkConduit> finish, String transferEncoding
    ) {
        if (transferEncoding == null) {
            if (exchange.isHttp11() && exchange.isPersistent()) {
                rh.put(Headers.TRANSFER_ENCODING, Headers.CHUNKED.toString());
                return head ? channel
                    : new ChunkedStreamSinkConduit(channel, exchange.getConnection().getByteBufferPool(), true, !exchange.isPersistent(), rh, finish, exchange);
            }
            exchange.setPersistent(false);
            rh.put(Headers.CONNECTION, Headers.CLOSE.toString());
            return head ? channel : new FinishableStreamSinkConduit(channel, finish);
        }
        if (new HttpString(transferEncoding).equals(Headers.CHUNKED)) {
            return head ? channel
                : new ChunkedStreamSinkConduit(channel, exchange.getConnection().getByteBufferPool(), true, !exchange.isPersistent(), rh, finish, exchange);
        }
        if (head) {
            return channel;
        }
        exchange.setPersistent(false);
        rh.put(Headers.CONNECTION, Headers.CLOSE.toString());
        return new FinishableStreamSinkConduit(channel, finish);
    }

    private static ConduitListener<FixedLengthStreamSourceConduit> fixedLengthDrainListener(HttpServerExchange exchange) {
        return c -> {
            if (c.getRemaining() > 0L) {
                UndertowLogger.REQUEST_LOGGER.requestWasNotFullyConsumed();
                exchange.setPersistent(false);
            }
            Connectors.terminateRequest(exchange);
        };
    }

    private static ConduitListener<ChunkedStreamSourceConduit> chunkedDrainListener(HttpServerExchange exchange) {
        return c -> {
            if (!c.isFinished()) {
                UndertowLogger.REQUEST_LOGGER.requestWasNotFullyConsumed();
                exchange.setPersistent(false);
            }
            Connectors.terminateRequest(exchange);
        };
    }

    private static ConduitListener<StreamSinkConduit> terminateResponseListener(HttpServerExchange exchange) {
        return c -> Connectors.terminateResponse(exchange);
    }

    /** Parses a non-negative decimal; throws NumberFormatException on anything else. */
    static long parsePositiveLong(String s) {
        long v = 0;
        int n = s.length();
        if (n == 0) {
            throw new NumberFormatException(s);
        }
        for (int i = 0; i < n; i++) {
            char c = s.charAt(i);
            if (c < '0' || c > '9') {
                throw new NumberFormatException(s);
            }
            v = v * 10 + (c - '0');
            if (v < 0) {
                throw new NumberFormatException(s);
            }
        }
        return v;
    }
}
