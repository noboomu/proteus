package io.sinistral.proteus.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that the annotated method should be executed on a virtual thread.
 * 
 * <p>This annotation instructs Proteus to invoke the annotated method on a new virtual thread
 * instead of the current thread (typically an I/O thread). This is useful for methods that
 * perform blocking operations without blocking the underlying platform thread.
 * 
 * <p><strong>Important considerations:</strong>
 * <ul>
 *   <li>Only use for I/O-bound workloads, not CPU-intensive operations</li>
 *   <li>Avoid synchronized blocks and native method calls that can pin the carrier thread</li>
 *   <li>Be careful with ThreadLocal usage as virtual threads are not pooled</li>
 * </ul>
 * 
 * <p>Example usage:
 * <pre>{@code
 * @GET
 * @Path("/blocking-operation")
 * @RunOnVirtualThread
 * public String performBlockingOperation() {
 *     // This will run on a virtual thread
 *     return callExternalService();
 * }
 * }</pre>
 * 
 * @see Thread#ofVirtual()
 * @since 1.0
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface RunOnVirtualThread {
    
    /**
     * Optional name prefix for the virtual thread.
     * If not specified, uses "proteus-virtual-thread-" as the default prefix.
     * 
     * @return the thread name prefix
     */
    String value() default "proteus-virtual-thread-";
}
