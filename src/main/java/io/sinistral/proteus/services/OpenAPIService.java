 
package io.sinistral.proteus.services;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

import javax.ws.rs.HttpMethod;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.typesafe.config.Config;

import io.sinistral.proteus.server.endpoints.EndpointInfo;
import io.sinistral.proteus.server.tools.oas.Reader;
import io.sinistral.proteus.server.tools.oas.ServerModelResolver;
import io.sinistral.proteus.server.tools.oas.ServerParameterExtension;
import io.swagger.v3.core.util.Json;
import io.swagger.v3.core.util.Yaml;
import io.swagger.v3.jaxrs2.ext.OpenAPIExtensions;
import io.swagger.v3.jaxrs2.integration.JaxrsApplicationAndAnnotationScanner;
import io.swagger.v3.oas.integration.GenericOpenApiContext;
import io.swagger.v3.oas.integration.SwaggerConfiguration;
import io.swagger.v3.oas.integration.api.OpenApiContext;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import io.undertow.server.HandlerWrapper;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.RoutingHandler;
import io.undertow.util.Headers;
import io.undertow.util.Methods;


@Singleton
public class OpenAPIService   extends BaseService implements Supplier<RoutingHandler>
{
	  
	private static Logger log = LoggerFactory.getLogger(OpenAPIService.class.getCanonicalName());
	
	protected final String resourcePathPrefix = "oas";
 
	
	@Inject
	@Named("openapi.resourcePrefix")
	protected String resourcePrefix;
	
	@Inject
	@Named("openapi.basePath")
	protected String basePath;
	
	@Inject
	@Named("openapi.specFilename")
	protected String specFilename;
	  
	@Inject
	@Named("openapi")
	protected Config openAPIConfig;
	
	@Inject
	@Named("application.name")
	protected String applicationName;
	
	@Inject
	@Named("openapi.port")
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
	
	@Inject
	@Named("registeredHandlerWrappers")
	protected Map<String,HandlerWrapper> registeredHandlerWrappers;
 
	protected ObjectMapper mapper = null;
	
	protected ObjectWriter writer = null; 
	
	protected ObjectMapper yamlMapper = null;
	
	protected Path resourcePath = null;
	
	protected ClassLoader serviceClassLoader = null;
	
	protected OpenAPI openApi = null;
	
	protected String spec = null;
	
	protected String indexHTML = null; 

 
	public OpenAPIService( )
	{ 
		mapper = Json.mapper();
		mapper.registerModule(new Jdk8Module()); 
		 
		yamlMapper = Yaml.mapper();
		
		
		writer = Yaml.pretty();
	}

	 
	@SuppressWarnings("rawtypes")
	public void generateSpec() throws Exception
	{
		
		Set<Class<?>> classes = this.registeredControllers;
		
		OpenAPIExtensions.setExtensions(Collections.singletonList(new ServerParameterExtension()));

		OpenAPI openApi = new OpenAPI();
				 
		Info info = mapper.convertValue(openAPIConfig.getValue("info").unwrapped(), Info.class);
		
		openApi.setInfo(info);
		 
		Map<String,SecurityScheme> securitySchemes = mapper.convertValue(openAPIConfig.getValue("securitySchemes").unwrapped(), new TypeReference<Map<String,SecurityScheme>>(){});
		
		if(openApi.getComponents() == null)
		{
			openApi.setComponents(new Components());
		}
		
		openApi.getComponents().setSecuritySchemes(securitySchemes);

		List<Server> servers = mapper.convertValue(openAPIConfig.getValue("servers").unwrapped(), new TypeReference<List<Server>>(){});

		openApi.setServers(servers);

		SwaggerConfiguration config = new SwaggerConfiguration()
				.resourceClasses(classes.stream().map( c -> c.getName()).collect(Collectors.toSet()))
				.openAPI(openApi);

		
		config.setModelConverterClassess(Collections.singleton(ServerModelResolver.class.getName()));

		OpenApiContext ctx = new GenericOpenApiContext()
				.openApiConfiguration(config)
				.openApiReader(new Reader(config))
				.openApiScanner(new JaxrsApplicationAndAnnotationScanner().openApiConfiguration(config))
				.init();

		openApi = ctx.read(); 
		
		this.openApi = openApi;
		
		this.spec = writer.writeValueAsString(openApi);
 		
	}

  
	public void generateHTML()
	{
		try
		{  
  
			try(InputStream templateInputStream = this.getClass().getClassLoader().getResourceAsStream(resourcePrefix + "/index.html"))
			{
				byte[] templateBytes = IOUtils.toByteArray(templateInputStream); 
				
				String templateString = new String(templateBytes,Charset.defaultCharset());
				
				templateString = templateString.replaceAll("\\{\\{ basePath \\}\\}", basePath);
				templateString = templateString.replaceAll("\\{\\{ title \\}\\}",applicationName + " Swagger UI");
				templateString = templateString.replaceAll("\\{\\{ filePath \\}\\}", basePath + ".yaml");
	
				this.indexHTML = templateString;   
			}
			 
  
			URL url = this.getClass().getClassLoader().getResource(resourcePrefix);
			
			if( url.toExternalForm().contains("!") )
			{
				log.debug("Copying OpenAPI resources...");

				String jarPathString = url.toExternalForm().substring(0, url.toExternalForm().indexOf("!") ).replaceAll("file:", "").replaceAll("jar:", "");
		 
				File srcFile = new File(jarPathString);
			
				try(JarFile jarFile = new JarFile(srcFile, false))
				{ 
					String appName = config.getString("application.name").replaceAll(" ", "_");
 					
					Path tmpDirParent = Files.createTempDirectory(appName);
					
					Path tmpDir = tmpDirParent.resolve("oas/");
					
					if(tmpDir.toFile().exists())
					{
						log.debug("Deleting existing OpenAPI directory at " + tmpDir);
						
						try
						{
							FileUtils.deleteDirectory(tmpDir.toFile());

						} catch (java.lang.IllegalArgumentException e)
						{
							log.debug("Tmp directory is not a directory...");
							tmpDir.toFile().delete();
						}
 					}
					
					java.nio.file.Files.createDirectory( tmpDir );
					
					this.resourcePath = tmpDir;
			 
					jarFile.stream().filter( ze ->  ze.getName().endsWith("js") || ze.getName().endsWith("css") || ze.getName().endsWith("map") || ze.getName().endsWith("html") ).forEach( ze -> {
						
						try
						{
							final InputStream entryInputStream = jarFile.getInputStream(ze);
							
							String filename = ze.getName().substring(resourcePrefix.length() + 1); 
							
							Path entryFilePath = tmpDir.resolve(filename); 

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
				this.resourcePath = Paths.get(this.getClass().getClassLoader().getResource(this.resourcePrefix).toURI());
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
		
		/*
		 * YAML path 
		 */
		
		String pathTemplate = this.applicationPath + File.separator + this.specFilename ;
		
		//FileResourceManager resourceManager = new FileResourceManager(this.resourcePath.toFile(),1024);
 				
		router.add(HttpMethod.GET, pathTemplate, new HttpHandler(){

			@Override
			public void handleRequest(HttpServerExchange exchange) throws Exception
			{ 
				exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, io.sinistral.proteus.server.MediaType.TEXT_YAML.contentType()); 
				
				
				try
				{
//					swaggerCopy.setHost(exchange.getHostAndPort());
//					
//					spec = writer.writeValueAsString(swaggerCopy);
					
				} catch (Exception e)
				{
					log.error(e.getMessage(),e);
				}
				
				exchange.getResponseSender().send(spec);
				
			}
			
		});
		
		this.registeredEndpoints.add(EndpointInfo.builder().withConsumes("*/*").withPathTemplate(pathTemplate).withControllerName(this.getClass().getSimpleName()).withMethod(Methods.GET).withProduces(io.sinistral.proteus.server.MediaType.TEXT_YAML.contentType()).build());
		
	  /*
		pathTemplate =  this.basePath;
		 
		router.add(HttpMethod.GET, pathTemplate , new HttpHandler(){

			@Override
			public void handleRequest(HttpServerExchange exchange) throws Exception
			{
 
 
 				exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, MediaType.TEXT_HTML);
 				exchange.getResponseSender().send(indexHTML);
				
			}
			
		});
 
		this.registeredEndpoints.add(EndpointInfo.builder().withConsumes(MediaType.WILDCARD).withProduces(MediaType.TEXT_HTML).withPathTemplate(pathTemplate).withControllerName(this.getClass().getSimpleName()).withMethod(Methods.GET).build());
 
 		
		try
		{
	  
			 pathTemplate =  this.basePath + "/*";
			 
			 router.add(HttpMethod.GET, pathTemplate, new ResourceHandler(resourceManager){

					@Override
					public void handleRequest(HttpServerExchange exchange) throws Exception
					{
						 
						String canonicalPath = CanonicalPathUtils.canonicalize((exchange.getRelativePath()));
					 
						canonicalPath =  canonicalPath.split(basePath)[1];  
						
						exchange.setRelativePath(canonicalPath);
						
						if(serviceClassLoader == null)
						{   
							super.handleRequest(exchange);
						}
						else
						{
							canonicalPath = resourcePrefix + canonicalPath;
							
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
			 
			 this.registeredEndpoints.add(EndpointInfo.builder().withConsumes(MediaType.WILDCARD).withProduces(MediaType.WILDCARD).withPathTemplate(pathTemplate).withControllerName(this.getClass().getSimpleName()).withMethod(Methods.GET).build());

 

		} catch (Exception e)
		{
			log.error(e.getMessage(),e);
		}
 		  
 		*/
		
		return router; 
	}

	 

	/* (non-Javadoc)
	 * @see com.google.common.util.concurrent.AbstractIdleService#startUp()
	 */
	@Override
	protected void startUp() throws Exception
	{
		// TODO Auto-generated method stub
		
		
		this.generateSpec();
		//this.generateHTML();
 
		log.debug("\nOpenAPI Spec:\n" +  writer.writeValueAsString(this.openApi));

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
