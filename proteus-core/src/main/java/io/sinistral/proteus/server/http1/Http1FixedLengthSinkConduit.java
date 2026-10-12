package io.sinistral.proteus.server.http1;

import io.undertow.conduits.AbstractFixedLengthStreamSinkConduit;
import io.undertow.server.Connectors;
import io.undertow.server.HttpServerExchange;
import org.xnio.conduits.StreamSinkConduit;

/** Per-connection fixed-length response sink, reset for each Content-Length response. */
final class Http1FixedLengthSinkConduit extends AbstractFixedLengthStreamSinkConduit {

    private HttpServerExchange exchange;

    Http1FixedLengthSinkConduit(StreamSinkConduit next) {
        super(next, 1, false, false);
    }

    void reset(long contentLength, HttpServerExchange exchange) {
        this.exchange = exchange;
        super.reset(contentLength, !exchange.isPersistent());
    }

    @Override
    protected void channelFinished() {
        HttpServerExchange e = exchange;
        if (e != null) {
            exchange = null;
            Connectors.terminateResponse(e);
        }
    }
}
