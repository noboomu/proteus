package io.sinistral.proteus.security.handlers;

import io.sinistral.proteus.security.SecurityContext;
import io.sinistral.proteus.security.jwt.JwtService;
import io.sinistral.proteus.annotations.security.RolesAllowed;
import io.sinistral.proteus.annotations.security.PermitAll;
import io.sinistral.proteus.annotations.security.DenyAll;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;
import io.undertow.util.StatusCodes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Optional;

/**
 * Security processor that handles JWT authentication and authorization for HTTP requests.
 * This processor integrates with Undertow handlers to provide transparent security enforcement.
 * 
 * @since 1.0
 */
public class SecurityProcessor {
    
    private static final Logger logger = LoggerFactory.getLogger(SecurityProcessor.class);
    private static final String SECURITY_CONTEXT_ATTRIBUTE = "proteus.security.context";
    
    private final JwtService jwtService;
    
    public SecurityProcessor(JwtService jwtService) {
        this.jwtService = jwtService;
    }
    
    /**
     * Creates a security-aware HTTP handler that wraps the original handler.
     * This method analyzes the target method's security annotations and applies
     * appropriate authentication and authorization checks.
     * 
     * @param originalHandler The original HTTP handler
     * @param targetMethod The target method to be secured
     * @return A wrapped handler with security enforcement
     */
    public HttpHandler createSecureHandler(HttpHandler originalHandler, Method targetMethod) {
        SecurityRequirement requirement = analyzeSecurityRequirements(targetMethod);
        
        return exchange -> {
            try {
                // Check for explicit denial
                if (requirement.isDenyAll()) {
                    sendUnauthorized(exchange, "Access denied");
                    return;
                }
                
                // Extract and validate security context
                Optional<SecurityContext> securityContext = extractSecurityContext(exchange);
                
                if (securityContext.isPresent()) {
                    // Store security context for use in handlers
                    exchange.putAttachment(SecurityContextAttachment.KEY, securityContext.get());
                }
                
                // Check authentication requirement
                if (requirement.requiresAuthentication() && securityContext.isEmpty()) {
                    sendUnauthorized(exchange, "Authentication required");
                    return;
                }
                
                // Check role authorization
                if (requirement.hasRoleRequirements() && securityContext.isPresent()) {
                    SecurityContext context = securityContext.get();
                    if (!context.hasAnyRole(requirement.getAllowedRoles())) {
                        sendForbidden(exchange, "Insufficient privileges");
                        return;
                    }
                }
                
                // All security checks passed, proceed with original handler
                originalHandler.handleRequest(exchange);
                
            } catch (Exception e) {
                logger.error("Security processing failed", e);
                sendInternalError(exchange, "Security processing error");
            }
        };
    }
    
    /**
     * Extracts security context from the HTTP request.
     * This method looks for JWT tokens in the Authorization header.
     * 
     * @param exchange The HTTP server exchange
     * @return Security context if authentication is successful, empty otherwise
     */
    public Optional<SecurityContext> extractSecurityContext(HttpServerExchange exchange) {
        String authHeader = exchange.getRequestHeaders().getFirst(Headers.AUTHORIZATION);
        if (authHeader == null || authHeader.trim().isEmpty()) {
            return Optional.empty();
        }
        
        try {
            return jwtService.processAuthorizationHeader(authHeader);
        } catch (Exception e) {
            logger.debug("Failed to process authorization header", e);
            return Optional.empty();
        }
    }
    
    /**
     * Gets the security context from the current exchange.
     * This method retrieves the security context that was stored during security processing.
     * 
     * @param exchange The HTTP server exchange
     * @return Security context if available, empty otherwise
     */
    public static Optional<SecurityContext> getSecurityContext(HttpServerExchange exchange) {
        return Optional.ofNullable(exchange.getAttachment(SecurityContextAttachment.KEY));
    }
    
    private SecurityRequirement analyzeSecurityRequirements(Method method) {
        SecurityRequirement.Builder builder = SecurityRequirement.builder();
        
        // Check method-level annotations first
        if (method.isAnnotationPresent(DenyAll.class)) {
            builder.denyAll();
        } else if (method.isAnnotationPresent(PermitAll.class)) {
            builder.permitAll();
        } else if (method.isAnnotationPresent(RolesAllowed.class)) {
            RolesAllowed rolesAnnotation = method.getAnnotation(RolesAllowed.class);
            builder.requireRoles(rolesAnnotation.value());
        } else {
            // Check class-level annotations
            Class<?> declaringClass = method.getDeclaringClass();
            if (declaringClass.isAnnotationPresent(DenyAll.class)) {
                builder.denyAll();
            } else if (declaringClass.isAnnotationPresent(PermitAll.class)) {
                builder.permitAll();
            } else if (declaringClass.isAnnotationPresent(RolesAllowed.class)) {
                RolesAllowed rolesAnnotation = declaringClass.getAnnotation(RolesAllowed.class);
                builder.requireRoles(rolesAnnotation.value());
            }
        }
        
        return builder.build();
    }
    
    private void sendUnauthorized(HttpServerExchange exchange, String message) {
        exchange.setStatusCode(StatusCodes.UNAUTHORIZED);
        exchange.getResponseHeaders().put(Headers.WWW_AUTHENTICATE, "Bearer");
        exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "application/json");
        
        String response = String.format("{\"error\":\"unauthorized\",\"message\":\"%s\"}", message);
        exchange.getResponseSender().send(response);
    }
    
    private void sendForbidden(HttpServerExchange exchange, String message) {
        exchange.setStatusCode(StatusCodes.FORBIDDEN);
        exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "application/json");
        
        String response = String.format("{\"error\":\"forbidden\",\"message\":\"%s\"}", message);
        exchange.getResponseSender().send(response);
    }
    
    private void sendInternalError(HttpServerExchange exchange, String message) {
        exchange.setStatusCode(StatusCodes.INTERNAL_SERVER_ERROR);
        exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "application/json");
        
        String response = String.format("{\"error\":\"internal_error\",\"message\":\"%s\"}", message);
        exchange.getResponseSender().send(response);
    }
    
    /**
     * Internal class representing security requirements for a method.
     */
    private static class SecurityRequirement {
        private final boolean denyAll;
        private final boolean permitAll;
        private final String[] allowedRoles;
        
        private SecurityRequirement(Builder builder) {
            this.denyAll = builder.denyAll;
            this.permitAll = builder.permitAll;
            this.allowedRoles = builder.allowedRoles;
        }
        
        public boolean isDenyAll() {
            return denyAll;
        }
        
        public boolean isPermitAll() {
            return permitAll;
        }
        
        public boolean requiresAuthentication() {
            return !permitAll && !denyAll && (allowedRoles.length > 0);
        }
        
        public boolean hasRoleRequirements() {
            return allowedRoles.length > 0;
        }
        
        public String[] getAllowedRoles() {
            return allowedRoles;
        }
        
        public static Builder builder() {
            return new Builder();
        }
        
        static class Builder {
            private boolean denyAll = false;
            private boolean permitAll = false;
            private String[] allowedRoles = new String[0];
            
            public Builder denyAll() {
                this.denyAll = true;
                this.permitAll = false;
                this.allowedRoles = new String[0];
                return this;
            }
            
            public Builder permitAll() {
                this.permitAll = true;
                this.denyAll = false;
                this.allowedRoles = new String[0];
                return this;
            }
            
            public Builder requireRoles(String... roles) {
                this.allowedRoles = roles != null ? roles : new String[0];
                this.denyAll = false;
                this.permitAll = false;
                return this;
            }
            
            public SecurityRequirement build() {
                return new SecurityRequirement(this);
            }
        }
    }
}
