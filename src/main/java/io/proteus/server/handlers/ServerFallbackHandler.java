/**
 * 
 */
package io.proteus.server.handlers;

import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;

/**
 * @author jbauer
 *
 */
public class ServerFallbackHandler implements HttpHandler
{ 
	 
		@Override
		public void handleRequest(HttpServerExchange exchange) throws Exception
		{
			  exchange.setStatusCode(404);
		      exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "text/plain");
		      exchange.getResponseSender().send("Page Not Found!!");
			
		} 
}
