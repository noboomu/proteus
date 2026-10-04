package io.sinistral.proteus.security;

import java.security.Principal;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Default implementation of SecurityContext.
 *
 * Represents validated authentication state associated with an HTTP request.
 * The context is stored on that request's Undertow exchange and is injected explicitly
 * into a controller when the controller declares a {@link SecurityContext} parameter.
 * It is not held in a thread-local variable.
 *
 * @since 0.9.5
 */
public class DefaultSecurityContext implements SecurityContext {

    /** The principal. */
    private final Principal principal;
    /** The user id. */
    private final String userId;
    /** The username. */
    private final String username;
    /** The roles. */
    private final List<String> roles;
    /** The permissions. */
    private final List<String> permissions;
    /** The authentication scheme. */
    private final String authenticationScheme;
    /** The raw token. */
    private final String rawToken;
    /** The attributes. */
    private final Map<String, Object> attributes;

    /**
     * Creates an immutable snapshot of the supplied builder's identity and authorization
     * values. Attributes are copied into this context and remain mutable through
     * {@link #setAttribute(String, Object)}.
     *
     * @param builder accumulated context values
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

    /**
     * Returns the principal.
     *
     * @return the principal, or null when unset
     */
    @Override
    public Optional<Principal> getPrincipal() {
        return Optional.ofNullable(principal);
    }

    /**
     * Returns the user id.
     *
     * @return the user id, or null when unset
     */
    @Override
    public Optional<String> getUserId() {
        return Optional.ofNullable(userId);
    }

    /**
     * Returns the username.
     *
     * @return the username, or null when unset
     */
    @Override
    public Optional<String> getUsername() {
        return Optional.ofNullable(username);
    }

    /**
     * Returns the roles.
     *
     * @return the roles, or null when unset
     */
    @Override
    public List<String> getRoles() {
        return roles;
    }

    /**
     * Returns the permissions.
     *
     * @return the permissions, or null when unset
     */
    @Override
    public List<String> getPermissions() {
        return permissions;
    }

    /**
     * Returns the authenticated.
     *
     * @return the authenticated, or null when unset
     */
    @Override
    public boolean isAuthenticated() {
        return principal != null || userId != null || rawToken != null;
    }

    /**
     * Returns whether the principal holds the role.
     *
     * @param role the role to check
     * @return true when the role is held
     */
    @Override
    public boolean hasRole(String role) {
        return roles.contains(role);
    }

    /**
     * Returns whether the principal holds any of the roles.
     *
     * @param rolesToCheck the roles to check
     * @return true when at least one role is held
     */
    @Override
    public boolean hasAnyRole(String... rolesToCheck) {
        for (String role : rolesToCheck) {
            if (hasRole(role)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns whether the principal holds every role.
     *
     * @param rolesToCheck the roles to check
     * @return true when all roles are held
     */
    @Override
    public boolean hasAllRoles(String... rolesToCheck) {
        for (String role : rolesToCheck) {
            if (!hasRole(role)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns whether the principal holds the permission.
     *
     * @param permission the permission to check
     * @return true when the permission is held
     */
    @Override
    public boolean hasPermission(String permission) {
        return permissions.contains(permission);
    }

    /**
     * Returns the attribute.
     *
     * @return the attribute, or null when unset
    * @param name the value
     */
    @Override
    public Optional<Object> getAttribute(String name) {
        return Optional.ofNullable(attributes.get(name));
    }

    /**
     * Sets the attribute.
     *
     * @param name the attribute
     * @param value the attribute
     */
    @Override
    public void setAttribute(String name, Object value) {
        attributes.put(name, value);
    }

    /**
     * Returns the authentication scheme.
     *
     * @return the authentication scheme, or null when unset
     */
    @Override
    public Optional<String> getAuthenticationScheme() {
        return Optional.ofNullable(authenticationScheme);
    }

    /**
     * Returns the raw token.
     *
     * @return the raw token, or null when unset
     */
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

    /** Builder for creating {@link DefaultSecurityContext} instances. */
    public static class Builder {
        /** The principal. */
        private Principal principal;
        /** The user id. */
        private String userId;
        /** The username. */
        private String username;
        /** The roles. */
        private List<String> roles = List.of();
        /** The permissions. */
        private List<String> permissions = List.of();
        /** The authentication scheme. */
        private String authenticationScheme;
        /** The raw token. */
        private String rawToken;
        /** The attributes. */
        private Map<String, Object> attributes = new HashMap<>();

        /** Creates an empty security-context builder. */
        public Builder() {}

        /**
         * Sets the authenticated principal.
         *
         * @param principal principal identity
         * @return this builder
         */
        public Builder principal(Principal principal) {
            this.principal = principal;
            return this;
        }

        /**
         * Sets the stable user identifier.
         *
         * @param userId user identifier
         * @return this builder
         */
        public Builder userId(String userId) {
            this.userId = userId;
            return this;
        }

        /**
         * Sets the display or login name.
         *
         * @param username user name
         * @return this builder
         */
        public Builder username(String username) {
            this.username = username;
            return this;
        }

        /**
         * Replaces the granted role list with an immutable copy.
         *
         * @param roles granted roles; {@code null} becomes an empty list
         * @return this builder
         */
        public Builder roles(List<String> roles) {
            this.roles = roles != null ? List.copyOf(roles) : List.of();
            return this;
        }

        /**
         * Replaces the granted permission list with an immutable copy.
         *
         * @param permissions granted permissions; {@code null} becomes an empty list
         * @return this builder
         */
        public Builder permissions(List<String> permissions) {
            this.permissions = permissions != null ? List.copyOf(permissions) : List.of();
            return this;
        }

        /**
         * Sets the authentication scheme label, such as {@code Bearer}.
         *
         * @param authenticationScheme authentication scheme
         * @return this builder
         */
        public Builder authenticationScheme(String authenticationScheme) {
            this.authenticationScheme = authenticationScheme;
            return this;
        }

        /**
         * Sets the raw credential that produced this context.
         *
         * @param rawToken raw credential
         * @return this builder
         */
        public Builder rawToken(String rawToken) {
            this.rawToken = rawToken;
            return this;
        }

        /**
         * Adds or replaces one custom attribute.
         *
         * @param name attribute name
         * @param value attribute value
         * @return this builder
         */
        public Builder attribute(String name, Object value) {
            this.attributes.put(name, value);
            return this;
        }

        /**
         * Merges custom attributes into this builder.
         *
         * @param attributes attributes to merge; {@code null} has no effect
         * @return this builder
         */
        public Builder attributes(Map<String, Object> attributes) {
            if (attributes != null) {
                this.attributes.putAll(attributes);
            }
            return this;
        }

        /**
         * Builds a context from the current values.
         *
         * @return new security context
         */
        public DefaultSecurityContext build() {
            return new DefaultSecurityContext(this);
        }
    }

    /**
     * Returns the to string.
     *
     * @return the to string, or null when unset
     */
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
