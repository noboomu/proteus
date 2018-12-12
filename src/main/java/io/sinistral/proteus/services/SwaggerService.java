
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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
import java.util.jar.JarFile;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.core.MediaType;

import io.swagger.models.Response;
import io.swagger.util.Json;
import io.swagger.util.Yaml;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigObject;

import io.sinistral.proteus.server.endpoints.EndpointInfo;
import io.sinistral.proteus.server.security.MapIdentityManager;
import io.sinistral.proteus.server.tools.swagger.ServerParameterExtension;
import io.swagger.jaxrs.ext.SwaggerExtension;
import io.swagger.jaxrs.ext.SwaggerExtensions;
import io.swagger.models.Info;
import io.swagger.models.Swagger;
import io.swagger.models.auth.ApiKeyAuthDefinition;
import io.swagger.models.auth.BasicAuthDefinition;
import io.undertow.attribute.ExchangeAttribute;
import io.undertow.attribute.ExchangeAttributes;
import io.undertow.predicate.Predicate;
import io.undertow.predicate.Predicates;
import io.undertow.security.api.AuthenticationMechanism;
import io.undertow.security.api.AuthenticationMode;
import io.undertow.security.handlers.AuthenticationCallHandler;
import io.undertow.security.handlers.AuthenticationConstraintHandler;
import io.undertow.security.handlers.AuthenticationMechanismsHandler;
import io.undertow.security.handlers.SecurityInitialHandler;
import io.undertow.security.idm.IdentityManager;
import io.undertow.security.impl.BasicAuthenticationMechanism;
import io.undertow.server.HandlerWrapper;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.RoutingHandler;
import io.undertow.server.handlers.PredicateHandler;
import io.undertow.server.handlers.ResponseCodeHandler;
import io.undertow.server.handlers.resource.FileResourceManager;
import io.undertow.server.handlers.resource.ResourceHandler;
import io.undertow.util.CanonicalPathUtils;
import io.undertow.util.Headers;
import io.undertow.util.HttpString;
import io.undertow.util.Methods;

@Singleton
public class SwaggerService extends BaseService implements Supplier<RoutingHandler>
{

	private static Logger log = LoggerFactory.getLogger(SwaggerService.class.getCanonicalName());

	protected io.sinistral.proteus.server.tools.swagger.Reader reader = null;

	protected final String resourcePathPrefix = "swagger";

	@Inject
	@Named("swagger.resourcePrefix")
	protected String resourcePrefix;

	@Inject
	@Named("swagger.basePath")
	protected String basePath;

	@Inject
	@Named("swagger.theme")
	protected String theme;

	@Inject
	@Named("swagger.specFilename")
	protected String specFilename;

	@Inject
	@Named("swagger")
	protected Config swaggerConfig;

	@Inject
	@Named("swagger.security")
	protected Config securityConfig;

	@Inject
	@Named("swagger.redocPath")
	protected String redocPath;

	@Inject
	@Named("swagger.host")
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

	@Inject
	@Named("registeredHandlerWrappers")
	protected Map<String, HandlerWrapper> registeredHandlerWrappers;

	protected ObjectMapper mapper;

	protected ObjectWriter writer;

	protected ObjectMapper yamlMapper;

	protected Path swaggerResourcePath = null;

	protected ClassLoader serviceClassLoader = null;

	protected Swagger swagger = null;

	protected String swaggerSpec = null;

	protected String swaggerIndexHTML = null;

	protected String redocHTML = null;
	

	@SuppressWarnings("deprecation")
	public SwaggerService()
	{
		mapper = Json.mapper();

		writer = mapper.writerWithDefaultPrettyPrinter();
		writer = writer.without(SerializationFeature.WRITE_NULL_MAP_VALUES);
		yamlMapper = Yaml.mapper();
	}

	public void generateSwaggerSpec() throws Exception
	{

		Set<Class<?>> classes = this.registeredControllers;
		
		log.debug("registeredControllers: " + this.registeredControllers);

		List<SwaggerExtension> extensions = new ArrayList<>();

		extensions.add(new ServerParameterExtension());

		SwaggerExtensions.setExtensions(extensions);

		log.debug("Added SwaggerExtension: ServerParameterExtension");

		Swagger swagger = new Swagger();

		swagger.setBasePath(applicationPath);

		swagger.setHost(host + ((port != 80 && port != 443) ? ":" + port : ""));

		Info info = mapper.convertValue(swaggerConfig.getValue("info").unwrapped(), Info.class);

		swagger.setInfo(info);

//		if (securityConfig.hasPath("apiKeys"))
//		{
//			List<? extends ConfigObject> apiKeys = securityConfig.getObjectList("apiKeys");
//
//			for (ConfigObject apiKey : apiKeys)
//			{
//				Config apiKeyConfig = apiKey.toConfig();
//
//				String key = apiKeyConfig.getString("key");
//				String name = apiKeyConfig.getString("name");
//				String value = apiKeyConfig.getString("value");
//
//				io.swagger.models.auth.In keyLocation = io.swagger.models.auth.In.valueOf(apiKeyConfig.getString("in"));
//
//				final Predicate predicate;
//
//				switch (keyLocation)
//				{
//					case HEADER:
//					{
//						ExchangeAttribute[] attributes = new ExchangeAttribute[] { ExchangeAttributes.requestHeader(HttpString.tryFromString(name)),
//								ExchangeAttributes.constant(value) };
//						predicate = Predicates.equals(attributes);
//						break;
//					}
//					case QUERY:
//					{
//						predicate = Predicates.contains(ExchangeAttributes.queryString(), value);
//						break;
//					}
//					default:
//						predicate = Predicates.truePredicate();
//						break;
//				}
//
//				if (predicate != null)
//				{
//					log.debug("Adding apiKey handler " + name + " in " + keyLocation + " named " + key);
//
//					final HandlerWrapper wrapper = new HandlerWrapper()
//					{
//						@Override
//						public HttpHandler wrap(final HttpHandler handler)
//						{
//							return new PredicateHandler(predicate, handler, ResponseCodeHandler.HANDLE_403);
//						}
//					};
//
//					ApiKeyAuthDefinition keyAuthDefinition = new ApiKeyAuthDefinition(name, keyLocation);
//					swagger.addSecurityDefinition(key, keyAuthDefinition);
//
//					registeredHandlerWrappers.put(key, wrapper);
//				}
//			}
//		}
//
//		if (securityConfig.hasPath("basicRealms"))
//		{
//			List<? extends ConfigObject> realms = securityConfig.getObjectList("basicRealms");
//
//			for (ConfigObject realm : realms)
//			{
//				Config realmConfig = realm.toConfig();
//
//				final String name = realmConfig.getString("name");
//
//				List<String> identities = realmConfig.getStringList("identities");
//
//				final Map<String, char[]> identityMap = new HashMap<>();
//
//				identities.stream().forEach(i ->
//				{
//					String[] identity = i.split(":");
//
//					identityMap.put(identity[0], identity[1].toCharArray());
//				});
//
//				final IdentityManager identityManager = new MapIdentityManager(identityMap);
//
//				log.debug("Adding basic handler for realm " + name + " with identities " + identityMap);
//
//				final HandlerWrapper wrapper = new HandlerWrapper()
//				{
//					@Override
//					public HttpHandler wrap(final HttpHandler handler)
//					{
//						HttpHandler authHandler = new AuthenticationCallHandler(handler);
//						authHandler = new AuthenticationConstraintHandler(authHandler);
//						final List<AuthenticationMechanism> mechanisms = Collections.<AuthenticationMechanism> singletonList(new BasicAuthenticationMechanism(name));
//						authHandler = new AuthenticationMechanismsHandler(authHandler, mechanisms);
//						authHandler = new SecurityInitialHandler(AuthenticationMode.PRO_ACTIVE, identityManager, authHandler);
//						return authHandler;
//					}
//				};
//
//				BasicAuthDefinition authDefinition = new BasicAuthDefinition();
//				swagger.addSecurityDefinition(name, authDefinition);
//
//				registeredHandlerWrappers.put(name, wrapper);
//
//			}
//		}

		this.reader = new io.sinistral.proteus.server.tools.swagger.Reader(swagger);

		classes.forEach(c -> this.reader.read(c));

		this.swagger = this.reader.getSwagger();
		
		this.swaggerSpec = writer.writeValueAsString(swagger);

	}

	public Swagger getSwagger()
	{
		return swagger;
	}

	public void setSwagger(Swagger swagger)
	{
		this.swagger = swagger;
	}

	protected void generateSwaggerHTML()
	{
		try
		{

			try (InputStream templateInputStream = getClass().getClassLoader().getResourceAsStream(resourcePrefix + "/index.html"))
			{
				byte[] templateBytes = IOUtils.toByteArray(templateInputStream);

				String templateString = new String(templateBytes, Charset.defaultCharset());

				String themePath = "swagger-ui.css";

				if (!theme.equals("default"))
				{
					themePath = "themes/theme-" + theme + ".css";
				}

				templateString = templateString.replaceAll("\\{\\{ themePath \\}\\}", themePath);
				templateString = templateString.replaceAll("\\{\\{ swaggerBasePath \\}\\}", basePath);
				templateString = templateString.replaceAll("\\{\\{ title \\}\\}", applicationName + " Swagger UI");
				templateString = templateString.replaceAll("\\{\\{ swaggerFilePath \\}\\}", basePath + ".json");

				this.swaggerIndexHTML = templateString;
			}

			try (InputStream templateInputStream = getClass().getClassLoader().getResourceAsStream(resourcePrefix + "/redoc.html"))
			{
				byte[] templateBytes = IOUtils.toByteArray(templateInputStream);

				String templateString = new String(templateBytes, Charset.defaultCharset());

				templateString = templateString.replaceAll("\\{\\{ specPath \\}\\}", this.basePath + ".json");
				templateString = templateString.replaceAll("\\{\\{ applicationName \\}\\}", applicationName);

				this.redocHTML = templateString;
			}

			URL url = getClass().getClassLoader().getResource(resourcePrefix);

			if (url.toExternalForm().contains("!"))
			{
				log.debug("Copying Swagger resources...");

				String appName = config.getString("application.name").replaceAll(" ", "_");

				Path tmpDirParent = Files.createTempDirectory(appName);

				Path swaggerTmpDir = tmpDirParent.resolve("swagger/");

				String jarPathString = url.toExternalForm().substring(0, url.toExternalForm().indexOf("!")).replaceAll("file:", "").replaceAll("jar:", "");

				File srcFile = new File(jarPathString);

				try (JarFile jarFile = new JarFile(srcFile, false))
				{

					if (swaggerTmpDir.toFile().exists())
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

					java.nio.file.Files.createDirectory(swaggerTmpDir);

					this.swaggerResourcePath = swaggerTmpDir;

					jarFile.stream().filter(ze -> ze.getName().endsWith("js") || ze.getName().endsWith("css") || ze.getName().endsWith("map") || ze.getName().endsWith("html"))
							.forEach(ze ->
							{

								try
								{
									final InputStream entryInputStream = jarFile.getInputStream(ze);

									String filename = ze.getName().substring(resourcePrefix.length() + 1);

									Path entryFilePath = swaggerTmpDir.resolve(filename);

									java.nio.file.Files.createDirectories(entryFilePath.getParent());

									java.nio.file.Files.copy(entryInputStream, entryFilePath, StandardCopyOption.REPLACE_EXISTING);

								} catch (Exception e)
								{
									log.error(e.getMessage() + " for entry " + ze.getName());
								}
							});
				}
			}
			else
			{
				this.swaggerResourcePath = Paths.get(this.getClass().getClassLoader().getResource(this.resourcePrefix).toURI());
				this.serviceClassLoader = this.getClass().getClassLoader();
			}

		} catch (Exception e)
		{
			log.error(e.getMessage(), e);
		}
	}

	public RoutingHandler get()
	{

		RoutingHandler router = new RoutingHandler();

		/*
		 * JSON path
		 */

		String pathTemplate = this.basePath + ".json";

		FileResourceManager resourceManager = new FileResourceManager(this.swaggerResourcePath.toFile(), 1024);

 
		router.add(HttpMethod.GET, pathTemplate, (HttpServerExchange exchange) ->
		{
			final Swagger swaggerCopy = swagger;

			exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, MediaType.APPLICATION_JSON);

			String spec = null;

			try
			{
				swaggerCopy.setHost(exchange.getHostAndPort());

				spec = writer.writeValueAsString(swaggerCopy);

			} catch (Exception e)
			{
				log.error(e.getMessage(), e);
			}

			exchange.getResponseSender().send(spec);

		});

		this.registeredEndpoints.add(
										EndpointInfo.builder().withConsumes("*/*").withPathTemplate(pathTemplate).withControllerName(this.getClass().getSimpleName())
												.withMethod(Methods.GET).withProduces(MediaType.APPLICATION_JSON).build());

		/*
		 * YAML path
		 */

		pathTemplate = this.basePath + ".yaml";

		router.add(HttpMethod.GET, pathTemplate, (HttpServerExchange exchange) ->
		{
			exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, io.sinistral.proteus.server.MediaType.TEXT_YAML.contentType());

				String spec = null;

				final Swagger swaggerCopy = swagger;

				try
				{
					swaggerCopy.setHost(exchange.getHostAndPort());

					spec = yamlMapper.writeValueAsString(swaggerCopy);

				} catch (Exception e)
				{
					log.error(e.getMessage(), e);
				}

				exchange.getResponseSender().send(spec);
		});

		this.registeredEndpoints.add(
										EndpointInfo.builder().withConsumes("*/*").withPathTemplate(pathTemplate).withControllerName(this.getClass().getSimpleName())
												.withMethod(Methods.GET).withProduces(io.sinistral.proteus.server.MediaType.TEXT_YAML.contentType()).build());

		pathTemplate = this.basePath + "/" + this.redocPath;

		router.add(HttpMethod.GET, pathTemplate, (HttpServerExchange exchange) ->
		{
			exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, MediaType.TEXT_HTML);
			exchange.getResponseSender().send(redocHTML);
		});

		this.registeredEndpoints.add(
										EndpointInfo.builder().withConsumes("*/*").withPathTemplate(pathTemplate).withControllerName(this.getClass().getSimpleName())
												.withMethod(Methods.GET).withProduces(MediaType.TEXT_HTML).build());

		pathTemplate = this.basePath;

		router.add(HttpMethod.GET, pathTemplate, (HttpServerExchange exchange) ->
		{
			exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, MediaType.TEXT_HTML);
			exchange.getResponseSender().send(swaggerIndexHTML);
		});

		this.registeredEndpoints.add(
										EndpointInfo.builder().withConsumes(MediaType.WILDCARD).withProduces(MediaType.TEXT_HTML).withPathTemplate(pathTemplate)
												.withControllerName(this.getClass().getSimpleName()).withMethod(Methods.GET).build());

		try
		{

			pathTemplate = this.basePath + "/*";

			router.add(HttpMethod.GET, pathTemplate, new ResourceHandler(resourceManager)
			{

				@Override
				public void handleRequest(HttpServerExchange exchange) throws Exception
				{

					String canonicalPath = CanonicalPathUtils.canonicalize((exchange.getRelativePath()));

					canonicalPath = canonicalPath.split(basePath)[1];

					exchange.setRelativePath(canonicalPath);

					if (serviceClassLoader == null)
					{
						super.handleRequest(exchange);
					}
					else
					{
						canonicalPath = resourcePrefix + canonicalPath;

						try (final InputStream resourceInputStream = serviceClassLoader.getResourceAsStream(canonicalPath))
						{

							if (resourceInputStream == null)
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

			this.registeredEndpoints.add(
											EndpointInfo.builder().withConsumes(MediaType.WILDCARD).withProduces(MediaType.WILDCARD).withPathTemplate(pathTemplate)
													.withControllerName(this.getClass().getSimpleName()).withMethod(Methods.GET).build());

		} catch (Exception e)
		{
			log.error(e.getMessage(), e);
		}

		return router;
	}

	@Override
	protected void startUp() throws Exception
	{
		super.startUp();

		this.generateSwaggerHTML();

		CompletableFuture.runAsync(() ->
		{

			try
			{

				generateSwaggerSpec();

				log.info("\nSwagger Spec:\n" + writer.writeValueAsString(swagger));

			} catch (Exception e)
			{
				log.error("Error generating swagger spec.", e);
			}

		});

		router.addAll(this.get());
	}

	@Override
	protected void shutDown() throws Exception
	{

	}

}
