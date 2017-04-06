/**
 * 
 */
package com.wurrly;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.Deque;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.util.concurrent.MoreExecutors;
import com.google.common.util.concurrent.Service;
import com.google.common.util.concurrent.ServiceManager;
import com.google.common.util.concurrent.ServiceManager.Listener;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;
import com.google.inject.name.Names;
import com.jsoniter.DecodingMode;
import com.jsoniter.JsonIterator;
import com.jsoniter.annotation.JacksonAnnotationSupport;
import com.jsoniter.annotation.JsoniterAnnotationSupport;
import com.jsoniter.output.EncodingMode;
import com.jsoniter.output.JsonStream;
import com.typesafe.config.Config;
import com.wurrly.controllers.Users;
import com.wurrly.models.User;
import com.wurrly.modules.ConfigModule;
import com.wurrly.modules.RoutingModule;
import com.wurrly.modules.SwaggerModule;
import com.wurrly.server.GeneratedRouteHandler;
import com.wurrly.server.RouteRecord;
import com.wurrly.server.ServerRequest;
import com.wurrly.server.generate.RouteGenerator;

import io.undertow.Undertow;
import io.undertow.UndertowOptions;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.RoutingHandler;
import io.undertow.util.Headers;
import io.undertow.util.HttpString;
import io.undertow.util.Methods;

/**
 * @author jbauer
 */
public class Application
{
	
 
	private static Logger log = LoggerFactory.getLogger(Application.class.getCanonicalName());

 
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
	protected Set<Class<?>> registeredControllers = new HashSet<>();
	
	public Application()
	{
		injector = Guice.createInjector(new ConfigModule(),new RoutingModule());
		
 
		 
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

	
		 log.debug(injector.getAllBindings()+"");
		RouteGenerator generator = new RouteGenerator("com.wurrly.controllers.handlers","RouteHandlers");
		
		injector.injectMembers(generator);
		
		generator.generateRoutes(this.registeredControllers);
		 
		Class<? extends GeneratedRouteHandler> handlerClass = generator.compileRoutes();
		
		log.debug("New class: " + handlerClass);
  
		GeneratedRouteHandler routeHandler = injector.getInstance(handlerClass);
		
		routeHandler.addRouteHandlers(router);
		
		addBenchmarkHandler(router);
		
		StringBuilder sb = new StringBuilder();
		
		Set<RouteRecord> routeRecords = injector.getInstance(Key.get(new TypeLiteral<Set<RouteRecord>>() {},Names.named("routeRecords")));
		
		routeRecords.stream().forEachOrdered( r -> sb.append(r.toString() + "\n"));
		
		log.info("\n\nRegistered the following endpoints: \n\n" + sb.toString());
		
		webServer = Undertow.builder()
				.addHttpListener(rootConfig.getInt("application.port"), "localhost")
				.setBufferSize(1024 * 16)
				.setIoThreads(Runtime.getRuntime().availableProcessors())
				.setServerOption(UndertowOptions.ENABLE_HTTP2, true)
		        .setServerOption(UndertowOptions.ALWAYS_SET_DATE, true) 
		        .setSocketOption(org.xnio.Options.BACKLOG, 10000)
		        .setServerOption(UndertowOptions.RECORD_REQUEST_START_TIME, false)

				.setWorkerThreads(Runtime.getRuntime().availableProcessors() * 8)
				.setHandler(new HttpHandler()
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
		this.registeredControllers.add(controllerClass);
	}
  	
	public static void main(String[] args)
	{

		try
		{
			
		  //  Injector injector = Guice.createInjector(new ConfigModule(),new RoutingModule());

 			Application app = new Application();
 			
 			 app.useService(SwaggerModule.class);
 			 
 			 app.useController(Users.class);
 			 
 			 app.start();
 			 
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		//	 Users usersController = injector.getInstance(Users.class);
			
		 //   injector.injectMembers(usersController);

			
		 
 
			 
			//Set<Class<?>> classes = RouteGenerator.getApiClasses("com.wurrly.controllers",null);

//			RouteGenerator generator = new RouteGenerator("com.wurrly.controllers.handlers","RouteHandlers");
//			generator.generateRoutes();
//			
//		 
			
			 
			
			//	generator.getRestRoutes().stream().forEachOrdered( r -> sb.append(r.toString() + "\n"));

//			SwaggerModule swaggerModule = injector.getInstance(SwaggerModule.class);
//			
//			
//			 Set<Service> services = Collections.singleton(swaggerModule);
//			
//		     ServiceManager manager = new ServiceManager(Collections.singleton(swaggerModule));
//		     
//		     manager.addListener(new Listener() {
//		         public void stopped() {}
//		         public void healthy() {
//		           // Services have been initialized and are healthy, start accepting requests...
//		        	 Logger.info("Services are healthy...");
//		         }
//		         public void failure(Service service) {
//		           // Something failed, at this point we could log it, notify a load balancer, or take
//		           // some other action.  For now we will just exit.
//		           System.exit(1);
//		         }
//		       },
//		       MoreExecutors.directExecutor());
//
//		     Runtime.getRuntime().addShutdownHook(new Thread() {
//		       public void run() {
//		         // Give the services 5 seconds to stop to ensure that we are responsive to shutdown
//		         // requests.
//		         try {
//		           manager.stopAsync().awaitStopped(5, TimeUnit.SECONDS);
//		         } catch (TimeoutException timeout) {
//		           // stopping timed out
//		         }
//		       }
//		     });
//		     manager.startAsync();  // start all the services asynchronously

//			swaggerModule.generateSwaggerSpec(classes);
//			
//			Logger.debug("swagger spec\n");
//			
//			Swagger swagger = swaggerModule.getSwagger();
//			
//			Logger.debug("swagger spec: " + JsonMapper.toPrettyJSON(swagger));
//			
//			swaggerModule.addRouteHandlers();
			
//			HttpHandler getUserHandler = null;
//			GetUsersHandler getUserHandler = new GetUsersHandler(usersController);
			
//			for( Method m : Users.class.getDeclaredMethods() )
//			{
//				System.out.println("method: " + m);
//				
//				if( m.isSynthetic() || !m.getDeclaringClass().equals(Users.class))
//				{
//					System.out.println("m " + m + " is shady");
//					continue;
//				}
//				
// 				HttpString httpMethod = HandleGenerator.extractHttpMethod.apply(m);
//				String pathTemplate = HandleGenerator.extractPathTemplate.apply(m);
//				HttpHandler handler = HandleGenerator.generateHandler(usersController, m, httpMethod.equals(Methods.POST));
//
//				Logger.info("\nFUNCTION: " + m + "\n\tMETHOD: " + httpMethod + "\n\tPATH: " + pathTemplate);
// 	 			
//				
//	 			router.add(httpMethod, pathTemplate,  handler );
//
// 				System.out.println("handler: " + handler);
//				 
//			}
			
//			 final HttpHandler createUserPostHandler = new HttpHandler() {
//				    @Override
//				    public void handleRequest(final HttpServerExchange exchange) throws Exception {
//				      if(exchange.isInIoThread()) {
//				        exchange.dispatch(this);
//				      }
//				      ServerRequest serverRequest = new ServerRequest(exchange);
//				      Optional<String> context = Optional.ofNullable(exchange.getQueryParameters().get("context")).map(Deque::getFirst);
//				      Any user = exchange.getAttachment(ServerRequest.REQUEST_JSON_BODY).readAny();
//				      Any response = usersController.createUser(serverRequest,context,user);
//				      exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "application/json");
//				      exchange.getResponseSender().send(com.jsoniter.output.JsonStream.serialize(response));
//				    }
//				  };
			
			
		 
			
//			Config rootConfig = injector.getInstance(Config.class);
// 		 
//			Undertow server = Undertow.builder()
//					.addHttpListener(rootConfig.getInt("application.port"), "localhost")
//					.setBufferSize(1024 * 16)
//					.setIoThreads(Runtime.getRuntime().availableProcessors())
//					.setServerOption(UndertowOptions.ENABLE_HTTP2, true)
//			        .setServerOption(UndertowOptions.ALWAYS_SET_DATE, true) 
//			        .setSocketOption(org.xnio.Options.BACKLOG, 10000)
//			        .setServerOption(UndertowOptions.RECORD_REQUEST_START_TIME, false)
//
//					.setWorkerThreads(Runtime.getRuntime().availableProcessors() * 8)
//					.setHandler(new HttpHandler()
//			{
//				@Override
//				public void handleRequest(final HttpServerExchange exchange) throws Exception
//				{
//					try
//					{
////						if(exchange.isInIoThread())
////						{
////							exchange.dispatch(this);
////							return;
////						}
//						 
////						exchange.getResponseHeaders().put(HttpString.tryFromString("Access-Control-Allow-Origin"), "*");
////						exchange.getResponseHeaders().put(HttpString.tryFromString("Access-Control-Allow-Methods"), "GET, POST, DELETE, PUT, PATCH, OPTIONS");
////						exchange.getResponseHeaders().put(HttpString.tryFromString("Access-Control-Allow-Headers"), "Content-Type, api_key, Authorization");
//						router.handleRequest(exchange);
//						
//					} catch (Exception e)
//					{
//						if (exchange.isResponseChannelAvailable())
//						{
//							e.printStackTrace();
//						}
//					}
//				}
//			}).build();
//			server.start();
			
//			Runtime.getRuntime().addShutdownHook( new Thread(){
//			 
//				@Override
//				public void run()
//				{
//					 
//				}
//			});
//
//
//		} catch (Exception e)
//		{
//			e.printStackTrace();
//		}

	}
	
	private final static class BenchmarkMessage
	{
		public String message = "hello world";
	}
	
	public static void addBenchmarkHandler(RoutingHandler routeHandler)
	{
	    final ByteBuffer msgBuffer  = ByteBuffer.wrap("hello world".getBytes());
	    
	    

		routeHandler.add(Methods.GET, "/string", new HttpHandler(){
 
			@Override
			public void handleRequest(HttpServerExchange exchange) throws Exception
			{
				// TODO Auto-generated method stub
				
				exchange.getResponseSender().send(msgBuffer);  
				
			} 
		} );
		
		routeHandler.add(Methods.GET, "/json", new HttpHandler(){
			 
			@Override
			public void handleRequest(HttpServerExchange exchange) throws Exception
			{
				// TODO Auto-generated method stub
				
				exchange.getResponseSender().send(JsonStream.serialize(new BenchmarkMessage()));  
				
			} 
		} );
	} 

}
