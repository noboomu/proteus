/**
 * 
 */
package com.wurrly.modules;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.wurrly.BaseServer.BaseHandlers;

import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.RoutingHandler;
import io.undertow.util.Methods;

/**
 * @author jbauer
 *
 */
@Singleton
public class RoutingModule extends AbstractModule
{

 
	/* (non-Javadoc)
	 * @see com.google.inject.AbstractModule#configure()
	 */
	@Override
	protected void configure()
	{
		RoutingHandler router = new RoutingHandler().setFallbackHandler(BaseHandlers::notFoundHandler);
		
		
	 
		
		System.out.println("router: " + router.hashCode());
		
		this.binder().bind(RoutingHandler.class).toInstance(router); 
	}

}
