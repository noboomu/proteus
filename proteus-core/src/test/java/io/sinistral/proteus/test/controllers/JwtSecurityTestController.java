package io.sinistral.proteus.test.controllers;

import io.sinistral.proteus.annotations.security.RolesAllowed;
import io.sinistral.proteus.annotations.security.PermitAll;
import io.sinistral.proteus.annotations.security.Claim;
import io.sinistral.proteus.server.ServerResponse;
import io.sinistral.proteus.security.SecurityContext;
import io.sinistral.proteus.security.handlers.SecurityContextAttachment;
import io.undertow.server.HttpServerExchange;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import java.util.List;
import java.util.Optional;

/**
 * Test controller demonstrating JWT security features.
 * This controller shows various security annotations and claim injection.
 */
@Singleton
@Path("/api/secure")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class JwtSecurityTestController {
    
    /**
     * Public endpoint accessible to all users.
     */
    @GET
    @Path("/public")
    @PermitAll
    public ServerResponse<?> getPublicData() {
        return ServerResponse.response()
                .textPlain()
                .entity("This is public data accessible to everyone");
    }
    
    /**
     * Protected endpoint requiring authentication.
     */
    @GET
    @Path("/protected")
    @RolesAllowed("user")
    public ServerResponse<?> getProtectedData(@Claim("sub") String userId,
                                         @Claim("roles") List<String> roles,
                                         HttpServerExchange exchange) {
        
        Optional<SecurityContext> securityContext = Optional.ofNullable(exchange.getAttachment(SecurityContextAttachment.KEY));
        
        return ServerResponse.response()
                .applicationJson()
                .entity(String.format(
                    "{\"message\":\"Protected data accessed\",\"userId\":\"%s\",\"roles\":%s,\"authenticated\":%b}",
                    userId, 
                    roles.toString().replace("'", "\""),
                    securityContext.isPresent() && securityContext.get().isAuthenticated()
                ));
    }
    
    /**
     * Admin-only endpoint.
     */
    @GET
    @Path("/admin")
    @RolesAllowed({"admin", "superuser"})
    public ServerResponse<?> getAdminData(@Claim("sub") String userId,
                                     @Claim(value = "email", defaultValue = "unknown") String email,
                                     @Claim("exp") Long expiration) {
        
        return ServerResponse.response()
                .applicationJson()
                .entity(String.format(
                    "{\"message\":\"Admin data accessed\",\"userId\":\"%s\",\"email\":\"%s\",\"expiration\":%d}",
                    userId, email, expiration
                ));
    }
    
    /**
     * Profile endpoint with optional claims.
     */
    @GET
    @Path("/profile")
    @RolesAllowed("user")
    public ServerResponse<?> getUserProfile(@Claim("sub") String userId,
                                       @Claim(value = "name", required = false) String name,
                                       @Claim(value = "email", required = false) String email,
                                       @Claim(value = "roles", required = false) List<String> roles,
                                       HttpServerExchange exchange) {
        
        Optional<SecurityContext> securityContext = Optional.ofNullable(exchange.getAttachment(SecurityContextAttachment.KEY));
        
        StringBuilder profileJson = new StringBuilder();
        profileJson.append("{");
        profileJson.append("\"userId\":\"").append(userId).append("\",");
        profileJson.append("\"name\":\"").append(name != null ? name : "N/A").append("\",");
        profileJson.append("\"email\":\"").append(email != null ? email : "N/A").append("\",");
        profileJson.append("\"roles\":").append(roles != null ? roles.toString().replace("'", "\"") : "[]").append(",");
        profileJson.append("\"authenticated\":").append(securityContext.isPresent() && securityContext.get().isAuthenticated());
        profileJson.append("}");
        
        return ServerResponse.response()
                .applicationJson()
                .entity(profileJson.toString());
    }
    
    /**
     * Multiple roles endpoint.
     */
    @POST
    @Path("/data")
    @RolesAllowed({"user", "admin", "moderator"})
    public ServerResponse<?> postData(@Claim("sub") String userId,
                                 @Claim("roles") List<String> roles,
                                 String requestBody) {
        
        return ServerResponse.response()
                .applicationJson()
                .entity(String.format(
                    "{\"message\":\"Data posted successfully\",\"userId\":\"%s\",\"roles\":%s,\"dataReceived\":\"%s\"}",
                    userId, 
                    roles.toString().replace("'", "\""),
                    requestBody != null ? requestBody.replace("\"", "\\\"") : ""
                ));
    }
    
    /**
     * Endpoint to get current security context information.
     */
    @GET
    @Path("/context")
    @RolesAllowed("user")
    public ServerResponse<?> getSecurityContext(HttpServerExchange exchange) {
        
        Optional<SecurityContext> securityContext = Optional.ofNullable(exchange.getAttachment(SecurityContextAttachment.KEY));
        
        if (securityContext.isEmpty()) {
            return ServerResponse.response()
                    .status(401)
                    .applicationJson()
                    .entity("{\"error\":\"No security context available\"}");
        }
        
        SecurityContext context = securityContext.get();
        
        StringBuilder contextJson = new StringBuilder();
        contextJson.append("{");
        contextJson.append("\"authenticated\":").append(context.isAuthenticated()).append(",");
        contextJson.append("\"userId\":\"").append(context.getUserId().orElse("N/A")).append("\",");
        contextJson.append("\"username\":\"").append(context.getUsername().orElse("N/A")).append("\",");
        contextJson.append("\"roles\":").append(context.getRoles().toString().replace("'", "\"")).append(",");
        contextJson.append("\"permissions\":").append(context.getPermissions().toString().replace("'", "\"")).append(",");
        contextJson.append("\"authenticationScheme\":\"").append(context.getAuthenticationScheme().orElse("N/A")).append("\"");
        contextJson.append("}");
        
        return ServerResponse.response()
                .applicationJson()
                .entity(contextJson.toString());
    }
}
