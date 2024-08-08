package io.sinistral.proteus.openapi.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.typesafe.config.Config;
import io.sinistral.proteus.openapi.jaxrs2.Reader;
import io.sinistral.proteus.openapi.jaxrs2.ServerModelResolver;
import io.sinistral.proteus.openapi.jaxrs2.ServerParameterExtension;
import io.sinistral.proteus.server.endpoints.EndpointInfo;
import io.sinistral.proteus.services.DefaultService;
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
import io.undertow.server.HttpServerExchange;
import io.undertow.server.RoutingHandler;
import io.undertow.server.handlers.ResponseCodeHandler;
import io.undertow.server.handlers.resource.FileResourceManager;
import io.undertow.server.handlers.resource.ResourceHandler;
import io.undertow.util.CanonicalPathUtils;
import io.undertow.util.Headers;
import io.undertow.util.Methods;
import jakarta.ws.rs.HttpMethod;
import jakarta.ws.rs.core.MediaType;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.concurrent.ForkJoinPool;
import java.util.function.Supplier;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

/**
 * A service for generating and serving an OpenAPI v3 spec and ui.
 *
 * @author jbauer
 */

@Singleton
public class OpenAPIService extends DefaultService implements Supplier<RoutingHandler> {

    private static Logger log = LoggerFactory.getLogger(OpenAPIService.class.getCanonicalName());

    protected ObjectMapper jsonMapper;

    protected Path resourcePath = null;

    protected ClassLoader serviceClassLoader = null;

    protected OpenAPI openApi = null;

    protected String yamlSpec = null;

    protected String jsonSpec = null;

    protected String indexHTML = null;

    protected String redocHTML = null;

    protected String elementsHTML = null;

    protected String rapidocHTML = null;

    protected final String resourcePrefix = "io/sinistral/proteus/openapi";

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
    @Named("openapi.redocPath")
    protected String redocPath;


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

    @Inject(optional = true)
    @Named("jackson.jsonView.queryParameterName")
    protected String jsonViewQueryParameterName;

    @Inject
    @Named("registeredHandlerWrappers")
    protected Map<String, HandlerWrapper> registeredHandlerWrappers;

    public OpenAPIService()
    {
        jsonMapper = Json.mapper();

        jsonMapper.registerModule(new Jdk8Module());
    }

    protected void generateHTML()
    {

        try
        {

            try (InputStream templateInputStream = this.getClass().getClassLoader().getResourceAsStream(resourcePrefix + "/index.html"))
            {

                byte[] templateBytes = IOUtils.toByteArray(templateInputStream);
                String templateString = new String(templateBytes, Charset.defaultCharset());

                templateString = templateString.replaceAll("\\{\\{ basePath \\}\\}", basePath);
                templateString = templateString.replaceAll("\\{\\{ title \\}\\}", applicationName + " Swagger UI");
                this.indexHTML = templateString;
            }

            try (InputStream templateInputStream = getClass().getClassLoader().getResourceAsStream(resourcePrefix + "/redoc.html"))
            {
                byte[] templateBytes = IOUtils.toByteArray(templateInputStream);

                this.redocHTML = new String(templateBytes, Charset.defaultCharset());
            }

            try (InputStream templateInputStream = getClass().getClassLoader().getResourceAsStream(resourcePrefix + "/rapidoc.html"))
            {
                byte[] templateBytes = IOUtils.toByteArray(templateInputStream);

                this.rapidocHTML = new String(templateBytes, Charset.defaultCharset());
            }


            try (InputStream templateInputStream = getClass().getClassLoader().getResourceAsStream(resourcePrefix + "/elements.html"))
            {
                byte[] templateBytes = IOUtils.toByteArray(templateInputStream);

                this.elementsHTML = new String(templateBytes, Charset.defaultCharset());
            }


            URL url = this.getClass().getClassLoader().getResource(resourcePrefix);

            if (url.toExternalForm().contains("!"))
            {
                log.debug("Copying OpenAPI resources...");

                String jarPathString = url.toExternalForm().substring(0, url.toExternalForm().indexOf("!")).replaceAll("file:", "").replaceAll("jar:", "");
                File srcFile = new File(jarPathString);

                try (JarFile jarFile = new JarFile(srcFile, false))
                {

                    String appName = config.getString("application.name").replaceAll(" ", "_");
                    Path tmpDirParent = Files.createTempDirectory(appName);
                    Path tmpDir = tmpDirParent.resolve("openapi/");

                    if (tmpDir.toFile().exists())
                    {
                        log.debug("Deleting existing OpenAPI directory at " + tmpDir);

                        try
                        {
                            FileUtils.deleteDirectory(tmpDir.toFile());
                        } catch (IllegalArgumentException e)
                        {

                            log.debug("Tmp directory is not a directory...");
                            tmpDir.toFile().delete();
                        }
                    }

                    Files.createDirectory(tmpDir);

                    this.resourcePath = tmpDir;

                    jarFile.stream().filter(ze -> ze.getName().endsWith("js") || ze.getName().endsWith("css") || ze.getName().endsWith("map") || ze.getName().endsWith("html"))
                           .forEach(ze ->
                           {
                               try
                               {

                                   final InputStream entryInputStream = jarFile.getInputStream(ze);
                                   String filename = ze.getName().substring(resourcePrefix.length() + 1);
                                   Path entryFilePath = tmpDir.resolve(filename);

                                   Files.createDirectories(entryFilePath.getParent());
                                   Files.copy(entryInputStream, entryFilePath, StandardCopyOption.REPLACE_EXISTING);

                               } catch (Exception e)
                               {
                                   log.error(e.getMessage() + " for entry " + ze.getName());
                               }
                           });

                    Runtime.getRuntime().addShutdownHook(new Thread(() -> {

                        try
                        {
                            FileUtils.deleteDirectory(tmpDirParent.toFile());
                        } catch (IOException ex)
                        {
                            log.error("Failed to delete temp openapi directory", ex);
                        }
                    }));
                }
            }
            else
            {
                this.resourcePath = Paths.get(this.getClass().getClassLoader().getResource(this.resourcePrefix).toURI());

                this.serviceClassLoader = this.getClass().getClassLoader();
            }

        } catch (Exception e)
        {
            log.error(e.getMessage(), e);
        }
    }

    @SuppressWarnings("rawtypes")
    protected void generateSpec() throws Exception
    {

        Set<Class<?>> classes = this.registeredControllers;

        OpenAPIExtensions.setExtensions(Collections.singletonList(new ServerParameterExtension()));

        OpenAPI openApi = new OpenAPI();

        Info info = jsonMapper.convertValue(openAPIConfig.getValue("info").unwrapped(), Info.class);

        openApi.setInfo(info);

        Map<String, SecurityScheme> securitySchemes = jsonMapper.convertValue(openAPIConfig.getValue("securitySchemes").unwrapped(), new TypeReference<Map<String, SecurityScheme>>() {});

        if (openApi.getComponents() == null)
        {
            openApi.setComponents(new Components());
        }

        openApi.getComponents().setSecuritySchemes(securitySchemes);

        List<Server> servers = jsonMapper.convertValue(openAPIConfig.getValue("servers").unwrapped(), new TypeReference<List<Server>>() {});

        openApi.setServers(servers);

        SwaggerConfiguration config = new SwaggerConfiguration().resourceClasses(classes.stream().map(Class::getName).collect(Collectors.toSet())).openAPI(openApi);

        if (jsonViewQueryParameterName != null)
        {

            if (config.getUserDefinedOptions() == null)
            {
                config.setUserDefinedOptions(new HashMap<>());
            }

            config.getUserDefinedOptions().put("jsonViewQueryParameterName", jsonViewQueryParameterName);
        }

        Set<String> modelConverterClasses = new HashSet<>();

        modelConverterClasses.add(ServerModelResolver.class.getName());

        List<String> additionalConverterClasses = openAPIConfig.getStringList("converterClasses");

        modelConverterClasses.addAll(additionalConverterClasses);

        config.setModelConverterClassess(modelConverterClasses);

        OpenApiContext ctx = new GenericOpenApiContext().openApiConfiguration(config)
                                                        .openApiReader(new Reader(config))
                                                        .openApiScanner(new JaxrsApplicationAndAnnotationScanner().openApiConfiguration(config))
                                                        .init();

        openApi = ctx.read();

        this.openApi = openApi;

        this.yamlSpec = Yaml.pretty().writeValueAsString(openApi);

        this.jsonSpec = Json.pretty().writeValueAsString(openApi);

    }

    public OpenAPI getOpenApi() {

        return openApi;
    }

    public String getYamlSpec() {

        return yamlSpec;
    }

    public String getJsonSpec() {

        return jsonSpec;
    }

    @Override
    protected void startUp() throws Exception
    {

        super.startUp();

        generateHTML();

       ForkJoinPool.commonPool().execute(() ->
        {
            try
            {
                generateSpec();

                log.debug("\nOpenAPI Spec:\n{}", Yaml.pretty().writeValueAsString(this.openApi));

            } catch (Exception e)
            {
                log.error("Error generating OpenAPI spec", e);
            }

        });

        router.addAll(this.get());
    }

    public RoutingHandler get()
    {

        FileResourceManager resourceManager = new FileResourceManager(this.resourcePath.toFile(), 1024);

        RoutingHandler router = new RoutingHandler();

        router.add(HttpMethod.GET, basePath, (HttpServerExchange exchange) ->
        {
            exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, MediaType.TEXT_HTML);
            exchange.getResponseSender().send(indexHTML);
        });

        /*
         * YAML path
         */

        String yamlTemplatePath = String.format("%s/%s.yaml", this.applicationPath, this.specFilename);

        router.add(HttpMethod.GET, yamlTemplatePath, (HttpServerExchange exchange) ->
        {

            String spec = getYamlSpec();

            if(spec == null)
            {
                exchange.setStatusCode(404).setReasonPhrase("Spec has not yet been generated").endExchange();
                return;
            }

            exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, io.sinistral.proteus.protocol.MediaType.TEXT_YAML.contentType());

            exchange.getResponseSender().send(spec);
        });

        this.registeredEndpoints.add(EndpointInfo.builder()
                                                 .withConsumes("*/*")
                                                 .withPathTemplate(yamlTemplatePath)
                                                 .withControllerName(this.getClass().getSimpleName())
                                                 .withMethod(Methods.GET)
                                                 .withProduces(io.sinistral.proteus.protocol.MediaType.TEXT_YAML.contentType())
                                                 .build());

        this.registeredEndpoints.add(EndpointInfo.builder()
                                                 .withConsumes(MediaType.WILDCARD)
                                                 .withProduces(MediaType.TEXT_HTML)
                                                 .withPathTemplate(yamlTemplatePath)
                                                 .withControllerName(this.getClass().getSimpleName())
                                                 .withMethod(Methods.GET)
                                                 .build());

        String jsonTemplatePath = String.format("%s/%s.json", this.applicationPath, this.specFilename);

        router.add(HttpMethod.GET, jsonTemplatePath, (HttpServerExchange exchange) ->
        {
            final String spec = this.getJsonSpec();

            if(spec == null)
            {
                exchange.setStatusCode(404).setReasonPhrase("Spec has not yet been generated").endExchange();
                return;
            }

            exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, io.sinistral.proteus.protocol.MediaType.JSON.contentType());

            exchange.getResponseSender().send(spec);
        });

        this.registeredEndpoints.add(EndpointInfo.builder()
                                                 .withConsumes("*/*")
                                                 .withPathTemplate(jsonTemplatePath)
                                                 .withControllerName(this.getClass().getSimpleName())
                                                 .withMethod(Methods.GET)
                                                 .withProduces(io.sinistral.proteus.protocol.MediaType.JSON.contentType())
                                                 .build());


        final String specPath = yamlTemplatePath;

        router.add(HttpMethod.GET, this.basePath + "/" + this.redocPath, (HttpServerExchange exchange) ->
        {
            exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, MediaType.TEXT_HTML);

            final String fullPath = String.format("%s://%s%s", exchange.getRequestScheme(), exchange.getHostAndPort(), specPath);

            final String html = redocHTML.replaceAll("\\{\\{ specPath \\}\\}", fullPath);

            exchange.getResponseSender().send(html);
        });

        this.registeredEndpoints.add(EndpointInfo.builder()
                .withConsumes(MediaType.WILDCARD)
                .withProduces(MediaType.TEXT_HTML)
                .withPathTemplate(this.basePath + "/" + this.redocPath)
                .withControllerName(this.getClass().getSimpleName())
                .withMethod(Methods.GET)
                .build());


        router.add(HttpMethod.GET, this.basePath + "/rapidoc", (HttpServerExchange exchange) ->
        {
            exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, MediaType.TEXT_HTML);

            final String fullPath = String.format("//%s%s", exchange.getHostAndPort(), specPath);

            final String html = rapidocHTML.replaceAll("\\{\\{ specPath \\}\\}", fullPath);

            exchange.getResponseSender().send(html);
        });

        this.registeredEndpoints.add(EndpointInfo.builder()
                .withConsumes(MediaType.WILDCARD)
                .withProduces(MediaType.TEXT_HTML)
                .withPathTemplate(this.basePath + "/rapidoc")
                .withControllerName(this.getClass().getSimpleName())
                .withMethod(Methods.GET)
                .build());


        router.add(HttpMethod.GET, this.basePath + "/elements", (HttpServerExchange exchange) ->
        {
            exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, MediaType.TEXT_HTML);

            final String fullPath = String.format("//%s%s",   exchange.getHostAndPort(), specPath);

            final String html = elementsHTML.replaceAll("\\{\\{ specPath \\}\\}", fullPath);

            exchange.getResponseSender().send(html);
        });

        this.registeredEndpoints.add(EndpointInfo.builder()
                .withConsumes(MediaType.WILDCARD)
                .withProduces(MediaType.TEXT_HTML)
                .withPathTemplate(this.basePath + "/elements")
                .withControllerName(this.getClass().getSimpleName())
                .withMethod(Methods.GET)
                .build());



        try
        {

            String wildcardPathTemplate = this.basePath + "/*";

            router.add(HttpMethod.GET,
                    wildcardPathTemplate,
                    new ResourceHandler(resourceManager) {
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

                                    io.sinistral.proteus.protocol.MediaType mediaType = io.sinistral.proteus.protocol.MediaType.getByFileName(canonicalPath);

                                    exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, mediaType.toString());
                                    exchange.getResponseSender().send(ByteBuffer.wrap(resourceBytes));
                                }
                            }
                        }
                    });

            this.registeredEndpoints.add(EndpointInfo.builder()
                                                     .withConsumes(MediaType.WILDCARD)
                                                     .withProduces(MediaType.WILDCARD)
                                                     .withPathTemplate(wildcardPathTemplate)
                                                     .withControllerName(this.getClass().getSimpleName())
                                                     .withMethod(Methods.GET)
                                                     .build());

        } catch (Exception e)
        {
            log.error(e.getMessage(), e);
        }

        return router;
    }

}
