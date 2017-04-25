/**
 * 
 */
package io.proteus.modules;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;
import com.google.inject.name.Names;
import com.typesafe.config.Config;

import io.proteus.server.endpoints.EndpointInfo;
import io.undertow.server.DefaultResponseListener;
import io.undertow.server.HttpHandler;
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
 
		
		this.binder().requestInjection(this);
		
 		
		RoutingHandler router = new RoutingHandler();
	 
		try
		{
			String className = config.getString("application.fallbackHandler");
			log.info("Installing FallbackListener " + className);
			Class<? extends HttpHandler> clazz = (Class<? extends HttpHandler>) Class.forName(className);
			router.setFallbackHandler(clazz.newInstance());
		} catch (Exception e)
		{
			log.error(e.getMessage(),e);
		}
		 		
		this.bind(RoutingHandler.class).toInstance(router); 
		
		this.bind(RoutingModule.class).toInstance(this);
		  
		
		try
		{
			String className = config.getString("application.defaultResponseListener");
			log.info("Installing DefaultResponseListener " + className); 
			Class<? extends DefaultResponseListener> clazz = (Class<? extends DefaultResponseListener>) Class.forName(className);
			this.bind(DefaultResponseListener.class).to(clazz).in(Singleton.class); 
		} catch (Exception e)
		{
			log.error(e.getMessage(),e);
		}
		
 
 
		this.bind(new TypeLiteral<Set<Class<?>>>() {}).annotatedWith(Names.named("registeredControllers")).toInstance(registeredControllers);
		this.bind(new TypeLiteral<Set<EndpointInfo>>() {}).annotatedWith(Names.named("registeredEndpoints")).toInstance(registeredEndpoints);
		
		this.bind(XmlMapper.class).toInstance(new XmlMapper()); 


	}

 
 

}
