package io.sinistral.proteus.security;

import io.sinistral.proteus.annotations.security.Claim;
import io.sinistral.proteus.annotations.security.DenyAll;
import io.sinistral.proteus.annotations.security.PermitAll;
import io.sinistral.proteus.annotations.security.RolesAllowed;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Resolves the effective Proteus security policy for an endpoint method.
 * Runtime handlers and OpenAPI generation both use this resolver so annotation precedence
 * cannot drift between authorization and published documentation.
 *
 * @since 0.9.5
 */
public final class SecurityPolicy {

    private SecurityPolicy() {}

    /** Supported endpoint access modes. */
    public enum Access {
        /** No Proteus authentication or authorization policy applies. */
        NONE,
        /** Anonymous access is permitted and a valid optional token may attach context. */
        PERMIT_ALL,
        /** Every request is denied. */
        DENY_ALL,
        /** A validated identity is required. */
        AUTHENTICATED,
        /** A validated identity with any declared role is required. */
        ROLES
    }

    /**
     * Immutable effective policy and its normalized role set.
     *
     * @param access resolved access mode
     * @param roles normalized roles for {@link Access#ROLES}; empty otherwise
     */
    public record Requirement(Access access, List<String> roles) {
        /** Copies the role list so the effective policy remains immutable. */
        public Requirement {
            roles = List.copyOf(roles);
        }

        /**
         * Reports whether this policy requires a validated security context.
         *
         * @return {@code true} for authenticated or role-based access
         */
        public boolean requiresAuthentication() {
            return access == Access.AUTHENTICATED || access == Access.ROLES;
        }
    }

    /**
     * Resolves the effective policy in fail-closed order: method declaration, class declaration,
     * authenticated claim fallback, then no policy. An explicit class or method policy always
     * beats the claim fallback. Empty {@code @RolesAllowed} means deny all.
     *
     * @param method endpoint controller method
     * @return immutable effective policy
     * @throws IllegalArgumentException if an element has conflicting policies or invalid roles
     */
    public static Requirement resolve(Method method) {
        Requirement methodRequirement = explicitRequirement(method);
        if (methodRequirement != null) {
            return methodRequirement;
        }

        Requirement classRequirement = explicitRequirement(method.getDeclaringClass());
        if (classRequirement != null) {
            return classRequirement;
        }

        boolean injectsClaim = Arrays.stream(method.getParameters())
            .anyMatch(parameter -> parameter.isAnnotationPresent(Claim.class));
        return injectsClaim
            ? new Requirement(Access.AUTHENTICATED, List.of())
            : new Requirement(Access.NONE, List.of());
    }

    private static Requirement explicitRequirement(
        java.lang.reflect.AnnotatedElement element
    ) {
        int declarationCount = 0;
        declarationCount += element.isAnnotationPresent(DenyAll.class) ? 1 : 0;
        declarationCount += element.isAnnotationPresent(PermitAll.class) ? 1 : 0;
        declarationCount += element.isAnnotationPresent(RolesAllowed.class) ? 1 : 0;
        if (declarationCount > 1) {
            throw new IllegalArgumentException(
                "Conflicting security annotations on " + element
            );
        }
        if (element.isAnnotationPresent(DenyAll.class)) {
            return new Requirement(Access.DENY_ALL, List.of());
        }
        if (element.isAnnotationPresent(PermitAll.class)) {
            return new Requirement(Access.PERMIT_ALL, List.of());
        }
        RolesAllowed rolesAllowed = element.getAnnotation(RolesAllowed.class);
        if (rolesAllowed == null) {
            return null;
        }
        List<String> roles = validateRoles(
            rolesAllowed.value(),
            "@RolesAllowed"
        );
        return roles.isEmpty()
            ? new Requirement(Access.DENY_ALL, List.of())
            : new Requirement(Access.ROLES, roles);
    }

    /**
     * Normalizes a declared role list and rejects blank or duplicate names.
     *
     * @param declaredRoles roles from an annotation
     * @param declaration declaration name for an error message
     * @return immutable normalized roles; an empty declaration remains empty
     */
    public static List<String> validateRoles(
        String[] declaredRoles,
        String declaration
    ) {
        if (declaredRoles == null || declaredRoles.length == 0) {
            return List.of();
        }

        Set<String> roles = new LinkedHashSet<>();
        for (String declaredRole : declaredRoles) {
            if (declaredRole == null || declaredRole.isBlank()) {
                throw new IllegalArgumentException(
                    declaration + " role names must not be blank"
                );
            }
            String role = declaredRole.trim();
            if (!roles.add(role)) {
                throw new IllegalArgumentException(
                    declaration + " contains duplicate role: " + role
                );
            }
        }
        return List.copyOf(roles);
    }
}
