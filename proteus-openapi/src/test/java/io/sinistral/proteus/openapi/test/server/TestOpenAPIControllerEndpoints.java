/**
 *
 */
package io.sinistral.proteus.openapi.test.server;

import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import tools.jackson.databind.JsonNode;

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

        if (response.statusCode() != 200) {
            System.err.println("Error response body: " + response.body());
        }
        assertEquals(200, response.statusCode(), "Expected 200 but got " + response.statusCode() + ": " + response.body());
        assertTrue(response.headers().firstValue("content-type").orElse("").contains("application/yaml"));
    }

    @Test
    public void testYmlSpec() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(OpenAPIDefaultServer.getBaseURI() + "v1/openapi.yaml"))
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
    }

    @Test
    public void testJsonSpecGenerics() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(OpenAPIDefaultServer.getBaseURI() + "v1/openapi.json"))
            .GET()
            .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        
        System.out.println("---- SPEC START ----");
        System.out.println(response.body());
        System.out.println("---- SPEC END ----");
        
        java.nio.file.Files.writeString(java.nio.file.Paths.get("target/openapi-spec.json"), response.body());
        
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(response.body());
        
        JsonNode genericPath = root.at("/paths/~1tests~1generic/get");
        assertTrue(!genericPath.isMissingNode(), "Generic path should exist in spec");
        
        JsonNode schemaRef = genericPath.at("/responses/200/content/application~1json/schema/$ref");
        assertTrue(!schemaRef.isMissingNode(), "Schema $ref should exist");
        assertEquals("#/components/schemas/PagedResponse_Order", schemaRef.asText(), "Generics should resolve correctly");
        
        JsonNode pagedResponseSchema = root.at("/components/schemas/PagedResponse_Order");
        assertTrue(!pagedResponseSchema.isMissingNode(), "Component schema for PagedResponse_Order should exist");
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
