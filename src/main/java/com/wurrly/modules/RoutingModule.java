/**
 * 
 */
package com.wurrly.modules;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;
import com.google.inject.name.Names;
import com.typesafe.config.Config;
import com.wurrly.Application.BaseHandlers;
import com.wurrly.server.endpoints.EndpointInfo;

import io.undertow.server.DefaultResponseListener;
import io.undertow.server.RoutingHandler;

/**
 * @author jbauer
 *
 */
@Singleton
public class RoutingModule extends AbstractModule  
{
	private static Logger log = LoggerFactory.getLogger(RoutingModule.class.getCanonicalName());

	protected Set<EndpointInfo> registeredEndpoints = new TreeSet<>();
	protected Set<Class<?>> registeredControllers = new HashSet<>();
	
	protected Config config;
	
	public RoutingModule(Config config)
	{
		this.config = config;
	}
	 
 
	@SuppressWarnings("unchecked")
	@Override
	protected void configure()
	{
		RoutingHandler router = new RoutingHandler()
				.setFallbackHandler(BaseHandlers::notFoundHandler);
		
		this.binder().requestInjection(this);
		 		
		this.bind(RoutingHandler.class).toInstance(router); 
		
		this.bind(RoutingModule.class).toInstance(this);
		 
 		
		try
		{
			String defaultResponseListenerClassName = config.getString("application.defaultResponseListener");
			Class<? extends DefaultResponseListener> defaultResponseListenerClass = (Class<? extends DefaultResponseListener>) Class.forName(defaultResponseListenerClassName);
			log.info("defaultResponseListenerClassName: " + defaultResponseListenerClassName);
			this.bind(DefaultResponseListener.class).to(defaultResponseListenerClass).in(Singleton.class); 
		} catch (Exception e)
		{
			log.error(e.getMessage(),e);
		}
 
		this.bind(new TypeLiteral<Set<Class<?>>>() {}).annotatedWith(Names.named("registeredControllers")).toInstance(registeredControllers);
		this.bind(new TypeLiteral<Set<EndpointInfo>>() {}).annotatedWith(Names.named("registeredEndpoints")).toInstance(registeredEndpoints);

	}

	/**
	 * @return the registeredEndpoints
	 */
	public Set<EndpointInfo> getRegisteredEndpoints()
	{
		return registeredEndpoints;
	}

	/**
	 * @return the registeredControllers
	 */
	public Set<Class<?>> getRegisteredControllers()
	{
		return registeredControllers;
 
	}

	

}
