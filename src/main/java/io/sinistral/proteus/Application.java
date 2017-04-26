/**
 * 
 */
package io.sinistral.proteus;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.util.concurrent.MoreExecutors;
import com.google.common.util.concurrent.Service;
import com.google.common.util.concurrent.ServiceManager;
import com.google.common.util.concurrent.ServiceManager.Listener;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.name.Named;
import com.jsoniter.DecodingMode;
import com.jsoniter.JsonIterator;
import com.jsoniter.annotation.JsoniterAnnotationSupport;
import com.jsoniter.output.EncodingMode;
import com.jsoniter.output.JsonStream;
import com.typesafe.config.Config;

import io.sinistral.proteus.controllers.Benchmarks;
import io.sinistral.proteus.controllers.Users;
import io.sinistral.proteus.modules.ConfigModule;
import io.sinistral.proteus.server.endpoints.EndpointInfo;
import io.sinistral.proteus.server.handlers.DefaultHttpHandler;
import io.sinistral.proteus.server.handlers.HandlerGenerator;
import io.sinistral.proteus.services.AssetsService;
import io.sinistral.proteus.services.SwaggerService;
import io.undertow.Undertow;
import io.undertow.UndertowOptions;
import io.undertow.server.HttpHandler;
import io.undertow.server.RoutingHandler;
/**
 * @author jbauer
 */
public class Application
{
	 
	private static Logger log = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(Application.class.getCanonicalName());
   
	@Inject
	@Named("registeredControllers")
	protected Set<Class<?>> registeredControllers;

	@Inject
	@Named("registeredEndpoints")
	protected Set<EndpointInfo> registeredEndpoints;
 
	@Inject
	@Named("registeredServices")
	protected Set<Class<? extends Service>> registeredServices;
	
	@Inject
	protected RoutingHandler router;
	
	@Inject
	protected Config config;
	
	protected List<com.google.inject.Module> registeredModules = new ArrayList<>();

	protected Injector injector = null;
	protected ServiceManager serviceManager = null;
	protected Undertow undertow = null; 
	protected Class<? extends HttpHandler> rootHandlerClass;
	protected HttpHandler rootHandler;
 

 	
	public Application()
	{
		
		injector = Guice.createInjector(new ConfigModule("application.conf"));  
		injector.injectMembers(this); 
		
	}
	
	public Application(String configFile)
	{
		
		injector = Guice.createInjector(new ConfigModule(configFile));  
		injector.injectMembers(this); 
		
	}
	
	public Application(URL configURL)
	{
		
		injector = Guice.createInjector(new ConfigModule(configURL));  
		injector.injectMembers(this); 
		
	}
	
	public void start()
	{

		injector = injector.createChildInjector(registeredModules);
		
		if( rootHandlerClass == null && rootHandler == null )
		{
			log.error("Cannot start the server without specifying the root handler class or a root HttpHandler!");
			System.exit(1);
		}
		
		log.info("Starting services...");
		 
		Set<Service> services = registeredServices.stream()
				.map( sc -> injector.getInstance(sc) )
				.collect(Collectors.toSet());
		
		serviceManager = new ServiceManager(services);

		serviceManager.addListener(new Listener()
		{
			public void stopped()
			{

			}

			public void healthy()
			{
				log.info("Services are healthy...");

				buildServer().start();
				
				printStatus(); 
			}

			public void failure(Service service)
			{
				log.error("Error on service: " + service);
				System.exit(1);
			}
		}, MoreExecutors.directExecutor());

		Runtime.getRuntime().addShutdownHook(new Thread()
		{
			@Override
			public void run()
			{
				try
				{
					log.info("Shutting down...");

					serviceManager.stopAsync().awaitStopped(5, TimeUnit.SECONDS);
					undertow.stop();

					log.info("Shutdown complete.");
				} catch (TimeoutException timeout)
				{
					timeout.printStackTrace();
				}
			}
		});

		 serviceManager.startAsync();
		 
 	}
 
	public Undertow buildServer()
	{
						
		for(Class<?> controllerClass : registeredControllers)
		{
			HandlerGenerator generator = new HandlerGenerator("io.sinistral.proteus.controllers.handlers",controllerClass);
			
			injector.injectMembers(generator);
		
			Supplier<RoutingHandler> generatedRouteSupplier = injector.getInstance(generator.compileClass());
			
			router.addAll(generatedRouteSupplier.get());
		}
		 
		final HttpHandler handler; 
		
		if( rootHandlerClass != null )
		{
			handler = injector.getInstance(rootHandlerClass);
		}
		else
		{
			handler = rootHandler;
		}
  
		undertow = Undertow.builder()
				.addHttpListener(config.getInt("application.port"),config.getString("application.host"))
				.setBufferSize(config.getBytes("undertow.bufferSize").intValue())
				.setIoThreads(config.getInt("undertow.ioThreads"))
 				.setServerOption(UndertowOptions.ENABLE_HTTP2, config.getBoolean("undertow.server.enableHttp2"))
		        .setServerOption(UndertowOptions.ALWAYS_SET_DATE, true) 
		        .setServerOption(UndertowOptions.BUFFER_PIPELINED_DATA, config.getBoolean("undertow.server.bufferPipelinedData")) 
 		        .setSocketOption(org.xnio.Options.BACKLOG, config.getInt("undertow.socket.backlog"))
 		        .setServerOption(UndertowOptions.ALWAYS_SET_KEEP_ALIVE, config.getBoolean("undertow.server.alwaysSetKeepAlive"))
		        .setServerOption(UndertowOptions.RECORD_REQUEST_START_TIME,  config.getBoolean("undertow.server.recordRequestStartTime"))
		        .setServerOption(UndertowOptions.MAX_ENTITY_SIZE, config.getBytes("undertow.server.maxEntitySize") )
				.setWorkerThreads(config.getInt("undertow.workerThreads"))
				.setDirectBuffers( config.getBoolean("undertow.directBuffers"))

				.setHandler( handler )
				.build();
		
		
		return undertow;
	}
	
	public Application addService(Class<? extends Service> serviceClass)
	{
		registeredServices.add(serviceClass);
		return this;
	}
	
	public Application addController(Class<?> controllerClass)
	{
		registeredControllers.add(controllerClass);
		return this;
	}
	
	public Application addModule(Module module)
	{
		registeredModules.add(module);
		return this;
	}
	
	public void setRootHandlerClass( Class<? extends HttpHandler> rootHandlerClass )
	{
		this.rootHandlerClass = rootHandlerClass;
	}
	
	public void setRootHandler( HttpHandler rootHandler )
	{
		this.rootHandler = rootHandler;
	}
	
  	 
	public Undertow getUndertow()
	{
		return undertow;
	}
 

	/**
	 * @return the serviceManager
	 */
	public ServiceManager getServiceManager()
	{
		return serviceManager;
	}

	/**
	 * @return the config
	 */
	public Config getConfig()
	{
		return config;
	}
	
	public void printStatus()
	{
		Config globalHeaders = config.getConfig("globalHeaders");
		
		Map<String,String> globalHeadersParameters = globalHeaders.entrySet().stream().collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue().render()));
		   
  
		StringBuilder sb = new StringBuilder(); 

		sb.append("\n\nUsing the following global headers: \n\n");
		sb.append(globalHeadersParameters.entrySet().stream().map( e -> "\t" + e.getKey() + " = " + e.getValue() ).collect(Collectors.joining("\n"))); 
 		sb.append("\n\nRegistered the following endpoints: \n\n");
 		sb.append(this.registeredEndpoints.stream().sorted().map(EndpointInfo::toString).collect(Collectors.joining("\n")));
 		sb.append("\n");
 		
 		log.info(sb.toString()); 
	}

	public static void main(String[] args)
	{

		try
		{ 
			
			JsonIterator.setMode(DecodingMode.DYNAMIC_MODE_AND_MATCH_FIELD_WITH_HASH);
			JsonStream.setMode(EncodingMode.DYNAMIC_MODE);
			JsoniterAnnotationSupport.enable();

 			Application app = new Application();
 			
 			 app.addService(SwaggerService.class);
 			 
 			 app.addService(AssetsService.class);

 			 app.addController(Users.class);
 			 
 			 app.addController(Benchmarks.class);
 			 
 			 app.setRootHandlerClass(DefaultHttpHandler.class);

 			 app.start();
 			 
 		 
 			 
		} catch (Exception e)
		{
			log.error(e.getMessage(),e);
		}


	}
 

}
