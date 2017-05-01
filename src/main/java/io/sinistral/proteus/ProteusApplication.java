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
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.common.util.concurrent.Service;
import com.google.common.util.concurrent.Service.State;
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
public class ProteusApplication
{
	 
	private static Logger log = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(ProteusApplication.class.getCanonicalName());
   
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
	protected AtomicBoolean running = new AtomicBoolean(false);

 	
	public ProteusApplication()
	{
		
		injector = Guice.createInjector(new ConfigModule("application.conf"));  
		injector.injectMembers(this); 
		
	}
	
	public ProteusApplication(String configFile)
	{
		
		injector = Guice.createInjector(new ConfigModule(configFile));  
		injector.injectMembers(this); 
		
	}
	
	public ProteusApplication(URL configURL)
	{
		
		injector = Guice.createInjector(new ConfigModule(configURL));  
		injector.injectMembers(this); 
		
	}
	
	public void start()
	{
		if(this.isRunning())
		{
			log.warn("Server has already started...");
			return;
		}
		
		injector = injector.createChildInjector(registeredModules);
		
		if( rootHandlerClass == null && rootHandler == null )
		{
			log.warn("No root handler class or root HttpHandler was specified, using default DefaultHttpHandler.");
			rootHandlerClass = DefaultHttpHandler.class;
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
				undertow.stop(); 
				running.set(false);
			}

			public void healthy()
			{
				log.info("Services are healthy...");

				buildServer();
				
				undertow.start();
								
				printStatus(); 
				
				running.set(true);
			}

			public void failure(Service service)
			{
				log.error("Service failure: " + service);
			}
			
		}, MoreExecutors.directExecutor());

		Runtime.getRuntime().addShutdownHook(new Thread()
		{
			@Override
			public void run()
			{
				try
				{
					shutdown();
				} catch (TimeoutException timeout)
				{
					timeout.printStackTrace();
				}
			}
		});

		 serviceManager.startAsync();
		 
 	}
	
	public void shutdown() throws TimeoutException
	{
		if(!this.isRunning())
		{
			log.warn("Server is not running..."); 
			
			return;
		}
		
		log.info("Shutting down...");

		serviceManager.stopAsync().awaitStopped(5, TimeUnit.SECONDS); 

		log.info("Shutdown complete.");
	}
	
	public boolean isRunning()
	{
		return this.running.get();
	}
 
	public void buildServer()
	{
						
		for(Class<?> controllerClass : registeredControllers)
		{
			HandlerGenerator generator = new HandlerGenerator("io.sinistral.proteus.controllers.handlers",controllerClass);
			
			injector.injectMembers(generator);
		
			try
			{
				Supplier<RoutingHandler> generatedRouteSupplier = injector.getInstance(generator.compileClass());
				
				router.addAll(generatedRouteSupplier.get());
				
			} catch (Exception e)
			{
				log.error("Exception creating handlers for " + controllerClass.getName() + "!!!\n" + e.getMessage(), e); 
			}
		 
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
  
		this.undertow = Undertow.builder()
				.addHttpListener(config.getInt("application.port"),config.getString("application.host"))
				.setBufferSize(16 * 1024)
				.setIoThreads( config.getInt("undertow.ioThreads") )
 				.setServerOption(UndertowOptions.ENABLE_HTTP2, true)
		        .setServerOption(UndertowOptions.ALWAYS_SET_DATE, true) 
 		        .setSocketOption(org.xnio.Options.BACKLOG,  config.getInt("undertow.socket.backlog") )
 		        .setServerOption(UndertowOptions.ALWAYS_SET_KEEP_ALIVE, false)
		        .setServerOption(UndertowOptions.RECORD_REQUEST_START_TIME,  false)
		        .setServerOption(UndertowOptions.MAX_ENTITY_SIZE, config.getBytes("undertow.server.maxEntitySize") )
				.setWorkerThreads( config.getInt("undertow.workerThreads") )
				.setHandler( handler )
				.build();
		 
	}
	
	public ProteusApplication addService(Class<? extends Service> serviceClass)
	{
		registeredServices.add(serviceClass);
		return this;
	}
	
	public ProteusApplication addController(Class<?> controllerClass)
	{
		registeredControllers.add(controllerClass);
		return this;
	}
	
	public ProteusApplication addModule(Module module)
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

		sb.append("\n\nUsing global headers: \n\n");
		sb.append(globalHeadersParameters.entrySet().stream().map( e -> "\t" + e.getKey() + " = " + e.getValue() ).collect(Collectors.joining("\n"))); 
 		sb.append("\n\nRegistered endpoints: \n\n");
 		sb.append(this.registeredEndpoints.stream().sorted().map(EndpointInfo::toString).collect(Collectors.joining("\n")));
 		sb.append("\n\nRegistered services: \n\n");
 		
 		ImmutableMultimap<State, Service> serviceStateMap = this.serviceManager.servicesByState();
 		
 		String serviceStrings = serviceStateMap.asMap().entrySet().stream().sorted().flatMap( e -> {
 			
  			
 			return e.getValue().stream().map( s -> {
 				return "\t" + s.getClass().getSimpleName() + "\t"  + e.getKey();
 			});

 			
 		}).collect(Collectors.joining("\n"));
 
 		sb.append(serviceStrings);
 		
 		sb.append("\n");
 		
 		sb.append("\nListening on port " + config.getInt("application.port"));
 		
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

 			ProteusApplication app = new ProteusApplication();
 			
 			 app.addService(SwaggerService.class);
 			 
 			 app.addService(AssetsService.class);
 
 			 app.setRootHandlerClass(DefaultHttpHandler.class);

 			 app.start();
 			 
 		 
 			 
		} catch (Exception e)
		{
			log.error(e.getMessage(),e);
		}


	}
 

}
