package io.sinistral.proteus.annotations.eventbus;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a method as an event consumer for the Proteus event bus.
 * This annotation enables reactive messaging patterns in Proteus applications.
 * 
 * The annotated method will be automatically registered as an event consumer
 * and will receive messages published to the specified address.
 * 
 * <p>Supported method signatures:
 * <ul>
 *   <li>{@code void consume(Message message)} - Fire and forget</li>
 *   <li>{@code CompletableFuture<T> consume(Message message)} - Async processing</li>
 *   <li>{@code T consume(Message message)} - Request/reply with return value</li>
 * </ul>
 * 
 * <p>Example usage:
 * <pre>{@code
 * @ConsumeEvent("user.created")
 * public void handleUserCreated(Message<User> message) {
 *     User user = message.body();
 *     // Process user creation
 * }
 * 
 * @ConsumeEvent(value = "user.validate", blocking = true)
 * public CompletableFuture<ValidationResult> validateUser(Message<User> message) {
 *     return CompletableFuture.supplyAsync(() -> {
 *         // Async validation logic
 *         return new ValidationResult(true);
 *     });
 * }
 * }</pre>
 * 
 * @since 1.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ConsumeEvent {
    
    /**
     * The event bus address to listen on.
     * This is the address that messages will be published to and consumed from.
     * 
     * @return the event bus address
     */
    String value();
    
    /**
     * Whether this consumer should be blocking or non-blocking.
     * 
     * <p>When {@code true}, the consumer will be executed on a worker thread
     * and can perform blocking operations without affecting the event loop.
     * 
     * <p>When {@code false} (default), the consumer will be executed on the
     * event loop and should not perform blocking operations.
     * 
     * @return {@code true} if the consumer should be blocking, {@code false} otherwise
     */
    boolean blocking() default false;
    
    /**
     * Whether this consumer should be ordered.
     * 
     * <p>When {@code true}, messages will be processed in the order they were
     * sent. This is useful for maintaining message ordering guarantees.
     * 
     * <p>When {@code false} (default), messages may be processed concurrently
     * for better performance.
     * 
     * @return {@code true} if message ordering should be preserved, {@code false} otherwise
     */
    boolean ordered() default false;
    
    /**
     * The local delivery mode for this consumer.
     * 
     * <p>When {@code true} (default), the consumer will only receive messages
     * published locally on the same Vert.x instance.
     * 
     * <p>When {@code false}, the consumer will receive messages from all
     * Vert.x instances in a clustered environment.
     * 
     * @return {@code true} for local delivery only, {@code false} for clustered delivery
     */
    boolean local() default true;
    
    /**
     * Optional codec name for message serialization/deserialization.
     * 
     * <p>If not specified, the default Jackson JSON codec will be used.
     * Custom codecs can be registered with the event bus for specialized
     * serialization requirements.
     * 
     * @return the codec name, or empty string for default codec
     */
    String codec() default "";
}
