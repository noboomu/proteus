/**
 *
 */
package io.sinistral.proteus.openapi.test.server;

import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import tools.jackson.databind.JsonNode;
import tools.jackson.dataformat.yaml.YAMLMapper;

/**
 * @author jbauer
 */
@ExtendWith(OpenAPIDefaultServer.class)
public class TestOpenAPIControllerEndpoints {

    private static final HttpClient httpClient = HttpClient.newHttpClient();

    @Test
    public void testYamlSpec() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(OpenAPIDefaultServer.getBaseURI() + "v1/openapi.yaml"))
            .GET()
            .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode(), "Expected 200 but got " + response.statusCode() + ": " + response.body());
        assertTrue(response.headers().firstValue("content-type").orElse("").contains("application/yaml"));
        assertTrue(response.body().contains("openapi:"));
        assertTrue(!response.body().trim().startsWith("{"));
        JsonNode yaml = new YAMLMapper().readTree(response.body());
        assertTrue(yaml.has("openapi"));
        assertTrue(!yaml.at("/paths/~1tests~1generic/get").isMissingNode());
    }

    /** Verifies that an explicit raw page annotation retains the method item type. */
    @Test
    public void testJsonSpecSpecializesGenericResponse() throws Exception {
        String response = when().get("v1/openapi.json").then().statusCode(200).extract().asString();
        JsonNode document = new ObjectMapper().readTree(response);
        JsonNode responseSchema = document.at("/paths/~1tests~1paged-response/get/responses/200/content/application~1json/schema/$ref");
        JsonNode itemSchema = document.at("/components/schemas/PagedResponsePojo/properties/data/items/$ref");

        assertEquals("#/components/schemas/PagedResponsePojo", responseSchema.asText());
        assertEquals("#/components/schemas/Pojo", itemSchema.asText());
    }

    /** Verifies a method with no declared success response still gets a typed one. */
    @Test
    public void testJsonSpecAddsTypedDefaultForUnannotatedResponses() throws Exception {
        String response = when().get("v1/openapi.json").then().statusCode(200).extract().asString();
        JsonNode document = new ObjectMapper().readTree(response);
        JsonNode responseSchema = document.at("/paths/~1tests~1paged-response-unannotated/get/responses/default/content/application~1json/schema/$ref");

        assertEquals("#/components/schemas/PagedResponsePojo", responseSchema.asText());
    }

    @Test
    public void testYmlSpec() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(OpenAPIDefaultServer.getBaseURI() + "v1/openapi.yml"))
            .GET()
            .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertTrue(response.headers().firstValue("content-type").orElse("").contains("application/yaml"));
    }

    @Test
    public void testJsonSpec() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(OpenAPIDefaultServer.getBaseURI() + "v1/openapi.json"))
            .GET()
            .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertTrue(response.headers().firstValue("content-type").orElse("").contains("application/json"));
        assertFalse(response.body().contains(": null"));

        JsonNode root = new ObjectMapper().readTree(response.body());
        assertTrue(root.at("/paths/~1tests~1generic/get/requestBody").isMissingNode());
        assertTrue(root.at("/paths/~1tests~1explicit-response/get/requestBody").isMissingNode());
    }

    private JsonNode openApiJson() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(OpenAPIDefaultServer.getBaseURI() + "v1/openapi.json"))
            .GET()
            .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        return new ObjectMapper().readTree(response.body());
    }

    @Test
    public void testOmitsUnsetExternalDocumentation() throws Exception {
        JsonNode root = openApiJson();
        assertTrue(
            root.findValues("externalDocs").isEmpty(),
            "OpenAPI must omit default-valued externalDocs annotations"
        );
    }

    @Test
    public void testJsonSpecGenerics() throws Exception {
        JsonNode root = openApiJson();
        JsonNode schemaRef = root.at(
            "/paths/~1tests~1generic/get/responses/200/content/application~1json/schema/$ref"
        );

        assertTrue(
            schemaRef.isMissingNode(),
            "legacy Proteus does not backfill an explicit 200 response from the return type"
        );
        JsonNode page = root.at("/components/schemas/PagedResponseOrder");
        assertTrue(!page.isMissingNode());
        assertTrue(root.at("/components/schemas/Order/properties/orderNumber").isObject());
        assertTrue(root.at("/components/schemas/Order/properties/order_number").isMissingNode());
        assertEquals(
            "#/components/schemas/Order",
            page.at("/properties/data/items/$ref").asText()
        );
        assertEquals("integer", page.at("/properties/total/type").asText());
        assertEquals("int32", page.at("/properties/total/format").asText());
    }

    @Test
    public void testExplicitAndInferredResponseSchemas() throws Exception {
        JsonNode root = openApiJson();

        assertEquals(
            "#/components/schemas/Order",
            root.at("/paths/~1tests~1explicit-response/get/responses/200/content/application~1json/schema/$ref").asText()
        );
        assertEquals(
            "Order example",
            root.at("/paths/~1tests~1explicit-response/get/responses/200/content/application~1json/examples/order/summary").asText()
        );
        assertTrue(
            root.at("/paths/~1tests~1explicit-response/get/responses/200/x-response-meta/stable").asBoolean()
        );
        assertTrue(
            root.at("/paths/~1tests~1explicit-response/get/responses/200/content/application~1json/x-media-meta/stable").asBoolean()
        );
        assertEquals(
            "integer",
            root.at("/paths/~1tests~1explicit-response/get/responses/200/headers/X-Rate-Limit/schema/type").asText()
        );
        assertEquals(
            "int32",
            root.at("/paths/~1tests~1explicit-response/get/responses/200/headers/X-Rate-Limit/schema/format").asText()
        );
        assertEquals(
            "explicitResponse",
            root.at("/paths/~1tests~1explicit-response/get/responses/200/links/orderById/operationId").asText()
        );
        assertEquals(
            "$response.body#/id",
            root.at("/paths/~1tests~1explicit-response/get/responses/200/links/orderById/parameters/id").asText()
        );
        assertEquals(
            "array",
            root.at("/paths/~1tests~1explicit-response/get/responses/200/headers/X-Order-Ids/schema/type").asText()
        );
        assertEquals(
            "int64",
            root.at("/paths/~1tests~1explicit-response/get/responses/200/headers/X-Order-Ids/schema/items/format").asText()
        );
        assertTrue(
            root.at("/paths/~1tests~1explicit-response/get/responses/200/headers/X-Order-Ids/explode").isMissingNode(),
            "legacy serialization omits default explode=false"
        );
        JsonNode headerExample = root.at(
            "/paths/~1tests~1explicit-response/get/responses/200/headers/X-Order-Ids/examples/ids/value"
        );
        assertTrue(headerExample.isArray());
        assertEquals(1, headerExample.get(0).asInt());
        assertEquals(2, headerExample.get(1).asInt());
        assertTrue(
            root.at("/paths/~1tests~1explicit-response/get/responses/200/links/orderById/server").isMissingNode(),
            "legacy link parsing omits the nested server"
        );
        assertTrue(
            root.at("/paths/~1tests~1explicit-response/get/responses/200/links/orderById/x-trace/enabled").asBoolean()
        );
        assertEquals(
            "array",
            root.at("/paths/~1tests~1explicit-array-response/get/responses/200/content/application~1json/schema/type").asText()
        );
        assertTrue(
            root.at("/paths/~1tests~1explicit-array-response/get/responses/200/content/application~1json/schema/description").isMissingNode(),
            "legacy parsing omits arraySchema metadata"
        );
        assertTrue(
            root.at("/paths/~1tests~1explicit-array-response/get/responses/200/content/application~1json/schema/x-array-meta/stable").asBoolean()
        );
        assertEquals(
            "#/components/schemas/Order",
            root.at("/paths/~1tests~1explicit-array-response/get/responses/200/content/application~1json/schema/items/$ref").asText()
        );
        assertTrue(
            root.at("/paths/~1tests~1return-type-schema/get/responses/200/content/application~1json/schema").isMissingNode(),
            "legacy Proteus does not apply useReturnTypeSchema to explicit 200 responses"
        );
        assertEquals(
            "Metadata-only response schema",
            root.at("/paths/~1tests~1metadata-return-type-schema/get/responses/200/content/application~1json/schema/description").asText()
        );
        assertEquals(
            "string",
            root.at("/paths/~1tests~1metadata-return-type-schema/get/responses/200/content/application~1json/schema/type").asText()
        );
        assertTrue(
            root.at("/paths/~1tests~1metadata-return-type-schema/get/responses/200/content/application~1json/schema/allOf").isMissingNode()
        );
        assertTrue(
            root.at("/paths/~1tests~1return-type-schema/get/responses/400/content").isMissingNode()
        );
        assertEquals(
            "#/components/schemas/Order",
            root.at("/paths/~1tests~1explicit-request/post/requestBody/content/application~1json/schema/$ref").asText()
        );
        assertTrue(
            root.at("/paths/~1tests~1explicit-request/post/requestBody/x-request-meta/stable").asBoolean()
        );
        assertTrue(
            root.at("/paths/~1tests~1explicit-request/post/requestBody/content/multipart~1form-data/x-multipart-meta/stable").asBoolean()
        );
        JsonNode encoding = root.at(
            "/paths/~1tests~1explicit-request/post/requestBody/content/multipart~1form-data/encoding/order"
        );
        assertEquals("application/json", encoding.get("contentType").asText());
        assertTrue(encoding.get("style") == null, "legacy parsing omits default encoding style");
        assertTrue(encoding.get("explode").asBoolean());
        assertTrue(encoding.get("allowReserved").asBoolean());
        assertEquals("string", encoding.at("/headers/X-Encoding/schema/type").asText());
        assertTrue(encoding.at("/x-encoding-meta/stable").asBoolean());
        assertEquals(
            "#/components/schemas/Order",
            root.at("/paths/~1tests~1explicit-request/post/responses/default/content/application~1json/schema/$ref").asText()
        );
        assertTrue(root.at("/paths/~1tests~1explicit-request/post/requestBody/required").asBoolean());
    }

    @Test
    public void testAllSerializedSchemaReferencesResolve() throws Exception {
        JsonNode json = openApiJson();
        assertAllLocalSchemaReferencesResolve(json);

        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(OpenAPIDefaultServer.getBaseURI() + "v1/openapi.yaml"))
            .GET()
            .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        JsonNode yaml = new YAMLMapper().readTree(response.body());
        assertAllLocalSchemaReferencesResolve(yaml);
        assertTrue(
            json.at("/paths/~1tests~1generic/get/responses/200/content/application~1json/schema").isMissingNode()
        );
        assertTrue(
            yaml.at("/paths/~1tests~1generic/get/responses/200/content/application~1json/schema").isMissingNode()
        );
    }

    private void assertAllLocalSchemaReferencesResolve(JsonNode root) {
        List<String> references = new ArrayList<>();
        collectLocalSchemaReferences(root, references);
        assertFalse(references.isEmpty(), "expected emitted schema references");

        for (String reference : references) {
            String pointer = reference.substring(1);
            assertFalse(
                root.at(pointer).isMissingNode(),
                () -> "Unresolved schema reference: " + reference
            );
        }
    }

    private void collectLocalSchemaReferences(JsonNode node, List<String> references) {
        if (node.isObject()) {
            JsonNode reference = node.get("$ref");
            if (reference != null && reference.isTextual()
                && reference.asText().startsWith("#/components/schemas/")) {
                references.add(reference.asText());
            }
        }
        node.forEach(child -> collectLocalSchemaReferences(child, references));
    }

    @Test
    public void testParameterMetadata() throws Exception {
        JsonNode root = openApiJson();
        JsonNode parameters = root.at("/paths/~1tests~1parameters/get/parameters");
        assertTrue(parameters.isArray());
        assertEquals(2, parameters.size());

        JsonNode limit = parameters.get(0);
        JsonNode mode = parameters.get(1);
        if (!"limit".equals(limit.get("name").asText())) {
            JsonNode swap = limit;
            limit = mode;
            mode = swap;
        }

        assertEquals("query", limit.get("in").asText());
        assertEquals("Maximum results", limit.get("description").asText());
        assertEquals(10, limit.get("example").asInt());
        assertEquals("integer", limit.at("/schema/type").asText());
        assertEquals("int32", limit.at("/schema/format").asText());
        assertEquals("1", limit.at("/schema/minimum").decimalValue().toPlainString());

        assertEquals("X-Mode", mode.get("name").asText());
        assertEquals("header", mode.get("in").asText());
        assertEquals("Execution mode", mode.get("description").asText());
        assertTrue(mode.get("required").asBoolean());
        assertEquals(List.of("fast", "safe"),
            new ObjectMapper().convertValue(mode.at("/schema/enum"), new TypeReference<List<String>>() {}));
    }

    @Test
    public void testDocumentation() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(OpenAPIDefaultServer.getBaseURI() + "v1/openapi"))
            .GET()
            .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertTrue(response.headers().firstValue("content-type").orElse("").contains("text/html"));
    }

    @Test
    public void testBearer() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(OpenAPIDefaultServer.getBaseURI() + "v1/tests/bearer"))
            .header("Authorization", "Bearer 123456")
            .GET()
            .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        assertNotNull(response.body());

        Map<String, Object> map = mapper.readValue(response.body(), new TypeReference<>() {
        });

        assertEquals("123456", map.get("token"));
        assertEquals(true, map.get("result"));
    }

}
