package io.sinistral.proteus.modules;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;
import com.google.inject.name.Names;
import com.typesafe.config.Config;
import io.sinistral.proteus.security.handlers.SecurityProcessor;
import io.sinistral.proteus.security.jwt.DefaultJwtConfiguration;
import io.sinistral.proteus.security.jwt.DefaultJwtService;
import io.sinistral.proteus.security.jwt.JwtConfiguration;
import io.sinistral.proteus.security.jwt.JwtService;
import io.sinistral.proteus.server.compilation.ControllerCompiler;
import io.sinistral.proteus.server.compilation.JdkControllerCompiler;
import io.sinistral.proteus.server.Extractors;
import io.sinistral.proteus.server.ServerResponse;
import io.sinistral.proteus.server.endpoints.EndpointInfo;

import io.sinistral.proteus.services.BaseService;
import io.sinistral.proteus.wrappers.JsonViewWrapper;
import io.undertow.server.DefaultResponseListener;
import io.undertow.server.HandlerWrapper;
import io.undertow.server.HttpHandler;
import io.undertow.server.RoutingHandler;

import java.util.*;
import java.util.concurrent.ConcurrentSkipListSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Root Guice module for framework defaults and application registries.
 * It installs configured JSON/XML mappers, binds route and endpoint registries, and creates
 * the JWT services used by generated security handlers.
 *
 * @author jbauer
 */
@Singleton
public class ApplicationModule extends AbstractModule {

    /** The log. */
    private static Logger log = LoggerFactory.getLogger(
        ApplicationModule.class.getCanonicalName()
    );

    /** Registered endpoint metadata exposed under the {@code registeredEndpoints} name. */
    protected Set<EndpointInfo> registeredEndpoints = new ConcurrentSkipListSet<>();
    /** Registered controller classes exposed under the {@code registeredControllers} name. */
    protected Set<Class<?>> registeredControllers = new HashSet<>();
    /** Registered service classes exposed under the {@code registeredServices} name. */
    protected Set<Class<? extends BaseService>> registeredServices =
        new HashSet<>();
    /** Registered middleware wrappers exposed under the {@code registeredHandlerWrappers} name. */
    protected Map<String, HandlerWrapper> registeredHandlerWrappers =
        new HashMap<>();

    /** Resolved application configuration. */
    protected Config config;

    /**
     * Creates the framework module from the resolved application configuration.
     *
     * @param config resolved Typesafe Config tree
     */
    public ApplicationModule(Config config) {
        this.config = config;
    }

    /**
     * Installs the configured Jackson and XML mapper modules, falling back to framework
     * defaults when a configured class cannot be created, then requests static injection for
     * framework holders.
     */
    public void bindMappers() {
        try {
            String className = config.getString("application.jacksonModule");

            Class<? extends AbstractModule> clazz = (Class<
                ? extends AbstractModule
            >) Class.forName(className);

            AbstractModule module = clazz
                .getDeclaredConstructor()
                .newInstance();

            install(module);
        } catch (Exception e) {
            this.binder().addError(e);

            log.error(e.getMessage(), e);

            install(new JacksonModule());
        }

        try {
            String className = config.getString("application.xmlModule");

            //   log.debug("Installing XmlModule " + className);

            Class<? extends AbstractModule> clazz = (Class<
                ? extends AbstractModule
            >) Class.forName(className);

            AbstractModule module = clazz
                .getDeclaredConstructor()
                .newInstance();

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

    /** Binds mappers, the router, response listener, fallback handler, and root handler from config. */
    @SuppressWarnings("unchecked")
    @Override
    protected void configure() {
        this.binder().requestInjection(this);

        this.bindMappers();

        RoutingHandler router = new RoutingHandler();

        try {
            String className = config.getString(
                "application.defaultResponseListener"
            );

            Class<? extends DefaultResponseListener> clazz = (Class<
                ? extends DefaultResponseListener
            >) Class.forName(className);

            this.bind(DefaultResponseListener.class)
                .to(clazz)
                .in(Singleton.class);
        } catch (Exception e) {
            log.error(e.getMessage(), e);

            this.bind(DefaultResponseListener.class)
                .to(
                    io.sinistral.proteus.server.handlers
                        .ServerDefaultResponseListener.class
                )
                .in(Singleton.class);
        }

        try {
            String className = config.getString("application.fallbackHandler");

            Class<? extends HttpHandler> clazz = (Class<
                ? extends HttpHandler
            >) Class.forName(className);
            HttpHandler fallbackHandler = clazz
                .getDeclaredConstructor()
                .newInstance();

            this.binder().requestInjection(fallbackHandler);
            router.setFallbackHandler(fallbackHandler);
        } catch (Exception e) {
            this.binder().addError(e);
            log.error(e.getMessage(), e);
        }

        this.bind(RoutingHandler.class).toInstance(router);
        this.bind(ApplicationModule.class).toInstance(this);

        this.bind(new TypeLiteral<Set<Class<?>>>() {})
            .annotatedWith(Names.named("registeredControllers"))
            .toInstance(registeredControllers);
        this.bind(new TypeLiteral<Set<EndpointInfo>>() {})
            .annotatedWith(Names.named("registeredEndpoints"))
            .toInstance(registeredEndpoints);
        this.bind(new TypeLiteral<Set<Class<? extends BaseService>>>() {})
            .annotatedWith(Names.named("registeredServices"))
            .toInstance(registeredServices);
        this.bind(new TypeLiteral<Map<String, HandlerWrapper>>() {})
            .annotatedWith(Names.named("registeredHandlerWrappers"))
            .toInstance(registeredHandlerWrappers);

        JwtConfiguration jwtConfiguration = new DefaultJwtConfiguration(config);
        JwtService jwtService = new DefaultJwtService(jwtConfiguration);
        this.bind(JwtConfiguration.class).toInstance(jwtConfiguration);
        this.bind(JwtService.class).toInstance(jwtService);
        this.bind(SecurityProcessor.class)
            .toInstance(new SecurityProcessor(jwtService));
        this.bind(ControllerCompiler.class)
            .to(JdkControllerCompiler.class)
            .in(Singleton.class);

    }
}
