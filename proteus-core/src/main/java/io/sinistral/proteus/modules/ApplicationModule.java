package io.sinistral.proteus.modules;

import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;
import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;
import com.google.inject.name.Names;
import com.typesafe.config.Config;
import io.sinistral.proteus.server.Extractors;
import io.sinistral.proteus.server.ServerResponse;
import io.sinistral.proteus.server.endpoints.EndpointInfo;
import io.sinistral.proteus.services.BaseService;
import io.sinistral.proteus.wrappers.JsonViewWrapper;
import io.undertow.server.DefaultResponseListener;
import io.undertow.server.HandlerWrapper;
import io.undertow.server.HttpHandler;
import io.undertow.server.RoutingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

         //   log.debug("Installing JacksonModule " + className);

            Class<? extends AbstractModule> clazz = (Class<? extends AbstractModule>) Class.forName(className);

            AbstractModule module = clazz.newInstance();

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

            AbstractModule module = clazz.newInstance();

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

            //log.debug("Installing DefaultResponseListener " + className);

            Class<? extends DefaultResponseListener> clazz = (Class<? extends DefaultResponseListener>) Class.forName(className);

            this.bind(DefaultResponseListener.class).to(clazz).in(Singleton.class);

        } catch (Exception e) {

            log.error(e.getMessage(), e);

            this.bind(DefaultResponseListener.class).to(io.sinistral.proteus.server.handlers.ServerDefaultResponseListener.class).in(Singleton.class);

        }

        try {

            String className = config.getString("application.fallbackHandler");

           // log.debug("Installing FallbackListener " + className);

            Class<? extends HttpHandler> clazz = (Class<? extends HttpHandler>) Class.forName(className);
            HttpHandler fallbackHandler = clazz.newInstance();

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

    }
}



