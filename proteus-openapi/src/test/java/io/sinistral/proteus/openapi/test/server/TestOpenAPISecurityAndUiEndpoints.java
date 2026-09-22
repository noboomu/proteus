package io.sinistral.proteus.openapi.test.server;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.dataformat.yaml.YAMLMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Running-server coverage for security components, live security enforcement,
 * and the documentation/UI routes.
 */
@ExtendWith(OpenAPIDefaultServer.class)
public class TestOpenAPISecurityAndUiEndpoints {

    private static final String SIGNING_SECRET = "proteus-test-hmac-secret-32-bytes!";

    private static final HttpClient httpClient = HttpClient.newHttpClient();

    // --- Security components ---

    /** Verifies the served document carries the declared API-key and bearer schemes. */
    @Test
    public void servedSecurityComponentsContainDeclaredSchemes() throws Exception {
        JsonNode schemes = openApiJson().path("components").path("securitySchemes");

        JsonNode apiKey = schemes.path("ApiKeyAuth");
        assertFalse(apiKey.isMissingNode(), "config-declared ApiKeyAuth scheme is served");
        assertEquals("apiKey", apiKey.path("type").asText());
        assertEquals("header", apiKey.path("in").asText());
        assertEquals("X-API-KEY", apiKey.path("name").asText());

        JsonNode bearer = schemes.path("bearerAuth");
        assertFalse(bearer.isMissingNode(), "JWT bearer scheme is served");
        assertEquals("http", bearer.path("type").asText());
        assertEquals("bearer", bearer.path("scheme").asText());
        assertEquals("JWT", bearer.path("bearerFormat").asText());
    }

    // --- Operation security metadata ---

    /** Verifies public, role-protected, and denied operations declare their security metadata. */
    @Test
    public void operationsDeclareConfiguredSecurityMetadata() throws Exception {
        JsonNode paths = openApiJson().path("paths");

        JsonNode publicOp = paths
            .path("/security-tests/public")
            .path("get");
        assertFalse(publicOp.path("security").isArray(), "public operation declares no security requirement");
        assertEquals(true, publicOp.path("x-security-public").asBoolean());

        JsonNode adminOp = paths
            .path("/security-tests/admin")
            .path("get");
        assertTrue(
            adminOp.path("security").isArray(),
            "role-protected operation declares bearerAuth requirement"
        );
        JsonNode requirement = adminOp.path("security").get(0).path("bearerAuth");
        assertTrue(requirement.isArray(), "role-protected operation requires bearerAuth");
        // addList(name) without roles emits an empty array: any authenticated bearer is accepted
        assertEquals(0, requirement.size(), "bearer requirement carries no role scopes: " + requirement);
        assertArrayEquals(
            new String[] {"admin"},
            jsonArrayToStrings(adminOp.path("x-required-roles")).toArray(String[]::new)
        );

        JsonNode deniedOp = paths
            .path("/security-tests/denied")
            .path("get");
        assertFalse(deniedOp.path("security").isArray(), "denied operation declares no security requirement");
        assertEquals(true, deniedOp.path("x-security-forbidden").asBoolean());

        JsonNode authenticatedOp = paths
            .path("/security-tests/authenticated")
            .path("get");
        assertTrue(
            authenticatedOp.path("security").isArray(),
            "claim-secured operation declares bearerAuth requirement"
        );
        assertTrue(
            authenticatedOp.path("security").get(0).has("bearerAuth"),
            "claim-secured operation requires bearerAuth"
        );
    }

    /** Verifies every security requirement in the served document resolves to a component. */
    @Test
    public void everySecurityRequirementResolvesToADeclaredScheme() throws Exception {
        JsonNode root = openApiJson();
        JsonNode schemes = root.path("components").path("securitySchemes");

        List<String> schemeNames = new ArrayList<>();
        schemes.properties().forEach(entry -> schemeNames.add(entry.getKey()));

        List<String> unresolved = new ArrayList<>();
        List<String> httpMethods = List.of("get", "post", "put", "delete", "patch", "head", "options");

        // document-level requirements first, then per-operation requirements
        List<JsonNode> requirements = new ArrayList<>();
        if (root.path("security").isArray()) {
            root.path("security").forEach(requirements::add);
        }
        root.path("paths").properties().forEach(pathEntry ->
            pathEntry.getValue().properties().forEach(methodEntry -> {
                if (httpMethods.contains(methodEntry.getKey())) {
                    JsonNode security = methodEntry.getValue().path("security");
                    if (security.isArray()) {
                        security.forEach(requirements::add);
                    }
                }
            })
        );

        requirements.forEach(requirement ->
            requirement.properties().forEach(nameEntry -> {
                if (!schemeNames.contains(nameEntry.getKey())) {
                    unresolved.add(nameEntry.getKey());
                }
            })
        );

        assertFalse(schemeNames.isEmpty(), "document declares security schemes");
        assertTrue(schemeNames.contains("bearerAuth"), "bearerAuth component is declared: " + schemeNames);
        assertFalse(requirements.isEmpty(), "document declares at least one security requirement");
        assertTrue(
            unresolved.isEmpty(),
            "every security requirement resolves to a declared scheme, unresolved: " + unresolved
        );
    }

    // --- Live enforcement through served routes ---

    /** Verifies anonymous access to the protected operation is rejected. */
    @Test
    public void protectedOperationRequiresAuthentication() throws Exception {
        HttpResponse<String> response = get("v1/security-tests/admin");

        assertEquals(401, response.statusCode(), response.body());
    }

    /** Verifies an authenticated caller without the role is forbidden. */
    @Test
    public void protectedOperationRejectsMissingRole() throws Exception {
        HttpResponse<String> response = httpClient.send(
            HttpRequest.newBuilder()
                .uri(URI.create(OpenAPIDefaultServer.getBaseURI() + "v1/security-tests/admin"))
                .header("Authorization", "Bearer " + token(List.of("user")))
                .GET()
                .build(),
            HttpResponse.BodyHandlers.ofString()
        );

        assertEquals(403, response.statusCode(), response.body());
    }

    /** Verifies an authenticated caller with the declared role reaches the operation. */
    @Test
    public void protectedOperationAcceptsDeclaredRole() throws Exception {
        HttpResponse<String> response = httpClient.send(
            HttpRequest.newBuilder()
                .uri(URI.create(OpenAPIDefaultServer.getBaseURI() + "v1/security-tests/admin"))
                .header("Authorization", "Bearer " + token(List.of("admin")))
                .GET()
                .build(),
            HttpResponse.BodyHandlers.ofString()
        );

        assertEquals(200, response.statusCode(), response.body());
        // the controller returns {"result":"admin"}; assert the parsed field, not substring noise
        JsonNode body = JSON.readTree(response.body());
        assertEquals("admin", body.path("result").asText(), body.toString());
    }

    /** Verifies claim-secured operations reject anonymous and accept authenticated callers. */
    @Test
    public void authenticatedOperationEnforcesAuthentication() throws Exception {
        HttpResponse<String> anonymous = get("v1/security-tests/authenticated");
        assertEquals(401, anonymous.statusCode(), anonymous.body());

        HttpResponse<String> authenticated = httpClient.send(
            HttpRequest.newBuilder()
                .uri(URI.create(OpenAPIDefaultServer.getBaseURI() + "v1/security-tests/authenticated"))
                .header("Authorization", "Bearer " + token(List.of("user")))
                .GET()
                .build(),
            HttpResponse.BodyHandlers.ofString()
        );
        assertEquals(200, authenticated.statusCode(), authenticated.body());
        JsonNode body = JSON.readTree(authenticated.body());
        assertEquals("authenticated", body.path("result").asText(), body.toString());
        assertEquals("openapi-coverage", body.path("subject").asText(), body.toString());
    }

    /** Verifies the public operation answers anonymous callers. */
    @Test
    public void publicOperationServesAnonymousRequests() throws Exception {
        HttpResponse<String> response = get("v1/security-tests/public");

        assertEquals(200, response.statusCode(), response.body());
    }

    /** Verifies the denied operation refuses every caller. */
    @Test
    public void deniedOperationRefusesAuthenticatedRequests() throws Exception {
        HttpResponse<String> anonymous = get("v1/security-tests/denied");
        assertEquals(403, anonymous.statusCode(), anonymous.body());

        HttpResponse<String> authenticated = httpClient.send(
            HttpRequest.newBuilder()
                .uri(URI.create(OpenAPIDefaultServer.getBaseURI() + "v1/security-tests/denied"))
                .header("Authorization", "Bearer " + token(List.of("admin")))
                .GET()
                .build(),
            HttpResponse.BodyHandlers.ofString()
        );
        assertEquals(403, authenticated.statusCode(), authenticated.body());
    }

    // --- Documentation and UI routes ---

    /** Verifies the documentation HTML references the served YAML document. */
    @Test
    public void documentationHtmlReferencesServedDocument() throws Exception {
        HttpResponse<String> response = get("v1/openapi");

        assertEquals(200, response.statusCode(), response.body());
        assertTrue(
            response.headers().firstValue("content-type").orElse("").contains("text/html"),
            "documentation content type"
        );
        String body = response.body();
        assertTrue(body.contains("swagger-ui"), "documentation loads swagger UI");
        assertTrue(
            body.contains("window.location.pathname + '.yaml'"),
            "documentation resolves the spec relative to its own path"
        );
    }

    /** Verifies the static OpenAPI UI assets are served under the base path. */
    @Test
    public void staticUiAssetsAreServed() throws Exception {
        HttpResponse<String> css = get("v1/openapi/swagger-ui.css");

        assertEquals(200, css.statusCode());
        assertTrue(css.body().length() > 100, "static stylesheet has content");
    }

    /** Verifies the redoc route renders HTML pointing at the served spec. */
    @Test
    public void redocRouteReferencesServedSpec() throws Exception {
        HttpResponse<String> response = get("v1/openapi/redoc");

        assertEquals(200, response.statusCode(), response.body());
        assertTrue(
            response.headers().firstValue("content-type").orElse("").contains("text/html"),
            "redoc content type"
        );
        assertTrue(
            response.body().contains("/v1/openapi.yaml"),
            "redoc resolves the served YAML document, body was:\n" + response.body()
        );
    }

    // --- JSON/YAML document equivalence ---

    /** Verifies JSON and YAML documents publish identical info and security schemes. */
    @Test
    public void jsonAndYamlDocumentsHaveEquivalentMetadata() throws Exception {
        JsonNode json = openApiJson();

        HttpResponse<String> response = get("v1/openapi.yaml");
        JsonNode yaml = YAML.readTree(response.body());

        assertEquals(json.path("info"), yaml.path("info"), "info");
        assertEquals(
            json.path("components").path("securitySchemes"),
            yaml.path("components").path("securitySchemes"),
            "securitySchemes"
        );
    }

    // --- Helpers ---

    private static final ObjectMapper JSON = new ObjectMapper();
    private static final YAMLMapper YAML = new YAMLMapper();

    private static HttpResponse<String> get(String path) throws Exception {
        return httpClient.send(
            HttpRequest.newBuilder()
                .uri(URI.create(OpenAPIDefaultServer.getBaseURI() + path))
                .GET()
                .build(),
            HttpResponse.BodyHandlers.ofString()
        );
    }

    private static JsonNode openApiJson() throws Exception {
        HttpResponse<String> response = get("v1/openapi.json");
        assertEquals(200, response.statusCode(), response.body());
        return JSON.readTree(response.body());
    }

    private static List<String> jsonArrayToStrings(JsonNode array) {
        List<String> values = new ArrayList<>();
        if (array.isArray()) {
            array.forEach(item -> values.add(item.asText()));
        }
        return values;
    }

    private static String token(List<String> roles) throws Exception {
        Instant now = Instant.now();
        // roles claim + expiration are both required by the default JWT policy
        JWTClaimsSet claims = new JWTClaimsSet.Builder()
            .subject("openapi-coverage")
            .issueTime(Date.from(now))
            .expirationTime(Date.from(now.plusSeconds(300)))
            .claim("roles", roles)
            .build();
        SignedJWT jwt = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), claims);
        jwt.sign(new MACSigner(SIGNING_SECRET.getBytes(StandardCharsets.UTF_8)));
        return jwt.serialize();
    }
}
