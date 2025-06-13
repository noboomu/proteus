package io.sinistral.proteus.security;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

/**
 * Represents the security context for a request, containing authentication
 * and authorization information derived from JWT tokens or other security mechanisms.
 * 
 * @since 1.0
 */
public interface SecurityContext {
    
    // Thread-local storage for security context
    ThreadLocal<SecurityContext> CURRENT_CONTEXT = new ThreadLocal<>();
    
    /**
     * Gets the current security context for the current thread.
     * 
     * @return The current security context or null if none is set
     */
    static SecurityContext getCurrent() {
        return CURRENT_CONTEXT.get();
    }
    
    /**
     * Sets the security context for the current thread.
     * 
     * @param context The security context to set
     */
    static void setCurrent(SecurityContext context) {
        CURRENT_CONTEXT.set(context);
    }
    
    /**
     * Clears the security context for the current thread.
     */
    static void clear() {
        CURRENT_CONTEXT.remove();
    }
    
    /**
     * Gets the authenticated principal (user) for this request.
     * 
     * @return The principal or empty if not authenticated
     */
    Optional<Principal> getPrincipal();
    
    /**
     * Gets the user ID from the security context.
     * This is typically extracted from the JWT subject or user_id claim.
     * 
     * @return The user ID or empty if not available
     */
    Optional<String> getUserId();
    
    /**
     * Gets the username from the security context.
     * This may be different from the user ID and is typically a human-readable identifier.
     * 
     * @return The username or empty if not available
     */
    Optional<String> getUsername();
    
    /**
     * Gets the roles assigned to the authenticated user.
     * 
     * @return List of roles, empty if no roles or not authenticated
     */
    List<String> getRoles();
    
    /**
     * Gets the permissions assigned to the authenticated user.
     * 
     * @return List of permissions, empty if no permissions or not authenticated
     */
    List<String> getPermissions();
    
    /**
     * Checks if the user is authenticated.
     * 
     * @return true if the user is authenticated
     */
    boolean isAuthenticated();
    
    /**
     * Checks if the user has the specified role.
     * 
     * @param role The role to check
     * @return true if the user has the role
     */
    boolean hasRole(String role);
    
    /**
     * Checks if the user has any of the specified roles.
     * 
     * @param roles The roles to check
     * @return true if the user has at least one of the roles
     */
    boolean hasAnyRole(String... roles);
    
    /**
     * Checks if the user has all of the specified roles.
     * 
     * @param roles The roles to check
     * @return true if the user has all of the roles
     */
    boolean hasAllRoles(String... roles);
    
    /**
     * Checks if the user has the specified permission.
     * 
     * @param permission The permission to check
     * @return true if the user has the permission
     */
    boolean hasPermission(String permission);
    
    /**
     * Gets a custom attribute from the security context.
     * This can be used to store additional security-related information.
     * 
     * @param name The attribute name
     * @return The attribute value or empty if not present
     */
    Optional<Object> getAttribute(String name);
    
    /**
     * Sets a custom attribute in the security context.
     * 
     * @param name The attribute name
     * @param value The attribute value
     */
    void setAttribute(String name, Object value);
    
    /**
     * Gets the authentication scheme used (e.g., "Bearer", "Basic").
     * 
     * @return The authentication scheme or empty if not available
     */
    Optional<String> getAuthenticationScheme();
    
    /**
     * Gets the raw authentication token if available.
     * For JWT tokens, this would be the raw JWT string.
     * 
     * @return The raw token or empty if not available
     */
    Optional<String> getRawToken();
}
