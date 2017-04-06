/**
 * 
 */
package com.wurrly.modules;

import java.util.Set;
import java.util.TreeSet;
 

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.AbstractModule;
import com.google.inject.Scope;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;
import com.google.inject.name.Names;
import com.wurrly.Application.BaseHandlers;
import com.wurrly.server.RouteRecord;

import io.undertow.server.RoutingHandler;

/**
 * @author jbauer
 *
 */
@Singleton
public class RoutingModule extends AbstractModule  
{
	private static Logger log = LoggerFactory.getLogger(RoutingModule.class.getCanonicalName());

	protected Set<RouteRecord> routeRecords = new TreeSet<>();
	 
	@Override
	protected void configure()
	{
		RoutingHandler router = new RoutingHandler().setFallbackHandler(BaseHandlers::notFoundHandler);
		 		
		this.binder().bind(RoutingHandler.class).toInstance(router); 
		
		this.binder().bind(new TypeLiteral<Set<RouteRecord>>() {}).annotatedWith(Names.named("routeRecords")).toInstance(routeRecords);
		
	}

	

}
