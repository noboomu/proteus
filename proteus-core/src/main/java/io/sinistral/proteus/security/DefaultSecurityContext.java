package io.sinistral.proteus.security;

import java.security.Principal;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Default implementation of SecurityContext.
 * This implementation stores security information in memory for the duration of a request.
 * 
 * @since 1.0
 */
public class DefaultSecurityContext implements SecurityContext {
    
    private final Principal principal;
    private final String userId;
    private final String username;
    private final List<String> roles;
    private final List<String> permissions;
    private final String authenticationScheme;
    private final String rawToken;
    private final Map<String, Object> attributes;
    
    /**
     * Creates a new DefaultSecurityContext.
     */
    public DefaultSecurityContext(Builder builder) {
        this.principal = builder.principal;
        this.userId = builder.userId;
        this.username = builder.username;
        this.roles = Collections.unmodifiableList(builder.roles);
        this.permissions = Collections.unmodifiableList(builder.permissions);
        this.authenticationScheme = builder.authenticationScheme;
        this.rawToken = builder.rawToken;
        this.attributes = new HashMap<>(builder.attributes);
    }
    
    @Override
    public Optional<Principal> getPrincipal() {
        return Optional.ofNullable(principal);
    }
    
    @Override
    public Optional<String> getUserId() {
        return Optional.ofNullable(userId);
    }
    
    @Override
    public Optional<String> getUsername() {
        return Optional.ofNullable(username);
    }
    
    @Override
    public List<String> getRoles() {
        return roles;
    }
    
    @Override
    public List<String> getPermissions() {
        return permissions;
    }
    
    @Override
    public boolean isAuthenticated() {
        return principal != null || userId != null;
    }
    
    @Override
    public boolean hasRole(String role) {
        return roles.contains(role);
    }
    
    @Override
    public boolean hasAnyRole(String... rolesToCheck) {
        for (String role : rolesToCheck) {
            if (hasRole(role)) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public boolean hasAllRoles(String... rolesToCheck) {
        for (String role : rolesToCheck) {
            if (!hasRole(role)) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public boolean hasPermission(String permission) {
        return permissions.contains(permission);
    }
    
    @Override
    public Optional<Object> getAttribute(String name) {
        return Optional.ofNullable(attributes.get(name));
    }
    
    @Override
    public void setAttribute(String name, Object value) {
        attributes.put(name, value);
    }
    
    @Override
    public Optional<String> getAuthenticationScheme() {
        return Optional.ofNullable(authenticationScheme);
    }
    
    @Override
    public Optional<String> getRawToken() {
        return Optional.ofNullable(rawToken);
    }
    
    /**
     * Creates a new Builder for constructing SecurityContext instances.
     * 
     * @return A new Builder
     */
    public static Builder builder() {
        return new Builder();
    }
    
    /**
     * Builder class for creating DefaultSecurityContext instances.
     */
    public static class Builder {
        private Principal principal;
        private String userId;
        private String username;
        private List<String> roles = List.of();
        private List<String> permissions = List.of();
        private String authenticationScheme;
        private String rawToken;
        private Map<String, Object> attributes = new HashMap<>();
        
        public Builder principal(Principal principal) {
            this.principal = principal;
            return this;
        }
        
        public Builder userId(String userId) {
            this.userId = userId;
            return this;
        }
        
        public Builder username(String username) {
            this.username = username;
            return this;
        }
        
        public Builder roles(List<String> roles) {
            this.roles = roles != null ? List.copyOf(roles) : List.of();
            return this;
        }
        
        public Builder permissions(List<String> permissions) {
            this.permissions = permissions != null ? List.copyOf(permissions) : List.of();
            return this;
        }
        
        public Builder authenticationScheme(String authenticationScheme) {
            this.authenticationScheme = authenticationScheme;
            return this;
        }
        
        public Builder rawToken(String rawToken) {
            this.rawToken = rawToken;
            return this;
        }
        
        public Builder attribute(String name, Object value) {
            this.attributes.put(name, value);
            return this;
        }
        
        public Builder attributes(Map<String, Object> attributes) {
            if (attributes != null) {
                this.attributes.putAll(attributes);
            }
            return this;
        }
        
        public DefaultSecurityContext build() {
            return new DefaultSecurityContext(this);
        }
    }
    
    @Override
    public String toString() {
        return "SecurityContext{" +
                "userId=" + userId +
                ", username=" + username +
                ", roles=" + roles +
                ", authenticated=" + isAuthenticated() +
                ", scheme=" + authenticationScheme +
                '}';
    }
}
