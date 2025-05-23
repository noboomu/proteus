/**
 * 
 */
package io.sinistral.proteus.openapi.test.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import com.google.common.io.Files;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.sinistral.proteus.annotations.Blocking;
import io.sinistral.proteus.annotations.Chain;
import io.sinistral.proteus.annotations.Debug;
import io.sinistral.proteus.openapi.test.models.Pojo;
import io.sinistral.proteus.openapi.wrappers.BearerTokenWrapper;
import io.sinistral.proteus.server.ServerRequest;
import io.sinistral.proteus.server.ServerResponse;
import io.sinistral.proteus.wrappers.JsonViewWrapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.Map;


/**
 * @author jbauer
 *
 */

@Tags({@Tag(name = "tests")})
@Path("/tests")
@Produces((MediaType.APPLICATION_JSON))
@Consumes((MediaType.APPLICATION_JSON))
@Singleton
public class OpenAPITests
{


    @GET
    @Path("/bearer")
    @Operation(description = "Test")
    @Blocking
    @Chain({BearerTokenWrapper.class})
    public ServerResponse<Map<String,Object>> test(ServerRequest request) throws Exception
    {
        String token = request.getAttachment(BearerTokenWrapper.BEARER_TOKEN_KEY);

        Object result = request.getAttachment(BearerTokenWrapper.BEARER_VALIDATION_RESULT_KEY);

        return ServerResponse.response(Map.of("token",token, "result", result)).applicationJson().ok();
    }


}
