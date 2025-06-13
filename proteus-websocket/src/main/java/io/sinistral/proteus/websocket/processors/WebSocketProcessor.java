package io.sinistral.proteus.websocket.processors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.sinistral.proteus.annotations.websocket.*;
import io.sinistral.proteus.security.SecurityContext;
import io.sinistral.proteus.server.handlers.virtualthreads.VirtualThreadExecutorService;
import io.sinistral.proteus.websocket.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * Processor for @WebSocket annotated classes.
 * 
 * This processor discovers classes annotated with @WebSocket and automatically
 * registers them as WebSocket endpoints with appropriate event handlers.
 * 
 * @since 1.0
 */
@Singleton
public class WebSocketProcessor {
    
    private static final Logger logger = LoggerFactory.getLogger(WebSocketProcessor.class);
    
    private final DefaultWebSocketService webSocketService;
    private final ObjectMapper objectMapper;
    private final VirtualThreadExecutorService virtualThreadExecutor;
    
    @Inject
    public WebSocketProcessor(DefaultWebSocketService webSocketService, 
                             ObjectMapper objectMapper,
                             VirtualThreadExecutorService virtualThreadExecutor) {
        this.webSocketService = webSocketService;
        this.objectMapper = objectMapper;
        this.virtualThreadExecutor = virtualThreadExecutor;
    }
    
    /**
     * Processes an object instance to find and register @WebSocket endpoints.
     * 
     * @param instance the object instance to process
     */
    public void processInstance(Object instance) {
        Class<?> clazz = instance.getClass();
        WebSocket webSocketAnnotation = clazz.getAnnotation(WebSocket.class);
        
        if (webSocketAnnotation == null) {
            return; // Not a WebSocket endpoint
        }
        
        String path = webSocketAnnotation.value();
        logger.debug("Processing WebSocket endpoint: {} for class {}", path, clazz.getSimpleName());
        
        // Find handler methods
        Method onOpenMethod = findMethodWithAnnotation(clazz, OnOpen.class);
        Method onCloseMethod = findMethodWithAnnotation(clazz, OnClose.class);
        Method onErrorMethod = findMethodWithAnnotation(clazz, OnError.class);
        Method[] onMessageMethods = findMethodsWithAnnotation(clazz, OnMessage.class);
        
        // Create endpoint handler
        WebSocketEndpointHandler handler = new AnnotationBasedEndpointHandler(
                instance, onOpenMethod, onCloseMethod, onErrorMethod, onMessageMethods);
        
        // Register with service
        webSocketService.registerEndpoint(path, handler);
        
        logger.info("Registered WebSocket endpoint: {} -> {}", path, clazz.getSimpleName());
    }
    
    private Method findMethodWithAnnotation(Class<?> clazz, Class<? extends java.lang.annotation.Annotation> annotationClass) {
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(annotationClass)) {
                return method;
            }
        }
        return null;
    }
    
    private Method[] findMethodsWithAnnotation(Class<?> clazz, Class<? extends java.lang.annotation.Annotation> annotationClass) {
        return java.util.Arrays.stream(clazz.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(annotationClass))
                .toArray(Method[]::new);
    }
    
    /**
     * Implementation of WebSocketEndpointHandler that delegates to annotated methods.
     */
    private class AnnotationBasedEndpointHandler implements WebSocketEndpointHandler {
        
        private final Object instance;
        private final Method onOpenMethod;
        private final Method onCloseMethod;
        private final Method onErrorMethod;
        private final Method[] onMessageMethods;
        
        public AnnotationBasedEndpointHandler(Object instance, 
                                            Method onOpenMethod,
                                            Method onCloseMethod, 
                                            Method onErrorMethod,
                                            Method[] onMessageMethods) {
            this.instance = instance;
            this.onOpenMethod = onOpenMethod;
            this.onCloseMethod = onCloseMethod;
            this.onErrorMethod = onErrorMethod;
            this.onMessageMethods = onMessageMethods;
            
            // Make methods accessible
            if (onOpenMethod != null) onOpenMethod.setAccessible(true);
            if (onCloseMethod != null) onCloseMethod.setAccessible(true);
            if (onErrorMethod != null) onErrorMethod.setAccessible(true);
            for (Method method : onMessageMethods) {
                method.setAccessible(true);
            }
        }
        
        @Override
        public void onOpen(WebSocketConnection connection) {
            if (onOpenMethod != null) {
                executeMethod(onOpenMethod, connection, null, null, null);
            }
        }
        
        @Override
        public void onMessage(WebSocketConnection connection, String message) {
            Method handler = findTextMessageHandler();
            if (handler != null) {
                executeMethod(handler, connection, message, null, null);
            }
        }
        
        @Override
        public void onMessage(WebSocketConnection connection, byte[] data) {
            Method handler = findBinaryMessageHandler();
            if (handler != null) {
                executeMethod(handler, connection, null, data, null);
            }
        }
        
        @Override
        public void onClose(WebSocketConnection connection, CloseReason closeReason) {
            if (onCloseMethod != null) {
                executeMethod(onCloseMethod, connection, null, null, closeReason);
            }
        }
        
        @Override
        public void onError(WebSocketConnection connection, Throwable error) {
            if (onErrorMethod != null) {
                executeMethod(onErrorMethod, connection, null, null, error);
            }
        }
        
        private Method findTextMessageHandler() {
            for (Method method : onMessageMethods) {
                Parameter[] params = method.getParameters();
                for (Parameter param : params) {
                    if (param.getType() == String.class) {
                        return method;
                    }
                }
            }
            return onMessageMethods.length > 0 ? onMessageMethods[0] : null;
        }
        
        private Method findBinaryMessageHandler() {
            for (Method method : onMessageMethods) {
                Parameter[] params = method.getParameters();
                for (Parameter param : params) {
                    if (param.getType() == byte[].class) {
                        return method;
                    }
                }
            }
            return null;
        }
        
        private void executeMethod(Method method, WebSocketConnection connection, String textMessage, 
                                 byte[] binaryData, Object extraParam) {
            try {
                // Check if method should run on virtual thread
                boolean useVirtualThread = shouldUseVirtualThread(method);
                
                Runnable execution = () -> {
                    try {
                        Object[] args = buildMethodArguments(method, connection, textMessage, binaryData, extraParam);
                        method.invoke(instance, args);
                    } catch (Exception e) {
                        logger.error("Error executing WebSocket method {}.{}", 
                                   instance.getClass().getSimpleName(), method.getName(), e);
                    }
                };
                
                if (useVirtualThread && virtualThreadExecutor != null) {
                    virtualThreadExecutor.execute(execution);
                } else {
                    execution.run();
                }
                
            } catch (Exception e) {
                logger.error("Error executing WebSocket method {}.{}", 
                           instance.getClass().getSimpleName(), method.getName(), e);
            }
        }
        
        private boolean shouldUseVirtualThread(Method method) {
            OnOpen onOpen = method.getAnnotation(OnOpen.class);
            if (onOpen != null) return onOpen.virtual();
            
            OnMessage onMessage = method.getAnnotation(OnMessage.class);
            if (onMessage != null) return onMessage.virtual();
            
            OnClose onClose = method.getAnnotation(OnClose.class);
            if (onClose != null) return onClose.virtual();
            
            OnError onError = method.getAnnotation(OnError.class);
            if (onError != null) return onError.virtual();
            
            return false;
        }
        
        private Object[] buildMethodArguments(Method method, WebSocketConnection connection, 
                                            String textMessage, byte[] binaryData, Object extraParam) throws Exception {
            Parameter[] parameters = method.getParameters();
            Object[] args = new Object[parameters.length];
            
            for (int i = 0; i < parameters.length; i++) {
                Parameter param = parameters[i];
                Class<?> paramType = param.getType();
                
                if (paramType == WebSocketConnection.class) {
                    args[i] = connection;
                } else if (paramType == SecurityContext.class) {
                    args[i] = connection.getSecurityContext().orElse(null);
                } else if (paramType == String.class && textMessage != null) {
                    args[i] = textMessage;
                } else if (paramType == byte[].class && binaryData != null) {
                    args[i] = binaryData;
                } else if (paramType == CloseReason.class && extraParam instanceof CloseReason) {
                    args[i] = extraParam;
                } else if (paramType == Throwable.class && extraParam instanceof Throwable) {
                    args[i] = extraParam;
                } else if (textMessage != null && !paramType.isPrimitive() && paramType != String.class) {
                    // Try to deserialize JSON to object
                    try {
                        args[i] = objectMapper.readValue(textMessage, paramType);
                    } catch (Exception e) {
                        logger.warn("Failed to deserialize JSON message to {}: {}", paramType.getSimpleName(), e.getMessage());
                        args[i] = null;
                    }
                } else {
                    args[i] = null;
                }
            }
            
            return args;
        }
    }
}
