/**
 * 
 */
package io.sinistral.proteus.server.handlers;

import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.typesafe.config.Config;

import io.undertow.server.DefaultResponseListener;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.RoutingHandler;
import io.undertow.util.HeaderMap;
import io.undertow.util.HeaderValues;
import io.undertow.util.HttpString;

/**
 * @author jbauer
 */
public class DefaultHttpHandler implements HttpHandler
{
	private static Logger log = LoggerFactory.getLogger(DefaultHttpHandler.class.getCanonicalName());

	@Inject(optional=true)
	protected DefaultResponseListener defaultResponseListener;
	
	@Inject
	protected volatile RoutingHandler next;
	
	protected final HeaderMap headers = new HeaderMap();

 
	@Inject
	public DefaultHttpHandler(Config config)
	{
		Config globalHeaders = config.getConfig("globalHeaders");

		Map<HttpString,String> globalHeaderParameters = globalHeaders.entrySet().stream().collect(Collectors.toMap(e -> HttpString.tryFromString(e.getKey()), e ->e.getValue().unwrapped()+""));
		   
		for( Map.Entry<HttpString,String> e : globalHeaderParameters.entrySet()  )
		{
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

 		if(this.defaultResponseListener != null)
		{
			exchange.addDefaultResponseListener(defaultResponseListener);
		}

		long fiGlobal = this.headers.fastIterateNonEmpty();
        while (fiGlobal != -1) {
      	  
      	  final HeaderValues headerValues = headers.fiCurrent(fiGlobal);
            exchange.getResponseHeaders().addAll(headerValues.getHeaderName(), headerValues);
            fiGlobal = headers.fiNextNonEmpty(fiGlobal);
        }
        
		next.handleRequest(exchange); 

	}

}
