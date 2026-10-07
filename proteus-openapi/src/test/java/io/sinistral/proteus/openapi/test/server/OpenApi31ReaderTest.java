package io.sinistral.proteus.openapi.test.server;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.sinistral.proteus.openapi.jaxrs2.Reader;
import io.sinistral.proteus.openapi.models.OpenAPI;
import io.sinistral.proteus.openapi.models.Operation;
import io.sinistral.proteus.openapi.models.PathItem;
import io.sinistral.proteus.openapi.util.Json;
import io.swagger.v3.oas.annotations.Webhook;
import io.swagger.v3.oas.annotations.Webhooks;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import org.junit.jupiter.api.Test;

/** OpenAPI 3.1 reader behaviors ported from archive/openapi31: webhooks and path template patterns. */
public class OpenApi31ReaderTest {

    @Path("/orders")
    @Produces("application/json")
    @Webhooks({
        @Webhook(
            name = "orderShipped",
            operation = @io.swagger.v3.oas.annotations.Operation(
                summary = "Order shipped",
                description = "Sent when an order leaves the warehouse",
                operationId = "orderShippedHook",
                responses = @ApiResponse(responseCode = "200", description = "Acknowledged")
            )
        ),
        @Webhook(
            name = "orderCancelled",
            operation = @io.swagger.v3.oas.annotations.Operation(
                summary = "Order cancelled",
                responses = @ApiResponse(responseCode = "204", description = "Acknowledged")
            )
        ),
    })
    public static class OrdersController {

        @GET
        @Path("/{id}")
        public String get() {
            return "{}";
        }
    }

    @Path("/items")
    public static class PatternController {

        @GET
        @Path("/{id:[0-9]+}/{slug}")
        public String get(
            @jakarta.ws.rs.PathParam("id") String id,
            @jakarta.ws.rs.PathParam("slug") String slug
        ) {
            return id + slug;
        }
    }

    @Path("/plain")
    public static class PlainController {

        @GET
        public String get() {
            return "ok";
        }
    }

    @Test
    public void webhooksAreReadAsPostPathItems() {
        OpenAPI openAPI = new Reader().read(OrdersController.class);

        assertNotNull(openAPI.getWebhooks());
        assertEquals(2, openAPI.getWebhooks().size());

        PathItem shipped = openAPI.getWebhooks().get("orderShipped");
        assertNotNull(shipped);
        Operation op = shipped.getPost();
        assertNotNull(op);
        assertNull(shipped.getGet());
        assertEquals("Order shipped", op.getSummary());
        assertEquals("Sent when an order leaves the warehouse", op.getDescription());
        assertEquals("orderShippedHook", op.getOperationId());
        assertNotNull(op.getResponses().get("200"));

        PathItem cancelled = openAPI.getWebhooks().get("orderCancelled");
        assertNotNull(cancelled);
        assertNotNull(cancelled.getPost().getResponses().get("204"));

        assertTrue(openAPI.getPaths().containsKey("/orders/{id}"));
    }

    @Test
    public void webhooksSerializeAtDocumentRoot() throws Exception {
        OpenAPI openAPI = new Reader().read(OrdersController.class);
        String json = Json.mapper().writeValueAsString(openAPI);

        assertTrue(json.contains("\"webhooks\""), json);
        assertTrue(json.contains("\"orderShipped\""), json);
        assertTrue(json.contains("\"orderShippedHook\""), json);
    }

    @Test
    public void pathTemplateRegexBecomesParameterPattern() {
        OpenAPI openAPI = new Reader().read(PatternController.class);
        PathItem item = openAPI.getPaths().get("/items/{id}/{slug}");
        assertNotNull(item, String.valueOf(openAPI.getPaths().keySet()));
        var params = item.getGet().getParameters();
        var id = params.stream().filter(p -> "id".equals(p.getName())).findFirst().orElseThrow();
        var slug = params.stream().filter(p -> "slug".equals(p.getName())).findFirst().orElseThrow();
        assertEquals("[0-9]+", id.getSchema().getPattern());
        assertNull(slug.getSchema().getPattern());
    }

    @Test
    public void controllerWithoutWebhooksLeavesRootNull() {
        OpenAPI openAPI = new Reader().read(PlainController.class);
        assertNull(openAPI.getWebhooks());
    }
}
