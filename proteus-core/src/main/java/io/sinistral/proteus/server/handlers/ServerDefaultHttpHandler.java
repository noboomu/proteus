
package io.sinistral.proteus.server.handlers;

import com.google.inject.Inject;
import com.typesafe.config.Config;
import io.sinistral.proteus.server.exceptions.ServerException;
import io.undertow.server.DefaultResponseListener;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.RoutingHandler;
import io.undertow.server.handlers.ExceptionHandler;
import io.undertow.util.HeaderMap;
import io.undertow.util.HeaderValues;
import io.undertow.util.Headers;
import io.undertow.util.HttpString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author jbauer
 */
public class ServerDefaultHttpHandler implements HttpHandler
{
    private static final Logger log = LoggerFactory.getLogger(ServerDefaultHttpHandler.class);

    protected final HeaderMap headers = new HeaderMap();

    @Inject(optional = true)
    protected DefaultResponseListener defaultResponseListener;

    @Inject
    protected volatile RoutingHandler next;

    @Inject
    public ServerDefaultHttpHandler(Config config)
    {
        Config globalHeaders = config.getConfig("globalHeaders");
        Map<HttpString, String> globalHeaderParameters = globalHeaders.entrySet().stream().collect(Collectors.toMap(e -> HttpString.tryFromString(e.getKey()), e -> e.getValue().unwrapped() + ""));

        for (Map.Entry<HttpString, String> e : globalHeaderParameters.entrySet()) {
            headers.add(e.getKey(), e.getValue());
        }
    }

    /*
     * (non-Javadoc)
     * @see io.undertow.server.HttpHandler#handleRequest(io.undertow.server.HttpServerExchange)
     */
    @Override
    public void handleRequest(final HttpServerExchange exchange) throws Exception
    {
        if (this.defaultResponseListener != null) {
            exchange.addDefaultResponseListener(defaultResponseListener);
        }

        long fiGlobal = this.headers.fastIterateNonEmpty();

        while (fiGlobal != -1) {
            final HeaderValues headerValues = headers.fiCurrent(fiGlobal);

            exchange.getResponseHeaders().addAll(headerValues.getHeaderName(), headerValues);

            fiGlobal = headers.fiNextNonEmpty(fiGlobal);
        }

        try {

            next.handleRequest(exchange);

        } catch (Exception e) {

            exchange.putAttachment(ExceptionHandler.THROWABLE,e);

            defaultResponseListener.handleDefaultResponse(exchange);
        }
    }
}



