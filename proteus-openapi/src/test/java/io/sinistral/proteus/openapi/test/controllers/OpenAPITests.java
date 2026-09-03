/**
 *
 */
package io.sinistral.proteus.openapi.test.controllers;

import tools.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import com.google.common.io.Files;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.sinistral.proteus.annotations.Blocking;
import io.sinistral.proteus.annotations.Chain;
import io.sinistral.proteus.annotations.Debug;
import io.sinistral.proteus.openapi.test.models.PagedResponse;
import io.sinistral.proteus.openapi.test.models.Pojo;
import io.sinistral.proteus.openapi.wrappers.BearerTokenWrapper;
import io.sinistral.proteus.server.ServerRequest;
import io.sinistral.proteus.server.ServerResponse;
import io.sinistral.proteus.wrappers.JsonViewWrapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.List;
import java.util.Map;


import io.sinistral.proteus.openapi.test.models.Order;
import io.sinistral.proteus.openapi.test.models.PagedResponse;
import java.util.List;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Encoding;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.enums.Explode;
import io.swagger.v3.oas.annotations.extensions.Extension;
import io.swagger.v3.oas.annotations.extensions.ExtensionProperty;
import io.swagger.v3.oas.annotations.links.Link;
import io.swagger.v3.oas.annotations.links.LinkParameter;
import io.swagger.v3.oas.annotations.servers.Server;
import java.util.concurrent.CompletableFuture;

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
    @Path("/generic")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(description = "Test generics processing")
    @ApiResponse(
            responseCode = "200",
            description = "Success",
            content = @Content(mediaType = "application/json")
    )
    public CompletableFuture<ServerResponse<PagedResponse<Order>>> testGenerics(ServerRequest request)
    {
        Order order = new Order(1L, "Test Order");
        PagedResponse<Order> pagedResponse = new PagedResponse<>(List.of(order), 1);
        return CompletableFuture.completedFuture(ServerResponse.response(pagedResponse).applicationJson().ok());
    }

    @GET
    @Path("/explicit-response")
    @Operation(description = "Explicit response schema")
    @ApiResponse(
        responseCode = "200",
        description = "Explicit order schema",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON,
            schema = @Schema(implementation = Order.class),
            examples = @ExampleObject(
                name = "order",
                summary = "Order example",
                value = "{\"id\":2,\"description\":\"Explicit\"}"
            ),
            extensions = @Extension(
                name = "media-meta",
                properties = @ExtensionProperty(name = "stable", value = "true", parseValue = true)
            )
        ),
        extensions = @Extension(
            name = "response-meta",
            properties = @ExtensionProperty(name = "stable", value = "true", parseValue = true)
        ),
        headers = {
            @Header(
                name = "X-Rate-Limit",
                description = "Remaining requests",
                schema = @Schema(type = "integer", format = "int32")
            ),
            @Header(
                name = "X-Order-Ids",
                description = "Related order identifiers",
                array = @ArraySchema(schema = @Schema(type = "integer", format = "int64")),
                explode = Explode.FALSE,
                examples = @ExampleObject(name = "ids", value = "[1,2]")
            )
        },
        links = @Link(
            name = "orderById",
            operationId = "explicitResponse",
            parameters = @LinkParameter(name = "id", expression = "$response.body#/id"),
            server = @Server(url = "https://api.example.test", description = "Linked API"),
            extensions = @Extension(
                name = "trace",
                properties = @ExtensionProperty(name = "enabled", value = "true", parseValue = true)
            )
        )
    )
    public ServerResponse<Object> explicitResponse(ServerRequest request) {
        return ServerResponse.response((Object) new Order(2L, "Explicit")).applicationJson().ok();
    }

    @GET
    @Path("/explicit-array-response")
    @Operation(description = "Explicit array response schema")
    @ApiResponse(
        responseCode = "200",
        description = "Explicit order array schema",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON,
            array = @ArraySchema(
                schema = @Schema(implementation = Order.class),
                arraySchema = @Schema(description = "Explicit order collection"),
                extensions = @Extension(
                    name = "array-meta",
                    properties = @ExtensionProperty(name = "stable", value = "true", parseValue = true)
                )
            )
        )
    )
    public ServerResponse<List<Order>> explicitArrayResponse(ServerRequest request) {
        return ServerResponse.response(List.of(new Order(3L, "Array"))).applicationJson().ok();
    }

    @GET
    @Path("/return-type-schema")
    @Operation(description = "Generic return type response schema")
    @ApiResponse(
        responseCode = "200",
        description = "Use generic return type",
        useReturnTypeSchema = true,
        content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    @ApiResponse(
        responseCode = "400",
        description = "Invalid request"
    )
    public ServerResponse<PagedResponse<Order>> returnTypeSchema(ServerRequest request) {
        return ServerResponse.response(
            new PagedResponse<>(List.of(new Order(4L, "Return type")), 1)
        ).applicationJson().ok();
    }

    @GET
    @Path("/metadata-return-type-schema")
    @Operation(description = "Metadata combined with generic return type")
    @ApiResponse(
        responseCode = "200",
        description = "Metadata plus return type",
        useReturnTypeSchema = true,
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON,
            schema = @Schema(description = "Metadata-only response schema")
        )
    )
    public ServerResponse<Order> metadataReturnTypeSchema(ServerRequest request) {
        return ServerResponse.response(new Order(5L, "Metadata")).applicationJson().ok();
    }

    @POST
    @Path("/explicit-request")
    @Operation(description = "Explicit request schema")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        required = true,
        description = "Explicit order request",
        extensions = @Extension(
            name = "request-meta",
            properties = @ExtensionProperty(name = "stable", value = "true", parseValue = true)
        ),
        content = {
            @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = Order.class)
            ),
            @Content(
                mediaType = MediaType.MULTIPART_FORM_DATA,
                schema = @Schema(implementation = Order.class),
                encoding = @Encoding(
                    name = "order",
                    contentType = MediaType.APPLICATION_JSON,
                    explode = true,
                    allowReserved = true,
                    headers = @Header(
                        name = "X-Encoding",
                        schema = @Schema(type = "string")
                    ),
                    extensions = @Extension(
                        name = "encoding-meta",
                        properties = @ExtensionProperty(name = "stable", value = "true", parseValue = true)
                    )
                ),
                extensions = @Extension(
                    name = "multipart-meta",
                    properties = @ExtensionProperty(name = "stable", value = "true", parseValue = true)
                )
            )
        }
    )
    public ServerResponse<Order> explicitRequest(ServerRequest request, Order order) {
        return ServerResponse.response(order).applicationJson().ok();
    }

    @GET
    @Path("/parameters")
    @Operation(description = "Explicit parameter metadata")
    public ServerResponse<Order> parameters(
        ServerRequest request,
        @QueryParam("limit")
        @io.swagger.v3.oas.annotations.Parameter(
            description = "Maximum results",
            example = "10",
            schema = @Schema(type = "integer", format = "int32", minimum = "1")
        )
        Integer limit,
        @HeaderParam("X-Mode")
        @io.swagger.v3.oas.annotations.Parameter(
            description = "Execution mode",
            required = true,
            schema = @Schema(allowableValues = {"fast", "safe"})
        )
        String mode
    ) {
        return ServerResponse.response(new Order(6L, mode)).applicationJson().ok();
    }

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

    /** Serves a parameterized page response with a raw OpenAPI response annotation. */
    @GET
    @Path("/paged-response")
    @Operation(description = "Test typed generic page response", responses = {
            @ApiResponse(responseCode = "200", description = "Success", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = PagedResponse.class)))})
    public ServerResponse<PagedResponse<Pojo>> pagedResponse(ServerRequest request)
    {
        return ServerResponse.response(new PagedResponse<>(List.of(new Pojo(1L, "page item")), 1)).applicationJson().ok();
    }

    /** Serves a parameterized page response with no response annotations. */
    @GET
    @Path("/paged-response-unannotated")
    @Operation(description = "Test typed generic page response without declared responses")
    public ServerResponse<PagedResponse<Pojo>> pagedResponseUnannotated(ServerRequest request)
    {
        return ServerResponse.response(new PagedResponse<>(List.of(new Pojo(2L, "unannotated item")), 2)).applicationJson().ok();
    }

}