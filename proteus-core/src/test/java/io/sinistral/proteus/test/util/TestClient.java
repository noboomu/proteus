package io.sinistral.proteus.test.util;

import org.hamcrest.Matcher;
import tools.jackson.databind.ObjectMapper;

import java.io.File;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test HTTP client with fluent API similar to REST Assured, using Java HttpClient and Jackson 3.0
 */
public class TestClient {

    private static final HttpClient HTTP_CLIENT = HttpClient.newBuilder()
        .version(HttpClient.Version.HTTP_1_1)
        .followRedirects(HttpClient.Redirect.NORMAL)
        .connectTimeout(Duration.ofSeconds(10))
        .build();

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static String baseUrl = "http://localhost:8090";

    public static void setBaseUrl(String url) {
        baseUrl = url;
    }

    public static RequestSpec given() {
        return new RequestSpec();
    }

    public static class RequestSpec {
        private final Map<String, String> headers = new HashMap<>();
        private final Map<String, List<String>> queryParams = new HashMap<>();
        private final Map<String, List<String>> formParams = new HashMap<>();
        private final List<MultipartPart> multiparts = new ArrayList<>();
        private String body;
        private String contentType;
        private String accept;
        private boolean followRedirects = true;

        public RequestSpec accept(String contentType) {
            this.accept = contentType;
            return this;
        }

        public RequestSpec contentType(String contentType) {
            this.contentType = contentType;
            return this;
        }

        public RequestSpec header(String name, Object value) {
            headers.put(name, String.valueOf(value));
            return this;
        }

        public RequestSpec queryParam(String name, Object value) {
            if (value instanceof Collection) {
                queryParams.put(name, ((Collection<?>) value).stream()
                    .map(String::valueOf)
                    .collect(Collectors.toList()));
            } else {
                queryParams.computeIfAbsent(name, k -> new ArrayList<>()).add(String.valueOf(value));
            }
            return this;
        }

        public RequestSpec body(Object body) {
            try {
                if (body instanceof String) {
                    this.body = (String) body;
                } else {
                    this.body = OBJECT_MAPPER.writeValueAsString(body);
                }
            } catch (Exception e) {
                throw new RuntimeException("Failed to serialize body", e);
            }
            return this;
        }

        public RequestSpec multiPart(String name, Object value) {
            multiparts.add(new MultipartPart(name, value, null));
            return this;
        }

        public RequestSpec multiPart(String name, Object value, String mimeType) {
            if (value instanceof File) {
                multiparts.add(new MultipartPart(name, (File) value, mimeType));
            } else if (value instanceof String) {
                multiparts.add(new MultipartPart(name, (String) value, mimeType));
            } else {
                multiparts.add(new MultipartPart(name, value, mimeType));
            }
            return this;
        }

        public RequestSpec multiPart(String controlName, File file) {
            multiparts.add(new MultipartPart(controlName, file, null));
            return this;
        }

        public RequestSpec multiPart(String controlName, File file, String mimeType) {
            multiparts.add(new MultipartPart(controlName, file, mimeType));
            return this;
        }

        public RequestSpec formParam(String name, Object value) {
            formParams.computeIfAbsent(name, k -> new ArrayList<>()).add(String.valueOf(value));
            return this;
        }

        public RedirectSpec redirects() {
            return new RedirectSpec(this);
        }

        public RequestSpec when() {
            return this;
        }

        public ResponseSpec get(String path) {
            return execute("GET", path);
        }

        public ResponseSpec post(String path) {
            return execute("POST", path);
        }

        public ResponseSpec put(String path) {
            return execute("PUT", path);
        }

        public ResponseSpec delete(String path) {
            return execute("DELETE", path);
        }

        private ResponseSpec execute(String method, String path) {
            try {
                String url = baseUrl + "/" + path;
                if (!queryParams.isEmpty()) {
                    url += "?" + buildQueryString();
                }

                HttpRequest.Builder builder = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .timeout(Duration.ofSeconds(30));

                if (accept != null) {
                    builder.header("Accept", accept);
                }

                if (contentType != null) {
                    builder.header("Content-Type", contentType);
                }

                headers.forEach(builder::header);

                HttpRequest.BodyPublisher bodyPublisher;
                if (!multiparts.isEmpty()) {
                    String boundary = "----Boundary" + UUID.randomUUID().toString().replace("-", "");
                    builder.header("Content-Type", "multipart/form-data; boundary=" + boundary);
                    bodyPublisher = buildMultipartBody(boundary);
                } else if (body != null) {
                    bodyPublisher = HttpRequest.BodyPublishers.ofString(body);
                } else {
                    bodyPublisher = HttpRequest.BodyPublishers.noBody();
                }

                builder.method(method, bodyPublisher);

                HttpClient client = followRedirects ? HTTP_CLIENT :
                    HttpClient.newBuilder()
                        .followRedirects(HttpClient.Redirect.NEVER)
                        .build();

                HttpRequest request = builder.build();
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

                return new ResponseSpec(response);
            } catch (Exception e) {
                throw new RuntimeException("Failed to execute request", e);
            }
        }

        private String buildQueryString() {
            return queryParams.entrySet().stream()
                .flatMap(entry -> entry.getValue().stream()
                    .map(value -> URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8) +
                        "=" + URLEncoder.encode(value, StandardCharsets.UTF_8)))
                .collect(Collectors.joining("&"));
        }

        private HttpRequest.BodyPublisher buildMultipartBody(String boundary) {
            try {
                StringBuilder sb = new StringBuilder();

                // Add multipart parts
                for (MultipartPart part : multiparts) {
                    sb.append("--").append(boundary).append("\r\n");

                    if (part.file != null) {
                        sb.append("Content-Disposition: form-data; name=\"")
                            .append(part.name)
                            .append("\"; filename=\"")
                            .append(part.file.getName())
                            .append("\"\r\n");
                        if (part.mimeType != null) {
                            sb.append("Content-Type: ").append(part.mimeType).append("\r\n");
                        }
                        sb.append("\r\n");
                        sb.append(new String(Files.readAllBytes(part.file.toPath())));
                        sb.append("\r\n");
                    } else if (part.stringContent != null) {
                        // Handle string content (like JSON strings)
                        sb.append("Content-Disposition: form-data; name=\"").append(part.name).append("\"\r\n");
                        if (part.mimeType != null) {
                            sb.append("Content-Type: ").append(part.mimeType).append("\r\n");
                        }
                        sb.append("\r\n");
                        sb.append(part.stringContent);
                        sb.append("\r\n");
                    } else {
                        // Handle object serialization
                        sb.append("Content-Disposition: form-data; name=\"").append(part.name).append("\"\r\n");
                        if (part.mimeType != null) {
                            sb.append("Content-Type: ").append(part.mimeType).append("\r\n");
                        } else {
                            sb.append("Content-Type: application/json\r\n");
                        }
                        sb.append("\r\n");
                        sb.append(OBJECT_MAPPER.writeValueAsString(part.value));
                        sb.append("\r\n");
                    }
                }

                // Add form parameters as multipart parts
                for (Map.Entry<String, List<String>> entry : formParams.entrySet()) {
                    for (String value : entry.getValue()) {
                        sb.append("--").append(boundary).append("\r\n");
                        sb.append("Content-Disposition: form-data; name=\"").append(entry.getKey()).append("\"\r\n");
                        sb.append("\r\n");
                        sb.append(value);
                        sb.append("\r\n");
                    }
                }

                sb.append("--").append(boundary).append("--\r\n");
                return HttpRequest.BodyPublishers.ofString(sb.toString());
            } catch (Exception e) {
                throw new RuntimeException("Failed to build multipart body", e);
            }
        }

        private static class MultipartPart {
            String name;
            Object value;
            File file;
            String stringContent;
            String mimeType;

            MultipartPart(String name, Object value, String mimeType) {
                this.name = name;
                this.value = value;
                this.mimeType = mimeType;
            }

            MultipartPart(String name, String stringContent, String mimeType) {
                this.name = name;
                this.stringContent = stringContent;
                this.mimeType = mimeType;
            }

            MultipartPart(String name, File file, String mimeType) {
                this.name = name;
                this.file = file;
                this.mimeType = mimeType;
            }
        }
    }

    public static class RedirectSpec {
        private final RequestSpec requestSpec;

        RedirectSpec(RequestSpec requestSpec) {
            this.requestSpec = requestSpec;
        }

        public RequestSpec follow(boolean follow) {
            requestSpec.followRedirects = follow;
            return requestSpec;
        }
    }

    public static class ResponseSpec {
        private final HttpResponse<String> response;

        ResponseSpec(HttpResponse<String> response) {
            this.response = response;
        }

        public ResponseSpec then() {
            return this;
        }

        public ResponseSpec statusCode(int expectedCode) {
            assertEquals(expectedCode, response.statusCode(),
                "Expected status code " + expectedCode + " but got " + response.statusCode());
            return this;
        }

        public ResponseSpec body(String matcher) {
            // Simple string contains check
            assertTrue(response.body().contains(matcher),
                "Response body does not contain: " + matcher);
            return this;
        }

        public ResponseSpec body(Matcher<?> matcher) {
            // Hamcrest matcher support
            assertTrue(matcher.matches(response.body()),
                "Response body does not match: " + matcher);
            return this;
        }

        public ResponseSpec body(String jsonPath, Object expectedValue) {
            // Simple JSON path checking using Jackson
            try {
                Map<?, ?> json = OBJECT_MAPPER.readValue(response.body(), Map.class);
                Object actualValue = json.get(jsonPath);

                // Handle Hamcrest matcher
                if (expectedValue instanceof Matcher) {
                    Matcher<?> matcher = (Matcher<?>) expectedValue;
                    assertTrue(matcher.matches(actualValue),
                        "Expected " + jsonPath + " to match " + matcher + " but was " + actualValue);
                } else {
                    assertEquals(expectedValue, actualValue,
                        "Expected " + jsonPath + " to be " + expectedValue + " but was " + actualValue);
                }
            } catch (Exception e) {
                fail("Failed to parse JSON response: " + e.getMessage());
            }
            return this;
        }

        public ResponseSpec header(String name, Object expectedValue) {
            Optional<String> headerValue = response.headers().firstValue(name);

            // Handle Hamcrest matcher
            if (expectedValue instanceof Matcher) {
                Matcher<?> matcher = (Matcher<?>) expectedValue;
                if (headerValue.isPresent()) {
                    assertTrue(matcher.matches(headerValue.get()),
                        "Expected header " + name + " to match " + matcher);
                } else {
                    // anything() matcher should pass even if header is absent
                    assertTrue(matcher.matches(null),
                        "Expected header " + name + " to match " + matcher);
                }
            } else if (expectedValue == null) {
                assertTrue(headerValue.isPresent(), "Expected header " + name + " to be present");
            } else {
                assertTrue(headerValue.isPresent() && headerValue.get().equals(String.valueOf(expectedValue)),
                    "Expected header " + name + " to be " + expectedValue);
            }
            return this;
        }

        public ResponseSpec and() {
            return this;
        }

        public ResponseSpec log() {
            System.out.println("Response: " + response.statusCode() + " - " + response.body());
            return this;
        }

        public <T> T as(Class<T> type) {
            try {
                return OBJECT_MAPPER.readValue(response.body(), type);
            } catch (Exception e) {
                throw new RuntimeException("Failed to deserialize response to " + type.getName(), e);
            }
        }

        public String body() {
            return response.body();
        }

        public int statusCode() {
            return response.statusCode();
        }

        public ResponseSpec extract() {
            // In REST Assured, extract() returns an extractable response
            // For our purposes, we just return this for method chaining
            return this;
        }

        public ResponseSpec andReturn() {
            // Similar to extract(), returns the response for further processing
            return this;
        }

        public java.io.InputStream asInputStream() {
            // Convert response body to InputStream
            return new java.io.ByteArrayInputStream(response.body().getBytes(StandardCharsets.UTF_8));
        }

        public byte[] asByteArray() {
            // Return response body as byte array
            return response.body().getBytes(StandardCharsets.UTF_8);
        }
    }
}
