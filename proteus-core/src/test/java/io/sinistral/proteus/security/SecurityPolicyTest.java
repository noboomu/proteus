package io.sinistral.proteus.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import io.sinistral.proteus.annotations.security.Claim;
import io.sinistral.proteus.annotations.security.DenyAll;
import io.sinistral.proteus.annotations.security.PermitAll;
import io.sinistral.proteus.annotations.security.RolesAllowed;
import java.lang.reflect.Method;
import java.util.List;
import org.junit.jupiter.api.Test;

public class SecurityPolicyTest {

    @Test
    public void classDenyAllPrecedesClaimAuthenticationFallback()
        throws Exception {
        SecurityPolicy.Requirement requirement = resolve(
            DeniedClaimController.class,
            "claim",
            String.class
        );

        assertEquals(SecurityPolicy.Access.DENY_ALL, requirement.access());
    }

    @Test
    public void classRolesPrecedeClaimAuthenticationFallback() throws Exception {
        SecurityPolicy.Requirement requirement = resolve(
            AdminClaimController.class,
            "claim",
            String.class
        );

        assertEquals(SecurityPolicy.Access.ROLES, requirement.access());
        assertEquals(List.of("admin"), requirement.roles());
    }

    @Test
    public void methodPolicyPrecedesClassPolicy() throws Exception {
        SecurityPolicy.Requirement requirement = resolve(
            AdminClaimController.class,
            "publicClaim",
            String.class
        );

        assertEquals(SecurityPolicy.Access.PERMIT_ALL, requirement.access());
    }

    @Test
    public void methodDenyAllPrecedesClassPermitAll() throws Exception {
        SecurityPolicy.Requirement requirement = resolve(
            PublicController.class,
            "denied"
        );

        assertEquals(SecurityPolicy.Access.DENY_ALL, requirement.access());
    }

    @Test
    public void emptyRolesDenyAll() throws Exception {
        assertEquals(
            SecurityPolicy.Access.DENY_ALL,
            resolve(InvalidRolesController.class, "empty").access()
        );
    }

    @Test
    public void blankAndDuplicateRolesAreRejected() {
        assertThrows(
            IllegalArgumentException.class,
            () -> resolve(InvalidRolesController.class, "blank")
        );
        assertThrows(
            IllegalArgumentException.class,
            () -> resolve(InvalidRolesController.class, "duplicate")
        );
    }

    @Test
    public void conflictingAnnotationsAreRejected() {
        assertThrows(
            IllegalArgumentException.class,
            () -> resolve(InvalidRolesController.class, "conflicting")
        );
    }

    private static SecurityPolicy.Requirement resolve(
        Class<?> type,
        String name,
        Class<?>... parameters
    ) throws Exception {
        Method method = type.getDeclaredMethod(name, parameters);
        return SecurityPolicy.resolve(method);
    }

    @DenyAll
    static class DeniedClaimController {
        void claim(@Claim("sub") String subject) {}
    }

    @RolesAllowed("admin")
    static class AdminClaimController {
        void claim(@Claim("sub") String subject) {}

        @PermitAll
        void publicClaim(@Claim("sub") String subject) {}
    }

    static class InvalidRolesController {
        @RolesAllowed({})
        void empty() {}

        @RolesAllowed(" ")
        void blank() {}

        @RolesAllowed({ "admin", "admin" })
        void duplicate() {}

        @PermitAll
        @RolesAllowed("admin")
        void conflicting() {}
    }

    @PermitAll
    static class PublicController {
        @DenyAll
        void denied() {}
    }
}
