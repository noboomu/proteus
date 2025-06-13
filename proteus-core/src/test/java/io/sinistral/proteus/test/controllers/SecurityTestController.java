package io.sinistral.proteus.test.controllers;

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
    public ServerResponse publicEndpoint() {
        return ServerResponse.response()
                .entity("{\"message\":\"This is a public endpoint accessible to everyone\"}")
                .applicationJson();
    }
    
    @GET
    @Path("/denied")
    @DenyAll
    public ServerResponse deniedEndpoint() {
        return ServerResponse.response()
                .entity("{\"message\":\"This endpoint should never be accessible\"}")
                .applicationJson();
    }
    
    @GET
    @Path("/admin")
    @RolesAllowed("admin")
    public ServerResponse adminEndpoint(@Claim("sub") String userId,
                                          @Claim("roles") List<String> roles,
                                          HttpServerExchange exchange) {
        Optional<SecurityContext> securityContext = SecurityProcessor.getSecurityContext(exchange);
        
        return ServerResponse.response()
                .entity("{\"message\":\"Admin endpoint\",\"userId\":\"" + userId + 
                       "\",\"roles\":" + roles + 
                       "\",\"authenticated\":" + securityContext.map(SecurityContext::isAuthenticated).orElse(false) + "}")
                .applicationJson();
    }
    
    @GET
    @Path("/user")
    @RolesAllowed({"user", "admin"})
    public ServerResponse userEndpoint(@Claim("sub") String userId,
                                         @Claim(value = "email", defaultValue = "unknown") String email,
                                         @Claim("exp") Optional<Long> expiration) {
        return ServerResponse.response()
                .entity("{\"message\":\"User endpoint\",\"userId\":\"" + userId + 
                       "\",\"email\":\"" + email + 
                       "\",\"expiration\":" + expiration.orElse(0L) + "}")
                .applicationJson();
    }
    
    @GET
    @Path("/moderator")
    @RolesAllowed({"moderator", "admin"})
    public ServerResponse moderatorEndpoint(@Claim("sub") String userId,
                                              @Claim("preferred_username") Optional<String> username,
                                              @Claim(value = "permissions", required = false) List<String> permissions) {
        return ServerResponse.response()
                .entity("{\"message\":\"Moderator endpoint\",\"userId\":\"" + userId + 
                       "\",\"username\":\"" + username.orElse("unknown") + 
                       "\",\"permissions\":" + permissions + "}")
                .applicationJson();
    }
    
    @GET
    @Path("/profile")
    public ServerResponse profileEndpoint(HttpServerExchange exchange) {
        Optional<SecurityContext> securityContext = SecurityProcessor.getSecurityContext(exchange);
        
        if (securityContext.isEmpty()) {
            return ServerResponse.response()
                    .status(401)
                    .entity("{\"error\":\"Authentication required\"}")
                    .applicationJson();
        }
        
        SecurityContext context = securityContext.get();
        
        return ServerResponse.response()
                .entity("{\"message\":\"User profile\"," +
                       "\"userId\":\"" + context.getUserId().orElse("unknown") + "\"," +
                       "\"username\":\"" + context.getUsername().orElse("unknown") + "\"," +
                       "\"roles\":" + context.getRoles() + "," +
                       "\"permissions\":" + context.getPermissions() + "," +
                       "\"authenticated\":" + context.isAuthenticated() + "}")
                .applicationJson();
    }
}
