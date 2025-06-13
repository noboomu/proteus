package io.sinistral.proteus.eventbus.processors;

import io.sinistral.proteus.annotations.eventbus.ConsumeEvent;
import io.sinistral.proteus.eventbus.EventBusConsumer;
import io.sinistral.proteus.eventbus.EventBusConsumerOptions;
import io.sinistral.proteus.eventbus.EventBusService;
import io.vertx.core.eventbus.Message;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.concurrent.CompletableFuture;

/**
 * Processor for @ConsumeEvent annotated methods.
 * 
 * This processor discovers methods annotated with @ConsumeEvent and automatically
 * registers them as event bus consumers.
 * 
 * @since 1.0
 */
@Singleton
public class ConsumeEventProcessor {
    
    private static final Logger logger = LoggerFactory.getLogger(ConsumeEventProcessor.class);
    
    private final EventBusService eventBusService;
    private final ObjectMapper objectMapper;
    
    @Inject
    public ConsumeEventProcessor(EventBusService eventBusService, ObjectMapper objectMapper) {
        this.eventBusService = eventBusService;
        this.objectMapper = objectMapper;
    }
    
    /**
     * Processes an object instance to find and register @ConsumeEvent methods.
     * 
     * @param instance the object instance to process
     */
    public void processInstance(Object instance) {
        Class<?> clazz = instance.getClass();
        
        for (Method method : clazz.getDeclaredMethods()) {
            ConsumeEvent annotation = method.getAnnotation(ConsumeEvent.class);
            if (annotation != null) {
                processConsumeEventMethod(instance, method, annotation);
            }
        }
    }
    
    /**
     * Processes a single @ConsumeEvent method.
     */
    private void processConsumeEventMethod(Object instance, Method method, ConsumeEvent annotation) {
        try {
            // Validate method signature
            validateMethodSignature(method);
            
            // Extract message type from method parameter
            Class<?> messageType = extractMessageType(method);
            
            // Create consumer options
            EventBusConsumerOptions options = new EventBusConsumerOptions()
                .setBlocking(annotation.blocking())
                .setOrdered(annotation.ordered())
                .setLocal(annotation.local())
                .setCodec(annotation.codec());
            
            // Create event bus consumer
            EventBusConsumer<Object> consumer = createConsumer(instance, method, messageType);
            
            // Register consumer
            eventBusService.registerConsumer(annotation.value(), consumer, options);
            
            logger.info("Registered event consumer: {} -> {}.{}", 
                       annotation.value(), instance.getClass().getSimpleName(), method.getName());
                       
        } catch (Exception e) {
            logger.error("Failed to register event consumer for method: {}.{}", 
                        instance.getClass().getSimpleName(), method.getName(), e);
            throw new RuntimeException("Failed to register event consumer", e);
        }
    }
    
    /**
     * Validates that the method has a valid signature for event consumption.
     */
    private void validateMethodSignature(Method method) {
        Parameter[] parameters = method.getParameters();
        
        if (parameters.length != 1) {
            throw new IllegalArgumentException(
                "Event consumer method must have exactly one parameter: " + method.getName());
        }
        
        Parameter param = parameters[0];
        if (!Message.class.isAssignableFrom(param.getType())) {
            throw new IllegalArgumentException(
                "Event consumer method parameter must be of type Message<T>: " + method.getName());
        }
        
        Class<?> returnType = method.getReturnType();
        if (returnType != void.class && 
            returnType != CompletableFuture.class && 
            !CompletableFuture.class.isAssignableFrom(returnType)) {
            
            // Allow any return type for request/reply pattern
            logger.debug("Method {} returns {}, will be used for request/reply pattern", 
                        method.getName(), returnType.getSimpleName());
        }
    }
    
    /**
     * Extracts the message type from the method parameter.
     */
    private Class<?> extractMessageType(Method method) {
        Parameter parameter = method.getParameters()[0];
        Type parameterType = parameter.getParameterizedType();
        
        if (parameterType instanceof ParameterizedType) {
            ParameterizedType pType = (ParameterizedType) parameterType;
            Type[] actualTypes = pType.getActualTypeArguments();
            
            if (actualTypes.length > 0) {
                Type messageType = actualTypes[0];
                if (messageType instanceof Class) {
                    return (Class<?>) messageType;
                } else if (messageType instanceof ParameterizedType) {
                    return (Class<?>) ((ParameterizedType) messageType).getRawType();
                }
            }
        }
        
        // Default to Object if we can't determine the type
        return Object.class;
    }
    
    /**
     * Creates an EventBusConsumer that wraps the annotated method.
     */
    private EventBusConsumer<Object> createConsumer(Object instance, Method method, Class<?> messageType) {
        method.setAccessible(true);
        
        return (Message<Object> message) -> {
            try {
                // Deserialize message body if needed
                Object messageBody = deserializeMessageBody(message.body(), messageType);
                
                // Create a message wrapper with the properly typed body
                MessageWrapper<Object> wrappedMessage = new MessageWrapper<>(message, messageBody);
                
                // Invoke the method
                Object result = method.invoke(instance, wrappedMessage);
                
                // Handle return value
                if (result == null) {
                    // Fire and forget
                    return null;
                } else if (result instanceof CompletableFuture) {
                    // Already a CompletableFuture
                    return (CompletableFuture<?>) result;
                } else {
                    // Wrap in completed future for request/reply
                    return CompletableFuture.completedFuture(result);
                }
                
            } catch (Exception e) {
                logger.error("Error invoking event consumer method: {}.{}", 
                            instance.getClass().getSimpleName(), method.getName(), e);
                return CompletableFuture.failedFuture(e);
            }
        };
    }
    
    /**
     * Deserializes the message body to the expected type.
     */
    private Object deserializeMessageBody(Object body, Class<?> expectedType) throws Exception {
        if (body == null) {
            return null;
        }
        
        if (expectedType.isAssignableFrom(body.getClass())) {
            return body;
        }
        
        if (body instanceof String && expectedType != String.class) {
            // Deserialize JSON string to expected type
            return objectMapper.readValue((String) body, expectedType);
        }
        
        return body;
    }
    
    /**
     * Wrapper for Vert.x Message that provides properly typed body access.
     */
    private static class MessageWrapper<T> implements Message<T> {
        private final Message<Object> originalMessage;
        private final T typedBody;
        
        public MessageWrapper(Message<Object> originalMessage, T typedBody) {
            this.originalMessage = originalMessage;
            this.typedBody = typedBody;
        }
        
        @Override
        public String address() {
            return originalMessage.address();
        }
        
        @Override
        public io.vertx.core.MultiMap headers() {
            return originalMessage.headers();
        }
        
        @Override
        public T body() {
            return typedBody;
        }
        
        @Override
        public String replyAddress() {
            return originalMessage.replyAddress();
        }
        
        @Override
        public boolean isSend() {
            return originalMessage.isSend();
        }
        
        @Override
        public void reply(Object message) {
            originalMessage.reply(message);
        }
        
        @Override
        public void reply(Object message, io.vertx.core.eventbus.DeliveryOptions options) {
            originalMessage.reply(message, options);
        }
        
        @Override
        public <R> void replyAndRequest(Object message, io.vertx.core.Handler<io.vertx.core.AsyncResult<Message<R>>> replyHandler) {
            originalMessage.replyAndRequest(message, replyHandler);
        }
        
        @Override
        public <R> void replyAndRequest(Object message, io.vertx.core.eventbus.DeliveryOptions options, io.vertx.core.Handler<io.vertx.core.AsyncResult<Message<R>>> replyHandler) {
            originalMessage.replyAndRequest(message, options, replyHandler);
        }
        
        @Override
        public <R> io.vertx.core.Future<Message<R>> replyAndRequest(Object message, io.vertx.core.eventbus.DeliveryOptions options) {
            return originalMessage.replyAndRequest(message, options);
        }
        
        @Override
        public void fail(int failureCode, String message) {
            originalMessage.fail(failureCode, message);
        }
    }
}
