package io.sinistral.proteus.openapi.test.server;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import io.sinistral.proteus.annotations.security.Claim;
import io.sinistral.proteus.annotations.security.DenyAll;
import io.sinistral.proteus.annotations.security.PermitAll;
import io.sinistral.proteus.annotations.security.RolesAllowed;
import io.sinistral.proteus.openapi.models.OpenAPI;
import io.sinistral.proteus.openapi.models.Components;
import io.sinistral.proteus.openapi.models.OpenAPI.SpecVersion;
import io.sinistral.proteus.openapi.models.Operation;
import io.sinistral.proteus.openapi.models.PathItem;
import io.sinistral.proteus.openapi.models.Paths;
import io.sinistral.proteus.openapi.models.security.SecurityScheme;
import io.sinistral.proteus.openapi.security.OpenApiSecuritySchemeService;
import io.sinistral.proteus.openapi.security.SecurityAnnotationExtension;
import io.sinistral.proteus.openapi.jaxrs2.ResolvedParameter;
import io.sinistral.proteus.openapi.jaxrs2.ServerParameterExtension;
import io.sinistral.proteus.security.jwt.DefaultJwtConfiguration;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import org.junit.jupiter.api.Test;

public class OpenApiSecurityTest {

    @Test
    public void rolesAllowedAddsBearerRequirementAndRoles() throws Exception {
        Operation operation = decorate("classSecured");

        assertNotNull(operation.getSecurity());
        assertTrue(operation.getSecurity().getFirst().containsKey("bearerAuth"));
        assertArrayEquals(
            new String[] { "user" },
            (String[]) operation.getExtensions().get("x-required-roles")
        );
    }

    @Test
    public void requiredClaimAddsBearerRequirement() throws Exception {
        Method method = ClaimController.class.getMethod("claimOnly", String.class);
        Operation operation = new Operation();
        new SecurityAnnotationExtension().decorateOperation(
            operation,
            method,
            Collections.emptyIterator()
        );

        assertNotNull(operation.getSecurity());
        assertTrue(operation.getSecurity().getFirst().containsKey("bearerAuth"));
    }

    @Test
    public void classSecurityPrecedesClaimFallback() throws Exception {
        Method method = SecuredController.class.getMethod(
            "classSecuredClaim",
            String.class
        );
        Operation operation = new Operation();
        new SecurityAnnotationExtension().decorateOperation(
            operation,
            method,
            Collections.emptyIterator()
        );

        assertArrayEquals(
            new String[] { "user" },
            (String[]) operation.getExtensions().get("x-required-roles")
        );
    }

    @Test
    public void classDenyAllPrecedesClaimFallback() throws Exception {
        Operation operation = decorate(
            DeniedController.class,
            "deniedClaim",
            String.class
        );

        assertEquals(true, operation.getExtensions().get("x-security-forbidden"));
        assertNull(operation.getSecurity());
    }

    @Test
    public void injectedClaimIsNotDocumentedAsRequestInput() throws Exception {
        java.lang.reflect.Parameter claimParameter = ClaimController.class
            .getMethod("claimOnly", String.class)
            .getParameters()[0];
        ResolvedParameter resolved = new ServerParameterExtension().extractParameters(
            Arrays.asList(claimParameter.getAnnotations()),
            claimParameter.getParameterizedType(),
            Set.of(),
            new Components(),
            null,
            null,
            true,
            null,
            Collections.emptyIterator()
        );

        assertTrue(resolved.parameters.isEmpty());
        assertTrue(resolved.formParameters.isEmpty());
        assertNull(resolved.requestBody);
    }

    @Test
    public void emptyRolesAllowedIsDocumentedAsForbidden() throws Exception {
        Operation operation = decorate("emptyRoles");

        assertEquals(true, operation.getExtensions().get("x-security-forbidden"));
        assertNull(operation.getSecurity());
    }

    @Test
    public void methodSecurityOverridesClassSecurity() throws Exception {
        Operation operation = decorate("adminOnly");

        assertArrayEquals(
            new String[] { "admin" },
            (String[]) operation.getExtensions().get("x-required-roles")
        );
    }

    @Test
    public void methodPermitAllOverridesClassRoles() throws Exception {
        Operation operation = decorate("publicEndpoint");

        assertEquals(true, operation.getExtensions().get("x-security-public"));
        assertNull(operation.getSecurity());
    }

    @Test
    public void methodDenyAllOverridesClassPermitAll() throws Exception {
        Operation operation = decorate(PublicController.class, "deniedEndpoint");

        assertEquals(true, operation.getExtensions().get("x-security-forbidden"));
        assertNull(operation.getSecurity());
    }

    @Test
    public void permitAllAndDenyAllAreDocumented() throws Exception {
        Operation publicOperation = decorate("publicEndpoint");
        Operation deniedOperation = decorate("deniedEndpoint");

        assertEquals(
            true,
            publicOperation.getExtensions().get("x-security-public")
        );
        assertNull(publicOperation.getSecurity());
        assertEquals(
            true,
            deniedOperation.getExtensions().get("x-security-forbidden")
        );
        assertNull(deniedOperation.getSecurity());
    }

    @Test
    public void configuredJwtAddsBearerScheme() {
        Config config = ConfigFactory.parseString(
            "proteus.security.jwt.hmac.secrets=[\"proteus-test-hmac-secret-32-bytes!\"]"
        ).withFallback(ConfigFactory.load()).resolve();
        OpenAPI openApi = new OpenAPI(SpecVersion.V31);

        new OpenApiSecuritySchemeService(
            new DefaultJwtConfiguration(config)
        ).configureSecuritySchemes(openApi);

        SecurityScheme scheme = openApi
            .getComponents()
            .getSecuritySchemes()
            .get(OpenApiSecuritySchemeService.JWT_BEARER_SCHEME);
        assertNotNull(scheme);
        assertEquals(SecurityScheme.Type.HTTP, scheme.getType());
        assertEquals("bearer", scheme.getScheme());
        assertEquals("JWT", scheme.getBearerFormat());
    }

    @Test
    public void absentJwtConfigurationPreservesExistingSchemes() {
        Config config = ConfigFactory.parseString(
            "proteus.security.jwt.hmac.secrets=[]"
        ).withFallback(ConfigFactory.load()).resolve();
        OpenAPI openApi = new OpenAPI(SpecVersion.V31);

        new OpenApiSecuritySchemeService(
            new DefaultJwtConfiguration(config)
        ).configureSecuritySchemes(openApi);

        assertTrue(
            openApi.getComponents().getSecuritySchemes() == null ||
            !openApi.getComponents()
                .getSecuritySchemes()
                .containsKey(OpenApiSecuritySchemeService.JWT_BEARER_SCHEME)
        );
    }

    @Test
    public void securedOperationDefinesBearerSchemeWithoutVerificationKeys()
        throws Exception {
        Operation operation = new Operation();
        new SecurityAnnotationExtension().decorateOperation(
            operation,
            ClaimController.class.getMethod("claimOnly", String.class),
            Collections.emptyIterator()
        );
        Paths paths = new Paths();
        paths.addPathItem("/claim", new PathItem().get(operation));
        OpenAPI openApi = new OpenAPI(SpecVersion.V31).paths(paths);
        Config config = ConfigFactory.parseString("""
            proteus.security.jwt {
              hmac.secrets = []
              rsa.publicKeys = []
              ec.publicKeys = []
              signatureVerificationRequired = true
            }
            """);

        new OpenApiSecuritySchemeService(
            new DefaultJwtConfiguration(config)
        ).configureSecuritySchemes(openApi);

        assertNotNull(
            openApi.getComponents().getSecuritySchemes().get("bearerAuth")
        );
    }

    private static Operation decorate(String methodName) throws Exception {
        return decorate(SecuredController.class, methodName);
    }

    private static Operation decorate(
        Class<?> type,
        String methodName,
        Class<?>... parameterTypes
    ) throws Exception {
        Method method = type.getMethod(methodName, parameterTypes);
        Operation operation = new Operation();
        new SecurityAnnotationExtension().decorateOperation(
            operation,
            method,
            Collections.emptyIterator()
        );
        return operation;
    }

    @RolesAllowed("user")
    public static class SecuredController {

        public void classSecured() {}

        public void classSecuredClaim(@Claim("sub") String subject) {}

        @RolesAllowed("admin")
        public void adminOnly() {}

        @PermitAll
        public void publicEndpoint() {}

        @DenyAll
        public void deniedEndpoint() {}

        @RolesAllowed({})
        public void emptyRoles() {}
    }

    public static class ClaimController {
        public void claimOnly(@Claim("sub") String subject) {}
    }

    @DenyAll
    public static class DeniedController {
        public void deniedClaim(@Claim("sub") String subject) {}
    }

    @PermitAll
    public static class PublicController {
        @DenyAll
        public void deniedEndpoint() {}
    }
}
