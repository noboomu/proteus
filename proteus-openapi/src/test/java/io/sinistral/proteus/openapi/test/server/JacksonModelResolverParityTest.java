package io.sinistral.proteus.openapi.test.server;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonView;
import io.sinistral.proteus.openapi.converter.AnnotatedType;
import io.sinistral.proteus.openapi.converter.ModelConverters;
import io.sinistral.proteus.openapi.converter.ResolvedSchema;
import io.sinistral.proteus.openapi.models.callbacks.Callback;
import io.sinistral.proteus.openapi.models.Paths;
import io.sinistral.proteus.openapi.models.Components;
import io.sinistral.proteus.openapi.jaxrs2.OpenAPIExtensions;
import io.sinistral.proteus.openapi.jaxrs2.ResolvedParameter;
import io.sinistral.proteus.openapi.jaxrs2.ServerParameterExtension;
import io.sinistral.proteus.openapi.models.media.Schema;
import io.sinistral.proteus.openapi.models.responses.ApiResponse;
import io.sinistral.proteus.openapi.models.responses.ApiResponses;
import io.sinistral.proteus.openapi.models.parameters.RequestBody;
import io.sinistral.proteus.openapi.util.Json;
import io.sinistral.proteus.openapi.util.OperationParser;
import io.swagger.v3.oas.annotations.media.Schema.AccessMode;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.junit.jupiter.api.Test;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.json.JsonMapper;

import java.io.File;
import java.math.BigDecimal;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URL;
import java.nio.ByteBuffer;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;

class JacksonModelResolverParityTest {

    static class Order {
        public Long id;
        public String description;
    }

    static class Page<T> {
        public List<T> items;
        public long total;
    }

    static class Envelope<T> {
        public T payload;
    }

    static class GenericTypes {
        Envelope<Page<Order>> nested;
        List<Order> list;
        Set<String> set;
        Queue<Order> queue;
        Deque<Order> deque;
        Collection<Order> collection;
        List<Map<String, List<Order>>> nestedContainers;
        List<? extends Order> boundedOrders;
        Map<String, List<Order>> mapOfLists;
        Map<String, Order> map;
        Optional<Order> optional;
        Order[] array;
        GenericNode<Order> recursiveGeneric;
    }

    static class Node {
        public String name;
        public Node parent;
        public List<Node> children;
    }

    static class GenericNode<T> {
        public T value;
        public GenericNode<T> next;
    }

    static class GenericBase<T> {
        public T value;
    }

    static class GenericOrderContainer extends GenericBase<Order> {
        public String label;
    }

    enum Status {
        ACTIVE, DISABLED
    }

    enum WireStatus {
        ACTIVE("active"), DISABLED("disabled");

        private final String wireValue;

        WireStatus(String wireValue) {
            this.wireValue = wireValue;
        }

        @JsonValue
        public String wireValue() {
            return wireValue;
        }
    }

    enum NumericStatus {
        ONE(1), TWO(2);

        private final int wireValue;

        NumericStatus(int wireValue) {
            this.wireValue = wireValue;
        }

        @JsonValue
        public int wireValue() {
            return wireValue;
        }
    }

    static class ScalarValue {
        private final String value = "scalar";

        @JsonValue
        public String value() {
            return value;
        }
    }

    static class Views {
        static class Public {}
        static class Internal extends Public {}
    }

    @io.swagger.v3.oas.annotations.media.Schema(
        name = "AnnotatedApiContract",
        title = "Annotated title",
        description = "Annotated description",
        requiredProperties = {"explicitRequired"}
    )
    static class AnnotatedContract {
        @JsonProperty("external_name")
        @io.swagger.v3.oas.annotations.media.Schema(
            description = "Visible name",
            example = "Ada",
            minLength = 2,
            maxLength = 40,
            pattern = "[A-Za-z]+",
            requiredMode = RequiredMode.REQUIRED
        )
        @NotBlank
        @Size(min = 2, max = 40)
        public String internalName;

        @JsonIgnore
        public String secret;

        @io.swagger.v3.oas.annotations.media.Schema(hidden = true)
        public String hidden;

        @io.swagger.v3.oas.annotations.media.Schema(accessMode = AccessMode.READ_ONLY)
        public UUID id;

        @Min(1)
        @Max(100)
        public int quantity;

        @JsonView(Views.Public.class)
        public String publicValue;

        @JsonView(Views.Internal.class)
        public String internalValue;

        @io.swagger.v3.oas.annotations.media.Schema(
            multipleOf = 0.25,
            minimum = "0",
            maximum = "10",
            extensions = @io.swagger.v3.oas.annotations.extensions.Extension(
                name = "constraint",
                properties = @io.swagger.v3.oas.annotations.extensions.ExtensionProperty(
                    name = "source",
                    value = "contract"
                )
            )
        )
        public BigDecimal amount;

        @io.swagger.v3.oas.annotations.media.Schema(
            description = "Documented value",
            externalDocs = @io.swagger.v3.oas.annotations.ExternalDocumentation(
                description = "Value documentation",
                url = "https://example.test/value"
            )
        )
        public String documented;

        @io.swagger.v3.oas.annotations.media.Schema(example = "3", defaultValue = "2")
        public int rank;

        @io.swagger.v3.oas.annotations.media.Schema(not = String.class)
        public Object notString;

        public String explicitRequired;
    }

    static class NamingContract {
        public String displayName;
    }

    static class BeanParameters {
        @jakarta.ws.rs.QueryParam("offset")
        @io.swagger.v3.oas.annotations.Parameter(
            description = "Result offset",
            schema = @io.swagger.v3.oas.annotations.media.Schema(
                type = "integer",
                format = "int32",
                minimum = "0"
            )
        )
        public Integer offset;

        @jakarta.ws.rs.HeaderParam("X-Trace")
        public Optional<String> trace;
    }

    static class ParameterMethods {
        void bean(@jakarta.ws.rs.BeanParam BeanParameters parameters) {}

        void defaulted(
            @jakarta.ws.rs.QueryParam("limit")
            @jakarta.ws.rs.DefaultValue("5")
            Integer limit
        ) {}

        void form(@jakarta.ws.rs.FormParam("name") String name) {}
    }

    record RecordContract(String name, int count) {}

    static class Coordinates {
        public double latitude;
        public double longitude;
    }

    static class UnwrappedContract {
        public String name;

        @JsonUnwrapped
        @NotNull
        public Coordinates coordinates;
    }

    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "kind")
    @JsonSubTypes({
        @JsonSubTypes.Type(value = Dog.class, name = "dog"),
        @JsonSubTypes.Type(value = Cat.class, name = "cat")
    })
    static abstract class Animal {
        public String name;
    }

    static class Dog extends Animal {
        public boolean barks;
    }

    static class Cat extends Animal {
        public int lives;
    }

    static class ScalarTypes {
        UUID uuid;
        URI uri;
        URL url;
        LocalDate date;
        LocalTime time;
        Instant instant;
        OffsetDateTime offsetDateTime;
        File file;
        ByteBuffer buffer;
        byte[] bytes;
    }

    static class ResponseTypes {
        CompletableFuture<io.sinistral.proteus.server.ServerResponse<Page<Order>>> futurePage() {
            return null;
        }
    }

    static class RequestBodyMethods {
        @io.swagger.v3.oas.annotations.parameters.RequestBody
        void empty() {}

        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Documented input",
            required = true
        )
        void documented() {}

        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            ref = "#/components/requestBodies/OrderInput",
            description = "Ignored because ref takes precedence",
            required = true
        )
        void reference() {}
    }

    static class ContextMethods {
        void contextual(@jakarta.ws.rs.core.Context jakarta.ws.rs.core.UriInfo uriInfo) {}
        void contextualString(@jakarta.ws.rs.core.Context String context) {}
    }

    static class ResponseAnnotationMethods {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Array response",
            content = @io.swagger.v3.oas.annotations.media.Content(
                mediaType = "application/json",
                array = @io.swagger.v3.oas.annotations.media.ArraySchema(
                    schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = Order.class),
                    arraySchema = @io.swagger.v3.oas.annotations.media.Schema(
                        description = "Array-level metadata"
                    )
                )
            )
        )
        void arrayResponse() {}

        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Linked response",
            links = @io.swagger.v3.oas.annotations.links.Link(
                name = "related",
                operationId = "readRelated",
                server = @io.swagger.v3.oas.annotations.servers.Server(
                    url = "https://api.example.test",
                    description = "Related API"
                )
            )
        )
        void linkedResponse() {}

        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "204")
        void bareResponse() {}

        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Link edge cases",
            links = {
                @io.swagger.v3.oas.annotations.links.Link(
                    name = "parameters-only",
                    parameters = @io.swagger.v3.oas.annotations.links.LinkParameter(
                        name = "id",
                        expression = "$response.body#/id"
                    )
                ),
                @io.swagger.v3.oas.annotations.links.Link(
                    name = "",
                    operationId = "blankNamedOperation"
                )
            }
        )
        void linkEdgeCases() {}
    }

    private ModelConverters converters() {
        return ModelConverters.create(JsonMapper.builder().build());
    }

    private Type fieldType(String name) throws Exception {
        Field field = GenericTypes.class.getDeclaredField(name);
        return field.getGenericType();
    }

    private ResolvedSchema resolve(ModelConverters converters, Type type) {
        return converters.resolveAsResolvedSchema(new AnnotatedType(type).resolveAsRef(true));
    }

    private Schema<?> component(ResolvedSchema resolved, String name) {
        assertNotNull(resolved.referencedSchemas, "referenced schemas");
        Schema<?> schema = resolved.referencedSchemas.get(name);
        assertNotNull(schema, "component " + name);
        return schema;
    }

    @Test
    void omitsLegacyArraySchemaMetadata() throws Exception {
        io.swagger.v3.oas.annotations.responses.ApiResponse annotation = ResponseAnnotationMethods.class
            .getDeclaredMethod("arrayResponse")
            .getAnnotation(io.swagger.v3.oas.annotations.responses.ApiResponse.class);
        ApiResponse response = OperationParser
            .getApiResponses(new io.swagger.v3.oas.annotations.responses.ApiResponse[] {annotation}, null, null, new Components(), null)
            .orElseThrow()
            .get("200");

        Schema<?> schema = response.getContent().get("application/json").getSchema();
        assertEquals("array", schema.getType());
        assertNull(schema.getDescription());
    }

    @Test
    void omitsLegacyBareApiResponse() throws Exception {
        io.swagger.v3.oas.annotations.responses.ApiResponse annotation = ResponseAnnotationMethods.class
            .getDeclaredMethod("bareResponse")
            .getAnnotation(io.swagger.v3.oas.annotations.responses.ApiResponse.class);

        assertTrue(
            OperationParser.getApiResponses(
                new io.swagger.v3.oas.annotations.responses.ApiResponse[] {annotation},
                null,
                null,
                new Components(),
                null
            ).isEmpty()
        );
    }

    @Test
    void preservesLegacyLinkExistenceAndBlankNameRules() throws Exception {
        io.swagger.v3.oas.annotations.responses.ApiResponse annotation = ResponseAnnotationMethods.class
            .getDeclaredMethod("linkEdgeCases")
            .getAnnotation(io.swagger.v3.oas.annotations.responses.ApiResponse.class);
        ApiResponse response = OperationParser
            .getApiResponses(new io.swagger.v3.oas.annotations.responses.ApiResponse[] {annotation}, null, null, new Components(), null)
            .orElseThrow()
            .get("200");

        assertEquals(Set.of(""), response.getLinks().keySet());
        assertEquals("blankNamedOperation", response.getLinks().get("").getOperationId());
    }

    @Test
    void omitsLegacyLinkServer() throws Exception {
        io.swagger.v3.oas.annotations.responses.ApiResponse annotation = ResponseAnnotationMethods.class
            .getDeclaredMethod("linkedResponse")
            .getAnnotation(io.swagger.v3.oas.annotations.responses.ApiResponse.class);
        ApiResponse response = OperationParser
            .getApiResponses(new io.swagger.v3.oas.annotations.responses.ApiResponse[] {annotation}, null, null, new Components(), null)
            .orElseThrow()
            .get("200");

        assertNotNull(response.getLinks().get("related"));
        assertNull(response.getLinks().get("related").getServer());
    }

    @Test
    void matchesLegacyRequestBodyEmptyAndReferenceRules() throws Exception {
        io.swagger.v3.oas.annotations.parameters.RequestBody empty = RequestBodyMethods.class
            .getDeclaredMethod("empty")
            .getAnnotation(io.swagger.v3.oas.annotations.parameters.RequestBody.class);
        assertTrue(OperationParser.getRequestBody(empty, null, null, new Components(), null).isEmpty());

        io.swagger.v3.oas.annotations.parameters.RequestBody documented = RequestBodyMethods.class
            .getDeclaredMethod("documented")
            .getAnnotation(io.swagger.v3.oas.annotations.parameters.RequestBody.class);
        RequestBody documentedResult = OperationParser
            .getRequestBody(documented, null, null, new Components(), null)
            .orElseThrow();
        assertEquals("Documented input", documentedResult.getDescription());
        assertEquals(Boolean.TRUE, documentedResult.getRequired());

        io.swagger.v3.oas.annotations.parameters.RequestBody reference = RequestBodyMethods.class
            .getDeclaredMethod("reference")
            .getAnnotation(io.swagger.v3.oas.annotations.parameters.RequestBody.class);
        RequestBody referenceResult = OperationParser
            .getRequestBody(reference, null, null, new Components(), null)
            .orElseThrow();
        assertEquals("#/components/requestBodies/OrderInput", referenceResult.get$ref());
        assertNull(referenceResult.getDescription());
        assertNull(referenceResult.getRequired());
    }

    @Test
    void preservesNestedGenericTypesAsDistinctComponents() throws Exception {
        ResolvedSchema resolved = resolve(converters(), fieldType("nested"));

        assertEquals("#/components/schemas/EnvelopePageOrder", resolved.schema.get$ref());
        Schema<?> envelope = component(resolved, "EnvelopePageOrder");
        assertEquals("#/components/schemas/PageOrder", envelope.getProperties().get("payload").get$ref());
        Schema<?> page = component(resolved, "PageOrder");
        assertEquals("array", page.getProperties().get("items").getType());
        assertEquals("#/components/schemas/Order", page.getProperties().get("items").getItems().get$ref());
        component(resolved, "Order");

        ResolvedSchema inherited = resolve(converters(), GenericOrderContainer.class);
        Schema<?> inheritedModel = component(inherited, "GenericOrderContainer");
        assertEquals("#/components/schemas/Order", inheritedModel.getProperties().get("value").get$ref());
        assertEquals("string", inheritedModel.getProperties().get("label").getType());
    }

    @Test
    void capturesMethodGenericReturnTypeThroughFrameworkWrappers() throws Exception {
        Method method = ResponseTypes.class.getDeclaredMethod("futurePage");
        ResolvedSchema resolved = resolve(converters(), method.getGenericReturnType());

        assertEquals("#/components/schemas/PageOrder", resolved.schema.get$ref());
        component(resolved, "PageOrder");
        component(resolved, "Order");
    }

    @Test
    void resolvesCollectionsMapsOptionalsAndArraysWithoutErasingElementTypes() throws Exception {
        ResolvedSchema list = resolve(converters(), fieldType("list"));
        assertEquals("array", list.schema.getType());
        assertEquals("#/components/schemas/Order", list.schema.getItems().get$ref());

        ResolvedSchema set = resolve(converters(), fieldType("set"));
        assertEquals("array", set.schema.getType());
        assertEquals(Boolean.TRUE, set.schema.getUniqueItems());
        assertEquals("string", set.schema.getItems().getType());

        for (String field : List.of("queue", "deque", "collection")) {
            ResolvedSchema collection = resolve(converters(), fieldType(field));
            assertEquals("array", collection.schema.getType());
            assertEquals("#/components/schemas/Order", collection.schema.getItems().get$ref());
        }

        ResolvedSchema nestedContainers = resolve(converters(), fieldType("nestedContainers"));
        assertEquals("array", nestedContainers.schema.getType());
        Schema<?> nestedMap = nestedContainers.schema.getItems();
        assertEquals("object", nestedMap.getType());
        assertInstanceOf(Schema.class, nestedMap.getAdditionalProperties());
        Schema<?> nestedList = (Schema<?>) nestedMap.getAdditionalProperties();
        assertEquals("array", nestedList.getType());
        assertEquals("#/components/schemas/Order", nestedList.getItems().get$ref());

        ResolvedSchema boundedOrders = resolve(converters(), fieldType("boundedOrders"));
        assertEquals("array", boundedOrders.schema.getType());
        assertEquals("#/components/schemas/Order", boundedOrders.schema.getItems().get$ref());

        ResolvedSchema mapOfLists = resolve(converters(), fieldType("mapOfLists"));
        assertInstanceOf(Schema.class, mapOfLists.schema.getAdditionalProperties());
        Schema<?> listValue = (Schema<?>) mapOfLists.schema.getAdditionalProperties();
        assertEquals("array", listValue.getType());
        assertEquals("#/components/schemas/Order", listValue.getItems().get$ref());

        ResolvedSchema map = resolve(converters(), fieldType("map"));
        assertEquals("object", map.schema.getType());
        assertInstanceOf(Schema.class, map.schema.getAdditionalProperties());
        assertEquals("#/components/schemas/Order", ((Schema<?>) map.schema.getAdditionalProperties()).get$ref());

        ResolvedSchema optional = resolve(converters(), fieldType("optional"));
        assertEquals("#/components/schemas/Order", optional.schema.get$ref());

        ResolvedSchema array = resolve(converters(), fieldType("array"));
        assertEquals("array", array.schema.getType());
        assertEquals("#/components/schemas/Order", array.schema.getItems().get$ref());
    }

    @Test
    void resolvesRecursiveModelsUsingStableReferences() throws Exception {
        ResolvedSchema resolved = resolve(converters(), Node.class);
        Schema<?> node = component(resolved, "Node");

        assertEquals("#/components/schemas/Node", node.getProperties().get("parent").get$ref());
        assertEquals("array", node.getProperties().get("children").getType());
        assertEquals("#/components/schemas/Node", node.getProperties().get("children").getItems().get$ref());

        ResolvedSchema generic = resolve(converters(), fieldType("recursiveGeneric"));
        assertEquals("#/components/schemas/GenericNodeOrder", generic.schema.get$ref());
        Schema<?> genericNode = component(generic, "GenericNodeOrder");
        assertEquals("#/components/schemas/Order", genericNode.getProperties().get("value").get$ref());
        assertEquals("#/components/schemas/GenericNodeOrder", genericNode.getProperties().get("next").get$ref());
    }

    @Test
    void resolvesEnumsAndJsonValueWireRepresentations() {
        ResolvedSchema status = resolve(converters(), Status.class);
        assertEquals(List.of("ACTIVE", "DISABLED"), status.schema.getEnum());

        ResolvedSchema wireStatus = resolve(converters(), WireStatus.class);
        assertEquals(List.of("active", "disabled"), wireStatus.schema.getEnum());

        ResolvedSchema numericStatus = resolve(converters(), NumericStatus.class);
        assertEquals("integer", numericStatus.schema.getType());
        assertEquals(List.of(1L, 2L), numericStatus.schema.getEnum());

        ResolvedSchema scalar = resolve(converters(), ScalarValue.class);
        assertEquals("string", scalar.schema.getType());
        assertNull(scalar.referencedSchemas == null ? null : scalar.referencedSchemas.get("ScalarValue"));
    }

    @Test
    void appliesJacksonVisibilityNamesUnwrappedPropertiesAndNamingStrategy() {
        ResolvedSchema annotated = resolve(converters(), AnnotatedContract.class);
        Schema<?> contract = component(annotated, "AnnotatedApiContract");
        assertTrue(contract.getProperties().containsKey("external_name"));
        assertFalse(contract.getProperties().containsKey("internalName"));
        assertFalse(contract.getProperties().containsKey("secret"));
        assertFalse(contract.getProperties().containsKey("hidden"));

        ResolvedSchema unwrapped = resolve(converters(), UnwrappedContract.class);
        Schema<?> flattened = component(unwrapped, "UnwrappedContract");
        assertTrue(flattened.getProperties().containsKey("latitude"));
        assertTrue(flattened.getProperties().containsKey("longitude"));
        assertFalse(flattened.getProperties().containsKey("coordinates"));
        assertTrue(flattened.getRequired() == null || !flattened.getRequired().contains("coordinates"));

        ModelConverters snakeCase = ModelConverters.create(
            JsonMapper.builder().propertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE).build()
        );
        Schema<?> named = component(resolve(snakeCase, NamingContract.class), "NamingContract");
        assertTrue(named.getProperties().containsKey("display_name"));
        assertFalse(named.getProperties().containsKey("displayName"));
    }

    @Test
    void resolvesRecordsAndJsonViews() {
        Schema<?> record = component(resolve(converters(), RecordContract.class), "RecordContract");
        assertEquals("string", record.getProperties().get("name").getType());
        assertEquals("integer", record.getProperties().get("count").getType());

        ResolvedSchema publicView = converters().resolveAsResolvedSchema(
            new AnnotatedType(AnnotatedContract.class)
                .resolveAsRef(true)
                .jsonViewAnnotation(jsonView(Views.Public.class))
        );
        assertEquals("#/components/schemas/AnnotatedApiContract_Public", publicView.schema.get$ref());
        Schema<?> publicContract = component(publicView, "AnnotatedApiContract_Public");
        assertTrue(publicContract.getProperties().containsKey("publicValue"));
        assertFalse(publicContract.getProperties().containsKey("internalValue"));
    }

    private JsonView jsonView(Class<?> view) {
        return new JsonView() {
            @Override
            public Class<?>[] value() {
                return new Class<?>[] {view};
            }

            @Override
            public Class<? extends java.lang.annotation.Annotation> annotationType() {
                return JsonView.class;
            }
        };
    }

    @Test
    void appliesSchemaAndValidationMetadata() {
        ResolvedSchema resolved = resolve(converters(), AnnotatedContract.class);
        assertEquals("#/components/schemas/AnnotatedApiContract", resolved.schema.get$ref());
        Schema<?> contract = component(resolved, "AnnotatedApiContract");

        assertEquals("Annotated title", contract.getTitle());
        assertEquals("Annotated description", contract.getDescription());
        assertTrue(contract.getRequired().contains("external_name"));
        assertTrue(contract.getRequired().contains("explicitRequired"));

        Schema<?> name = contract.getProperties().get("external_name");
        assertEquals("Visible name", name.getDescription());
        assertEquals("Ada", name.getExample());
        assertEquals(2, name.getMinLength());
        assertEquals(40, name.getMaxLength());
        assertEquals("[A-Za-z]+", name.getPattern());

        assertEquals(Boolean.TRUE, contract.getProperties().get("id").getReadOnly());
        assertEquals("uuid", contract.getProperties().get("id").getFormat());
        assertEquals("1", contract.getProperties().get("quantity").getMinimum().toPlainString());
        assertEquals("100", contract.getProperties().get("quantity").getMaximum().toPlainString());

        Schema<?> amount = contract.getProperties().get("amount");
        assertEquals("0.25", amount.getMultipleOf().toPlainString());
        assertEquals("contract", ((Map<?, ?>) amount.getExtensions().get("x-constraint")).get("source"));
        assertNull(amount.getExclusiveMinimum());
        assertNull(amount.getExclusiveMaximum());

        Schema<?> documented = contract.getProperties().get("documented");
        assertNotNull(documented.getExternalDocs());
        assertEquals("Value documentation", documented.getExternalDocs().getDescription());
        assertEquals("https://example.test/value", documented.getExternalDocs().getUrl());

        Schema<?> rank = contract.getProperties().get("rank");
        assertEquals(3, rank.getExample());
        assertEquals(2, rank.getDefault());

        Schema<?> notString = contract.getProperties().get("notString");
        assertEquals("object", notString.getType());
        assertNull(notString.getNot());
    }

    @Test
    void preservesLegacyPolymorphismAndInheritanceShape() {
        ResolvedSchema resolved = resolve(converters(), Animal.class);
        Schema<?> animal = component(resolved, "Animal");

        assertNotNull(animal.getDiscriminator());
        assertEquals("kind", animal.getDiscriminator().getPropertyName());
        assertNull(animal.getDiscriminator().getMapping());
        assertNull(animal.getOneOf());
        assertEquals("string", animal.getProperties().get("kind").getType());
        assertEquals("string", animal.getProperties().get("name").getType());
        assertTrue(animal.getRequired().contains("kind"));

        Schema<?> dog = component(resolved, "Dog");
        Schema<?> cat = component(resolved, "Cat");
        assertEquals("object", dog.getType());
        assertEquals("object", cat.getType());
        assertEquals("#/components/schemas/Animal", dog.getAllOf().getFirst().get$ref());
        assertEquals("#/components/schemas/Animal", cat.getAllOf().getFirst().get$ref());
        Schema<?> dogChild = (Schema<?>) dog.getAllOf().get(1);
        Schema<?> catChild = (Schema<?>) cat.getAllOf().get(1);
        assertEquals("boolean", dogChild.getProperties().get("barks").getType());
        assertEquals("integer", catChild.getProperties().get("lives").getType());
        assertNull(dog.getProperties());
        assertNull(cat.getProperties());
    }

    @Test
    void mapsStandardScalarFormatsAndBinaryTypes() throws Exception {
        ModelConverters converters = converters();
        assertPropertyFormat(converters, "uuid", "string", "uuid");
        assertPropertyFormat(converters, "uri", "string", "uri");
        assertPropertyFormat(converters, "url", "string", "url");
        assertPropertyFormat(converters, "date", "string", "date");
        assertPropertyFormat(converters, "time", "string", "partial-time");
        assertPropertyFormat(converters, "instant", "string", "date-time");
        assertPropertyFormat(converters, "offsetDateTime", "string", "date-time");
        assertPropertyFormat(converters, "file", "string", "binary");
        assertPropertyFormat(converters, "buffer", "string", "binary");
        assertPropertyFormat(converters, "bytes", "string", "byte");
    }

    @Test
    void preservesLegacyFormParameterExtraction() throws Exception {
        Method method = ParameterMethods.class.getDeclaredMethod("form", String.class);
        ResolvedParameter resolved = new ServerParameterExtension().extractParameters(
            List.of(method.getParameterAnnotations()[0]),
            method.getGenericParameterTypes()[0],
            new java.util.HashSet<>(),
            new Components(),
            null,
            null,
            true,
            null,
            java.util.Collections.emptyIterator()
        );

        assertTrue(resolved.parameters.isEmpty());
        assertNull(resolved.requestBody);
        assertEquals(1, resolved.formParameters.size());
        io.sinistral.proteus.openapi.models.parameters.Parameter parameter = resolved.formParameters.getFirst();
        assertEquals("name", parameter.getName());
        assertNull(parameter.getIn());
        assertEquals("string", parameter.getSchema().getType());
    }

    @Test
    void appliesLegacyDefaultValueToParameterSchema() throws Exception {
        Method method = ParameterMethods.class.getDeclaredMethod("defaulted", Integer.class);
        ResolvedParameter resolved = new ServerParameterExtension().extractParameters(
            List.of(method.getParameterAnnotations()[0]),
            method.getGenericParameterTypes()[0],
            new java.util.HashSet<>(),
            new Components(),
            null,
            null,
            true,
            null,
            java.util.Collections.emptyIterator()
        );

        assertEquals(1, resolved.parameters.size());
        io.sinistral.proteus.openapi.models.parameters.Parameter parameter = resolved.parameters.getFirst();
        assertEquals("limit", parameter.getName());
        assertEquals("query", parameter.getIn());
        assertEquals(Boolean.TRUE, parameter.getRequired());
        assertEquals("integer", parameter.getSchema().getType());
        assertEquals("int32", parameter.getSchema().getFormat());
        assertEquals(5, ((Number) parameter.getSchema().getDefault()).intValue());
    }

    @Test
    void omitsLegacyContextParameters() throws Exception {
        for (Method method : List.of(
            ContextMethods.class.getDeclaredMethod("contextual", jakarta.ws.rs.core.UriInfo.class),
            ContextMethods.class.getDeclaredMethod("contextualString", String.class)
        )) {
            ResolvedParameter resolved = new ServerParameterExtension().extractParameters(
                List.of(method.getParameterAnnotations()[0]),
                method.getGenericParameterTypes()[0],
                new java.util.HashSet<>(),
                new Components(),
                null,
                null,
                true,
                null,
                java.util.Collections.emptyIterator()
            );

            assertTrue(resolved.parameters.isEmpty());
            assertTrue(resolved.formParameters.isEmpty());
            assertNull(resolved.requestBody);
        }
    }

    @Test
    void preservesLegacyBeanParameterOmission() throws Exception {
        OpenAPIExtensions.register(new ServerParameterExtension());
        Method method = ParameterMethods.class.getDeclaredMethod("bean", BeanParameters.class);
        ResolvedParameter resolved = new ServerParameterExtension().extractParameters(
            List.of(method.getParameterAnnotations()[0]),
            method.getGenericParameterTypes()[0],
            new java.util.HashSet<>(),
            new Components(),
            null,
            null,
            true,
            null,
            java.util.Collections.emptyIterator()
        );

        assertTrue(resolved.parameters.isEmpty());
        assertTrue(resolved.formParameters.isEmpty());
        assertNotNull(resolved.requestBody);
        assertEquals("#/components/schemas/BeanParameters", resolved.requestBody.getSchema().get$ref());
    }

    @Test
    void serializesSpecialEntriesFromMapBackedModels() throws Exception {
        tools.jackson.databind.JsonNode callback = new tools.jackson.databind.ObjectMapper().readTree(
            Json.pretty(new Callback().$ref("#/components/callbacks/Hook"))
        );
        assertEquals("#/components/callbacks/Hook", callback.get("$ref").asText());

        tools.jackson.databind.JsonNode responses = new tools.jackson.databind.ObjectMapper().readTree(
            Json.pretty(new ApiResponses()._default(new ApiResponse().description("default")))
        );
        assertEquals("default", responses.at("/default/description").asText());

        Paths paths = new Paths();
        paths.addExtension("x-test", true);
        tools.jackson.databind.ObjectMapper objectMapper = new tools.jackson.databind.ObjectMapper();
        tools.jackson.databind.JsonNode serializedPaths = objectMapper.readTree(Json.pretty(paths));
        assertTrue(serializedPaths.get("x-test").asBoolean());

        Paths parsedPaths = objectMapper.readValue(
            "{\"/orders\":{},\"x-owner\":\"proteus\"}",
            Paths.class
        );
        assertTrue(parsedPaths.containsKey("/orders"));
        assertEquals("proteus", parsedPaths.getExtensions().get("x-owner"));

        Callback parsedCallback = objectMapper.readValue(
            "{\"$ref\":\"#/components/callbacks/Hook\",\"x-owner\":\"proteus\"}",
            Callback.class
        );
        assertEquals("#/components/callbacks/Hook", parsedCallback.get$ref());
        assertEquals("proteus", parsedCallback.getExtensions().get("x-owner"));

        ApiResponses extendedResponses = objectMapper.readValue(
            "{\"default\":{\"description\":\"fallback\"},\"x-owner\":\"proteus\"}",
            ApiResponses.class
        );
        assertEquals("fallback", extendedResponses.getDefault().getDescription());
        assertEquals("proteus", extendedResponses.getExtensions().get("x-owner"));
        tools.jackson.databind.JsonNode serializedResponses = objectMapper.readTree(Json.pretty(extendedResponses));
        assertEquals("proteus", serializedResponses.get("x-owner").asText());
    }

    private void assertPropertyFormat(ModelConverters converters, String field, String type, String format) throws Exception {
        Type fieldType = ScalarTypes.class.getDeclaredField(field).getGenericType();
        Schema<?> schema = resolve(converters, fieldType).schema;
        assertEquals(type, schema.getType(), field + " type");
        assertEquals(format, schema.getFormat(), field + " format");
    }
}
