/**
 * 
 */
package com.wurrly;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.HashSet;
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
import com.google.inject.name.Named;
import com.jsoniter.DecodingMode;
import com.jsoniter.JsonIterator;
import com.jsoniter.annotation.JsoniterAnnotationSupport;
import com.jsoniter.output.Codegen;
import com.jsoniter.output.EncodingMode;
import com.jsoniter.output.JsonStream;
import com.jsoniter.spi.Encoder;
import com.jsoniter.spi.TypeLiteral;
import com.typesafe.config.Config;
import com.wurrly.controllers.Users;
import com.wurrly.modules.ConfigModule;
import com.wurrly.modules.RoutingModule;
import com.wurrly.server.endpoints.EndpointInfo;
import com.wurrly.server.handlers.HandlerGenerator;
import com.wurrly.server.handlers.benchmark.BenchmarkHandlers;
import com.wurrly.services.AssetsService;
import com.wurrly.services.SwaggerService;
 
import io.undertow.Handlers;
import io.undertow.Undertow;
import io.undertow.UndertowOptions;
import io.undertow.server.DefaultResponseListener;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.RoutingHandler;
import io.undertow.util.Headers;
import io.undertow.util.HttpString;
/**
 * @author jbauer
 */
public class Application
{
	
 
	private static Logger log = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(Application.class.getCanonicalName());
 	
	 /*
	  *  public static ExecutorService EXECUTOR =
            new ThreadPoolExecutor(
                    cpuCount * 2, cpuCount * 25, 200, TimeUnit.MILLISECONDS,
                    new LinkedBlockingQueue<Runnable>(cpuCount * 100),
                    new ThreadPoolExecutor.CallerRunsPolicy());
	  */
 
 
 
	protected Injector injector = null;
	protected ServiceManager serviceManager = null;
	protected Undertow webServer = null;

	protected Set<Class<? extends Service>> registeredServices = new HashSet<>();
	
	@Inject
	@Named("registeredControllers")
	protected Set<Class<?>> registeredControllers;

	@Inject
	@Named("registeredEndpoints")
	protected Set<EndpointInfo> registeredEndpoints;
	 
 	
	public Application()
	{
		
		injector = Guice.createInjector(new ConfigModule());  
		injector.injectMembers(this);
		
	}
	
	public void start()
	{
		log.info("Starting services...");
		

		
		Set<Service> services = registeredServices.stream()
				.map( sc -> injector.getInstance(sc))
				.collect(Collectors.toSet());
		
		this.serviceManager = new ServiceManager(services);
		
		this.serviceManager.addListener(new Listener() {
		         public void stopped() {}
		         public void healthy() {
		        	 log.info("Services are healthy...");
		         
		        	 
		        	 buildServer().start(); 
		         }
		         public void failure(Service service) 
		         {
		           System.exit(1);
		         }
		       },
		       MoreExecutors.directExecutor());

		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
   
         try {
        	 serviceManager.stopAsync().awaitStopped(5, TimeUnit.SECONDS);
        	 webServer.stop();
         } catch (TimeoutException timeout) {
           // stopping timed out
         }}));
		     
		 serviceManager.startAsync();
	}
 
	public Undertow buildServer()
	{
		
		final Config rootConfig = injector.getInstance(Config.class);
		
		final RoutingHandler router = injector.getInstance(RoutingHandler.class);

		final DefaultResponseListener defaultResponseListener = injector.getInstance(DefaultResponseListener.class); 
		 
		HandlerGenerator generator = new HandlerGenerator("com.wurrly.controllers.handlers","RouteHandlers");
		
		injector.injectMembers(generator);
	
		Supplier<RoutingHandler> generatedRouteSupplier = injector.getInstance(generator.compileClass());
		
		router.addAll(generatedRouteSupplier.get());
				 
		router.addAll(new BenchmarkHandlers().get());
		
		StringBuilder sb = new StringBuilder();
		
 		
		this.registeredEndpoints.stream().forEachOrdered( r -> sb.append(r.toString()).append("\n"));
		
		log.info("\n\nRegistered the following endpoints: \n\n" + sb.toString());
		
 
		
		webServer = Undertow.builder()
				.addHttpListener(rootConfig.getInt("application.port"),rootConfig.getString("application.host"))
				.setBufferSize(1024 * 16)
				.setIoThreads(Runtime.getRuntime().availableProcessors()*2)
				.setServerOption(UndertowOptions.ENABLE_HTTP2, false)
		        .setServerOption(UndertowOptions.ALWAYS_SET_DATE, true) 
		     //   .setServerOption(UndertowOptions.BUFFER_PIPELINED_DATA, true) 
 		        .setSocketOption(org.xnio.Options.BACKLOG, 10000)
 		       .setServerOption(UndertowOptions.ALWAYS_SET_KEEP_ALIVE, false)
		        .setServerOption(UndertowOptions.RECORD_REQUEST_START_TIME, false)
		        .setServerOption(UndertowOptions.MAX_ENTITY_SIZE, 1000000L * 200 )
				.setWorkerThreads(Runtime.getRuntime().availableProcessors()*8)
				.setHandler(
 new HttpHandler()
		{
			
			@Override
			public void handleRequest(final HttpServerExchange exchange) throws Exception
			{
  
					exchange.addDefaultResponseListener(defaultResponseListener);
					 
//					exchange.getResponseHeaders().put(HttpString.tryFromString("Access-Control-Allow-Origin"), "*");
//					exchange.getResponseHeaders().put(HttpString.tryFromString("Access-Control-Allow-Methods"), "*");
//					exchange.getResponseHeaders().put(HttpString.tryFromString("Access-Control-Allow-Headers"), "*");
					//exchange.getResponseHeaders().put(Headers.SERVER, "Bowser"); 

					 try {
						   router.handleRequest(exchange);
					 }   catch (Exception e)
					{
						 if(exchange.isResponseChannelAvailable()) {
				               log.error(e.getMessage());
				               exchange.endExchange();
				           }
					}
				 
			}
			}
 		).build();
		
	 
		
		return webServer;
	}
	
	public Application useService(Class<? extends Service> serviceClass)
	{
		this.registeredServices.add(serviceClass);
		return this;
	}
	
	public Application useController(Class<?> controllerClass)
	{
		this.registeredControllers.add(controllerClass);
		return this;
	}
	
  	
	public static void main(String[] args)
	{

		try
		{
			JsonIterator.setMode(DecodingMode.DYNAMIC_MODE_AND_MATCH_FIELD_WITH_HASH);
			JsonStream.setMode(EncodingMode.DYNAMIC_MODE);
			JsoniterAnnotationSupport.enable();

 			Application app = new Application();
 			
 			 app.useService(SwaggerService.class);
 			 
 			 app.useService(AssetsService.class);

 			 app.useController(Users.class);
 			 
 			 app.start();
 			 
		} catch (Exception e)
		{
			log.error(e.getMessage(),e);
		}


	}
	
 
	public static class BaseHandlers
	{
		 public static void notFoundHandler(HttpServerExchange exchange) {
		        exchange.setStatusCode(404);
		        exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "text/plain");
		        exchange.getResponseSender().send("Page Not Found!!");
		    }
	}

}
