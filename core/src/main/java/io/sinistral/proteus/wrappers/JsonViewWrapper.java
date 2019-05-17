package io.sinistral.proteus.wrappers;

import com.google.inject.Inject;
import io.undertow.server.HandlerWrapper;
import io.undertow.server.HttpHandler;
import io.undertow.util.AttachmentKey;

import javax.inject.Named;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class JsonViewWrapper implements HandlerWrapper
{
    public static AttachmentKey<Class> JSON_VIEW_KEY = AttachmentKey.create(Class.class);

    @Named("jackson.jsonView.className")
    @Inject(optional = true)
    private static String VIEW_CLASS_NAME;

    @Named("jackson.jsonView.queryParameterName")
    @Inject(optional = true)
    private static String QUERY_PARAMETER_NAME = "context";

    private static Map<String, Class> CLASS_MAP;

    public JsonViewWrapper()
    {
        super();

        if (CLASS_MAP == null && VIEW_CLASS_NAME != null) {

            try {

                Class clazz = Class.forName(VIEW_CLASS_NAME);

                CLASS_MAP = new HashMap<>();

                final Class[] contexts = clazz.getClasses();

                for (Class c : contexts) {
                    CLASS_MAP.put(c.getSimpleName().toLowerCase(), c);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public HttpHandler wrap(HttpHandler handler)
    {
        return exchange -> {

            if (CLASS_MAP != null) {

                Optional.ofNullable(exchange.getQueryParameters().get(QUERY_PARAMETER_NAME))
                        .filter(q -> q.size() > 0)
                        .map(Deque::getFirst)
                        .ifPresent(cn -> {

                            Class viewClass = CLASS_MAP.get(cn);

                            exchange.putAttachment(JSON_VIEW_KEY, viewClass);
                        });
            }

            handler.handleRequest(exchange);
        };
    }
}
