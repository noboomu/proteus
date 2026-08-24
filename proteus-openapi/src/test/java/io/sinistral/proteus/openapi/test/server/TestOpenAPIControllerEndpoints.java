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
import tools.jackson.dataformat.yaml.YAMLMapper;

@ExtendWith(OpenAPIDefaultServer.class)
public class TestOpenAPIControllerEndpoints {
    private static final HttpClient httpClient = HttpClient.newHttpClient();

    @Test
    public void testYamlSpec() throws Exception {
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(OpenAPIDefaultServer.getBaseURI() + "v1/openapi.yaml")).GET().build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertTrue(response.body().contains("openapi:"));
    }

    @Test
    public void testYmlSpec() throws Exception {
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(OpenAPIDefaultServer.getBaseURI() + "v1/openapi.yml")).GET().build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertTrue(response.body().contains("openapi:"));
    }

    @Test
    public void testJsonSpec() throws Exception {
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(OpenAPIDefaultServer.getBaseURI() + "v1/openapi.json")).GET().build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertTrue(response.body().startsWith("{"));
    }

    @Test
    public void testDocumentation() throws Exception {
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(OpenAPIDefaultServer.getBaseURI() + "v1/openapi/redoc")).GET().build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertTrue(response.body().contains("<html"));
    }

    @Test
    public void testBearer() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(OpenAPIDefaultServer.getBaseURI() + "v1/tests/bearer"))
            .header("Authorization", "Bearer 123456")
            .GET()
            .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        assertNotNull(response.body());
        Map<String, Object> map = new ObjectMapper().readValue(response.body(), new TypeReference<Map<String, Object>>() {});
        assertEquals("123456", map.get("token"));
    }
}
