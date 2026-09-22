package io.sinistral.proteus.openapi.test.controllers;

import com.google.inject.Singleton;
import io.sinistral.proteus.annotations.security.Claim;
import io.sinistral.proteus.annotations.security.PermitAll;
import io.sinistral.proteus.annotations.security.RolesAllowed;
import io.sinistral.proteus.server.ServerRequest;
import io.sinistral.proteus.server.ServerResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.Map;

/**
 * Endpoints whose only variable is the security policy, used to verify served
 * security metadata and live enforcement against one running server.
 */
@Tags({@Tag(name = "security-tests")})
@Path("/security-tests")
@Produces(MediaType.APPLICATION_JSON)
@Singleton
public class SecurityMetadataTests {

    @GET
    @Path("/public")
    @PermitAll
    @Operation(description = "Public operation")
    public ServerResponse<Map<String, String>> publicOperation(ServerRequest request) {
        return ServerResponse.response(Map.of("result", "public")).applicationJson().ok();
    }

    @GET
    @Path("/admin")
    @RolesAllowed("admin")
    @Operation(description = "Admin operation")
    public ServerResponse<Map<String, String>> adminOperation(ServerRequest request) {
        return ServerResponse.response(Map.of("result", "admin")).applicationJson().ok();
    }

    /** Claim-secured: requires authentication but no specific role. */
    @GET
    @Path("/authenticated")
    @Operation(description = "Authenticated operation")
    public ServerResponse<Map<String, String>> authenticatedOperation(
        ServerRequest request,
        @Claim("sub") String subject
    ) {
        return ServerResponse.response(Map.of("result", "authenticated", "subject", subject))
            .applicationJson().ok();
    }

    @GET
    @Path("/denied")
    @io.sinistral.proteus.annotations.security.DenyAll
    @Operation(description = "Denied operation")
    public ServerResponse<Map<String, String>> deniedOperation(ServerRequest request) {
        return ServerResponse.response(Map.of("result", "denied")).applicationJson().ok();
    }
}
