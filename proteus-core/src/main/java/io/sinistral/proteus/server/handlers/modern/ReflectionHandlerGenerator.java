package io.sinistral.proteus.server.handlers.modern;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import io.sinistral.proteus.annotations.Blocking;
import io.sinistral.proteus.server.Extractors;
import io.sinistral.proteus.server.ServerRequest;
import io.sinistral.proteus.server.ServerResponse;
import io.sinistral.proteus.server.endpoints.EndpointInfo;
import io.undertow.server.HandlerWrapper;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.RoutingHandler;
import io.undertow.server.handlers.form.FormEncodedDataDefinition;
import io.undertow.server.handlers.form.MultiPartParserDefinition;
import io.undertow.util.Headers;
import io.undertow.util.Methods;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.function.Supplier;

/**
 * Modern reflection-based handler generator that replaces runtime compilation
 * with optimized MethodHandle-based parameter extraction and invocation.
 * 
 * Benefits:
 * - No runtime compilation overhead
 * - Faster startup times
 * - Lower memory usage
 * - Better performance with modern JVM optimizations
 */
public class ReflectionHandlerGenerator implements Supplier<RoutingHandler> {
    
    private static final Logger log = LoggerFactory.getLogger(ReflectionHandlerGenerator.class);
    
    private final Class<?> controllerClass;
    private final Object controllerInstance;
    private final MethodHandles.Lookup lookup;
    
    @Inject
    @Named("registeredEndpoints")
    protected Set<EndpointInfo> registeredEndpoints;

    @Inject
    @Named("registeredHandlerWrappers")
    protected Map<String, HandlerWrapper> registeredHandlerWrappers;
    
    public ReflectionHandlerGenerator(Class<?> controllerClass, Object controllerInstance) {
        this.controllerClass = controllerClass;
        this.controllerInstance = controllerInstance;
        this.lookup = MethodHandles.lookup();
    }
    
    @Override
    public RoutingHandler get() {
        RoutingHandler router = new RoutingHandler();
        
        // Process all @Path annotated methods
        Arrays.stream(controllerClass.getDeclaredMethods())
            .filter(method -> method.isAnnotationPresent(Path.class))
            .forEach(method -> processMethod(router, method));
            
        return router;
    }
    
    private void processMethod(RoutingHandler router, Method method) {
        try {
            String methodPath = extractMethodPath(method);
            String httpMethod = extractHttpMethod(method);
            
            if (httpMethod == null) {
                log.warn("No HTTP method annotation found for {}.{}", controllerClass.getSimpleName(), method.getName());
                return;
            }
            
            HttpHandler handler = createHandler(method);
            handler = wrapHandler(handler, method);
            
            router.add(Methods.fromString(httpMethod), methodPath, handler);
            
            // Register endpoint info
            registerEndpoint(method, methodPath, httpMethod);
            
        } catch (Exception e) {
            log.error("Failed to process method {}.{}", controllerClass.getSimpleName(), method.getName(), e);
        }
    }
    
    private HttpHandler createHandler(Method method) throws Exception {
        MethodHandle methodHandle = lookup.unreflect(method);
        ParameterExtractor[] extractors = createParameterExtractors(method);
        boolean isAsync = isAsyncMethod(method);
        
        return new ReflectionHttpHandler(
            controllerInstance,
            methodHandle,
            extractors,
            isAsync
        );
    }
    
    private ParameterExtractor[] createParameterExtractors(Method method) {
        Parameter[] parameters = method.getParameters();
        ParameterExtractor[] extractors = new ParameterExtractor[parameters.length];
        
        for (int i = 0; i < parameters.length; i++) {
            extractors[i] = createParameterExtractor(parameters[i]);
        }
        
        return extractors;
    }
    
    private ParameterExtractor createParameterExtractor(Parameter parameter) {
        Class<?> rawType = parameter.getType();
        
        // Handle special framework types
        if (rawType == ServerRequest.class) {
            return new ServerRequestExtractor();
        }
        if (rawType == HttpServerExchange.class) {
            return new ExchangeExtractor();
        }
        
        // Handle JAX-RS annotations
        QueryParam queryParam = parameter.getAnnotation(QueryParam.class);
        if (queryParam != null) {
            return new QueryParameterExtractor(queryParam.value(), rawType);
        }
        
        PathParam pathParam = parameter.getAnnotation(PathParam.class);
        if (pathParam != null) {
            return new PathParameterExtractor(pathParam.value(), rawType);
        }
        
        HeaderParam headerParam = parameter.getAnnotation(HeaderParam.class);
        if (headerParam != null) {
            return new HeaderParameterExtractor(headerParam.value(), rawType);
        }
        
        FormParam formParam = parameter.getAnnotation(FormParam.class);
        if (formParam != null) {
            return new FormParameterExtractor(formParam.value(), rawType);
        }
        
        BeanParam beanParam = parameter.getAnnotation(BeanParam.class);
        if (beanParam != null) {
            return new BeanParameterExtractor(rawType);
        }
        
        // Default to body parameter for complex types
        return new BodyParameterExtractor(rawType);
    }
    
    private boolean isAsyncMethod(Method method) {
        Class<?> returnType = method.getReturnType();
        return CompletableFuture.class.isAssignableFrom(returnType) ||
               CompletionStage.class.isAssignableFrom(returnType);
    }
    
    private HttpHandler wrapHandler(HttpHandler handler, Method method) {
        // Apply blocking wrapper if needed
        boolean isBlocking = method.isAnnotationPresent(Blocking.class);
        String consumesContentType = extractConsumes(method);
        
        HttpHandler result = handler;
        
        if (isBlocking || needsBlocking(consumesContentType)) {
            final HttpHandler innerHandler = result;
            result = exchange -> {
                if (exchange.isInIoThread()) {
                    exchange.dispatch(innerHandler);
                } else {
                    exchange.startBlocking();
                    innerHandler.handleRequest(exchange);
                }
            };
        }
        
        // Apply method-level wrappers
        io.sinistral.proteus.annotations.Chain chain = method.getAnnotation(io.sinistral.proteus.annotations.Chain.class);
        if (chain != null) {
            for (Class<? extends HandlerWrapper> wrapperClass : chain.value()) {
                HandlerWrapper wrapper = registeredHandlerWrappers.get(wrapperClass.getSimpleName());
                if (wrapper != null) {
                    result = wrapper.wrap(result);
                }
            }
        }
        
        return result;
    }
    
    private boolean needsBlocking(String consumesContentType) {
        return consumesContentType != null && (
            consumesContentType.contains(FormEncodedDataDefinition.APPLICATION_X_WWW_FORM_URLENCODED) ||
            consumesContentType.contains(MultiPartParserDefinition.MULTIPART_FORM_DATA)
        );
    }
    
    private String extractMethodPath(Method method) {
        Path pathAnnotation = method.getAnnotation(Path.class);
        String methodPath = pathAnnotation.value();
        
        // Combine with class-level path if present
        Path classPath = controllerClass.getAnnotation(Path.class);
        if (classPath != null) {
            String classPathValue = classPath.value();
            if (!classPathValue.endsWith("/") && !methodPath.startsWith("/")) {
                methodPath = classPathValue + "/" + methodPath;
            } else {
                methodPath = classPathValue + methodPath;
            }
        }
        
        return methodPath.replaceAll("//+", "/");
    }
    
    private String extractHttpMethod(Method method) {
        for (Annotation annotation : method.getAnnotations()) {
            String annotationName = annotation.annotationType().getSimpleName();
            switch (annotationName) {
                case "GET": return "GET";
                case "POST": return "POST";
                case "PUT": return "PUT";
                case "DELETE": return "DELETE";
                case "PATCH": return "PATCH";
                case "HEAD": return "HEAD";
                case "OPTIONS": return "OPTIONS";
            }
        }
        return null;
    }
    
    private String extractConsumes(Method method) {
        Consumes consumes = method.getAnnotation(Consumes.class);
        if (consumes == null) {
            consumes = controllerClass.getAnnotation(Consumes.class);
        }
        return consumes != null ? String.join(",", consumes.value()) : null;
    }
    
    private String extractProduces(Method method) {
        Produces produces = method.getAnnotation(Produces.class);
        if (produces == null) {
            produces = controllerClass.getAnnotation(Produces.class);
        }
        return produces != null ? String.join(",", produces.value()) : MediaType.APPLICATION_JSON;
    }
    
    private void registerEndpoint(Method method, String path, String httpMethod) {
        EndpointInfo endpointInfo = new EndpointInfo();
        endpointInfo.setControllerName(controllerClass.getSimpleName());
        endpointInfo.setControllerMethod(method.getName());
        endpointInfo.setPathTemplate(path);
        endpointInfo.setMethod(Extractors.httpMethodFromMethod.apply(method));
        endpointInfo.setConsumes(extractConsumes(method));
        endpointInfo.setProduces(extractProduces(method));
        
        registeredEndpoints.add(endpointInfo);
    }
    
    /**
     * Modern HTTP handler that uses MethodHandles for optimal performance
     */
    private static class ReflectionHttpHandler implements HttpHandler {
        private final Object controllerInstance;
        private final MethodHandle methodHandle;
        private final ParameterExtractor[] parameterExtractors;
        private final boolean isAsync;
        
        public ReflectionHttpHandler(Object controllerInstance, MethodHandle methodHandle,
                                   ParameterExtractor[] parameterExtractors, boolean isAsync) {
            this.controllerInstance = controllerInstance;
            this.methodHandle = methodHandle;
            this.parameterExtractors = parameterExtractors;
            this.isAsync = isAsync;
        }
        
        @Override
        public void handleRequest(HttpServerExchange exchange) throws Exception {
            // Extract parameters
            Object[] args = new Object[parameterExtractors.length];
            for (int i = 0; i < parameterExtractors.length; i++) {
                args[i] = parameterExtractors[i].extract(exchange);
            }
            
            if (isAsync) {
                handleAsyncRequest(exchange, args);
            } else {
                handleSyncRequest(exchange, args);
            }
        }
        
        private void handleAsyncRequest(HttpServerExchange exchange, Object[] args) throws Exception {
            try {
                CompletionStage<?> future = (CompletionStage<?>) methodHandle.invokeWithArguments(
                    prepend(controllerInstance, args)
                );
                
                future.whenComplete((result, throwable) -> {
                    if (throwable != null) {
                        exchange.putAttachment(io.undertow.server.handlers.ExceptionHandler.THROWABLE, throwable);
                        exchange.setStatusCode(500);
                        exchange.endExchange();
                    } else {
                        sendResponse(exchange, result);
                    }
                });
            } catch (Throwable t) {
                throw new Exception("Failed to invoke async method", t);
            }
        }
        
        private void handleSyncRequest(HttpServerExchange exchange, Object[] args) throws Exception {
            try {
                Object result = methodHandle.invokeWithArguments(
                    prepend(controllerInstance, args)
                );
                sendResponse(exchange, result);
            } catch (Throwable t) {
                throw new Exception("Failed to invoke sync method", t);
            }
        }
        
        private void sendResponse(HttpServerExchange exchange, Object result) {
            if (result instanceof ServerResponse) {
                ((ServerResponse<?>) result).send(exchange);
            } else if (result instanceof String) {
                exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, MediaType.TEXT_PLAIN);
                exchange.getResponseSender().send((String) result);
            } else {
                // Use ServerResponse for JSON serialization
                ServerResponse.response(result).applicationJson().send(exchange);
            }
        }
        
        private Object[] prepend(Object first, Object[] array) {
            Object[] result = new Object[array.length + 1];
            result[0] = first;
            System.arraycopy(array, 0, result, 1, array.length);
            return result;
        }
    }
    
    // Parameter extractors using modern reflection
    private interface ParameterExtractor {
        Object extract(HttpServerExchange exchange) throws Exception;
    }
    
    private static class ServerRequestExtractor implements ParameterExtractor {
        @Override
        public Object extract(HttpServerExchange exchange) throws Exception {
            try {
                return new ServerRequest(exchange);
            } catch (IOException e) {
                throw new Exception("Failed to create ServerRequest", e);
            }
        }
    }
    
    private static class ExchangeExtractor implements ParameterExtractor {
        @Override
        public Object extract(HttpServerExchange exchange) {
            return exchange;
        }
    }
    
    private static class QueryParameterExtractor implements ParameterExtractor {
        private final String paramName;
        private final Class<?> type;
        
        public QueryParameterExtractor(String paramName, Class<?> type) {
            this.paramName = paramName;
            this.type = type;
        }
        
        @Override
        public Object extract(HttpServerExchange exchange) throws Exception {
            if (type == String.class) {
                return Extractors.string(exchange, paramName);
            } else if (type == Integer.class || type == int.class) {
                return Extractors.integerValue(exchange, paramName);
            } else if (type == Long.class || type == long.class) {
                return Extractors.longValue(exchange, paramName);
            } else if (type == Boolean.class || type == boolean.class) {
                return Extractors.booleanValue(exchange, paramName);
            } else if (type == Float.class || type == float.class) {
                return Extractors.floatValue(exchange, paramName);
            } else if (type == Double.class || type == double.class) {
                return Extractors.doubleValue(exchange, paramName);
            } else {
                // For complex types, try to convert from string
                String value = Extractors.string(exchange, paramName);
                return convertStringToType(value, type);
            }
        }
    }
    
    private static class PathParameterExtractor implements ParameterExtractor {
        private final String paramName;
        private final Class<?> type;
        
        public PathParameterExtractor(String paramName, Class<?> type) {
            this.paramName = paramName;
            this.type = type;
        }
        
        @Override
        public Object extract(HttpServerExchange exchange) throws Exception {
            // Path parameters are extracted from the exchange path parameters
            String value = exchange.getPathParameters().get(paramName).getFirst();
            if (type == String.class) {
                return value;
            } else if (type == Integer.class || type == int.class) {
                return Integer.parseInt(value);
            } else if (type == Long.class || type == long.class) {
                return Long.parseLong(value);
            } else if (type == Boolean.class || type == boolean.class) {
                return Boolean.parseBoolean(value);
            } else if (type == Float.class || type == float.class) {
                return Float.parseFloat(value);
            } else if (type == Double.class || type == double.class) {
                return Double.parseDouble(value);
            } else {
                return convertStringToType(value, type);
            }
        }
    }
    
    private static class HeaderParameterExtractor implements ParameterExtractor {
        private final String headerName;
        private final Class<?> type;
        
        public HeaderParameterExtractor(String headerName, Class<?> type) {
            this.headerName = headerName;
            this.type = type;
        }
        
        @Override
        public Object extract(HttpServerExchange exchange) throws Exception {
            if (type == String.class) {
                return Extractors.Header.string(exchange, headerName);
            } else {
                String value = Extractors.Header.string(exchange, headerName);
                return convertStringToType(value, type);
            }
        }
    }
    
    private static class FormParameterExtractor implements ParameterExtractor {
        private final String paramName;
        private final Class<?> type;
        
        public FormParameterExtractor(String paramName, Class<?> type) {
            this.paramName = paramName;
            this.type = type;
        }
        
        @Override
        public Object extract(HttpServerExchange exchange) throws Exception {
            // For form parameters, we need to handle them differently
            // This is a simplified implementation
            if (type == String.class) {
                return Extractors.string(exchange, paramName);
            } else {
                String value = Extractors.string(exchange, paramName);
                return convertStringToType(value, type);
            }
        }
    }
    
    private static class BeanParameterExtractor implements ParameterExtractor {
        private final Class<?> beanClass;
        
        public BeanParameterExtractor(Class<?> beanClass) {
            this.beanClass = beanClass;
        }
        
        @Override
        public Object extract(HttpServerExchange exchange) throws Exception {
            // For bean parameters, we would need to instantiate and populate the bean
            // This is a complex operation that typically involves reflection
            // For now, return null and implement later
            throw new UnsupportedOperationException("Bean parameter extraction not implemented yet for type: " + beanClass.getName());
        }
    }
    
    private static class BodyParameterExtractor implements ParameterExtractor {
        private final Class<?> type;
        
        public BodyParameterExtractor(Class<?> type) {
            this.type = type;
        }
        
        @Override
        public Object extract(HttpServerExchange exchange) throws Exception {
            if (type == String.class) {
                return Extractors.jsonNode(exchange).asText();
            } else {
                return Extractors.model(exchange, type);
            }
        }
    }
    
    private static Object convertStringToType(String value, Class<?> type) throws Exception {
        if (value == null) {
            return null;
        }
        
        if (type == String.class) {
            return value;
        } else if (type == Integer.class || type == int.class) {
            return Integer.parseInt(value);
        } else if (type == Long.class || type == long.class) {
            return Long.parseLong(value);
        } else if (type == Boolean.class || type == boolean.class) {
            return Boolean.parseBoolean(value);
        } else if (type == Float.class || type == float.class) {
            return Float.parseFloat(value);
        } else if (type == Double.class || type == double.class) {
            return Double.parseDouble(value);
        } else {
            throw new IllegalArgumentException("Unsupported parameter type: " + type);
        }
    }
}
