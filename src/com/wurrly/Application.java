/**
 * 
 */
package com.wurrly;

import java.util.HashSet;
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
import com.google.inject.Injector;
import com.google.inject.name.Named;
import com.typesafe.config.Config;
import com.wurrly.controllers.Users;
import com.wurrly.modules.ConfigModule;
import com.wurrly.modules.RoutingModule;
import com.wurrly.server.handlers.HandlerGenerator;
import com.wurrly.server.handlers.benchmark.BenchmarkHandlers;
import com.wurrly.server.route.RouteInfo;
import com.wurrly.services.SwaggerService;

import io.undertow.Undertow;
import io.undertow.UndertowOptions;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.RoutingHandler;
import io.undertow.server.protocol.http2.Http2UpgradeHandler;
import io.undertow.util.Headers;
import static org.fusesource.jansi.Ansi.*;
import static org.fusesource.jansi.Ansi.Color.*;
/**
 * @author jbauer
 */
public class Application
{
	
 
	private static Logger log = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(Application.class.getCanonicalName());

 
	static final String CHARSET = "UTF-8";
	
	 

	
	public static class BaseHandlers
	{
		 public static void notFoundHandler(HttpServerExchange exchange) {
		        exchange.setStatusCode(404);
		        exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "text/plain");
		        exchange.getResponseSender().send("Page Not Found!!");
		    }
	}
 
 
	protected Injector injector = null;
	protected ServiceManager serviceManager = null;
	protected Undertow webServer = null;
	protected Set<Class<? extends Service>> registeredServices = new HashSet<>();
 	protected RoutingModule routingModule = null;
 	
	public Application()
	{
		this.routingModule = new RoutingModule();
		injector = Guice.createInjector(new ConfigModule(),this.routingModule); 
	}
	
	public void start()
	{
		log.info("Starting services...");
		

		
		Set<Service> services = registeredServices.stream().map( sc -> {
			
			return injector.getInstance(sc);
			
		}).collect(Collectors.toSet());
		
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
		Config rootConfig = injector.getInstance(Config.class);
		RoutingHandler router = injector.getInstance(RoutingHandler.class);
		RoutingModule routingModule = injector.getInstance(RoutingModule.class);

		HandlerGenerator generator = new HandlerGenerator("com.wurrly.controllers.handlers","RouteHandlers",routingModule.getRegisteredControllers());
		
		injector.injectMembers(generator);
	
		Supplier<RoutingHandler> generatedRouteSupplier = injector.getInstance(generator.compileClass());
		
		router.addAll(generatedRouteSupplier.get());
		
		Supplier<RoutingHandler> benchmarkRouteSupplier = new BenchmarkHandlers();
		
		router.addAll(benchmarkRouteSupplier.get());
		
		StringBuilder sb = new StringBuilder();
		
		Set<RouteInfo> routingInfo = routingModule.getRegisteredRoutes(); //injector.getInstance(Key.get(new TypeLiteral<Set<RouteInfo>>() {},Names.named("routeInfo")));
		
		routingInfo.stream().forEachOrdered( r -> sb.append(r.toString() + "\n"));
		
		log.info("\n\nRegistered the following endpoints: \n\n" + sb.toString());
		
		webServer = Undertow.builder()
				.addHttpListener(rootConfig.getInt("application.port"), "localhost")
				.setBufferSize(1024 * 16 * 10)
				.setIoThreads(Runtime.getRuntime().availableProcessors())
				.setServerOption(UndertowOptions.ENABLE_HTTP2, true)
		        .setServerOption(UndertowOptions.ALWAYS_SET_DATE, true) 
 
 		        .setSocketOption(org.xnio.Options.BACKLOG, 10000)
		        .setServerOption(UndertowOptions.RECORD_REQUEST_START_TIME, false)
		        .setServerOption(UndertowOptions.MAX_ENTITY_SIZE, 1000000l * 200 )
				.setWorkerThreads(Runtime.getRuntime().availableProcessors() * 8)
				.setHandler( new HttpHandler()
		{
			@Override
			public void handleRequest(final HttpServerExchange exchange) throws Exception
			{
				try
				{
//					if(exchange.isInIoThread())
//					{
//						exchange.dispatch(this);
//						return;
//					}
					 
//					exchange.getResponseHeaders().put(HttpString.tryFromString("Access-Control-Allow-Origin"), "*");
//					exchange.getResponseHeaders().put(HttpString.tryFromString("Access-Control-Allow-Methods"), "GET, POST, DELETE, PUT, PATCH, OPTIONS");
//					exchange.getResponseHeaders().put(HttpString.tryFromString("Access-Control-Allow-Headers"), "Content-Type, api_key, Authorization");
					exchange.getResponseHeaders().put(Headers.SERVER, "Bowser"); 

					router.handleRequest(exchange);
					
				} catch (Exception e)
				{
					if (exchange.isResponseChannelAvailable())
					{
						log.error(e.getMessage(),e);
					}
				}
			}
		}).build();
		
		return webServer;
	}
	
	public void useService(Class<? extends Service> serviceClass)
	{
		this.registeredServices.add(serviceClass);
	}
	
	public void useController(Class<?> controllerClass)
	{
		this.routingModule.getRegisteredControllers().add(controllerClass);
	}
  	
	public static void main(String[] args)
	{

		try
		{
  

 			Application app = new Application();
 			
 			 app.useService(SwaggerService.class);
 			 
 			 app.useController(Users.class);
 			 
 			 app.start();
 			 
		} catch (Exception e)
		{
			log.error(e.getMessage(),e);
		}


	}
	
 

}
