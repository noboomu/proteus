package io.sinistral.proteus.openapi.security;

import io.sinistral.proteus.annotations.security.RolesAllowed;
import io.sinistral.proteus.annotations.security.PermitAll;
import io.sinistral.proteus.annotations.security.DenyAll;
import io.swagger.v3.jaxrs2.ext.AbstractOpenAPIExtension;
import io.swagger.v3.jaxrs2.ext.OpenAPIExtension;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Iterator;

/**
 * OpenAPI extension that automatically generates security requirements based on
 * Proteus security annotations (@RolesAllowed, @PermitAll, @DenyAll).
 * 
 * This extension scans JAX-RS methods for security annotations and automatically
 * adds appropriate security requirements to the OpenAPI operation definition.
 * 
 * @since 1.0
 */
public class SecurityAnnotationExtension extends AbstractOpenAPIExtension {
    
    private static final Logger logger = LoggerFactory.getLogger(SecurityAnnotationExtension.class);
    
    @Override
    public void decorateOperation(Operation operation, Method method, Iterator<OpenAPIExtension> chain) {
        // Process security annotations for this method
        processSecurityAnnotations(operation, method);
        
        // Continue with the chain
        if (chain.hasNext()) {
            chain.next().decorateOperation(operation, method, chain);
        }
    }
    
    /**
     * Processes security annotations on the method and its declaring class.
     * Method-level annotations take precedence over class-level annotations.
     * 
     * @param operation The OpenAPI operation to decorate
     * @param method The JAX-RS method being processed
     */
    private void processSecurityAnnotations(Operation operation, Method method) {
        SecurityAnnotationInfo securityInfo = analyzeSecurityAnnotations(method);
        
        if (securityInfo.isDenyAll()) {
            // For @DenyAll, we might want to exclude this operation entirely
            // or add a special security requirement that indicates it's forbidden
            operation.addExtension("x-security-forbidden", true);
            operation.setDescription(appendSecurityDescription(operation.getDescription(), 
                "⚠️ This endpoint is forbidden and not accessible via API."));
            logger.debug("Added security restriction for denied endpoint: {}", method.getName());
            return;
        }
        
        if (securityInfo.isPermitAll()) {
            // For @PermitAll, we explicitly indicate no authentication required
            operation.addExtension("x-security-public", true);
            operation.setDescription(appendSecurityDescription(operation.getDescription(), 
                "🔓 This endpoint is publicly accessible and requires no authentication."));
            logger.debug("Added public access indication for endpoint: {}", method.getName());
            return;
        }
        
        if (securityInfo.hasRoleRequirements()) {
            // For @RolesAllowed, add JWT Bearer security requirement
            SecurityRequirement jwtRequirement = new SecurityRequirement();
            jwtRequirement.addList(OpenApiSecuritySchemeService.JWT_BEARER_SCHEME);
            operation.addSecurityItem(jwtRequirement);
            
            // Add role information as extension
            operation.addExtension("x-required-roles", securityInfo.getAllowedRoles());
            
            String rolesDescription = String.format(
                "🔐 This endpoint requires authentication and one of the following roles: %s", 
                String.join(", ", securityInfo.getAllowedRoles())
            );
            operation.setDescription(appendSecurityDescription(operation.getDescription(), rolesDescription));
            
            logger.debug("Added JWT security requirement for endpoint: {} with roles: {}", 
                method.getName(), securityInfo.getAllowedRoles());
        }
    }
    
    /**
     * Analyzes security annotations on a method and its declaring class.
     * Method-level annotations take precedence over class-level annotations.
     * 
     * @param method The method to analyze
     * @return Security annotation information
     */
    private SecurityAnnotationInfo analyzeSecurityAnnotations(Method method) {
        SecurityAnnotationInfo.Builder builder = SecurityAnnotationInfo.builder();
        
        // Check method-level annotations first (they take precedence)
        if (method.isAnnotationPresent(DenyAll.class)) {
            builder.denyAll();
        } else if (method.isAnnotationPresent(PermitAll.class)) {
            builder.permitAll();
        } else if (method.isAnnotationPresent(RolesAllowed.class)) {
            RolesAllowed rolesAnnotation = method.getAnnotation(RolesAllowed.class);
            builder.requireRoles(rolesAnnotation.value());
        } else {
            // Check class-level annotations if no method-level annotation
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
    
    /**
     * Appends security information to the operation description.
     * 
     * @param existingDescription The existing description (may be null)
     * @param securityDescription The security description to append
     * @return The combined description
     */
    private String appendSecurityDescription(String existingDescription, String securityDescription) {
        if (existingDescription == null || existingDescription.trim().isEmpty()) {
            return securityDescription;
        }
        
        return existingDescription + "\n\n" + securityDescription;
    }
    
    /**
     * Internal class to hold security annotation information.
     */
    private static class SecurityAnnotationInfo {
        private final boolean denyAll;
        private final boolean permitAll;
        private final String[] allowedRoles;
        
        private SecurityAnnotationInfo(Builder builder) {
            this.denyAll = builder.denyAll;
            this.permitAll = builder.permitAll;
            this.allowedRoles = builder.allowedRoles != null ? builder.allowedRoles : new String[0];
        }
        
        public boolean isDenyAll() {
            return denyAll;
        }
        
        public boolean isPermitAll() {
            return permitAll;
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
            private String[] allowedRoles;
            
            public Builder denyAll() {
                this.denyAll = true;
                this.permitAll = false;
                this.allowedRoles = null;
                return this;
            }
            
            public Builder permitAll() {
                this.permitAll = true;
                this.denyAll = false;
                this.allowedRoles = null;
                return this;
            }
            
            public Builder requireRoles(String... roles) {
                this.allowedRoles = roles;
                this.denyAll = false;
                this.permitAll = false;
                return this;
            }
            
            public SecurityAnnotationInfo build() {
                return new SecurityAnnotationInfo(this);
            }
        }
    }
}
