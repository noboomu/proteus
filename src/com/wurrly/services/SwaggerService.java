 
package com.wurrly.services;

import java.io.File;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

import javax.ws.rs.HttpMethod;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.j256.simplemagic.ContentType;
import com.mitchellbosecke.pebble.PebbleEngine;
import com.mitchellbosecke.pebble.template.PebbleTemplate;
import com.typesafe.config.Config;
import com.wurrly.modules.RoutingModule;
import com.wurrly.server.handlers.HandlerGenerator;
import com.wurrly.server.route.RouteInfo;
import com.wurrly.server.swagger.ServerParameterExtension;
import com.wurrly.utilities.JsonMapper;

import io.swagger.jaxrs.ext.SwaggerExtension;
import io.swagger.jaxrs.ext.SwaggerExtensions;
import io.swagger.models.Info;
import io.swagger.models.Swagger;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.RoutingHandler;
import io.undertow.server.handlers.resource.ClassPathResourceManager;
import io.undertow.server.handlers.resource.Resource;
import io.undertow.server.handlers.resource.ResourceHandler;
import io.undertow.util.CanonicalPathUtils;
import io.undertow.util.Headers;
import io.undertow.util.Methods;

import com.j256.simplemagic.ContentType;

 
public class SwaggerService   extends ConfigurableService implements Supplier<RoutingHandler>
{
	private static Logger log = LoggerFactory.getLogger(SwaggerService.class.getCanonicalName());

	protected com.wurrly.server.swagger.Reader reader = null;
	
	protected String swaggerResourcePath = "resources/swagger/";
	
	protected final String swaggerThemesPath = "resources/swagger/themes";

	protected Swagger swagger = null;
	
	private String swaggerSpec = null;
	
	private String swaggerIndexHTML = null;
	
	
	@Inject
	@Named("swagger.basePath")
	protected String swaggerBasePath;
	
	@Inject
	@Named("swagger.theme")
	protected String swaggerTheme;
	
	@Inject
	@Named("swagger.specFilename")
	protected String specFilename;
	
	@Inject
	@Named("swagger.info")
	protected Config swaggerInfo;
	
	
	@Inject
	@Named("application.host")
	protected String host;
	
	@Inject
	@Named("application.name")
	protected String applicationName;
	
	@Inject
	@Named("application.port")
	protected Integer port;
	
	@Inject
	@Named("application.path")
	protected String applicationPath;
	

	@Inject
	protected RoutingHandler rootHandler;
	
	@Inject
	protected RoutingModule routingModule;
	
 
	public SwaggerService()
	{ 
		 this.configFile = "swagger.conf";
  	}
	
	public void generateSwaggerSpec()
	{
		
		Set<Class<?>> classes = this.routingModule.getRegisteredControllers();
		
		List<SwaggerExtension> extensions = new ArrayList<>();
		
		extensions.add(new ServerParameterExtension());

		SwaggerExtensions.setExtensions(extensions);

		log.debug("Added SwaggerExtension: ServerParameterExtension");
		
		
		Swagger swagger = new Swagger();
		
		swagger.setBasePath(applicationPath);
		
		swagger.setHost(host+((port != 80 && port != 443) ? ":" + port : ""));
		
		Info info = new Info();
		
		if(swaggerInfo.hasPath("title"))
		{
			info.title(swaggerInfo.getString("title"));
		}
		
		if(swaggerInfo.hasPath("version"))
		{
			info.version(swaggerInfo.getString("version"));
		}
		
		if(swaggerInfo.hasPath("description"))
		{
			info.description(swaggerInfo.getString("description"));
		}
		
		swagger.setInfo(info);


		this.reader = new com.wurrly.server.swagger.Reader(swagger);
 
		classes.forEach( c -> {
			
			this.reader.read(c);
			
		});
		
		this.swagger = this.reader.getSwagger();
		
		this.swaggerSpec =  JsonMapper.toPrettyJSON(this.swagger);
		
 		
	}


	@Override
	protected void configure()
	{
	//	log.debug("Configuring : " + this.getClass().getSimpleName() + " swaggerBasePath: " + swaggerBasePath);
		 
 		
		super.configure();
	}

	/**
	 * @return the swagger
	 */
	public Swagger getSwagger()
	{
		return swagger;
	}

	/**
	 * @param swagger the swagger to set
	 */
	public void setSwagger(Swagger swagger)
	{
		this.swagger = swagger;
	}
	
	public void generateSwaggerHTML()
	{
		try
		{  
			PebbleEngine engine = new PebbleEngine.Builder().build();
	
			PebbleTemplate compiledTemplate = engine.getTemplate("resources/swagger/index.html");
	
			String themePath = "swagger-ui.css";
			 
			if(!this.swaggerTheme.equals("default"))
			{
				themePath= "themes/theme-" + this.swaggerTheme + ".css"; 
			} 
			
			log.debug("theme: " + themePath);
			
			Map<String, Object> context = new HashMap<>();

			context.put("themePath", themePath);
			context.put("title", applicationName + " Swagger UI");
			context.put("swaggerBasePath", this.swaggerBasePath);
			context.put("swaggerFullPath", "http://" + host + ((port != 80 && port != 443) ? ":" + port : "") + this.swaggerBasePath + ".json"  ) ;
			
			Writer writer = new StringWriter();
			 
			compiledTemplate.evaluate(writer, context);
	
			this.swaggerIndexHTML = writer.toString();
	
			//log.debug("found swaggerContent: " + swaggerIndexHTML); 
		
		} catch (Exception e)
		{ 
			log.error(e.getMessage(),e);
		}
	}
	
	public RoutingHandler get()
	{
		
		RoutingHandler router = new RoutingHandler();
		
		String pathTemplate = this.swaggerBasePath + ".json";
		
 		
		router.add(HttpMethod.GET, pathTemplate, new HttpHandler(){

			@Override
			public void handleRequest(HttpServerExchange exchange) throws Exception
			{
 
				exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, ContentType.JSON.getMimeType());
 

				exchange.getResponseSender().send(swaggerSpec);
				
			}
			
		});
		
   
		routingModule.getRegisteredRoutes().add(RouteInfo.builder().withConsumes("*/*").withPathTemplate(pathTemplate).withControllerName("Swagger").withMethod(Methods.GET).withProduces(ContentType.JSON.getMimeType()).build());
		 
		pathTemplate =  this.swaggerBasePath;
		
 		
		router.add(HttpMethod.GET, pathTemplate , new HttpHandler(){

			@Override
			public void handleRequest(HttpServerExchange exchange) throws Exception
			{
 
				
 				exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, ContentType.HTML.getMimeType());
 				exchange.getResponseSender().send(swaggerIndexHTML);
				
			}
			
		});
 
		routingModule.getRegisteredRoutes().add(RouteInfo.builder().withConsumes("*/*").withProduces("text/html").withPathTemplate(pathTemplate).withControllerName("Swagger").withMethod(Methods.GET).build());
 
		ClassPathResourceManager resourceManager = new ClassPathResourceManager(this.getClass().getClassLoader());

		pathTemplate = this.swaggerBasePath + "/themes/*";
		
		router.add(HttpMethod.GET, pathTemplate, new ResourceHandler(resourceManager){

			@Override
			public void handleRequest(HttpServerExchange exchange) throws Exception
			{
 
				String canonicalPath = CanonicalPathUtils.canonicalize((exchange.getRelativePath()));
						
				canonicalPath = swaggerThemesPath + canonicalPath.split(swaggerBasePath+"/themes")[1]; 
				
				exchange.setRelativePath(canonicalPath);

				super.handleRequest(exchange);
				
			}
			
		});
		 
		
		routingModule.getRegisteredRoutes().add(RouteInfo.builder().withConsumes("*/*").withProduces("text/css").withPathTemplate(pathTemplate).withControllerName("Swagger").withMethod(Methods.GET).build());

 		
		try
		{
			//Resource swaggerUIResource = resourceManager.getResource(webJarPath);
			
			 pathTemplate =  this.swaggerBasePath + "/*";
			 
			 router.add(HttpMethod.GET, pathTemplate, new ResourceHandler(resourceManager){

					@Override
					public void handleRequest(HttpServerExchange exchange) throws Exception
					{
 
						String canonicalPath = CanonicalPathUtils.canonicalize((exchange.getRelativePath()));
								
						canonicalPath = swaggerResourcePath + canonicalPath.split(swaggerBasePath)[1]; 
						
						exchange.setRelativePath(canonicalPath);

						super.handleRequest(exchange);
						
					}
					
				});
			

				
			routingModule.getRegisteredRoutes().add(RouteInfo.builder().withConsumes("*/*").withProduces("*/*").withPathTemplate(pathTemplate).withControllerName("Swagger").withMethod(Methods.GET).build());

 

		} catch (Exception e)
		{
			log.error(e.getMessage(),e);
		}
 		  
		return router; 
	}

	 

	/* (non-Javadoc)
	 * @see com.google.common.util.concurrent.AbstractIdleService#startUp()
	 */
	@Override
	protected void startUp() throws Exception
	{
		// TODO Auto-generated method stub
		
		
		this.generateSwaggerSpec();
		this.generateSwaggerHTML();

 		
		Swagger swagger = this.getSwagger();
		
		log.info("swagger spec: " + JsonMapper.toPrettyJSON(swagger));

		rootHandler.addAll(this.get()); 
	}

	/* (non-Javadoc)
	 * @see com.google.common.util.concurrent.AbstractIdleService#shutDown()
	 */
	@Override
	protected void shutDown() throws Exception
	{
		// TODO Auto-generated method stub
		
	}
	
}
