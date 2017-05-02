/**
 * 
 */
package io.sinistral.proteus.modules;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.google.common.util.concurrent.Service;
import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;
import com.google.inject.name.Names;
import com.jsoniter.DecodingMode;
import com.jsoniter.JsonIterator;
import com.jsoniter.annotation.JsoniterAnnotationSupport;
import com.jsoniter.output.EncodingMode;
import com.jsoniter.output.JsonStream;
import com.typesafe.config.Config;

import io.sinistral.proteus.server.endpoints.EndpointInfo;
import io.undertow.server.DefaultResponseListener;
import io.undertow.server.HttpHandler;
import io.undertow.server.RoutingHandler;

/**
 * @author jbauer
 */
@Singleton
public class ServerModule extends AbstractModule
{
	private static Logger log = LoggerFactory.getLogger(ServerModule.class.getCanonicalName());

	protected Set<EndpointInfo> registeredEndpoints = new TreeSet<>();
	protected Set<Class<?>> registeredControllers = new HashSet<>();
	protected Set<Class<? extends Service>> registeredServices = new HashSet<>();

	protected Config config;

	public ServerModule(Config config)
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

		this.bind(ServerModule.class).toInstance(this);

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

		this.bind(XmlMapper.class).toInstance(new XmlMapper());

		JsonIterator.setMode(DecodingMode.DYNAMIC_MODE_AND_MATCH_FIELD_WITH_HASH);
		JsonStream.setMode(EncodingMode.DYNAMIC_MODE);
		JsoniterAnnotationSupport.enable();

	}

}
