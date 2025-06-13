package io.sinistral.proteus.server.handlers.virtualthreads;

import java.lang.reflect.Method;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.sinistral.proteus.annotations.RunOnVirtualThread;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;

/**
 * Processor that handles virtual thread execution for methods annotated with {@link RunOnVirtualThread}.
 * 
 * <p>This class integrates with Undertow's {@link HttpServerExchange} to offload execution
 * to virtual threads while maintaining proper request/response lifecycle management.
 * 
 * <p>Key features:
 * <ul>
 *   <li>Detects {@code @RunOnVirtualThread} annotations on methods</li>
 *   <li>Wraps handlers to execute on virtual threads</li>
 *   <li>Maintains exchange state and error handling</li>
 *   <li>Supports async completion with CompletableFuture</li>
 * </ul>
 */
public class VirtualThreadProcessor {
    
    private static final Logger log = LoggerFactory.getLogger(VirtualThreadProcessor.class);
    
    private final VirtualThreadExecutorService executorService;
    
    /**
     * Creates a new virtual thread processor.
     * 
     * @param executorService the virtual thread executor service
     */
    public VirtualThreadProcessor(VirtualThreadExecutorService executorService) {
        this.executorService = executorService;
    }
    
    /**
     * Checks if a method should be executed on a virtual thread.
     * 
     * @param method the method to check
     * @return true if the method has {@code @RunOnVirtualThread} annotation
     */
    public boolean shouldRunOnVirtualThread(Method method) {
        return method.isAnnotationPresent(RunOnVirtualThread.class) ||
               method.getDeclaringClass().isAnnotationPresent(RunOnVirtualThread.class);
    }
    
    /**
     * Wraps an HTTP handler to execute on a virtual thread if needed.
     * 
     * @param originalHandler the original handler
     * @param method the controller method
     * @return wrapped handler that may execute on virtual thread
     */
    public HttpHandler wrapHandler(HttpHandler originalHandler, Method method) {
        if (!shouldRunOnVirtualThread(method)) {
            return originalHandler;
        }
        
        if (!VirtualThreadExecutorService.isVirtualThreadSupported()) {
            log.warn("Virtual threads not supported, falling back to regular execution for method: {}", 
                    method.getName());
            return originalHandler;
        }
        
        RunOnVirtualThread annotation = getAnnotation(method);
        String threadName = annotation != null ? annotation.value() : "proteus-virtual-thread-";
        
        return new VirtualThreadHandler(originalHandler, threadName);
    }
    
    /**
     * Creates a virtual thread-aware handler for async operations.
     * 
     * @param asyncOperation the async operation to execute
     * @param method the controller method
     * @return handler that executes the async operation on virtual thread
     */
    public HttpHandler createAsyncHandler(AsyncOperation asyncOperation, Method method) {
        if (!shouldRunOnVirtualThread(method)) {
            // Return regular async handler
            return exchange -> {
                try {
                    CompletableFuture<?> future = asyncOperation.execute(exchange);
                    if (future != null) {
                        exchange.dispatch();
                        future.whenComplete((result, throwable) -> {
                            if (throwable != null) {
                                handleAsyncError(exchange, throwable);
                            } else {
                                handleAsyncResult(exchange, result);
                            }
                        });
                    }
                } catch (Exception e) {
                    handleAsyncError(exchange, e);
                }
            };
        }
        
        if (!VirtualThreadExecutorService.isVirtualThreadSupported()) {
            log.warn("Virtual threads not supported, falling back to regular async execution for method: {}", 
                    method.getName());
            return createAsyncHandler(asyncOperation, null); // Recursive call without virtual thread
        }
        
        // Virtual thread async handler
        return exchange -> {
            exchange.dispatch();
            executorService.execute(() -> {
                try {
                    CompletableFuture<?> future = asyncOperation.execute(exchange);
                    if (future != null) {
                        future.whenComplete((result, throwable) -> {
                            if (throwable != null) {
                                handleAsyncError(exchange, throwable);
                            } else {
                                handleAsyncResult(exchange, result);
                            }
                        });
                    }
                } catch (Exception e) {
                    handleAsyncError(exchange, e);
                }
            });
        };
    }
    
    /**
     * Gets the RunOnVirtualThread annotation from method or class.
     * 
     * @param method the method
     * @return the annotation, or null if not present
     */
    private RunOnVirtualThread getAnnotation(Method method) {
        RunOnVirtualThread annotation = method.getAnnotation(RunOnVirtualThread.class);
        if (annotation == null) {
            annotation = method.getDeclaringClass().getAnnotation(RunOnVirtualThread.class);
        }
        return annotation;
    }
    
    /**
     * Handles async operation results.
     * 
     * @param exchange the HTTP exchange
     * @param result the result
     */
    private void handleAsyncResult(HttpServerExchange exchange, Object result) {
        try {
            if (!exchange.isComplete()) {
                // Result handling would be done by the original handler
                exchange.endExchange();
            }
        } catch (Exception e) {
            log.error("Error handling async result", e);
            if (!exchange.isComplete()) {
                exchange.setStatusCode(500);
                exchange.endExchange();
            }
        }
    }
    
    /**
     * Handles async operation errors.
     * 
     * @param exchange the HTTP exchange
     * @param throwable the error
     */
    private void handleAsyncError(HttpServerExchange exchange, Throwable throwable) {
        log.error("Error in async virtual thread operation", throwable);
        try {
            if (!exchange.isComplete()) {
                exchange.setStatusCode(500);
                exchange.getResponseSender().send("Internal Server Error");
            }
        } catch (Exception e) {
            log.error("Error sending error response", e);
        }
    }
    
    /**
     * Virtual thread HTTP handler wrapper.
     */
    private class VirtualThreadHandler implements HttpHandler {
        
        private final HttpHandler delegate;
        private final String threadNamePrefix;
        
        public VirtualThreadHandler(HttpHandler delegate, String threadNamePrefix) {
            this.delegate = delegate;
            this.threadNamePrefix = threadNamePrefix;
        }
        
        @Override
        public void handleRequest(HttpServerExchange exchange) throws Exception {
            // Dispatch to virtual thread
            exchange.dispatch();
            executorService.execute(() -> {
                try {
                    delegate.handleRequest(exchange);
                } catch (Exception e) {
                    log.error("Error in virtual thread handler", e);
                    try {
                        if (!exchange.isComplete()) {
                            exchange.setStatusCode(500);
                            exchange.getResponseSender().send("Internal Server Error");
                        }
                    } catch (Exception sendError) {
                        log.error("Error sending error response", sendError);
                    }
                }
            });
        }
    }
    
    /**
     * Interface for async operations that can be executed on virtual threads.
     */
    @FunctionalInterface
    public interface AsyncOperation {
        CompletableFuture<?> execute(HttpServerExchange exchange) throws Exception;
    }
}
