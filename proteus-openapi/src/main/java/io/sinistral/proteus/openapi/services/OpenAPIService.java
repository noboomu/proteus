package io.sinistral.proteus.openapi.services;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.typesafe.config.Config;
import io.sinistral.proteus.openapi.converter.ModelConverters;
import io.sinistral.proteus.openapi.integration.SwaggerConfiguration;
import io.sinistral.proteus.openapi.jaxrs2.OpenAPIExtensions;
import io.sinistral.proteus.openapi.jaxrs2.Reader;
import io.sinistral.proteus.openapi.jaxrs2.ServerModelResolver;
import io.sinistral.proteus.openapi.jaxrs2.ServerParameterExtension;
import io.sinistral.proteus.openapi.models.Components;
import io.sinistral.proteus.openapi.models.OpenAPI;
import io.sinistral.proteus.openapi.models.OpenAPI.SpecVersion;
import io.sinistral.proteus.openapi.models.info.Info;
import io.sinistral.proteus.openapi.models.security.SecurityScheme;
import io.sinistral.proteus.openapi.models.servers.Server;
import io.sinistral.proteus.openapi.util.Json;
import io.sinistral.proteus.server.endpoints.EndpointInfo;
import io.sinistral.proteus.services.DefaultService;
import io.undertow.server.HandlerWrapper;
import io.undertow.server.HttpHandler;
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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.jar.JarFile;
import java.util.stream.Collectors;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

/**
 * A service for generating and serving an OpenAPI v3 spec and ui.
 *
 * @author jbauer
 */

@Singleton
public class OpenAPIService
    extends DefaultService
    implements Supplier<RoutingHandler> {

    private static Logger log = LoggerFactory.getLogger(
        OpenAPIService.class.getCanonicalName()
    );

    protected ObjectMapper jsonMapper;

    protected Path resourcePath = null;

    protected ClassLoader serviceClassLoader = null;

    protected OpenAPI openApi = null;

    protected String yamlSpec = null;

    protected String jsonSpec = null;

    protected String indexHTML = null;

    protected String redocHTML = null;

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

    ExecutorService executor = Executors.newSingleThreadExecutor();

    public OpenAPIService() {
        jsonMapper = Json.mapper();
    }

    /**
     *   public static ObjectMapper create(JsonFactory jsonFactory, boolean openapi31) {
           ObjectMapper mapper = jsonFactory == null ? new ObjectMapper() : new ObjectMapper(jsonFactory);

           if (!openapi31) {
               // handle ref schema serialization skipping all other props
               mapper.registerModule(new SimpleModule() {
                   @Override
                   public void setupModule(SetupContext context) {
                       super.setupModule(context);
                       context.addBeanSerializerModifier(new BeanSerializerModifier() {
                           @Override
                           public JsonSerializer<?> modifySerializer(
                                   SerializationConfig config, BeanDescription desc, JsonSerializer<?> serializer) {
                               if (Schema.class.isAssignableFrom(desc.getBeanClass())) {
                                   return new SchemaSerializer((JsonSerializer<Object>) serializer);
                               } else if (MediaType.class.isAssignableFrom(desc.getBeanClass())) {
                                   return new MediaTypeSerializer((JsonSerializer<Object>) serializer);
                               } else if (Example.class.isAssignableFrom(desc.getBeanClass())) {
                                   return new ExampleSerializer((JsonSerializer<Object>) serializer);
                               }
                               return serializer;
                           }
                       });
                   }
               });
           } else {
     */

    protected void generateHTML() {
        try {
            try (
                InputStream templateInputStream = this.getClass()
                    .getClassLoader()
                    .getResourceAsStream(resourcePrefix + "/index.html")
            ) {
                byte[] templateBytes = IOUtils.toByteArray(templateInputStream);
                String templateString = new String(
                    templateBytes,
                    Charset.defaultCharset()
                );

                templateString = templateString.replaceAll(
                    "\\{\\{ basePath \\}\\}",
                    basePath
                );
                templateString = templateString.replaceAll(
                    "\\{\\{ title \\}\\}",
                    applicationName + " Swagger UI"
                );
                this.indexHTML = templateString;
            }

            try (
                InputStream templateInputStream = getClass()
                    .getClassLoader()
                    .getResourceAsStream(resourcePrefix + "/redoc.html")
            ) {
                byte[] templateBytes = IOUtils.toByteArray(templateInputStream);

                this.redocHTML = new String(
                    templateBytes,
                    Charset.defaultCharset()
                );
            }

            URL url = this.getClass()
                .getClassLoader()
                .getResource(resourcePrefix);

            assert url != null;

            if (url.toExternalForm().contains("!")) {
                log.debug("Copying OpenAPI resources...");

                String jarPathString = url
                    .toExternalForm()
                    .substring(0, url.toExternalForm().indexOf("!"))
                    .replaceAll("file:", "")
                    .replaceAll("jar:", "");
                File srcFile = new File(jarPathString);

                try (JarFile jarFile = new JarFile(srcFile, false)) {
                    String appName = config
                        .getString("application.name")
                        .replaceAll(" ", "_");
                    Path tmpDirParent = Files.createTempDirectory(appName);
                    Path tmpDir = tmpDirParent.resolve("openapi/");

                    if (tmpDir.toFile().exists()) {
                        log.debug(
                            "Deleting existing OpenAPI directory at {}",
                            tmpDir
                        );

                        try {
                            FileUtils.deleteDirectory(tmpDir.toFile());
                        } catch (IllegalArgumentException e) {
                            log.debug("Tmp directory is not a directory...");
                            tmpDir.toFile().delete();
                        }
                    }

                    Files.createDirectory(tmpDir);

                    this.resourcePath = tmpDir;

                    jarFile
                        .stream()
                        .filter(
                            ze ->
                                ze.getName().endsWith("js") ||
                                ze.getName().endsWith("css") ||
                                ze.getName().endsWith("map") ||
                                ze.getName().endsWith("html")
                        )
                        .forEach(ze -> {
                            try {
                                final InputStream entryInputStream =
                                    jarFile.getInputStream(ze);
                                String filename = ze
                                    .getName()
                                    .substring(resourcePrefix.length() + 1);
                                Path entryFilePath = tmpDir.resolve(filename);

                                Files.createDirectories(
                                    entryFilePath.getParent()
                                );
                                Files.copy(
                                    entryInputStream,
                                    entryFilePath,
                                    StandardCopyOption.REPLACE_EXISTING
                                );
                            } catch (Exception e) {
                                log.error(
                                    "{} for entry {}",
                                    e.getMessage(),
                                    ze.getName()
                                );
                            }
                        });

                    Runtime.getRuntime().addShutdownHook(
                        new Thread(() -> {
                            try {
                                FileUtils.deleteDirectory(
                                    tmpDirParent.toFile()
                                );
                            } catch (IOException ex) {
                                log.error(
                                    "Failed to delete temp openapi directory",
                                    ex
                                );
                            }
                        })
                    );
                }
            } else {
                this.resourcePath = Paths.get(
                    Objects.requireNonNull(
                        this.getClass()
                            .getClassLoader()
                            .getResource(this.resourcePrefix)
                    ).toURI()
                );

                this.serviceClassLoader = this.getClass().getClassLoader();
            }
        } catch (Exception e) {
            log.error("Failed to generate html", e);
        }
    }

    @SuppressWarnings("rawtypes")
    protected void generateSpec() throws Exception {
        Set<Class<?>> classes = this.registeredControllers;

        OpenAPIExtensions.register(new ServerParameterExtension());

        OpenAPI openApi = new OpenAPI(SpecVersion.V31);

        openApi.setOpenapi("3.1.1");

        Info info = jsonMapper.convertValue(
            openAPIConfig.getValue("info").unwrapped(),
            Info.class
        );

        openApi.setInfo(info);

        Map<String, SecurityScheme> securitySchemes = jsonMapper.convertValue(
            openAPIConfig.getValue("securitySchemes").unwrapped(),
            new TypeReference<>() {}
        );

        if (openApi.getComponents() == null) {
            openApi.setComponents(new Components());
        }

        openApi.getComponents().setSecuritySchemes(securitySchemes);

        List<Server> servers = jsonMapper.convertValue(
            openAPIConfig.getValue("servers").unwrapped(),
            new TypeReference<>() {}
        );

        openApi.setServers(servers);

        SwaggerConfiguration config = new SwaggerConfiguration()
            .resourceClasses(
                classes.stream().map(Class::getName).collect(Collectors.toSet())
            )
            .openAPI(openApi);

        if (jsonViewQueryParameterName != null) {
            if (config.getUserDefinedOptions() == null) {
                config.setUserDefinedOptions(new HashMap<>());
            }

            config
                .getUserDefinedOptions()
                .put("jsonViewQueryParameterName", jsonViewQueryParameterName);
        }

        // Register ServerModelResolver with ModelConverters
        ModelConverters.getInstance().addConverter(new ServerModelResolver(jsonMapper));

        // Use Reader directly to scan and generate OpenAPI
        Reader reader = new Reader(config);
        openApi = reader.read(classes);

        this.openApi = openApi;

        // Generate YAML spec (for now, use JSON - YAML support can be added later)
        this.yamlSpec = Json.pretty(openApi);

        this.jsonSpec = Json.pretty(openApi);
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

    private volatile boolean specGenerated = false;
    private volatile Exception specGenerationError = null;

    @Override
    protected void startUp() throws Exception {
        super.startUp();

        generateHTML();

        log.info("Adding OpenAPI routes to main router...");
        router.addAll(this.get());
        log.info("OpenAPI routes added to main router");

        executor.submit(() -> {
            try {
                generateSpec();
                specGenerated = true;
                log.info("OpenAPI spec generated successfully");
            } catch (Exception e) {
                specGenerationError = e;
                log.error("Error generating OpenAPI spec", e);
            }
        });
    }

    public boolean isSpecGenerated() {
        return specGenerated;
    }

    public Exception getSpecGenerationError() {
        return specGenerationError;
    }

    public void waitForSpecGeneration(long timeoutMs) throws InterruptedException, Exception {
        long start = System.currentTimeMillis();
        while (!specGenerated && specGenerationError == null) {
            if (System.currentTimeMillis() - start > timeoutMs) {
                throw new RuntimeException("Timeout waiting for spec generation");
            }
            Thread.sleep(100);
        }
        if (specGenerationError != null) {
            throw new RuntimeException("Spec generation failed", specGenerationError);
        }
    }

    public RoutingHandler get() {
        FileResourceManager resourceManager = new FileResourceManager(
            this.resourcePath.toFile(),
            1024
        );

        RoutingHandler router = new RoutingHandler();

        router.add(HttpMethod.GET, basePath, (HttpServerExchange exchange) -> {
            exchange
                .getResponseHeaders()
                .put(Headers.CONTENT_TYPE, MediaType.TEXT_HTML);
            exchange.getResponseSender().send(indexHTML);
        });

        /*
         * YAML path
         */

        HttpHandler yamlHandler = exchange -> {
            String spec = getYamlSpec();

            if (spec == null) {
                exchange
                    .setStatusCode(404)
                    .setReasonPhrase("Spec has not yet been generated")
                    .endExchange();
                return;
            }

            exchange
                .getResponseHeaders()
                .put(
                    Headers.CONTENT_TYPE,
                    io.sinistral.proteus.protocol.MediaType.APPLICATION_YAML.contentType()
                );

            exchange.getResponseSender().send(spec);
        };

        HttpHandler jsonHandler = exchange -> {
            final String spec = this.getJsonSpec();

            if (spec == null) {
                exchange
                    .setStatusCode(404)
                    .setReasonPhrase("Spec has not yet been generated")
                    .endExchange();
                return;
            }

            exchange
                .getResponseHeaders()
                .put(
                    Headers.CONTENT_TYPE,
                    io.sinistral.proteus.protocol.MediaType.APPLICATION_JSON.contentType()
                );

            exchange.getResponseSender().send(spec);
        };

        String yamlTemplatePath = String.format(
            "%s/%s.yaml",
            this.applicationPath,
            this.specFilename
        );

        log.info("Registering OpenAPI YAML route: GET {}", yamlTemplatePath);
        router.add(HttpMethod.GET, yamlTemplatePath, yamlHandler);

        String ymlTemplatePath = String.format(
            "%s/%s.yml",
            this.applicationPath,
            this.specFilename
        );

        log.info("Registering OpenAPI YML route: GET {}", ymlTemplatePath);
        router.add(HttpMethod.GET, ymlTemplatePath, yamlHandler);

        String jsonTemplatePath = String.format(
            "%s/%s.json",
            this.applicationPath,
            this.specFilename
        );

        log.info("Registering OpenAPI JSON route: GET {}", jsonTemplatePath);
        router.add(HttpMethod.GET, jsonTemplatePath, jsonHandler);

        this.registeredEndpoints.add(
            EndpointInfo.builder()
                .withConsumes("*/*")
                .withPathTemplate(yamlTemplatePath)
                .withControllerName(this.getClass().getSimpleName())
                .withMethod(Methods.GET)
                .withProduces(
                    io.sinistral.proteus.protocol.MediaType.APPLICATION_YAML.contentType()
                )
                .build()
        );

        this.registeredEndpoints.add(
            EndpointInfo.builder()
                .withConsumes(MediaType.WILDCARD)
                .withProduces(
                    io.sinistral.proteus.protocol.MediaType.APPLICATION_YAML.contentType()
                )
                .withPathTemplate(ymlTemplatePath)
                .withControllerName(this.getClass().getSimpleName())
                .withMethod(Methods.GET)
                .build()
        );

        this.registeredEndpoints.add(
            EndpointInfo.builder()
                .withConsumes("*/*")
                .withPathTemplate(jsonTemplatePath)
                .withControllerName(this.getClass().getSimpleName())
                .withMethod(Methods.GET)
                .withProduces(
                    io.sinistral.proteus.protocol.MediaType.JSON.contentType()
                )
                .build()
        );

        final String specPath = yamlTemplatePath;

        router.add(
            HttpMethod.GET,
            this.basePath + "/" + this.redocPath,
            (HttpServerExchange exchange) -> {
                exchange
                    .getResponseHeaders()
                    .put(Headers.CONTENT_TYPE, MediaType.TEXT_HTML);

                final String fullPath = String.format(
                    "%s://%s%s",
                    exchange.getRequestScheme(),
                    exchange.getHostAndPort(),
                    specPath
                );

                final String html = redocHTML.replaceAll(
                    "\\{\\{ specPath }}",
                    fullPath
                );

                exchange.getResponseSender().send(html);
            }
        );

        this.registeredEndpoints.add(
            EndpointInfo.builder()
                .withConsumes(MediaType.WILDCARD)
                .withProduces(MediaType.TEXT_HTML)
                .withPathTemplate(this.basePath + "/" + this.redocPath)
                .withControllerName(this.getClass().getSimpleName())
                .withMethod(Methods.GET)
                .build()
        );

        try {
            String wildcardPathTemplate = this.basePath + "/*";

            router.add(
                HttpMethod.GET,
                wildcardPathTemplate,
                new ResourceHandler(resourceManager) {
                    @Override
                    public void handleRequest(HttpServerExchange exchange)
                        throws Exception {
                        String canonicalPath = CanonicalPathUtils.canonicalize(
                            (exchange.getRelativePath())
                        );

                        canonicalPath = canonicalPath.split(basePath)[1];

                        exchange.setRelativePath(canonicalPath);

                        if (serviceClassLoader == null) {
                            super.handleRequest(exchange);
                        } else {
                            canonicalPath = resourcePrefix + canonicalPath;

                            try (
                                final InputStream resourceInputStream =
                                    serviceClassLoader.getResourceAsStream(
                                        canonicalPath
                                    )
                            ) {
                                if (resourceInputStream == null) {
                                    ResponseCodeHandler.HANDLE_404.handleRequest(
                                        exchange
                                    );

                                    return;
                                }

                                byte[] resourceBytes = IOUtils.toByteArray(
                                    resourceInputStream
                                );

                                io.sinistral.proteus.protocol.MediaType mediaType =
                                    io.sinistral.proteus.protocol.MediaType.getByFileName(
                                        canonicalPath
                                    );

                                exchange
                                    .getResponseHeaders()
                                    .put(
                                        Headers.CONTENT_TYPE,
                                        mediaType.toString()
                                    );
                                exchange
                                    .getResponseSender()
                                    .send(ByteBuffer.wrap(resourceBytes));
                            }
                        }
                    }
                }
            );

            this.registeredEndpoints.add(
                EndpointInfo.builder()
                    .withConsumes(MediaType.WILDCARD)
                    .withProduces(MediaType.WILDCARD)
                    .withPathTemplate(wildcardPathTemplate)
                    .withControllerName(this.getClass().getSimpleName())
                    .withMethod(Methods.GET)
                    .build()
            );
        } catch (Exception e) {
            log.error("Failed to retrieve OpenAPI path", e);
        }

        return router;
    }
}
