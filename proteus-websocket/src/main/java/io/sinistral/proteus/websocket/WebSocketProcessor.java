package io.sinistral.proteus.websocket;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * Scans Guice-bound singleton beans at application startup and registers every
 * {@link WebSocket @WebSocket} endpoint with the {@link DefaultWebSocketService}.
 *
 * <p>Endpoint methods annotated {@link OnOpen}, {@link OnMessage}, {@link OnClose}, or
 * {@link OnError} are validated here: static methods are rejected and each handler must
 * declare parameters only from the supported set (connection, text, binary, close reason,
 * throwable), matching the dispatch rules in {@code EndpointReceiveListener}.
 *
 * @author jbauer
 */
@Singleton
public class WebSocketProcessor {

    /** Class logger. */
    private static final Logger log = LoggerFactory.getLogger(WebSocketProcessor.class);

    /** Guice injector used to realize endpoint singletons. */
    private final Injector injector;
    /** Service receiving the registered endpoints. */
    private final DefaultWebSocketService service;

    /** Creates the processor.
     *
     * @param injector Guice injector for endpoint lookup
     * @param service the websocket service receiving registrations
     */
    @Inject
    public WebSocketProcessor(Injector injector, DefaultWebSocketService service) {
        this.injector = injector;
        this.service = service;
    }

    /**
     * Registers endpoints for every annotated singleton.
     *
     * @param beanClasses singleton bean classes to scan
     * @return the number of endpoints registered
     */
    public int process(Iterable<Class<?>> beanClasses) {
        int registered = 0;
        for (Class<?> beanClass : beanClasses) {
            WebSocket annotation = beanClass.getAnnotation(WebSocket.class);
            if (annotation == null) {
                continue;
            }
            Object bean = injector.getInstance(beanClass);
            validate(beanClass);
            service.registerEndpoint(annotation.value(), bean);
            registered++;
        }
        log.info("Registered {} @WebSocket endpoints", registered);
        return registered;
    }

    /** Validates handler method shapes on one endpoint class.
     *
     * @param beanClass the endpoint class to validate
     */
    private void validate(Class<?> beanClass) {
        for (Method method : beanClass.getDeclaredMethods()) {
            if (method.isAnnotationPresent(OnOpen.class)) {
                requireHandler(method, WebSocketConnection.class, "@OnOpen");
            } else if (method.isAnnotationPresent(OnMessage.class)) {
                requireHandler(method, null, "@OnMessage");
                for (Class<?> type : method.getParameterTypes()) {
                    if (type != WebSocketConnection.class && type != String.class && type != byte[].class) {
                        throw new IllegalArgumentException(
                            "@OnMessage unsupported parameter type " + type + ": " + method);
                    }
                }
            } else if (method.isAnnotationPresent(OnClose.class)) {
                requireHandler(method, null, "@OnClose");
                for (Class<?> type : method.getParameterTypes()) {
                    if (type != WebSocketConnection.class && type != CloseReason.class) {
                        throw new IllegalArgumentException(
                            "@OnClose unsupported parameter type " + type + ": " + method);
                    }
                }
            } else if (method.isAnnotationPresent(OnError.class)) {
                requireHandler(method, null, "@OnError");
                for (Class<?> type : method.getParameterTypes()) {
                    if (type != WebSocketConnection.class && type != Throwable.class) {
                        throw new IllegalArgumentException(
                            "@OnError unsupported parameter type " + type + ": " + method);
                    }
                }
            }
        }
    }

    /** Rejects static handlers.
     *
     * @param method the annotated handler method
     * @param required required first parameter type, or null when unconstrained
     * @param label annotation label used in error messages
     */
    private void requireHandler(Method method, Class<?> required, String label) {
        if (Modifier.isStatic(method.getModifiers())) {
            throw new IllegalArgumentException(label + " method must not be static: " + method);
        }
    }
}
