package io.sinistral.proteus.modules;

 
import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;
import com.google.inject.name.Names;
import com.typesafe.config.Config;
import io.sinistral.proteus.server.Extractors;
import io.sinistral.proteus.server.ServerResponse;
import io.sinistral.proteus.server.endpoints.EndpointInfo;
import io.sinistral.proteus.server.handlers.virtualthreads.VirtualThreadExecutorService;
import io.sinistral.proteus.server.handlers.virtualthreads.VirtualThreadProcessor;
import io.sinistral.proteus.services.BaseService;
import io.sinistral.proteus.wrappers.JsonViewWrapper;
import io.undertow.server.DefaultResponseListener;
import io.undertow.server.HandlerWrapper;
import io.undertow.server.HttpHandler;
import io.undertow.server.RoutingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory; 
import io.sinistral.proteus.security.jwt.JwtConfiguration;
import io.sinistral.proteus.security.jwt.DefaultJwtConfiguration;
import io.sinistral.proteus.security.jwt.JwtService;
import io.sinistral.proteus.security.jwt.DefaultJwtService;
import io.sinistral.proteus.security.handlers.SecurityProcessor;
import io.sinistral.proteus.eventbus.EventBusService;
import io.sinistral.proteus.eventbus.DefaultEventBusService;
import io.sinistral.proteus.eventbus.processors.ConsumeEventProcessor;
import io.vertx.core.Vertx;

import java.util.*;

/**
 * @author jbauer
 */
@Singleton
public class ApplicationModule extends AbstractModule
{
    private static Logger log = LoggerFactory.getLogger(ApplicationModule.class.getCanonicalName());

    protected Set<EndpointInfo> registeredEndpoints = new TreeSet<>();
    protected Set<Class<?>> registeredControllers = new HashSet<>();
    protected Set<Class<? extends BaseService>> registeredServices = new HashSet<>();
    protected Map<String, HandlerWrapper> registeredHandlerWrappers = new HashMap<>();

    protected Config config;

    public ApplicationModule(Config config)
    {
        this.config = config;
    }

    /**
     * Override for customizing XmlMapper and ObjectMapper
     */
    public void bindMappers()
    {


        try {

            String className = config.getString("application.jacksonModule");

            Class<? extends AbstractModule> clazz = (Class<? extends AbstractModule>) Class.forName(className);

            AbstractModule module = clazz.getDeclaredConstructor().newInstance();

            install(module);

        } catch (Exception e) {

            this.binder().addError(e);

            log.error(e.getMessage(), e);

            install(new JacksonModule());

        }

        try {

            String className = config.getString("application.xmlModule");

         //   log.debug("Installing XmlModule " + className);

            Class<? extends AbstractModule> clazz = (Class<? extends AbstractModule>) Class.forName(className);

            AbstractModule module = clazz.getDeclaredConstructor().newInstance();

            install(module);

        } catch (Exception e) {

            this.binder().addError(e);

            log.error("Failed to install standard serialization modules", e);

            install(new XmlModule());

        }

        this.requestStaticInjection(Extractors.class);
        this.requestStaticInjection(ServerResponse.class);
        this.requestStaticInjection(JsonViewWrapper.class);

     }

    @SuppressWarnings("unchecked")
    @Override
    protected void configure()
    {
        this.binder().requestInjection(this);

        this.bindMappers();

        RoutingHandler router = new RoutingHandler();

        try {

            String className = config.getString("application.defaultResponseListener");

            Class<? extends DefaultResponseListener> clazz = (Class<? extends DefaultResponseListener>) Class.forName(className);

            this.bind(DefaultResponseListener.class).to(clazz).in(Singleton.class);

        } catch (Exception e) {

            log.error(e.getMessage(), e);

            this.bind(DefaultResponseListener.class).to(io.sinistral.proteus.server.handlers.ServerDefaultResponseListener.class).in(Singleton.class);

        }

        try {

            String className = config.getString("application.fallbackHandler");

            Class<? extends HttpHandler> clazz = (Class<? extends HttpHandler>) Class.forName(className);
            HttpHandler fallbackHandler = clazz.getDeclaredConstructor().newInstance();

            this.binder().requestInjection(fallbackHandler);
            router.setFallbackHandler(fallbackHandler);

        } catch (Exception e) {

            this.binder().addError(e);
            log.error(e.getMessage(), e);
        }

        this.bind(RoutingHandler.class).toInstance(router);
        this.bind(ApplicationModule.class).toInstance(this);

        this.bind(new TypeLiteral<Set<Class<?>>>()
        {
        }).annotatedWith(Names.named("registeredControllers")).toInstance(registeredControllers);
        this.bind(new TypeLiteral<Set<EndpointInfo>>()
        {
        }).annotatedWith(Names.named("registeredEndpoints")).toInstance(registeredEndpoints);
        this.bind(new TypeLiteral<Set<Class<? extends BaseService>>>()
        {
        }).annotatedWith(Names.named("registeredServices")).toInstance(registeredServices);
        this.bind(new TypeLiteral<Map<String, HandlerWrapper>>()
        {
        }).annotatedWith(Names.named("registeredHandlerWrappers")).toInstance(registeredHandlerWrappers);

        // Configure virtual thread support if available
        if (VirtualThreadExecutorService.isVirtualThreadSupported()) {
            try {
                VirtualThreadExecutorService virtualThreadExecutor = new VirtualThreadExecutorService(config);
                VirtualThreadProcessor virtualThreadProcessor = new VirtualThreadProcessor(virtualThreadExecutor);
                
                this.bind(VirtualThreadExecutorService.class).toInstance(virtualThreadExecutor);
                this.bind(VirtualThreadProcessor.class).toInstance(virtualThreadProcessor);
                
                log.info("Virtual thread support enabled");
            } catch (Exception e) {
                log.warn("Failed to initialize virtual thread support", e);
            }
        } else {
            log.info("Virtual threads not supported in this JVM, skipping virtual thread configuration");
        }

        // Configure JWT security support
        try {
            JwtConfiguration jwtConfiguration = new DefaultJwtConfiguration(config);
            JwtService jwtService = new DefaultJwtService(jwtConfiguration);
            SecurityProcessor securityProcessor = new SecurityProcessor(jwtService);
            
            this.bind(JwtConfiguration.class).toInstance(jwtConfiguration);
            this.bind(JwtService.class).toInstance(jwtService);
            this.bind(SecurityProcessor.class).toInstance(securityProcessor);
            
            log.info("JWT security support enabled");
        } catch (Exception e) {
            log.warn("Failed to initialize JWT security support", e);
        }

        // Configure Event Bus support
        try {
            if (config.hasPath("eventbus.enabled") && config.getBoolean("eventbus.enabled")) {
                Vertx vertx = Vertx.vertx();
                // ObjectMapper will be injected by the DI framework
                this.bind(Vertx.class).toInstance(vertx);
                this.bind(EventBusService.class).to(DefaultEventBusService.class).in(Singleton.class);
                this.bind(ConsumeEventProcessor.class).in(Singleton.class);
                
                log.info("Event Bus support enabled with Vert.x");
            } else {
                log.info("Event Bus disabled in configuration");
            }
        } catch (Exception e) {
            log.warn("Failed to initialize Event Bus support", e);
        }

    }
}



