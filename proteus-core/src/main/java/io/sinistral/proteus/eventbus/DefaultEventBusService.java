package io.sinistral.proteus.eventbus;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.sinistral.proteus.security.SecurityContext;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import io.vertx.core.eventbus.MessageConsumer;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Default implementation of the EventBusService using Vert.x EventBus.
 * 
 * This implementation provides:
 * - Jackson JSON serialization by default
 * - Virtual thread-compatible async processing
 * - Security context propagation
 * - Performance metrics tracking
 * - Custom codec support
 * 
 * @since 1.0
 */
@Singleton
public class DefaultEventBusService implements EventBusService {
    
    private static final Logger logger = LoggerFactory.getLogger(DefaultEventBusService.class);
    
    private final Vertx vertx;
    private final EventBus eventBus;
    private final ObjectMapper objectMapper;
    
    // Metrics tracking
    private final AtomicLong messagesSent = new AtomicLong(0);
    private final AtomicLong messagesReceived = new AtomicLong(0);
    private final AtomicLong messagesPublished = new AtomicLong(0);
    private final AtomicLong requestsSent = new AtomicLong(0);
    private final AtomicLong repliesReceived = new AtomicLong(0);
    private final AtomicLong errorCount = new AtomicLong(0);
    private final AtomicLong timeoutCount = new AtomicLong(0);
    
    // Active registrations
    private final Map<String, EventBusRegistration> registrations = new ConcurrentHashMap<>();
    
    // Custom codecs
    private final Map<String, EventBusCodec<?>> codecs = new ConcurrentHashMap<>();
    
    @Inject
    public DefaultEventBusService(Vertx vertx, ObjectMapper objectMapper) {
        this.vertx = vertx;
        this.eventBus = vertx.eventBus();
        this.objectMapper = objectMapper;
        
        logger.info("Event Bus Service initialized with Vert.x instance");
    }
    
    @Override
    public <T> void publish(String address, T message) {
        try {
            String serialized = serializeMessage(message);
            eventBus.publish(address, serialized);
            messagesPublished.incrementAndGet();
            
            logger.debug("Published message to address: {}", address);
        } catch (Exception e) {
            errorCount.incrementAndGet();
            logger.error("Failed to publish message to address: {}", address, e);
            throw new EventBusException("Failed to publish message", e);
        }
    }
    
    @Override
    public <T> void send(String address, T message) {
        try {
            String serialized = serializeMessage(message);
            eventBus.send(address, serialized);
            messagesSent.incrementAndGet();
            
            logger.debug("Sent message to address: {}", address);
        } catch (Exception e) {
            errorCount.incrementAndGet();
            logger.error("Failed to send message to address: {}", address, e);
            throw new EventBusException("Failed to send message", e);
        }
    }
    
    @Override
    public <T, R> CompletableFuture<R> request(String address, T message, Class<R> responseType) {
        return request(address, message, responseType, 30000); // 30 second default timeout
    }
    
    @Override
    public <T, R> CompletableFuture<R> request(String address, T message, Class<R> responseType, long timeoutMs) {
        CompletableFuture<R> future = new CompletableFuture<>();
        
        try {
            String serialized = serializeMessage(message);
            DeliveryOptions options = new DeliveryOptions().setSendTimeout(timeoutMs);
            
            requestsSent.incrementAndGet();
            
            eventBus.request(address, serialized, options, ar -> {
                if (ar.succeeded()) {
                    try {
                        Message<Object> reply = ar.result();
                        String responseBody = reply.body().toString();
                        R response = deserializeMessage(responseBody, responseType);
                        
                        repliesReceived.incrementAndGet();
                        future.complete(response);
                        
                        logger.debug("Received reply for request to address: {}", address);
                    } catch (Exception e) {
                        errorCount.incrementAndGet();
                        logger.error("Failed to deserialize reply from address: {}", address, e);
                        future.completeExceptionally(new EventBusException("Failed to deserialize reply", e));
                    }
                } else {
                    if (ar.cause() instanceof io.vertx.core.eventbus.ReplyException) {
                        io.vertx.core.eventbus.ReplyException replyEx = (io.vertx.core.eventbus.ReplyException) ar.cause();
                        if (replyEx.failureType() == io.vertx.core.eventbus.ReplyFailure.TIMEOUT) {
                            timeoutCount.incrementAndGet();
                            logger.warn("Request to address {} timed out after {}ms", address, timeoutMs);
                        }
                    }
                    errorCount.incrementAndGet();
                    logger.error("Request to address {} failed", address, ar.cause());
                    future.completeExceptionally(new EventBusException("Request failed", ar.cause()));
                }
            });
            
            logger.debug("Sent request to address: {}", address);
        } catch (Exception e) {
            errorCount.incrementAndGet();
            logger.error("Failed to send request to address: {}", address, e);
            future.completeExceptionally(new EventBusException("Failed to send request", e));
        }
        
        return future;
    }
    
    @Override
    public <T> EventBusRegistration registerConsumer(String address, EventBusConsumer<T> consumer) {
        EventBusConsumerOptions options = new EventBusConsumerOptions();
        return registerConsumer(address, consumer, options);
    }
    
    @Override
    public <T> EventBusRegistration registerConsumer(String address, EventBusConsumer<T> consumer, EventBusConsumerOptions options) {
        try {
            MessageConsumer<Object> vertxConsumer;
            
            if (options.isLocal()) {
                vertxConsumer = eventBus.localConsumer(address);
            } else {
                vertxConsumer = eventBus.consumer(address);
            }
            
            vertxConsumer.handler(message -> {
                messagesReceived.incrementAndGet();
                
                try {
                    // Preserve security context if available
                    SecurityContext securityContext = SecurityContext.getCurrent();
                    
                    if (options.isBlocking()) {
                        // Execute on worker thread for blocking operations
                        vertx.executeBlocking(() -> {
                            try {
                                if (securityContext != null) {
                                    SecurityContext.setCurrent(securityContext);
                                }
                                
                                @SuppressWarnings("unchecked")
                                CompletableFuture<?> result = consumer.handle((Message<T>) message);
                                if (result != null) {
                                    result.whenComplete((response, throwable) -> {
                                        if (throwable != null) {
                                            logger.error("Consumer error for address: {}", address, throwable);
                                            message.fail(500, throwable.getMessage());
                                        } else {
                                            try {
                                                if (response != null) {
                                                    String serializedResponse = serializeMessage(response);
                                                    message.reply(serializedResponse);
                                                }
                                            } catch (Exception e) {
                                                logger.error("Failed to serialize response for address: {}", address, e);
                                                message.fail(500, "Serialization error");
                                            }
                                        }
                                    });
                                }
                                // Fire and forget case handled by not having result
                            } catch (Exception e) {
                                logger.error("Consumer error for address: {}", address, e);
                                message.fail(500, e.getMessage());
                                throw e;
                            } finally {
                                if (securityContext != null) {
                                    SecurityContext.clear();
                                }
                            }
                            return null;
                        }, false);
                    } else {
                        // Execute on event loop (non-blocking)
                        try {
                            if (securityContext != null) {
                                SecurityContext.setCurrent(securityContext);
                            }
                            
                            @SuppressWarnings("unchecked")
                            CompletableFuture<?> result = consumer.handle((Message<T>) message);
                            if (result != null) {
                                result.whenComplete((response, throwable) -> {
                                    if (throwable != null) {
                                        errorCount.incrementAndGet();
                                        logger.error("Consumer error for address: {}", address, throwable);
                                        message.fail(500, throwable.getMessage());
                                    } else {
                                        try {
                                            if (response != null) {
                                                String serializedResponse = serializeMessage(response);
                                                message.reply(serializedResponse);
                                            }
                                        } catch (Exception e) {
                                            errorCount.incrementAndGet();
                                            logger.error("Failed to serialize response for address: {}", address, e);
                                            message.fail(500, "Serialization error");
                                        }
                                    }
                                });
                            }
                        } catch (Exception e) {
                            errorCount.incrementAndGet();
                            logger.error("Consumer error for address: {}", address, e);
                            message.fail(500, e.getMessage());
                        } finally {
                            if (securityContext != null) {
                                SecurityContext.clear();
                            }
                        }
                    }
                } catch (Exception e) {
                    errorCount.incrementAndGet();
                    logger.error("Unexpected error processing message for address: {}", address, e);
                    message.fail(500, "Internal error");
                }
            });
            
            DefaultEventBusRegistration registration = new DefaultEventBusRegistration(address, vertxConsumer);
            registrations.put(address + "-" + System.nanoTime(), registration);
            
            logger.info("Registered consumer for address: {} (blocking: {}, local: {})", 
                       address, options.isBlocking(), options.isLocal());
            
            return registration;
            
        } catch (Exception e) {
            errorCount.incrementAndGet();
            logger.error("Failed to register consumer for address: {}", address, e);
            throw new EventBusException("Failed to register consumer", e);
        }
    }
    
    @Override
    public <T> void registerCodec(String codecName, EventBusCodec<T> codec) {
        codecs.put(codecName, codec);
        logger.info("Registered custom codec: {}", codecName);
    }
    
    @Override
    public boolean isAvailable() {
        return vertx != null && eventBus != null;
    }
    
    @Override
    public EventBusMetrics getMetrics() {
        long pendingRequests = requestsSent.get() - repliesReceived.get() - timeoutCount.get();
        
        return new EventBusMetrics(
            messagesSent.get(),
            messagesReceived.get(),
            messagesPublished.get(),
            requestsSent.get(),
            repliesReceived.get(),
            Math.max(0, pendingRequests),
            0, // Average latency - would need more sophisticated tracking
            0, // Max latency - would need more sophisticated tracking
            errorCount.get(),
            timeoutCount.get()
        );
    }
    
    private <T> String serializeMessage(T message) throws JsonProcessingException {
        if (message instanceof String) {
            return (String) message;
        }
        return objectMapper.writeValueAsString(message);
    }
    
    private <T> T deserializeMessage(String json, Class<T> type) throws JsonProcessingException {
        if (type == String.class) {
            return type.cast(json);
        }
        return objectMapper.readValue(json, type);
    }
    
    /**
     * Default implementation of EventBusRegistration.
     */
    private static class DefaultEventBusRegistration implements EventBusRegistration {
        private final String address;
        private final MessageConsumer<Object> consumer;
        private volatile boolean active = true;
        
        public DefaultEventBusRegistration(String address, MessageConsumer<Object> consumer) {
            this.address = address;
            this.consumer = consumer;
        }
        
        @Override
        public void unregister() {
            if (active) {
                consumer.unregister();
                active = false;
            }
        }
        
        @Override
        public String getAddress() {
            return address;
        }
        
        @Override
        public boolean isActive() {
            return active;
        }
    }
    
    /**
     * Exception for event bus related errors.
     */
    public static class EventBusException extends RuntimeException {
        public EventBusException(String message) {
            super(message);
        }
        
        public EventBusException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
