/**
 * 
 */
package io.proteus.server.handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.undertow.server.DefaultResponseListener;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;

/**
 * @author jbauer
 */
public class BaseHttpHandler implements HttpHandler
{
	private static Logger log = LoggerFactory.getLogger(BaseHttpHandler.class.getCanonicalName());

	protected DefaultResponseListener defaultResponseListener;
	protected HttpHandler rootHandler;

	/**
	 * @param defaultResponseListener
	 * @param rootHandler
	 */
	public BaseHttpHandler(HttpHandler rootHandler,DefaultResponseListener defaultResponseListener)
	{
		this.defaultResponseListener = defaultResponseListener;
		this.rootHandler = rootHandler;
	}

	/*
	 * (non-Javadoc)
	 * @see io.undertow.server.HttpHandler#handleRequest(io.undertow.server.HttpServerExchange)
	 */
	@Override
	public void handleRequest(HttpServerExchange exchange) throws Exception
	{

		//exchange.addDefaultResponseListener(defaultResponseListener);

		try
		{
			rootHandler.handleRequest(exchange);
		} catch (Exception e)
		{
			if (exchange.isResponseChannelAvailable())
			{
				log.error(e.getMessage());
				exchange.endExchange();
			}
		}

	}

}
