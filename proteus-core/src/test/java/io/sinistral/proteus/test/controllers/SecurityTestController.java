package io.sinistral.proteus.test.controllers;

import io.sinistral.proteus.annotations.Blocking;
import io.sinistral.proteus.annotations.security.Claim;
import io.sinistral.proteus.annotations.security.RolesAllowed;
import io.sinistral.proteus.annotations.security.PermitAll;
import io.sinistral.proteus.annotations.security.DenyAll;
import io.sinistral.proteus.security.SecurityContext;
import io.sinistral.proteus.security.handlers.SecurityProcessor;
import io.sinistral.proteus.server.ServerResponse;
import io.undertow.server.HttpServerExchange;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import com.google.inject.Singleton;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Test controller for demonstrating JWT security features.
 * This controller showcases various authentication and authorization scenarios.
 */
@Singleton
@Path("/security")
@Produces(MediaType.APPLICATION_JSON)
public class SecurityTestController {

    @GET
    @Path("/public")
    @PermitAll
    public ServerResponse<?> publicEndpoint() {
        return ServerResponse.response(Map.of(
            "message",
            "This is a public endpoint accessible to everyone"
        )).applicationJson();
    }

    @GET
    @Path("/denied")
    @DenyAll
    public ServerResponse<?> deniedEndpoint() {
        return ServerResponse.response(Map.of(
            "message",
            "This endpoint should never be accessible"
        )).applicationJson();
    }

    @GET
    @Path("/admin")
    @RolesAllowed("admin")
    public ServerResponse<?> adminEndpoint(@Claim("sub") String userId,
                                          @Claim("roles") List<String> roles,
                                          HttpServerExchange exchange) {
        Optional<SecurityContext> securityContext = SecurityProcessor.getSecurityContext(exchange);

        return ServerResponse.response(Map.of(
            "message", "Admin endpoint",
            "userId", userId,
            "roles", roles,
            "authenticated", securityContext
                .map(SecurityContext::isAuthenticated)
                .orElse(false)
        )).applicationJson();
    }

    @GET
    @Path("/user")
    @RolesAllowed({"user", "admin"})
    public ServerResponse<?> userEndpoint(@Claim("sub") String userId,
                                         @Claim(value = "email", defaultValue = "unknown") String email,
                                         @Claim("exp") Optional<Long> expiration) {
        return ServerResponse.response(Map.of(
            "message", "User endpoint",
            "userId", userId,
            "email", email,
            "expiration", expiration.orElse(0L)
        )).applicationJson();
    }

    @GET
    @Path("/moderator")
    @RolesAllowed({"moderator", "admin"})
    public ServerResponse<?> moderatorEndpoint(@Claim("sub") String userId,
                                              @Claim("preferred_username") Optional<String> username,
                                              @Claim(value = "permissions", required = false) List<String> permissions) {
        return ServerResponse.response(Map.of(
            "message", "Moderator endpoint",
            "userId", userId,
            "username", username.orElse("unknown"),
            "permissions", permissions
        )).applicationJson();
    }

    @GET
    @Path("/profile")
    public ServerResponse<?> profileEndpoint(HttpServerExchange exchange) {
        Optional<SecurityContext> securityContext = SecurityProcessor.getSecurityContext(exchange);

        if (securityContext.isEmpty()) {
            return ServerResponse.response(Map.of(
                "error",
                "Authentication required"
            )).status(401).applicationJson();
        }

        SecurityContext context = securityContext.get();

        return ServerResponse.response(Map.of(
            "message", "User profile",
            "userId", context.getUserId().orElse("unknown"),
            "username", context.getUsername().orElse("unknown"),
            "roles", context.getRoles(),
            "permissions", context.getPermissions(),
            "authenticated", context.isAuthenticated()
        )).applicationJson();
    }

    @GET
    @Path("/claim-only")
    public ServerResponse<?> claimOnlyEndpoint(@Claim("sub") String userId) {
        return ServerResponse.response(Map.of("userId", userId)).applicationJson();
    }

    @GET
    @Path("/empty-roles")
    @RolesAllowed({})
    public ServerResponse<?> emptyRolesEndpoint() {
        return ServerResponse.response(Map.of("unreachable", true)).applicationJson();
    }

    @GET
    @Path("/blocking-context")
    @Blocking
    @RolesAllowed("admin")
    public ServerResponse<?> blockingContextEndpoint(
        SecurityContext securityContext,
        HttpServerExchange exchange
    ) {
        return ServerResponse.response(Map.of(
            "attached", SecurityProcessor.getSecurityContext(exchange).isPresent(),
            "injected", securityContext != null,
            "sameContext", SecurityProcessor
                .getSecurityContext(exchange)
                .filter(securityContext::equals)
                .isPresent(),
            "virtual", Thread.currentThread().isVirtual()
        )).applicationJson();
    }

    @GET
    @Path("/context-state")
    public ServerResponse<?> contextStateEndpoint(SecurityContext securityContext) {
        return ServerResponse.response(Map.of(
            "injected", securityContext != null
        )).applicationJson();
    }

    @Singleton
    @Path("/security/class-denied")
    @Produces(MediaType.APPLICATION_JSON)
    @DenyAll
    public static class ClassDeniedController {

        @GET
        @Path("/claim")
        public ServerResponse<?> claim(@Claim("sub") String userId) {
            return ServerResponse.response(Map.of("userId", userId)).applicationJson();
        }
    }

    @Singleton
    @Path("/security/class-admin")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("admin")
    public static class ClassAdminController {

        @GET
        @Path("/claim")
        public ServerResponse<?> claim(@Claim("sub") String userId) {
            return ServerResponse.response(Map.of("userId", userId)).applicationJson();
        }

        @GET
        @Path("/public")
        @PermitAll
        public ServerResponse<?> publicOverride() {
            return ServerResponse.response(Map.of("public", true)).applicationJson();
        }
    }

    @Singleton
    @Path("/security/class-public")
    @Produces(MediaType.APPLICATION_JSON)
    @PermitAll
    public static class ClassPublicController {

        @GET
        @Path("/denied")
        @DenyAll
        public ServerResponse<?> deniedOverride() {
            return ServerResponse.response(Map.of("unreachable", true)).applicationJson();
        }
    }
}
