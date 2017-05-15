 
package io.sinistral.proteus.services;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;
import java.util.jar.JarFile;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.core.MediaType;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.typesafe.config.Config;
 
import io.sinistral.proteus.server.endpoints.EndpointInfo;
import io.sinistral.proteus.server.swagger.ServerParameterExtension;
import io.swagger.jaxrs.ext.SwaggerExtension;
import io.swagger.jaxrs.ext.SwaggerExtensions;
import io.swagger.models.Info;
import io.swagger.models.Swagger;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.RoutingHandler;
import io.undertow.server.handlers.ResponseCodeHandler;
import io.undertow.server.handlers.resource.FileResourceManager;
import io.undertow.server.handlers.resource.ResourceHandler;
import io.undertow.util.CanonicalPathUtils;
import io.undertow.util.Headers;
import io.undertow.util.Methods;


 
public class SwaggerService   extends BaseService implements Supplier<RoutingHandler>
{
	  
	private static Logger log = LoggerFactory.getLogger(SwaggerService.class.getCanonicalName());

	protected io.sinistral.proteus.server.swagger.Reader reader = null;
	
	protected final String swaggerResourcePathPrefix = "swagger";

	@Inject
	@Named("swagger.resourcePrefix")
	protected String swaggerResourcePrefix;
	
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
	@Named("swagger.redocPath")
	protected String redocPath;
	
	@Inject
	@Named("application.host")
	protected String host;
	
	@Inject
	@Named("application.name")
	protected String applicationName;
	
	@Inject
	@Named("swagger.port")
	protected Integer port;
	
	@Inject
	@Named("application.path")
	protected String applicationPath;
	
	@Inject
	protected RoutingHandler router;
	
	@Inject
	@Named("registeredEndpoints")
	protected Set<EndpointInfo> registeredEndpoints;
	 
	@Inject
	@Named("registeredControllers")
	protected Set<Class<?>> registeredControllers;
 
	protected ObjectMapper mapper = new ObjectMapper();
	
	protected ObjectWriter writer = null; 
	
	protected Path swaggerResourcePath = null;
	
	protected ClassLoader serviceClassLoader = null;
	
	protected Swagger swagger = null;
	
	protected String swaggerSpec = null;
	
	protected String swaggerIndexHTML = null;
	
	protected String redocHTML = null;

	public SwaggerService( )
	{ 
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		mapper.configure(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT, true);
		mapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
		mapper.configure(DeserializationFeature.EAGER_DESERIALIZER_FETCH,true); 
		mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
		mapper.setSerializationInclusion(Include.NON_NULL);

		mapper.registerModule(new Jdk8Module());

		
		writer = mapper.writerWithDefaultPrettyPrinter();
		writer = writer.without(SerializationFeature.WRITE_NULL_MAP_VALUES); 
	}

	 
	public void generateSwaggerSpec()
	{
		
		Set<Class<?>> classes = this.registeredControllers;
		
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


		this.reader = new io.sinistral.proteus.server.swagger.Reader(swagger);
 
		classes.forEach( c -> this.reader.read(c));
		
		this.swagger = this.reader.getSwagger();
	 
		
		try
		{
			
			this.swaggerSpec = writer.writeValueAsString(this.swagger);
			
		} catch (Exception e)
		{
			log.error(e.getMessage(),e);
		}
 		
 		
	}

 
	public Swagger getSwagger()
	{
		return swagger;
	}

 
	public void setSwagger(Swagger swagger)
	{
		this.swagger = swagger;
	}
	
	public void generateSwaggerHTML()
	{
		try
		{  
  
			
			try(InputStream templateInputStream = this.getClass().getClassLoader().getResourceAsStream(swaggerResourcePrefix + "/index.html"))
			{
				byte[] templateBytes = IOUtils.toByteArray(templateInputStream); 
				
				String templateString = new String(templateBytes,Charset.defaultCharset());
				 
				String themePath = "swagger-ui.css";
				 
				if(!swaggerTheme.equals("default"))
				{
					themePath= "themes/theme-" + swaggerTheme + ".css"; 
				} 
				 
				templateString = templateString.replaceAll("\\{\\{ themePath \\}\\}", themePath);
				templateString = templateString.replaceAll("\\{\\{ swaggerBasePath \\}\\}", swaggerBasePath);
				templateString = templateString.replaceAll("\\{\\{ title \\}\\}",applicationName + " Swagger UI");
				templateString = templateString.replaceAll("\\{\\{ swaggerFullPath \\}\\}","//" + host + ((port != 80 && port != 443) ? ":" + port : "") + swaggerBasePath + ".json");
	
				this.swaggerIndexHTML = templateString;   
			}
			
			try(InputStream templateInputStream = this.getClass().getClassLoader().getResourceAsStream(swaggerResourcePrefix + "/redoc.html"))
			{
				byte[] templateBytes = IOUtils.toByteArray(templateInputStream); 
				
				String templateString = new String(templateBytes,Charset.defaultCharset());
				
				templateString = templateString.replaceAll("\\{\\{ swaggerSpecPath \\}\\}", this.swaggerBasePath + ".json");
				templateString = templateString.replaceAll("\\{\\{ applicationName \\}\\}", applicationName);
	
				this.redocHTML = templateString;   
			}
  
			URL url = this.getClass().getClassLoader().getResource(swaggerResourcePrefix);
			
			if( url.toExternalForm().contains("!") )
			{
				log.debug("Copying Swagger resources...");

				String jarPathString = url.toExternalForm().substring(0, url.toExternalForm().indexOf("!") ).replaceAll("file:", "").replaceAll("jar:", "");
		 
				File srcFile = new File(jarPathString);
			
				try(JarFile jarFile = new JarFile(srcFile, false))
				{ 
					String appName = config.getString("application.name").replaceAll(" ", "_");
 					
					Path tmpDirParent = Files.createTempDirectory(appName);
					
					Path swaggerTmpDir = tmpDirParent.resolve("swagger/");
					
					if(swaggerTmpDir.toFile().exists())
					{
						log.debug("Deleting existing Swagger directory at " + swaggerTmpDir);
						
						try
						{
							FileUtils.deleteDirectory(swaggerTmpDir.toFile());

						} catch (java.lang.IllegalArgumentException e)
						{
							log.debug("Swagger tmp directory is not a directory...");
							swaggerTmpDir.toFile().delete();
						}
 					}
					
					java.nio.file.Files.createDirectory( swaggerTmpDir );
					
					this.swaggerResourcePath = swaggerTmpDir;
			 
					jarFile.stream().filter( ze ->  ze.getName().endsWith("js") || ze.getName().endsWith("css") || ze.getName().endsWith("map") || ze.getName().endsWith("html") ).forEach( ze -> {
						
						try
						{
							final InputStream entryInputStream = jarFile.getInputStream(ze);
							
							String filename = ze.getName().substring(swaggerResourcePrefix.length() + 1); 
							
							Path entryFilePath = swaggerTmpDir.resolve(filename); 

							java.nio.file.Files.createDirectories(entryFilePath.getParent());
							
							java.nio.file.Files.copy(entryInputStream, entryFilePath,StandardCopyOption.REPLACE_EXISTING);
							
						} catch (Exception e)
						{
							log.error(e.getMessage() + " for entry " + ze.getName());
						} 
					}); 
				}
			}
			else
			{
				this.swaggerResourcePath = Paths.get(this.getClass().getClassLoader().getResource(this.swaggerResourcePrefix).toURI());
				this.serviceClassLoader = this.getClass().getClassLoader();
			}
		
		} catch (Exception e)
		{ 
			log.error(e.getMessage(),e);
		}
	}
	
	public RoutingHandler get()
	{
		
		RoutingHandler router = new RoutingHandler();
		
		String pathTemplate = this.swaggerBasePath + ".json";
		
		FileResourceManager resourceManager = new FileResourceManager(this.swaggerResourcePath.toFile(),1024);
 		
		router.add(HttpMethod.GET, pathTemplate, new HttpHandler(){

			@Override
			public void handleRequest(HttpServerExchange exchange) throws Exception
			{
 
				exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, MediaType.APPLICATION_JSON); 
				exchange.getResponseSender().send(swaggerSpec);
				
			}
			
		});
		
		this.registeredEndpoints.add(EndpointInfo.builder().withConsumes("*/*").withPathTemplate(pathTemplate).withControllerName("Swagger").withMethod(Methods.GET).withProduces(MediaType.APPLICATION_JSON).build());
		
		pathTemplate = this.swaggerBasePath + "/" + this.redocPath;
				 
		router.add(HttpMethod.GET,pathTemplate, new HttpHandler(){

			@Override
			public void handleRequest(HttpServerExchange exchange) throws Exception
			{
 
				exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, MediaType.TEXT_HTML); 
				exchange.getResponseSender().send(redocHTML);
				
			}
			
		});
		
   
		this.registeredEndpoints.add(EndpointInfo.builder().withConsumes("*/*").withPathTemplate(pathTemplate).withControllerName("Swagger").withMethod(Methods.GET).withProduces(MediaType.TEXT_HTML).build());
		 
		pathTemplate =  this.swaggerBasePath;
		 
		router.add(HttpMethod.GET, pathTemplate , new HttpHandler(){

			@Override
			public void handleRequest(HttpServerExchange exchange) throws Exception
			{
 
 
 				exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, MediaType.TEXT_HTML);
 				exchange.getResponseSender().send(swaggerIndexHTML);
				
			}
			
		});
 
		this.registeredEndpoints.add(EndpointInfo.builder().withConsumes(MediaType.WILDCARD).withProduces(MediaType.TEXT_HTML).withPathTemplate(pathTemplate).withControllerName("Swagger").withMethod(Methods.GET).build());
 
 		
		try
		{
	 

			 pathTemplate =  this.swaggerBasePath + "/*";
			 
			 router.add(HttpMethod.GET, pathTemplate, new ResourceHandler(resourceManager){

					@Override
					public void handleRequest(HttpServerExchange exchange) throws Exception
					{
						 
						String canonicalPath = CanonicalPathUtils.canonicalize((exchange.getRelativePath()));
					 
						canonicalPath =  canonicalPath.split(swaggerBasePath)[1];  
						
						exchange.setRelativePath(canonicalPath);
						
						if(serviceClassLoader == null)
						{   
							super.handleRequest(exchange);
						}
						else
						{
							canonicalPath = swaggerResourcePrefix + canonicalPath;
							
							try(final InputStream resourceInputStream = serviceClassLoader.getResourceAsStream(  canonicalPath))
							{
							 
								if(resourceInputStream == null)
								{
									ResponseCodeHandler.HANDLE_404.handleRequest(exchange);
									return;
								}
								
								byte[] resourceBytes = IOUtils.toByteArray(resourceInputStream); 
								
								io.sinistral.proteus.server.MediaType mediaType = io.sinistral.proteus.server.MediaType.getByFileName(canonicalPath);
								
								exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, mediaType.toString());
							
								exchange.getResponseSender().send(ByteBuffer.wrap(resourceBytes)); 
							}
						}
						
					}
					
				});
			

				
			 this.registeredEndpoints.add(EndpointInfo.builder().withConsumes(MediaType.WILDCARD).withProduces(MediaType.WILDCARD).withPathTemplate(pathTemplate).withControllerName("Swagger").withMethod(Methods.GET).build());

 

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
 
		log.debug("\nSwagger Spec:\n" +  writer.writeValueAsString(this.swagger));

		router.addAll(this.get()); 
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
