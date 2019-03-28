/**
 *
 */
package io.sinistral.proteus.server.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.google.inject.Inject;
import io.sinistral.proteus.server.predicates.ServerPredicates;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;
import io.undertow.util.StatusCodes;

/**
 * @author jbauer
 */
public class ServerFallbackHandler implements HttpHandler
{
    @Inject
    protected XmlMapper xmlMapper;
    @Inject
    protected ObjectMapper objectMapper;

    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception
    {
        final int statusCode = 404;

        exchange.setStatusCode(statusCode);

        final String responseBody;
        final String reason = StatusCodes.getReason(statusCode);

        if (ServerPredicates.ACCEPT_JSON_PREDICATE.resolve(exchange)) {
            responseBody = objectMapper.writeValueAsString(new Message(statusCode, reason));

            exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, javax.ws.rs.core.MediaType.APPLICATION_JSON);
        } else if (ServerPredicates.ACCEPT_XML_PREDICATE.resolve(exchange)) {
            responseBody = xmlMapper.writeValueAsString(new Message(statusCode, reason));

            exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, javax.ws.rs.core.MediaType.APPLICATION_XML);
        } else if (ServerPredicates.ACCEPT_HTML_PREDICATE.resolve(exchange)) {
            responseBody = "<html><head><title>Error</title></head><body>" + statusCode + " - " + reason + "</body></html>";

            exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, javax.ws.rs.core.MediaType.TEXT_HTML);
        } else {
            responseBody = statusCode + " - " + reason;

            exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, javax.ws.rs.core.MediaType.TEXT_PLAIN);
        }

        exchange.getResponseHeaders().put(Headers.CONTENT_LENGTH, "" + responseBody.length());
        exchange.getResponseSender().send(responseBody);
    }

    private class Message
    {
        @SuppressWarnings("unused")
        public final Integer statusCode;
        @SuppressWarnings("unused")
        public final String reason;

        /**
         * @param statusCode
         * @param reason
         */
        public Message(Integer statusCode, String reason)
        {
            this.statusCode = statusCode;
            this.reason = reason;
        }
    }
}



