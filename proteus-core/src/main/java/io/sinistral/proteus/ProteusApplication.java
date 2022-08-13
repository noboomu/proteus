package io.sinistral.proteus;

import com.google.common.collect.ImmutableMap;
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
import com.typesafe.config.Config;
import io.sinistral.proteus.modules.ConfigModule;
import io.sinistral.proteus.server.endpoints.EndpointInfo;
import io.sinistral.proteus.server.handlers.HandlerGenerator;
import io.sinistral.proteus.server.handlers.ServerDefaultHttpHandler;
import io.sinistral.proteus.services.BaseService;
import io.sinistral.proteus.utilities.SecurityUtilities;
import io.sinistral.proteus.utilities.TablePrinter;
import io.undertow.Undertow;
import io.undertow.Undertow.ListenerInfo;
import io.undertow.UndertowOptions;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.RoutingHandler;
import io.undertow.server.handlers.GracefulShutdownHandler;
import io.undertow.server.session.SessionAttachmentHandler;
import io.undertow.util.Headers;
import io.undertow.util.Methods;
import net.openhft.compiler.CachedCompiler;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xnio.Xnio;
import org.xnio.XnioWorker;

import javax.ws.rs.core.MediaType;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.net.SocketAddress;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyStore;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * The base class for proteus applications.
 *
 * @author jbauer
 */

@SuppressWarnings({"UnusedReturnValue", "unchecked"})
public class ProteusApplication {

    private static Logger log = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(ProteusApplication.class.getCanonicalName());

    private static final String TMP_DIRECTORY_NAME = "proteus_generated_classes";

    @Inject
    @Named("registeredControllers")
    public Set<Class<?>> registeredControllers;

    @Inject
    @Named("registeredEndpoints")
    public Set<EndpointInfo> registeredEndpoints;

    @Inject
    @Named("registeredServices")
    public Set<Class<? extends BaseService>> registeredServices;

    @Inject
    public RoutingHandler router;

    @Inject
    public Config config;

    public List<Class<? extends Module>> registeredModules = new ArrayList<>();

    public Injector injector;

    public ServiceManager serviceManager = null;

    public Undertow undertow = null;

    public Class<? extends HttpHandler> rootHandlerClass;

    public HttpHandler rootHandler;

    public AtomicBoolean running = new AtomicBoolean(false);

    public List<Integer> ports = new ArrayList<>();

    public Function<Undertow.Builder, Undertow.Builder> serverConfigurationFunction = null;

    public Duration startupDuration;

    final Instant startTime = Instant.now();

    public ProteusApplication()
    {

        injector = Guice.createInjector(new ConfigModule());
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

    public ProteusApplication(Module... modules)
    {

        injector = Guice.createInjector(modules);
        injector.injectMembers(this);

    }

    public void start()
    {

        if (this.isRunning())
        {
            log.warn("Server has already started...");
            return;
        }

        final Thread mainThread = Thread.currentThread();

        log.info("Installing modules: {}", registeredModules.stream().map(Class::getSimpleName).collect(Collectors.joining(",")));

        Set<Module> modules = registeredModules.stream().map(mc -> injector.getInstance(mc)).collect(Collectors.toSet());

        injector = injector.createChildInjector(modules);

        if (rootHandlerClass == null && rootHandler == null)
        {
            log.debug("No root handler class or root HttpHandler was specified, using default ServerDefaultHttpHandler.");
            rootHandlerClass = ServerDefaultHttpHandler.class;
        }

        log.info("Installing services: {}", registeredServices.stream().map(Class::getSimpleName).collect(Collectors.joining(",")));

        Set<BaseService> services = registeredServices.stream().map(sc -> injector.getInstance(sc)).collect(Collectors.toSet());

        //injector = injector.createChildInjector(services);

        serviceManager = new ServiceManager(services);

        serviceManager.addListener(new Listener() {
            public void stopped()
            {

                log.info("Services are stopped");

            }

            public void healthy()
            {

                log.info("Services are healthy");

                startupDuration = Duration.between(startTime, Instant.now());

                for (ListenerInfo info : undertow.getListenerInfo())
                {
                    log.debug("listener info: " + info);
                    SocketAddress address = info.getAddress();

                    if (address != null)
                    {
                        ports.add(((java.net.InetSocketAddress) address).getPort());
                    }
                }

                printStatus();

            }

            public void failure(Service service)
            {

                log.error("Service failure: " + service);

                startupDuration = Duration.between(startTime, Instant.now());

                for (ListenerInfo info : undertow.getListenerInfo())
                {
                    log.debug("listener info: " + info);
                    SocketAddress address = info.getAddress();

                    if (address != null)
                    {
                        ports.add(((java.net.InetSocketAddress) address).getPort());
                    }
                }

                printStatus();

                running.set(false);
            }

        }, MoreExecutors.directExecutor());

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {

            if (!this.isRunning())
            {
                log.warn("Server is not running...");
                return;
            }

            try
            {
                shutdown();
                mainThread.join();
            } catch (InterruptedException ex)
            {
                log.error("Shutdown was interrupted", ex);
            } catch (TimeoutException timeout)
            {
                log.error("Shutdown timed out", timeout);
            }
        }));

        buildServer();

        undertow.start();

        Duration timeout = config.getDuration("application.services.timeout");

        try
        {
            serviceManager.startAsync().awaitHealthy(timeout);
        } catch (TimeoutException e)
        {
            log.error("Failed start to services within {} minutes", timeout, e);
        } catch (Exception e)
        {
            log.error("Failed to start services", e);
        }

        this.running.set(true);

//        serviceManager.startAsync();

    }

    public void shutdown() throws TimeoutException
    {

        if (!this.isRunning())
        {
            log.warn("Server is not running...");
            return;
        }

        log.info("Shutting down...");

        undertow.stop();

        serviceManager.stopAsync().awaitStopped(2, TimeUnit.SECONDS);

        this.running.set(false);

        log.info("Shutdown complete");
    }

    public boolean isRunning()
    {

        return this.running.get();
    }

    public void buildServer()
    {

        final Instant compilationStartTime = Instant.now();

        ExecutorService handlerCompilationExecutor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        CountDownLatch countDownLatch = new CountDownLatch(registeredControllers.size());

        CopyOnWriteArrayList<Class<? extends Supplier<RoutingHandler>>> routerClasses = new CopyOnWriteArrayList<>();

        ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

        log.info("Compiling route handlers...");

        for (Class<?> controllerClass : registeredControllers)
        {

            handlerCompilationExecutor.submit(() -> {

                try
                {

                    //  log.debug("Generating {}...", controllerClass);

                    final File tempFile = getTemporaryDirectoryPath().toFile();

                    HandlerGenerator generator = new HandlerGenerator("io.sinistral.proteus.controllers.handlers", controllerClass);

                    injector.injectMembers(generator);

                    //   log.debug("Compiling {}...", controllerClass);

                    try (CachedCompiler cachedCompiler = new CachedCompiler(null, tempFile))
                    {
                        final String source = generator.generateClassSource();

                        Class<? extends Supplier<RoutingHandler>> routerClass = cachedCompiler.loadFromJava(generator.getCanonicalName(), source);

                        lock.writeLock().lock();

                        routerClasses.add(routerClass);

                        lock.writeLock().unlock();
                    }



                } catch (Exception e)
                {
                    log.error("Exception creating handlers for {}", controllerClass.getName(), e);
                }

                countDownLatch.countDown();
            });

        }

        try
        {
            countDownLatch.await();
        } catch (Exception e)
        {
            log.error("Failed waiting for handlers to generate", e);
        }

        log.debug("Compilation completed in {}", DurationFormatUtils.formatDurationHMS(Duration.between(compilationStartTime, Instant.now()).toMillis()));

        for (Class<? extends Supplier<RoutingHandler>> clazz : routerClasses)
        {
            Supplier<RoutingHandler> generatedRouteSupplier = injector.getInstance(clazz);
            router.addAll(generatedRouteSupplier.get());
        }

        this.addDefaultRoutes(router);

        log.info("Route handlers generated");

        HttpHandler handler;

        if (rootHandlerClass != null)
        {
            handler = injector.getInstance(rootHandlerClass);
        }
        else
        {
            handler = rootHandler;
        }

        SessionAttachmentHandler sessionAttachmentHandler = null;

        try
        {
            sessionAttachmentHandler = injector.getInstance(SessionAttachmentHandler.class);
        } catch (Exception e)
        {
            log.debug("No session attachment handler found.");
        }

        if (sessionAttachmentHandler != null)
        {
            log.info("Using session attachment handler.");

            sessionAttachmentHandler.setNext(handler);
            handler = sessionAttachmentHandler;
        }

        if (config.hasPath("undertow.gracefulShutdown") && config.getBoolean("undertow.gracefulShutdown"))
        {
            handler = new GracefulShutdownHandler(handler);
        }

        int httpPort = config.getInt("application.ports.http");

        if (System.getProperty("http.port") != null)
        {
            httpPort = Integer.parseInt(System.getProperty("http.port"));
        }

        final int processorCount = Runtime.getRuntime().availableProcessors();

        Undertow.Builder undertowBuilder = Undertow.builder().addHttpListener(httpPort, config.getString("application.host"))

                                                   .setBufferSize(Long.valueOf(config.getMemorySize("undertow.bufferSize").toBytes()).intValue())
                                                   .setIoThreads(processorCount * config.getInt("undertow.ioThreadsMultiplier"))
                                                   .setWorkerThreads(processorCount * config.getInt("undertow.workerThreadsMultiplier"))
                                                   .setDirectBuffers(config.getBoolean("undertow.directBuffers"))
                                                   .setSocketOption(org.xnio.Options.BACKLOG, config.getInt("undertow.socket.backlog"))
                                                   .setSocketOption(org.xnio.Options.REUSE_ADDRESSES, config.getBoolean("undertow.socket.reuseAddresses"))
                                                   .setSocketOption(org.xnio.Options.READ_TIMEOUT, config.getInt("undertow.socket.readTimeout"))
                                                   .setSocketOption(org.xnio.Options.WRITE_TIMEOUT, config.getInt("undertow.socket.writeTimeout"))
                                                   .setServerOption(UndertowOptions.ENABLE_HTTP2, config.getBoolean("undertow.server.enableHttp2"))
                                                   .setServerOption(UndertowOptions.ALWAYS_SET_DATE, config.getBoolean("undertow.server.alwaysSetDate"))
                                                   .setServerOption(UndertowOptions.ALWAYS_SET_KEEP_ALIVE, config.getBoolean("undertow.server.alwaysSetKeepAlive"))
                                                   .setServerOption(UndertowOptions.RECORD_REQUEST_START_TIME, config.getBoolean("undertow.server.recordRequestStartTime"))
                                                   .setServerOption(UndertowOptions.MAX_ENTITY_SIZE, config.getBytes("undertow.server.maxEntitySize"))
                                                   .setServerOption(UndertowOptions.MAX_BUFFERED_REQUEST_SIZE, config.getInt("undertow.server.maxBufferedRequestSize"))
                                                   .setHandler(handler);

        if (config.getBoolean("undertow.ssl.enabled"))
        {
            try
            {
                int httpsPort = config.getInt("application.ports.https");

                if (System.getProperty("https.port") != null)
                {
                    httpsPort = Integer.parseInt(System.getProperty("https.port"));
                }

                KeyStore keyStore = SecurityUtilities.loadKeyStore(config.getString("undertow.ssl.keystorePath"), config.getString("undertow.ssl.keystorePassword"));
                KeyStore trustStore = SecurityUtilities.loadKeyStore(config.getString("undertow.ssl.truststorePath"), config.getString("undertow.ssl.truststorePassword"));

                undertowBuilder.addHttpsListener(httpsPort, config.getString("application.host"), SecurityUtilities.createSSLContext(keyStore, trustStore, config.getString("undertow.ssl.keystorePassword")));

            } catch (Exception e)
            {
                log.error(e.getMessage(), e);
            }
        }

        if (serverConfigurationFunction != null)
        {
            undertowBuilder = serverConfigurationFunction.apply(undertowBuilder);
        }

        this.undertow = undertowBuilder.build();

    }

    /**
     * Add a service class to the application
     *
     * @param serviceClass
     * @return the application
     */
    public ProteusApplication addService(Class<? extends BaseService> serviceClass)
    {

        registeredServices.add(serviceClass);
        return this;
    }

    /**
     * Add a controller class to the application
     *
     * @param controllerClass
     * @return the application
     */
    public ProteusApplication addController(Class<?> controllerClass)
    {

        registeredControllers.add(controllerClass);
        return this;
    }

    /**
     * Add a module class to the application
     *
     * @param moduleClass
     * @return the application
     */
    public ProteusApplication addModule(Class<? extends Module> moduleClass)
    {

        registeredModules.add(moduleClass);
        return this;
    }

    /**
     * Add utility routes the router
     *
     * @param router
     */
    public ProteusApplication addDefaultRoutes(RoutingHandler router)
    {

        if (config.hasPath("health.statusPath"))
        {
            try
            {
                final String statusPath = config.getString("health.statusPath");

                router.add(Methods.GET, statusPath, (final HttpServerExchange exchange) ->
                {
                    exchange.getResponseHeaders().add(Headers.CONTENT_TYPE, MediaType.TEXT_PLAIN);

                    if (this.serviceManager.servicesByState().values().stream().allMatch(Service::isRunning))
                    {
                        exchange.setStatusCode(200).getResponseSender().send("OK");
                    }
                    else
                    {
                        exchange.setStatusCode(500).getResponseSender().send("NOT_HEALTHY");
                    }
                });

                this.registeredEndpoints.add(EndpointInfo.builder().withConsumes("*/*").withProduces("text/plain").withPathTemplate(statusPath).withControllerName("Internal").withMethod(Methods.GET).build());

            } catch (Exception e)
            {
                log.error("Error adding health status route.", e);
            }
        }

        if (config.hasPath("application.favicon"))
        {
            try
            {

                final ByteBuffer faviconImageBuffer;

                final File faviconFile = new File(config.getString("application.favicon"));

                if (!faviconFile.exists())
                {
                    try (final InputStream stream = this.getClass().getResourceAsStream(config.getString("application.favicon")))
                    {
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();

                        byte[] buffer = new byte[4096];
                        int read = 0;
                        while (read != -1)
                        {
                            read = stream.read(buffer);
                            if (read > 0)
                            {
                                baos.write(buffer, 0, read);
                            }
                        }

                        faviconImageBuffer = ByteBuffer.wrap(baos.toByteArray());
                    }

                }
                else
                {
                    try (final InputStream stream = Files.newInputStream(Paths.get(config.getString("application.favicon"))))
                    {
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();

                        byte[] buffer = new byte[4096];
                        int read = 0;
                        while (read != -1)
                        {
                            read = stream.read(buffer);
                            if (read > 0)
                            {
                                baos.write(buffer, 0, read);
                            }
                        }

                        faviconImageBuffer = ByteBuffer.wrap(baos.toByteArray());
                    }
                }

                router.add(Methods.GET, "favicon.ico", (final HttpServerExchange exchange) ->
                {
                    exchange.getResponseHeaders().add(Headers.CONTENT_TYPE, io.sinistral.proteus.protocol.MediaType.IMAGE_X_ICON.toString());
                    exchange.getResponseSender().send(faviconImageBuffer);
                });

            } catch (Exception e)
            {
                log.error("Error adding favicon route.", e);
            }
        }

        return this;
    }

    /**
     * Set the root HttpHandler class
     *
     * @param rootHandlerClass
     * @return the application
     */
    public ProteusApplication setRootHandlerClass(Class<? extends HttpHandler> rootHandlerClass)
    {

        this.rootHandlerClass = rootHandlerClass;
        return this;
    }

    /**
     * Set the root HttpHandler
     *
     * @param rootHandler
     * @return the application
     */
    public ProteusApplication setRootHandler(HttpHandler rootHandler)
    {

        this.rootHandler = rootHandler;
        return this;
    }

    /**
     * Allows direct access to the Undertow.Builder for custom configuration
     *
     * @param serverConfigurationFunction the serverConfigurationFunction
     */
    public ProteusApplication setServerConfigurationFunction(Function<Undertow.Builder, Undertow.Builder> serverConfigurationFunction)
    {

        this.serverConfigurationFunction = serverConfigurationFunction;
        return this;
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

    /**
     * @return the router
     */
    public RoutingHandler getRouter()
    {

        return router;
    }

    /**
     * @return a list of used ports
     */
    public List<Integer> getPorts()
    {

        return ports;
    }

    public void stop() {

        undertow.stop();
    }

    public Xnio getXnio() {

        return undertow.getXnio();
    }

    public XnioWorker getWorker() {

        return undertow.getWorker();
    }

    public List<ListenerInfo> getListenerInfo() {

        return undertow.getListenerInfo();
    }

    /**
     * @return The Undertow server
     */
    public Undertow getUndertow()
    {

        return undertow;
    }

    public void printStatus()
    {

        Config globalHeaders = config.getConfig("globalHeaders");

        Map<String, String> globalHeadersParameters = globalHeaders.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().render()));

        StringBuilder sb = new StringBuilder();

        sb.append("\nUsing global headers: \n");

        List<String> tableHeaders = Arrays.asList("Header", "Value");

        List<List<String>> tableRows = globalHeadersParameters.entrySet().stream().map(e -> Arrays.asList(e.getKey(), e.getValue()))
                                                              .collect(Collectors.toList());

        TablePrinter printer = new TablePrinter(tableHeaders, tableRows);

        sb.append(printer.toString());

        sb.append("\nRegistered endpoints: \n");

        tableHeaders = Arrays.asList("Method", "Path", "Consumes", "Produces", "Controller");

        tableRows = this.registeredEndpoints.stream().sorted().map(e ->
                                Arrays.asList(e.getMethod().toString(), e.getPathTemplate(), String.format("[%s]", e.getConsumes()), String.format("[%s]", e.getProduces()), String.format("(%s.%s)", e.getControllerName(), e.getControllerMethod())))
                                            .collect(Collectors.toList());

        printer = new TablePrinter(tableHeaders, tableRows);

        sb.append(printer.toString()).append("\nRegistered services: \n");

        ImmutableMultimap<State, Service> serviceStateMap = this.serviceManager.servicesByState();

        ImmutableMap<Service, Long> serviceStartupTimeMap = this.serviceManager.startupTimes();

        tableHeaders = Arrays.asList("Service", "State", "Startup Time");

        tableRows = serviceStateMap.asMap().entrySet().stream().flatMap(e ->
                                           e.getValue().stream().map(s ->
                                                   Arrays.asList(s.getClass().getSimpleName(), e.getKey().toString(), DurationFormatUtils.formatDurationHMS(serviceStartupTimeMap.get(s)))))
                                   .collect(Collectors.toList());

        printer = new TablePrinter(tableHeaders, tableRows);

        sb.append(printer.toString()).append("\nListening On: " + this.ports).append("\nApplication Startup Time: ").append(DurationFormatUtils.formatDurationHMS(this.startupDuration.toMillis())).append("\n");

        log.info(sb.toString());
    }

    protected java.nio.file.Path getTemporaryDirectoryPath() throws Exception
    {

        String tmpDirLocation = System.getProperty("java.io.tmpdir");

        java.nio.file.Path tmpPath = Paths.get(tmpDirLocation);

        try
        {
            return Files.createDirectory(tmpPath.resolve(TMP_DIRECTORY_NAME));

        } catch (Exception e)
        {
            return tmpPath;
        }

    }

}
