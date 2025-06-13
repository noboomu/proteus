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
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.Map;
import java.util.UUID;


/**
 * @author jbauer
 */

@Tags({@Tag(name = "tests")})
@Path("/tests")
@Produces((MediaType.APPLICATION_JSON))
@Consumes((MediaType.APPLICATION_JSON))
@ApiResponses(
        {@ApiResponse(
                responseCode = "404",
                description = "Unable to find resource",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = OpenAPITests.APIException.class)
                )
        )}
)
@Singleton
public class OpenAPITests
{

    public static class APIException extends Exception
    {
        private static final long serialVersionUID = 1L;

        int statusCode = 500;

        public APIException(String message)
        {
            super(message);
            statusCode = 500;
        }

        public APIException(String message, Throwable cause)
        {
            super(message, cause);
            statusCode = 500;
        }

        public int getStatusCode()
        {
            return statusCode;
        }

        public void setStatusCode(int statusCode)
        {
            this.statusCode = statusCode;
        }
    }

    public static class GenericBean<T>
    {
        private T value;

        public GenericBean(T value)
        {
            this.value = value;
        }

        public T getValue()
        {
            return value;
        }

        public void setValue(T value)
        {
            this.value = value;
        }
    }

    Map<String,GenericBean<Integer>> map = ImmutableMap.of("test", new GenericBean<>(1));


    @GET
    @Path("/bearer")
    @Operation(description = "Test")
    @Blocking
    @Chain({BearerTokenWrapper.class})
    public ServerResponse<Map<String, Object>> test(ServerRequest request, @QueryParam("terst") String terst) throws Exception
    {
        String token = request.getAttachment(BearerTokenWrapper.BEARER_TOKEN_KEY);

        Object result = request.getAttachment(BearerTokenWrapper.BEARER_VALIDATION_RESULT_KEY);

        return ServerResponse.response(Map.of("token", token, "terst", terst, "result", result)).applicationJson().ok();
    }


    @GET
    @Path("/api-responses")
//    @Operation(
//            description = "Return a typed generic bean",
//            responses = { @ApiResponse(
//                    responseCode = "500",
//                    description = "Internal server error",
//                    useReturnTypeSchema = true
//            )}
//    )
    @Blocking
    public ServerResponse<GenericBean<Integer>> apiResponses(ServerRequest request, @QueryParam("number") Integer number) throws Exception
    {

        return ServerResponse.response(new GenericBean<>(number)).applicationJson().ok();
    }

    @GET
    @Path("/api-responses-2")
    @Operation(
            description = "Return a typed generic bean"

    )
    @Blocking
    public ServerResponse<GenericBean<Integer>> apiResponses2(ServerRequest request, @BeanParam Map<String, Integer> ids ) throws Exception
    {

        return ServerResponse.response(new GenericBean<>(ids.size())).applicationJson().ok();
    }



     @POST
    @Path("/api-responses-complex")
    @Operation(
            description = "Return a typed generic bean",
            responses = { @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Exception.class)
                    )
            )}
    )
    @Blocking
    public ServerResponse<Map<String,GenericBean<Integer>>> apiResponsesComplex(ServerRequest request, @BeanParam GenericBean<String> bean) throws Exception
    {

        return ServerResponse.response(map).applicationJson().ok();
    }


}
