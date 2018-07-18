/**
 * 
 */
package io.sinistral.proteus.modules;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.module.afterburner.AfterburnerModule;
import com.google.common.util.concurrent.Service;
import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;
import com.google.inject.name.Names;
import com.typesafe.config.Config;

import io.sinistral.proteus.server.Extractors;
import io.sinistral.proteus.server.ServerResponse;
import io.sinistral.proteus.server.endpoints.EndpointInfo;
import io.undertow.server.DefaultResponseListener;
import io.undertow.server.HandlerWrapper;
import io.undertow.server.HttpHandler;
import io.undertow.server.RoutingHandler;

/**
 * @author jbauer
 */
@Singleton
public class ApplicationModule extends AbstractModule
{
	private static Logger log = LoggerFactory.getLogger(ApplicationModule.class.getCanonicalName());

	protected Set<EndpointInfo> registeredEndpoints = new TreeSet<>();
	protected Set<Class<?>> registeredControllers = new HashSet<>();
	protected Set<Class<? extends Service>> registeredServices = new HashSet<>();
	protected Map<String,HandlerWrapper> registeredHandlerWrappers = new HashMap<>();

	protected Config config;

	public ApplicationModule(Config config)
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
			log.error(e.getMessage(), e);
		}

		this.bind(RoutingHandler.class).toInstance(router);

		this.bind(ApplicationModule.class).toInstance(this);

		try
		{
			String className = config.getString("application.defaultResponseListener");
			log.info("Installing DefaultResponseListener " + className);
			Class<? extends DefaultResponseListener> clazz = (Class<? extends DefaultResponseListener>) Class.forName(className);
			this.bind(DefaultResponseListener.class).to(clazz).in(Singleton.class);
		} catch (Exception e)
		{
			log.error(e.getMessage(), e);
		}

		this.bind(new TypeLiteral<Set<Class<?>>>()
		{
		}).annotatedWith(Names.named("registeredControllers")).toInstance(registeredControllers);
		
		this.bind(new TypeLiteral<Set<EndpointInfo>>()
		{
		}).annotatedWith(Names.named("registeredEndpoints")).toInstance(registeredEndpoints);
		
		this.bind(new TypeLiteral<Set<Class<? extends Service>>>()
		{
		}).annotatedWith(Names.named("registeredServices")).toInstance(registeredServices);
		
		this.bind(new TypeLiteral<Map<String,HandlerWrapper>>()
		{
		}).annotatedWith(Names.named("registeredHandlerWrappers")).toInstance(registeredHandlerWrappers);

		
		this.bindMappers();

	}
	
	/**
	 * Override for customizing XmlMapper and ObjectMapper
	 */
	public void bindMappers()
	{
		this.bind(XmlMapper.class).toInstance(new XmlMapper());

//		JsonIterator.setMode(DecodingMode.DYNAMIC_MODE_AND_MATCH_FIELD_WITH_HASH);
//		JsonStream.setMode(EncodingMode.DYNAMIC_MODE);
//		JsoniterAnnotationSupport.enable();
		
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT, true);
		objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
		objectMapper.configure(DeserializationFeature.EAGER_DESERIALIZER_FETCH,true); 
		objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
		objectMapper.configure(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS, true);

		objectMapper.registerModule(new AfterburnerModule());
		objectMapper.registerModule(new Jdk8Module());
		
		this.bind(ObjectMapper.class).toInstance(objectMapper);
		
		this.requestStaticInjection(Extractors.class);
		
		this.requestStaticInjection(ServerResponse.class);
	}

}
