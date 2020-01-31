package io.sinistral.proteus.wrappers;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.undertow.server.HandlerWrapper;
import io.undertow.server.HttpHandler;
import io.undertow.util.AttachmentKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Named;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Singleton
public class JsonViewWrapper implements HandlerWrapper
{
    public static AttachmentKey<Class> JSON_VIEW_KEY = AttachmentKey.create(Class.class);

    private static final Logger logger = LoggerFactory.getLogger(JsonViewWrapper.class.getName());

    @Named("jackson.jsonView.className")
    @Inject(optional = true)
    private static String VIEW_CLASS_NAME = null;

    @Named("jackson.jsonView.defaultViewClass")
    @Inject(optional = true)
    private static String DEFAULT_VIEW_CLASS_NAME = null;

    @Named("jackson.jsonView.queryParameterName")
    @Inject(optional = true)
    private static String QUERY_PARAMETER_NAME = "context";

    private Map<String, Class> CLASS_MAP = new ConcurrentHashMap<>();

    private Class defaultViewClass = null;

    public JsonViewWrapper()
    {
        super();

         if (  VIEW_CLASS_NAME != null) {

            try {

                Class clazz = Class.forName(VIEW_CLASS_NAME);

                final Class[] contexts = clazz.getClasses();

                for (Class c : contexts) {
                    CLASS_MAP.put(c.getSimpleName().toLowerCase(), c);
                }

                if(DEFAULT_VIEW_CLASS_NAME != null)
                {
                    defaultViewClass = Class.forName(DEFAULT_VIEW_CLASS_NAME);
                }


            } catch (Exception e) {
                logger.error("Error processing JsonView", e);
            }
        }
    }

    @Override
    public HttpHandler wrap(HttpHandler handler)
    {

        return exchange -> {

            if (CLASS_MAP != null) {

                String className = Optional.ofNullable(exchange.getQueryParameters().get(QUERY_PARAMETER_NAME))
                        .filter(q -> q.size() > 0)
                        .map(Deque::getFirst).orElse(null);

                if(className != null) {
                    Class viewClass = CLASS_MAP.getOrDefault(className, defaultViewClass);

                    exchange.putAttachment(JSON_VIEW_KEY, viewClass);
                }
                else if(defaultViewClass != null)
                {
                    exchange.putAttachment(JSON_VIEW_KEY, defaultViewClass);
                }
            }

            handler.handleRequest(exchange);
        };
    }
}
