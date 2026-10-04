/**
 *
 */
package io.sinistral.proteus.server;

import com.google.inject.Inject;
import io.sinistral.proteus.server.predicates.ServerPredicates;
import io.sinistral.proteus.utilities.DataUtilities;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.handlers.form.FormData;
import io.undertow.server.handlers.form.FormDataParser;
import io.undertow.util.HeaderValues;
import io.undertow.util.Headers;
import io.undertow.util.HttpString;
import io.undertow.util.Methods;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.JavaType;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.dataformat.xml.XmlMapper;

/**
 * Static extraction utilities for controller parameter binding on an {@link HttpServerExchange}.
 *
 * <p>Two parallel APIs exist: the required variants throw {@link IllegalArgumentException} when a
 * value is missing, and the nested {@link Optional} variants return {@link java.util.Optional}
 * instead. Values are pulled from query parameters, request headers, form data, or the buffered
 * request body, with JSON and XML deserialization through the injected mappers.
 *
 * @author jbauer
 */
public class Extractors {

    /** Default constructor for static-only use. */
    public Extractors() {}

    /** The log. */
    private static Logger log = LoggerFactory.getLogger(
        Extractors.class.getCanonicalName()
    );

    /** The xml_pattern. */
    private static final Pattern XML_PATTERN = Pattern.compile(
        "^(application/(xml|xhtml\\+xml)|text/xml)(;.*)?$",
        Pattern.CASE_INSENSITIVE
    );

    /** The json_pattern. */
    private static final Pattern JSON_PATTERN = Pattern.compile(
        "^(application/(json|x-javascript)|text/(json|x-javascript|x-json))(;.*)?$",
        Pattern.CASE_INSENSITIVE
    );

    /** The java_type_map. */
    private static final Map<Type, JavaType> JAVA_TYPE_MAP =
        new ConcurrentHashMap<>();

    /** The xml_mapper. */
    @Inject
    private static XmlMapper XML_MAPPER;

    /** The object_mapper. */
    @Inject
    private static ObjectMapper OBJECT_MAPPER;

    private static JsonNode parseJson(byte[] bytes) {
        try {
            return OBJECT_MAPPER.readTree(bytes);
        } catch (Exception e) {
            log.error("Failed to parse JSON", e);
            return null;
        }
    }

    private static <T> T parseTypedJson(final Class<T> type, byte[] bytes) {
        try {
            return OBJECT_MAPPER.readValue(bytes, type);
        } catch (Exception e) {
            log.error("Failed to parse JSON for type {}", type, e);
            return null;
        }
    }

    private static <T> T parseTypedJson(
        final TypeReference<T> type,
        byte[] bytes
    ) {
        try {
            final Type _rawType = type.getType();

            JavaType _javaType = JAVA_TYPE_MAP.get(_rawType);

            if (_javaType == null) {
                _javaType = OBJECT_MAPPER.getTypeFactory().constructType(
                    _rawType
                );
                JAVA_TYPE_MAP.put(_rawType, _javaType);
            }

            return OBJECT_MAPPER.readValue(bytes, _javaType);
        } catch (Exception e) {
            log.error("Failed to parse JSON for type {}", type, e);
            return null;
        }
    }

    private static <T> T parseTypedXML(final Class<T> type, byte[] bytes) {
        try {
            return XML_MAPPER.readValue(bytes, type);
        } catch (Exception e) {
            log.error("Failed to parse XML for type {}", type, e);
            return null;
        }
    }

    private static <T> T parseTypedXML(
        final TypeReference<T> type,
        byte[] bytes
    ) {
        try {
            final Type _rawType = type.getType();

            JavaType _javaType = JAVA_TYPE_MAP.get(_rawType);

            if (_javaType == null) {
                _javaType = OBJECT_MAPPER.getTypeFactory().constructType(
                    _rawType
                );
                JAVA_TYPE_MAP.put(_rawType, _javaType);
            }

            return XML_MAPPER.readValue(bytes, _javaType);
        } catch (Exception e) {
            log.error("Failed to parse XML for type {}", type, e);
            return null;
        }
    }

    private static Path formValueFileItemPath(FormData.FileItem fileItem) {
        if (fileItem.isInMemory()) {
            try {
                Path path = Files.createTempFile("proteus", "upload");

                path.toFile().deleteOnExit();

                DataUtilities.writeStreamToPath(
                    fileItem.getInputStream(),
                    path
                );

                return path;
            } catch (Exception e) {
                log.error("Failed to create temporary file for form item", e);
                return Paths.get(System.getProperty("java.io.tmpdir"));
            }
        } else {
            return fileItem.getFile();
        }
    }

    private static java.util.Optional<Path> formValueFilePath(
        final HttpServerExchange exchange,
        final String name
    ) {
        return formValueFileItem(exchange, name).map(
            Extractors::formValueFileItemPath
        );
    }

    private static java.util.Optional<Stream<Path>> formValueFilePaths(
        final HttpServerExchange exchange,
        final String name
    ) {
        return formValueFileItems(exchange, name).map(items ->
            items.map(Extractors::formValueFileItemPath)
        );
    }

    private static java.util.Optional<Map<String, Path>> formValuePathMap(
        final HttpServerExchange exchange,
        final String name
    ) {
        return java.util.Optional.ofNullable(
            exchange.getAttachment(FormDataParser.FORM_DATA).get(name)
        ).map(deque ->
            deque
                .stream()
                .filter(fv -> fv.getFileItem() != null)
                .collect(
                    Collectors.toMap(FormData.FormValue::getFileName, fv ->
                        formValueFileItemPath(fv.getFileItem())
                    )
                )
        );
    }

    private static java.util.Optional<Map<String, File>> formValueFileMap(
        final HttpServerExchange exchange,
        final String name
    ) {
        return java.util.Optional.ofNullable(
            exchange.getAttachment(FormDataParser.FORM_DATA).get(name)
        ).map(deque ->
            deque
                .stream()
                .filter(fv -> fv.getFileItem() != null)
                .collect(
                    Collectors.toMap(FormData.FormValue::getFileName, fv ->
                        formValueFileItemPath(fv.getFileItem()).toFile()
                    )
                )
        );
    }

    private static java.util.Optional<ByteBuffer> formValueBuffer(
        final HttpServerExchange exchange,
        final String name
    ) {
        FormData formData = exchange.getAttachment(FormDataParser.FORM_DATA);

        //      //
        //
        //        for (String s : formData) {
        //            //
        //
        //            Deque<FormData.FormValue> deque = formData.get(s);
        //
        //            var values = deque.stream().map(v -> {
        //
        //
        //                if (v.isFileItem()) {
        //                    return Map.of("headers", v.getHeaders(), "fileItem", v.getFileItem(), "fileName", v.getFileName());
        //                } else {
        //                    return Map.of("headers", v.getHeaders(), "value", v.getValue());
        //                }
        //
        //
        //            }).collect(Collectors.toList());
        //
        //            //
        //
        //        }

        return java.util.Optional.ofNullable(formData.get(name))
            .map(Deque::getFirst)
            .map(fi -> {
                try {
                    if (fi.isFileItem()) {
                        FormData.FileItem fileItem = fi.getFileItem();

                        log.trace(
                            "fileItem: {} {} ",
                            fileItem,
                            fileItem.getFile()
                        );

                        if (fileItem.isInMemory()) {
                            log.trace(
                                "fileItem: {} is in memory {}",
                                fileItem,
                                fileItem.getFileSize()
                            );
                        }
                        return DataUtilities.fileItemToBuffer(fileItem);
                    }
                } catch (Exception e) {
                    log.error(
                        "Failed to parse buffer for field name {}",
                        name,
                        e
                    );
                }

                return null;
            });
    }

    private static java.util.Optional<FormData.FormValue> formValue(
        final HttpServerExchange exchange,
        final String name
    ) {
        return java.util.Optional.ofNullable(
            exchange.getAttachment(FormDataParser.FORM_DATA).get(name)
        ).map(Deque::getFirst);
    }

    private static java.util.Optional<FormData.FileItem> formValueFileItem(
        final HttpServerExchange exchange,
        final String name
    ) {
        return java.util.Optional.ofNullable(
            exchange.getAttachment(FormDataParser.FORM_DATA).get(name)
        )
            .map(Deque::getFirst)
            .map(FormData.FormValue::getFileItem);
    }

    private static java.util.Optional<
        Stream<FormData.FileItem>
    > formValueFileItems(final HttpServerExchange exchange, final String name) {
        return java.util.Optional.ofNullable(
            exchange.getAttachment(FormDataParser.FORM_DATA).get(name)
        ).map(deque -> deque.stream().map(FormData.FormValue::getFileItem));
    }

    private static <T> T formValueModel(
        final FormData.FormValue formValue,
        final TypeReference<T> type,
        final String name
    ) {
        if (
            formValue.getHeaders().get(Headers.CONTENT_TYPE) != null &&
            XML_PATTERN.matcher(
                formValue.getHeaders().getFirst(Headers.CONTENT_TYPE)
            ).matches()
        ) {
            if (formValue.isFileItem()) {
                try {
                    ByteBuffer byteBuffer = DataUtilities.fileItemToBuffer(
                        formValue.getFileItem()
                    );

                    return parseTypedXML(type, byteBuffer.array());
                } catch (Exception e) {
                    log.error("Failed to parse buffered XML for {}", name, e);
                    return null;
                }
            } else {
                try {
                    return parseTypedXML(type, formValue.getValue().getBytes());
                } catch (Exception e) {
                    log.error("Failed to parse XML for {}", name, e);
                    return null;
                }
            }
        } else if (
            formValue.getHeaders().get(Headers.CONTENT_TYPE) == null ||
            (formValue.getHeaders().get(Headers.CONTENT_TYPE) != null &&
                JSON_PATTERN.matcher(
                    formValue.getHeaders().getFirst(Headers.CONTENT_TYPE)
                ).matches())
        ) {
            if (formValue.isFileItem()) {
                try {
                    ByteBuffer byteBuffer = DataUtilities.fileItemToBuffer(
                        formValue.getFileItem()
                    );

                    return parseTypedJson(type, byteBuffer.array());
                } catch (Exception e) {
                    log.error("Failed to parse buffered json for {}", name, e);
                    return null;
                }
            } else {
                return parseTypedJson(type, formValue.getValue().getBytes());
            }
        } else {
            log.warn("FormValue for {} is not a file item", name);
            return null;
        }
    }

    private static <T> T formValueModel(
        final FormData.FormValue formValue,
        final Class<T> type,
        final String name
    ) {
        if (
            formValue.getHeaders().get(Headers.CONTENT_TYPE) != null &&
            XML_PATTERN.matcher(
                formValue.getHeaders().getFirst(Headers.CONTENT_TYPE)
            ).matches()
        ) {
            if (formValue.isFileItem()) {
                try {
                    ByteBuffer byteBuffer = DataUtilities.fileItemToBuffer(
                        formValue.getFileItem()
                    );

                    return parseTypedXML(type, byteBuffer.array());
                } catch (Exception e) {
                    log.error("Failed to parse buffered XML for {}", name, e);
                    return null;
                }
            } else {
                try {
                    return parseTypedXML(type, formValue.getValue().getBytes());
                } catch (Exception e) {
                    log.error("Failed to parse XML for {}", name, e);
                    return null;
                }
            }
        } else if (
            formValue.getHeaders().get(Headers.CONTENT_TYPE) == null ||
            (formValue.getHeaders().get(Headers.CONTENT_TYPE) != null &&
                JSON_PATTERN.matcher(
                    formValue.getHeaders().getFirst(Headers.CONTENT_TYPE)
                ).matches())
        ) {
            if (formValue.isFileItem()) {
                try {
                    ByteBuffer byteBuffer = DataUtilities.fileItemToBuffer(
                        formValue.getFileItem()
                    );

                    return parseTypedJson(type, byteBuffer.array());
                } catch (Exception e) {
                    log.error("Failed to parse buffered json for {}", name, e);
                    return null;
                }
            } else {
                return parseTypedJson(type, formValue.getValue().getBytes());
            }
        } else {
            log.warn("FormValue for {} is not a file item", name);
            return null;
        }
    }

    /** Extractors for request header parameters. */
    public static class Header {

        /** Utility class, not instantiated. */
        public Header() {}

        /**
         * Returns the first value of the named request header.
         *
         * @param exchange the current server exchange
         * @param name the header name
         * @return the first header value
         * @throws IllegalArgumentException when the header is absent
         */
        public static String string(
            final HttpServerExchange exchange,
            final String name
        ) throws IllegalArgumentException {
            return java.util.Optional.ofNullable(
                exchange.getRequestHeaders().get(name)
            )
                .map(HeaderValues::getFirst)
                .orElseThrow(() ->
                    new IllegalArgumentException("Invalid parameter " + name)
                );
        }

        /** Extractors for optional request header parameters. */
        public static class Optional {

            /** Utility class, not instantiated. */
            public Optional() {}

            /**
             * Returns the first value of the named request header.
             *
             * @param exchange the current server exchange
             * @param name the header name
             * @return the first header value, or empty when the header is absent
             */
            public static java.util.Optional<String> string(
                final HttpServerExchange exchange,
                final String name
            ) {
                return java.util.Optional.ofNullable(
                    exchange.getRequestHeaders().get(name)
                ).map(HeaderValues::getFirst);
            }
        }
    }

    /** Extractors that return {@link java.util.Optional} instead of throwing on missing values. */
    public static class Optional {

        /** Utility class, not instantiated. */
        public Optional() {}

        /**
         * Extracts a named query parameter and converts it with the given function.
         *
         * @param <T> the converted value type
         * @param exchange the current server exchange
         * @param name the query parameter name
         * @param function the string conversion function
         * @return the converted value, or empty when the parameter is absent
         */
        public static <T> java.util.Optional<T> extractWithFunction(
            final HttpServerExchange exchange,
            final String name,
            Function<String, T> function
        ) {
            return string(exchange, name).map(function);
        }

        /**
         * Parses the buffered request body as a JSON tree.
         *
         * @param exchange the current server exchange
         * @return the parsed tree, or empty when no body is buffered
         */
        public static java.util.Optional<JsonNode> namedJsonNode(
            final HttpServerExchange exchange
        ) {
            return jsonModel(exchange, JsonNode.class);
        }

        /**
         * Parses a named form value as a JSON tree.
         *
         * @param exchange the current server exchange
         * @param name the form field name
         * @return the parsed tree, or empty when the field is absent
         */
        public static java.util.Optional<JsonNode> namedJsonNode(
            final HttpServerExchange exchange,
            final String name
        ) {
            return formValue(exchange, name).map(fv ->
                formValueModel(fv, JsonNode.class, name)
            );
        }

        /**
         * Parses the buffered request body as XML or JSON per the content type.
         *
         * @param <T> the model type
         * @param exchange the current server exchange
         * @param type the target type reference
         * @return the parsed model, or empty when no body is buffered
         */
        public static <T> java.util.Optional<T> model(
            final HttpServerExchange exchange,
            final TypeReference<T> type
        ) {
            if (ServerPredicates.XML_PREDICATE.resolve(exchange)) {
                return xmlModel(exchange, type);
            } else {
                return jsonModel(exchange, type);
            }
        }

        /**
         * Parses the buffered request body as XML or JSON per the content type.
         *
         * @param <T> the model type
         * @param exchange the current server exchange
         * @param type the target class
         * @return the parsed model, or empty when no body is buffered
         */
        public static <T> java.util.Optional<T> model(
            final HttpServerExchange exchange,
            final Class<T> type
        ) {
            if (ServerPredicates.XML_PREDICATE.resolve(exchange)) {
                return xmlModel(exchange, type);
            } else {
                return jsonModel(exchange, type);
            }
        }

        /**
         * Parses a named form value as a model of the given class.
         *
         * @param <T> the model type
         * @param exchange the current server exchange
         * @param type the target class
         * @param name the form field name
         * @return the parsed model, or empty when the field is absent
         */
        public static <T> java.util.Optional<T> namedModel(
            final HttpServerExchange exchange,
            final Class<T> type,
            final String name
        ) {
            return formValue(exchange, name).map(fv ->
                formValueModel(fv, type, name)
            );
        }

        /**
         * Parses a named form value as a model of the given type reference.
         *
         * @param <T> the model type
         * @param exchange the current server exchange
         * @param type the target type reference
         * @param name the form field name
         * @return the parsed model, or empty when the field is absent
         */
        public static <T> java.util.Optional<T> namedModel(
            final HttpServerExchange exchange,
            final TypeReference<T> type,
            final String name
        ) {
            return formValue(exchange, name).map(fv ->
                formValueModel(fv, type, name)
            );
        }

        /**
         * Parses the buffered request body as JSON.
         *
         * @param <T> the model type
         * @param exchange the current server exchange
         * @param type the target type reference
         * @return the parsed model, or empty when no body is buffered
         */
        public static <T> java.util.Optional<T> jsonModel(
            final HttpServerExchange exchange,
            final TypeReference<T> type
        ) {
            return java.util.Optional.ofNullable(
                exchange.getAttachment(ServerRequest.BYTE_BUFFER_KEY)
            )
                .map(ByteBuffer::array)
                .map(b -> parseTypedJson(type, b));
        }

        /**
         * Parses the buffered request body as JSON.
         *
         * @param <T> the model type
         * @param exchange the current server exchange
         * @param type the target class
         * @return the parsed model, or empty when no body is buffered
         */
        public static <T> java.util.Optional<T> jsonModel(
            final HttpServerExchange exchange,
            final Class<T> type
        ) {
            return java.util.Optional.ofNullable(
                exchange.getAttachment(ServerRequest.BYTE_BUFFER_KEY)
            )
                .map(ByteBuffer::array)
                .map(b -> parseTypedJson(type, b));
        }

        /**
         * Parses the buffered request body as XML.
         *
         * @param <T> the model type
         * @param exchange the current server exchange
         * @param type the target type reference
         * @return the parsed model, or empty when no body is buffered
         */
        public static <T> java.util.Optional<T> xmlModel(
            final HttpServerExchange exchange,
            final TypeReference<T> type
        ) {
            return java.util.Optional.ofNullable(
                exchange.getAttachment(ServerRequest.BYTE_BUFFER_KEY)
            )
                .map(ByteBuffer::array)
                .map(b -> parseTypedXML(type, b));
        }

        /**
         * Parses the buffered request body as XML.
         *
         * @param <T> the model type
         * @param exchange the current server exchange
         * @param type the target class
         * @return the parsed model, or empty when no body is buffered
         */
        public static <T> java.util.Optional<T> xmlModel(
            final HttpServerExchange exchange,
            final Class<T> type
        ) {
            return java.util.Optional.ofNullable(
                exchange.getAttachment(ServerRequest.BYTE_BUFFER_KEY)
            )
                .map(ByteBuffer::array)
                .map(b -> parseTypedXML(type, b));
        }

        /**
         * Returns a named query parameter parsed as an {@link OffsetDateTime} date.
         *
         * @param exchange the current server exchange
         * @param name the query parameter name
         * @return the parsed date, or empty when absent
         */
        public static java.util.Optional<Date> date(
            final HttpServerExchange exchange,
            final String name
        ) {
            return string(exchange, name)
                .map(OffsetDateTime::parse)
                .map(OffsetDateTime::toInstant)
                .map(Date::from);
        }

        /**
         * Creates an instance from fset date time.
         *
         * @param exchange the current server exchange
         * @param name the query parameter name
         * @return the parsed value, or empty when absent
         */
        public static java.util.Optional<OffsetDateTime> offsetDateTime(
            final HttpServerExchange exchange,
            final String name
        ) {
            return string(exchange, name).map(OffsetDateTime::parse);
        }

        /**
         * Returns a named query parameter parsed as a {@link ZonedDateTime}.
         *
         * @param exchange the current server exchange
         * @param name the query parameter name
         * @return the parsed value, or empty when absent
         */
        public static java.util.Optional<ZonedDateTime> zonedDateTime(
            final HttpServerExchange exchange,
            final String name
        ) {
            return string(exchange, name).map(ZonedDateTime::parse);
        }

        /**
         * Returns a named query parameter parsed as an {@link Instant}.
         *
         * @param exchange the current server exchange
         * @param name the query parameter name
         * @return the parsed value, or empty when absent
         */
        public static java.util.Optional<Instant> instant(
            final HttpServerExchange exchange,
            final String name
        ) {
            return string(exchange, name).map(Instant::parse);
        }

        /**
         * Returns a named query parameter parsed as an {@link Integer}.
         *
         * @param exchange the current server exchange
         * @param name the query parameter name
         * @return the parsed value, or empty when absent
         */
        public static java.util.Optional<Integer> integerValue(
            final HttpServerExchange exchange,
            final String name
        ) {
            return string(exchange, name).map(Integer::parseInt);
        }

        /**
         * Returns a named query parameter parsed as a {@link Short}.
         *
         * @param exchange the current server exchange
         * @param name the query parameter name
         * @return the parsed value, or empty when absent
         */
        public static java.util.Optional<Short> shortValue(
            final HttpServerExchange exchange,
            final String name
        ) {
            return string(exchange, name).map(Short::parseShort);
        }

        /**
         * Returns a named query parameter parsed as a {@link Float}.
         *
         * @param exchange the current server exchange
         * @param name the query parameter name
         * @return the parsed value, or empty when absent
         */
        public static java.util.Optional<Float> floatValue(
            final HttpServerExchange exchange,
            final String name
        ) {
            return string(exchange, name).map(Float::parseFloat);
        }

        /**
         * Returns a named query parameter parsed as a {@link Double}.
         *
         * @param exchange the current server exchange
         * @param name the query parameter name
         * @return the parsed value, or empty when absent
         */
        public static java.util.Optional<Double> doubleValue(
            final HttpServerExchange exchange,
            final String name
        ) {
            return string(exchange, name).map(Double::parseDouble);
        }

        /**
         * Returns a named query parameter parsed as a {@link BigDecimal}.
         *
         * @param exchange the current server exchange
         * @param name the query parameter name
         * @return the parsed value, or empty when absent
         */
        public static java.util.Optional<BigDecimal> bigDecimalValue(
            final HttpServerExchange exchange,
            final String name
        ) {
            return string(exchange, name).map(BigDecimal::new);
        }

        /**
         * Returns a named query parameter parsed as a {@link Long}.
         *
         * @param exchange the current server exchange
         * @param name the query parameter name
         * @return the parsed value, or empty when absent
         */
        public static java.util.Optional<Long> longValue(
            final HttpServerExchange exchange,
            final String name
        ) {
            return string(exchange, name).map(Long::parseLong);
        }

        /**
         * Returns a named query parameter parsed as a {@link Boolean}.
         *
         * @param exchange the current server exchange
         * @param name the query parameter name
         * @return the parsed value, or empty when absent
         */
        public static java.util.Optional<Boolean> booleanValue(
            final HttpServerExchange exchange,
            final String name
        ) {
            return string(exchange, name).map(Boolean::parseBoolean);
        }

        //		public static  <E extends Enum<E>> java.util.Optional<E> enumValue(final HttpServerExchange exchange, final Class<E> clazz, final String name)
        //		{
        //			return string(exchange, name).map(e -> Enum.valueOf(clazz, name));
        //		}

        /**
         * Returns a named query parameter.
         *
         * @param exchange the current server exchange
         * @param name the query parameter name
         * @return the first value, or empty when absent
         */
        public static java.util.Optional<String> string(
            final HttpServerExchange exchange,
            final String name
        ) {
            return java.util.Optional.ofNullable(
                exchange.getQueryParameters().get(name)
            ).map(Deque::getFirst);
        }

        /**
         * Returns a named form file value as a path.
         *
         * @param exchange the current server exchange
         * @param name the form field name
         * @return the file path, or empty when absent; in-memory items are spilled to a temp file
         */
        public static java.util.Optional<Path> filePath(
            final HttpServerExchange exchange,
            final String name
        ) {
            return formValueFilePath(exchange, name);
        }

        /**
         * Returns a named form file value as a {@link File}.
         *
         * @param exchange the current server exchange
         * @param name the form field name
         * @return the file, or empty when absent
         */
        public static java.util.Optional<File> file(
            final HttpServerExchange exchange,
            final String name
        ) {
            return formValueFilePath(exchange, name).map(Path::toFile);
        }

        /**
         * Returns the buffered request body.
         *
         * @param exchange the current server exchange
         * @return the body buffer, or empty when absent
         * @throws IOException when the buffer cannot be read
         */
        public static java.util.Optional<ByteBuffer> byteBuffer(
            final HttpServerExchange exchange
        ) throws IOException {
            return java.util.Optional.ofNullable(
                exchange.getAttachment(ServerRequest.BYTE_BUFFER_KEY)
            );
        }

        /**
         * Returns a named form file value as a byte buffer.
         *
         * @param exchange the current server exchange
         * @param name the form field name
         * @return the buffered file content, or empty when absent
         * @throws IOException when the content cannot be read
         */
        public static java.util.Optional<ByteBuffer> namedByteBuffer(
            final HttpServerExchange exchange,
            final String name
        ) throws IOException {
            return formValueBuffer(exchange, name);
        }
    }

    /**
     * Returns a named query parameter parsed as an {@link OffsetDateTime} date.
     *
     * @param exchange the current server exchange
     * @param name the query parameter name
     * @return the parsed date
     * @throws IllegalArgumentException when the parameter is absent or unparseable
     */
    public static Date date(
        final HttpServerExchange exchange,
        final String name
    ) throws IllegalArgumentException {
        return Date.from(
            OffsetDateTime.parse(string(exchange, name)).toInstant()
        );
    }

    /**
     * Returns a named query parameter parsed as a {@link ZonedDateTime}.
     *
     * @param exchange the current server exchange
     * @param name the query parameter name
     * @return the parsed value
     * @throws IllegalArgumentException when the parameter is absent or unparseable
     */
    public static ZonedDateTime zonedDateTime(
        final HttpServerExchange exchange,
        final String name
    ) throws IllegalArgumentException {
        return ZonedDateTime.parse(string(exchange, name));
    }

    /**
     * Creates an instance from fset date time.
     *
     * @param exchange the current server exchange
     * @param name the query parameter name
     * @return the parsed value
     * @throws IllegalArgumentException when the parameter is absent or unparseable
     */
    public static OffsetDateTime offsetDateTime(
        final HttpServerExchange exchange,
        final String name
    ) throws IllegalArgumentException {
        return OffsetDateTime.parse(string(exchange, name));
    }

    /**
     * Returns a named form file value as a path.
     *
     * @param exchange the current server exchange
     * @param name the form field name
     * @return the file path; in-memory items are spilled to a temp file
     * @throws IllegalArgumentException when the field is absent
     */
    public static Path filePath(
        final HttpServerExchange exchange,
        final String name
    ) throws IllegalArgumentException {
        return formValueFilePath(exchange, name).orElseThrow(() ->
            new IllegalArgumentException("Invalid parameter " + name)
        );
    }

    /**
     * Returns every form file value of a field as a list of paths.
     *
     * @param exchange the current server exchange
     * @param name the form field name
     * @return the file paths, empty when the field is absent
     * @throws IllegalArgumentException never; retained for generated-code symmetry
     */
    public static List<Path> pathList(
        final HttpServerExchange exchange,
        final String name
    ) throws IllegalArgumentException {
        return formValueFilePaths(exchange, name)
            .map(s -> s.collect(Collectors.toList()))
            .orElse(new ArrayList<>());
    }

    /**
     * Returns every form file value of a field as a list of files.
     *
     * @param exchange the current server exchange
     * @param name the form field name
     * @return the files, empty when the field is absent
     * @throws IllegalArgumentException never; retained for generated-code symmetry
     */
    public static List<File> fileList(
        final HttpServerExchange exchange,
        final String name
    ) throws IllegalArgumentException {
        return formValueFilePaths(exchange, name)
            .map(s -> s.map(Path::toFile).collect(Collectors.toList()))
            .orElse(new ArrayList<>());
    }

    /**
     * Returns every form file value of a field mapped by file name.
     *
     * @param exchange the current server exchange
     * @param name the form field name
     * @return the file name to path map, empty when the field is absent
     * @throws IllegalArgumentException never; retained for generated-code symmetry
     */
    public static Map<String, Path> pathMap(
        final HttpServerExchange exchange,
        final String name
    ) throws IllegalArgumentException {
        return formValuePathMap(exchange, name).orElse(new HashMap<>());
    }

    /**
     * Returns every form file value of a field mapped by file name.
     *
     * @param exchange the current server exchange
     * @param name the form field name
     * @return the file name to file map, empty when the field is absent
     * @throws IllegalArgumentException never; retained for generated-code symmetry
     */
    public static Map<String, File> fileMap(
        final HttpServerExchange exchange,
        final String name
    ) throws IllegalArgumentException {
        return formValueFileMap(exchange, name).orElse(new HashMap<>());
    }

    /**
     * Returns a named form file value as a {@link File}.
     *
     * @param exchange the current server exchange
     * @param name the form field name
     * @return the file
     * @throws IllegalArgumentException when the field is absent
     */
    public static File file(
        final HttpServerExchange exchange,
        final String name
    ) throws IllegalArgumentException {
        return formValueFilePath(exchange, name)
            .map(Path::toFile)
            .orElseThrow(() ->
                new IllegalArgumentException("Invalid parameter " + name)
            );
    }

    /**
     * Returns the buffered request body.
     *
     * @param exchange the current server exchange
     * @return the body buffer, or null when absent
     * @throws IOException when the buffer cannot be read
     */
    public static ByteBuffer byteBuffer(final HttpServerExchange exchange)
        throws IOException {
        return exchange.getAttachment(ServerRequest.BYTE_BUFFER_KEY);
    }

    /**
     * Returns a named form file value as a byte buffer.
     *
     * @param exchange the current server exchange
     * @param name the form field name
     * @return the buffered file content
     * @throws IllegalArgumentException when the field is absent
     * @throws IOException when the content cannot be read
     */
    public static ByteBuffer namedByteBuffer(
        final HttpServerExchange exchange,
        final String name
    ) throws IOException {
        return formValueBuffer(exchange, name).orElseThrow(() ->
            new IllegalArgumentException("Invalid parameter " + name)
        );
    }

    /**
     * Returns a named query parameter.
     *
     * @param exchange the current server exchange
     * @param name the query parameter name
     * @return the first value
     * @throws IllegalArgumentException when the parameter is absent
     */
    public static String string(
        final HttpServerExchange exchange,
        final String name
    ) throws IllegalArgumentException {
        try {
            return exchange.getQueryParameters().get(name).getFirst();
        } catch (NullPointerException e) {
            throw new IllegalArgumentException("Invalid parameter " + name, e);
        }
    }

    /**
     * Extracts a named query parameter and converts it with the given function.
     *
     * @param <T> the converted value type
     * @param exchange the current server exchange
     * @param name the query parameter name
     * @param function the string conversion function
     * @return the converted value
     * @throws IllegalArgumentException when the parameter is absent
     */
    public static <T> T extractWithFunction(
        final HttpServerExchange exchange,
        final String name,
        Function<String, T> function
    ) throws IllegalArgumentException {
        return function.apply(string(exchange, name));
    }

    /**
     * Returns a named query parameter parsed as a {@link Float}.
     *
     * @param exchange the current server exchange
     * @param name the query parameter name
     * @return the parsed value
     * @throws IllegalArgumentException when the parameter is absent or unparseable
     */
    public static Float floatValue(
        final HttpServerExchange exchange,
        final String name
    ) throws IllegalArgumentException {
        return Float.parseFloat(string(exchange, name));
    }

    /**
     * Returns a named query parameter parsed as a {@link Double}.
     *
     * @param exchange the current server exchange
     * @param name the query parameter name
     * @return the parsed value
     * @throws IllegalArgumentException when the parameter is absent or unparseable
     */
    public static Double doubleValue(
        final HttpServerExchange exchange,
        final String name
    ) throws IllegalArgumentException {
        return Double.parseDouble(string(exchange, name));
    }

    /**
     * Returns a named query parameter parsed as a {@link BigDecimal}.
     *
     * @param exchange the current server exchange
     * @param name the query parameter name
     * @return the parsed value
     * @throws IllegalArgumentException when the parameter is absent or unparseable
     */
    public static BigDecimal bigDecimalValue(
        final HttpServerExchange exchange,
        final String name
    ) {
        return new BigDecimal(string(exchange, name));
    }

    /**
     * Returns a named query parameter parsed as a {@link Long}.
     *
     * @param exchange the current server exchange
     * @param name the query parameter name
     * @return the parsed value
     * @throws IllegalArgumentException when the parameter is absent or unparseable
     */
    public static Long longValue(
        final HttpServerExchange exchange,
        final String name
    ) throws IllegalArgumentException {
        return Long.parseLong(string(exchange, name));
    }

    /**
     * Returns a named query parameter parsed as an {@link Instant}.
     *
     * @param exchange the current server exchange
     * @param name the query parameter name
     * @return the parsed value
     * @throws IllegalArgumentException when the parameter is absent or unparseable
     */
    public static Instant instant(
        final HttpServerExchange exchange,
        final String name
    ) throws IllegalArgumentException {
        return Instant.parse(string(exchange, name));
    }

    /**
     * Returns a named query parameter parsed as an {@link Integer}.
     *
     * @param exchange the current server exchange
     * @param name the query parameter name
     * @return the parsed value
     * @throws IllegalArgumentException when the parameter is absent or unparseable
     */
    public static Integer integerValue(
        final HttpServerExchange exchange,
        final String name
    ) throws IllegalArgumentException {
        return Integer.parseInt(string(exchange, name));
    }

    /**
     * Returns a named query parameter parsed as a {@link Short}.
     *
     * @param exchange the current server exchange
     * @param name the query parameter name
     * @return the parsed value
     * @throws IllegalArgumentException when the parameter is absent or unparseable
     */
    public static Short shortValue(
        final HttpServerExchange exchange,
        final String name
    ) throws IllegalArgumentException {
        return Short.parseShort(string(exchange, name));
    }

    /**
     * Returns a named query parameter parsed as a {@link Boolean}.
     *
     * @param exchange the current server exchange
     * @param name the query parameter name
     * @return the parsed value
     * @throws IllegalArgumentException when the parameter is absent or unparseable
     */
    public static Boolean booleanValue(
        final HttpServerExchange exchange,
        final String name
    ) throws IllegalArgumentException {
        return Boolean.parseBoolean(string(exchange, name));
    }

    /**
     * Parses the buffered request body as JSON.
     *
     * @param <T> the model type
     * @param exchange the current server exchange
     * @param type the target type reference
     * @return the parsed model, or null when parsing fails
     * @throws IllegalArgumentException when no body is buffered
     */
    public static <T> T jsonModel(
        final HttpServerExchange exchange,
        final TypeReference<T> type
    ) throws IllegalArgumentException {
        return parseTypedJson(
            type,
            exchange.getAttachment(ServerRequest.BYTE_BUFFER_KEY).array()
        );
    }

    /**
     * Parses the buffered request body as JSON.
     *
     * @param <T> the model type
     * @param exchange the current server exchange
     * @param type the target class
     * @return the parsed model, or null when parsing fails
     * @throws IllegalArgumentException when no body is buffered
     */
    public static <T> T jsonModel(
        final HttpServerExchange exchange,
        final Class<T> type
    ) throws IllegalArgumentException {
        return parseTypedJson(
            type,
            exchange.getAttachment(ServerRequest.BYTE_BUFFER_KEY).array()
        );
    }

    /**
     * Parses the buffered request body as XML.
     *
     * @param <T> the model type
     * @param exchange the current server exchange
     * @param type the target class
     * @return the parsed model, or null when parsing fails
     * @throws IllegalArgumentException when no body is buffered
     */
    public static <T> T xmlModel(
        final HttpServerExchange exchange,
        final Class<T> type
    ) throws IllegalArgumentException {
        return parseTypedXML(
            type,
            exchange.getAttachment(ServerRequest.BYTE_BUFFER_KEY).array()
        );
    }

    /**
     * Parses the buffered request body as XML.
     *
     * @param <T> the model type
     * @param exchange the current server exchange
     * @param type the target type reference
     * @return the parsed model, or null when parsing fails
     * @throws IllegalArgumentException when no body is buffered
     */
    public static <T> T xmlModel(
        final HttpServerExchange exchange,
        final TypeReference<T> type
    ) throws IllegalArgumentException {
        return parseTypedXML(
            type,
            exchange.getAttachment(ServerRequest.BYTE_BUFFER_KEY).array()
        );
    }

    /**
     * Parses the buffered request body as an arbitrary JSON tree.
     *
     * @param exchange the current server exchange
     * @return the parsed tree, or null when parsing fails
     */
    public static JsonNode any(final HttpServerExchange exchange) {
        return parseJson(
            exchange.getAttachment(ServerRequest.BYTE_BUFFER_KEY).array()
        );
    }

    /**
     * Parses the buffered request body as a JSON tree.
     *
     * @param exchange the current server exchange
     * @return the parsed tree, or null when parsing fails
     */
    public static JsonNode jsonNode(final HttpServerExchange exchange) {
        return parseJson(
            exchange.getAttachment(ServerRequest.BYTE_BUFFER_KEY).array()
        );
    }

    /**
     * Parses a named form value as a JSON tree.
     *
     * @param exchange the current server exchange
     * @param name the form field name
     * @return the parsed tree
     * @throws IllegalArgumentException when the field is absent
     */
    public static JsonNode namedJsonNode(
        final HttpServerExchange exchange,
        final String name
    ) {
        return formValue(exchange, name)
            .map(fv -> formValueModel(fv, JsonNode.class, name))
            .orElseThrow(() ->
                new IllegalArgumentException("Invalid parameter " + name)
            );
    }

    /**
     * Parses the buffered request body as XML or JSON per the content type.
     *
     * @param <T> the model type
     * @param exchange the current server exchange
     * @param type the target type reference
     * @return the parsed model, or null when parsing fails
     * @throws IllegalArgumentException when no body is buffered
     */
    public static <T> T model(
        final HttpServerExchange exchange,
        final TypeReference<T> type
    ) throws IllegalArgumentException {
        if (ServerPredicates.XML_PREDICATE.resolve(exchange)) {
            return xmlModel(exchange, type);
        } else {
            return jsonModel(exchange, type);
        }
    }

    /**
     * Parses the buffered request body as XML or JSON per the content type.
     *
     * @param <T> the model type
     * @param exchange the current server exchange
     * @param type the target class
     * @return the parsed model, or null when parsing fails
     * @throws IllegalArgumentException when no body is buffered
     */
    public static <T> T model(
        final HttpServerExchange exchange,
        final Class<T> type
    ) throws IllegalArgumentException {
        if (ServerPredicates.XML_PREDICATE.resolve(exchange)) {
            return xmlModel(exchange, type);
        } else {
            return jsonModel(exchange, type);
        }
    }

    /**
     * Parses a named form value as a model of the given type reference.
     *
     * @param <T> the model type
     * @param exchange the current server exchange
     * @param type the target type reference
     * @param name the form field name
     * @return the parsed model
     * @throws IllegalArgumentException when the field is absent
     */
    public static <T> T namedModel(
        final HttpServerExchange exchange,
        final TypeReference<T> type,
        final String name
    ) throws IllegalArgumentException {
        return formValue(exchange, name)
            .map(fv -> formValueModel(fv, type, name))
            .orElseThrow(() ->
                new IllegalArgumentException("Invalid parameter " + name)
            );
    }

    /**
     * Parses a named form value as a model of the given class.
     *
     * @param <T> the model type
     * @param exchange the current server exchange
     * @param type the target class
     * @param name the form field name
     * @return the parsed model
     * @throws IllegalArgumentException when the field is absent
     */
    public static <T> T namedModel(
        final HttpServerExchange exchange,
        final Class<T> type,
        final String name
    ) throws IllegalArgumentException {
        return formValue(exchange, name)
            .map(fv -> formValueModel(fv, type, name))
            .orElseThrow(() ->
                new IllegalArgumentException("Invalid parameter " + name)
            );
    }

    /** Maps a controller method to its Undertow HTTP method from its JAX-RS annotation. */
    public static Function<Method, HttpString> httpMethodFromMethod = m ->
        Arrays.stream(m.getDeclaredAnnotations())
            .map(a -> {
                if (a instanceof jakarta.ws.rs.POST) {
                    return Methods.POST;
                } else if (a instanceof jakarta.ws.rs.GET) {
                    return Methods.GET;
                } else if (a instanceof jakarta.ws.rs.PUT) {
                    return Methods.PUT;
                } else if (a instanceof jakarta.ws.rs.DELETE) {
                    return Methods.DELETE;
                } else if (a instanceof jakarta.ws.rs.OPTIONS) {
                    return Methods.OPTIONS;
                } else if (a instanceof jakarta.ws.rs.HEAD) {
                    return Methods.HEAD;
                } else if (a instanceof jakarta.ws.rs.PATCH) {
                    return Methods.PATCH;
                } else {
                    return null;
                }
            })
            .filter(Objects::nonNull)
            .findFirst()
            .get();

    /** Maps a controller method to its full route path by joining the class and method {@code @Path} values. */
    public static Function<Method, String> pathTemplateFromMethod = m -> {
        jakarta.ws.rs.Path childPath = m.getDeclaredAnnotation(
            jakarta.ws.rs.Path.class
        );

        jakarta.ws.rs.Path parentPath = m
            .getDeclaringClass()
            .getDeclaredAnnotation(jakarta.ws.rs.Path.class);

        if (!childPath.value().equals("/")) {
            return (
                String.format("%s/%s", parentPath.value(), childPath.value())
            ).replaceAll("//", "\\/");
        }

        return (parentPath.value());
    };
}
