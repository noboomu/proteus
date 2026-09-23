package io.sinistral.proteus.messaging;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import io.vertx.core.eventbus.Message;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Scans Guice singleton beans at application startup and registers every
 * {@link ConsumeEvent @ConsumeEvent} method on the event bus.
 *
 * <p>An annotated method accepts either the raw payload type or the {@code Message} type as
 * its single parameter. It may return a value, a {@code CompletableFuture}, or {@code void};
 * a returned value or completed future becomes the reply body for request-reply messages.
 *
 * @author jbauer
 */
@Singleton
public class ConsumeEventProcessor {

    /** Class logger. */
    private static final Logger log = LoggerFactory.getLogger(ConsumeEventProcessor.class);

    /** Guice injector used to discover singleton beans. */
    private final Injector injector;

    /** Event bus the consumers bind to. */
    private final EventBusService eventBusService;

    /** Active registrations created by this processor. */
    private final List<EventBusRegistration> registrations = new ArrayList<>();

    /** Creates the processor.
     *
     * @param injector the Guice injector
     * @param eventBusService the event bus service
     */
    @Inject
    public ConsumeEventProcessor(Injector injector, EventBusService eventBusService) {
        this.injector = injector;
        this.eventBusService = eventBusService;
    }

    /**
     * Registers consumers for every annotated method found on Guice-bound singletons.
     *
     * @param beanClasses singleton bean classes to scan
     * @return the number of consumers registered
     */
    public int process(Iterable<Class<?>> beanClasses) {
        int registered = 0;
        for (Class<?> beanClass : beanClasses) {
            Object bean = injector.getInstance(beanClass);
            for (Method method : beanClass.getDeclaredMethods()) {
                ConsumeEvent annotation = method.getAnnotation(ConsumeEvent.class);
                if (annotation == null) {
                    continue;
                }
                register(bean, method, annotation);
                registered++;
            }
        }
        log.info("Registered {} @ConsumeEvent consumers", registered);
        return registered;
    }

    /** Unregisters every consumer created by {@link #process(Iterable)}. */
    public void unregisterAll() {
        registrations.forEach(EventBusRegistration::unregister);
        registrations.clear();
    }

    /** Validates one annotated method and binds it to its address.
     *
     * @param bean the singleton bean instance owning the method
     * @param method the annotated method
     * @param annotation the {@link ConsumeEvent} declaration
     */
    private void register(Object bean, Method method, ConsumeEvent annotation) {
        if (Modifier.isStatic(method.getModifiers())) {
            throw new IllegalArgumentException(
                "@ConsumeEvent method must not be static: " + method);
        }
        Class<?>[] parameterTypes = method.getParameterTypes();
        if (parameterTypes.length != 1) {
            throw new IllegalArgumentException(
                "@ConsumeEvent method must declare exactly one parameter: " + method);
        }
        boolean messageStyle = Message.class.isAssignableFrom(parameterTypes[0]);
        Class<?> returnType = method.getReturnType();
        if (returnType != void.class
                && !CompletableFuture.class.isAssignableFrom(returnType)
                && returnType == Void.class) {
            throw new IllegalArgumentException(
                "@ConsumeEvent method must return a value, CompletableFuture, or void: "
                    + method);
        }
        method.setAccessible(true);

        EventBusConsumer<Object> consumer = message -> {
            Object argument = messageStyle ? message : message.body();
            Object result;
            try {
                result = method.invoke(bean, argument);
            } catch (ReflectiveOperationException e) {
                return CompletableFuture.failedFuture(
                    e.getCause() != null ? e.getCause() : e);
            }
            if (result instanceof CompletableFuture<?> future) {
                @SuppressWarnings("unchecked")
                CompletableFuture<Object> typed = (CompletableFuture<Object>) future;
                return typed;
            }
            return CompletableFuture.completedFuture(result);
        };

        EventBusConsumerOptions options = new EventBusConsumerOptions()
            .withBlocking(annotation.blocking())
            .withOrdered(annotation.ordered())
            .withLocal(annotation.local())
            .withCodec(annotation.codec());
        registrations.add(eventBusService.registerConsumer(annotation.value(), consumer, options));
    }
}
