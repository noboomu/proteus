/**
 *
 */
package io.sinistral.proteus.server;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author jbauer
 */
public class Extractors {

    private static Logger log = LoggerFactory.getLogger(Extractors.class.getCanonicalName());

    private final static Pattern XML_PATTERN = Pattern.compile("^(application/(xml|xhtml\\+xml)|text/xml)(;.*)?$", Pattern.CASE_INSENSITIVE);

    private final static Pattern JSON_PATTERN = Pattern.compile("^(application/(json|x-javascript)|text/(json|x-javascript|x-json))(;.*)?$", Pattern.CASE_INSENSITIVE);

    private static final Map<Type, JavaType> JAVA_TYPE_MAP = new ConcurrentHashMap<>();

    @Inject
    private static XmlMapper XML_MAPPER;

    @Inject
    private static ObjectMapper OBJECT_MAPPER;

    private static JsonNode parseJson(byte[] bytes)
    {

        try
        {
            return OBJECT_MAPPER.readTree(bytes);
        } catch (Exception e)
        {
            log.error("Failed to parse JSON", e);
            return null;
        }
    }

    private static <T> T parseTypedJson(final Class<T> type, byte[] bytes)
    {

        try
        {
            return OBJECT_MAPPER.readValue(bytes, type);
        } catch (Exception e)
        {
            log.error("Failed to parse JSON for type {}", type, e);
            return null;
        }
    }

    private static <T> T parseTypedJson(final TypeReference<T> type, byte[] bytes)
    {

        try
        {
            final Type _rawType = type.getType();

            JavaType _javaType = JAVA_TYPE_MAP.get(_rawType);

            if (_javaType == null)
            {
                _javaType = OBJECT_MAPPER.getTypeFactory().constructType(_rawType);
                JAVA_TYPE_MAP.put(_rawType, _javaType);
            }

            return OBJECT_MAPPER.readValue(bytes, _javaType);
        } catch (Exception e)
        {
            log.error("Failed to parse JSON for type {}", type, e);
            return null;
        }
    }

    private static <T> T parseTypedXML(final Class<T> type, byte[] bytes)
    {

        try
        {
            return XML_MAPPER.readValue(bytes, type);
        } catch (Exception e)
        {
            log.error("Failed to parse XML for type {}", type, e);
            return null;
        }
    }

    private static <T> T parseTypedXML(final TypeReference<T> type, byte[] bytes)
    {

        try
        {
            final Type _rawType = type.getType();

            JavaType _javaType = JAVA_TYPE_MAP.get(_rawType);

            if (_javaType == null)
            {
                _javaType = OBJECT_MAPPER.getTypeFactory().constructType(_rawType);
                JAVA_TYPE_MAP.put(_rawType, _javaType);
            }

            return XML_MAPPER.readValue(bytes, _javaType);
        } catch (Exception e)
        {
            log.error("Failed to parse XML for type {}", type, e);
            return null;
        }
    }

    private static Path formValueFileItemPath(FormData.FileItem fileItem) {

        if (fileItem.isInMemory())
        {
            try
            {
                Path path = Files.createTempFile("proteus", "upload");

                path.toFile().deleteOnExit();

                DataUtilities.writeStreamToPath(fileItem.getInputStream(), path);

                return path;

            } catch (Exception e)
            {
                log.error("Failed to create temporary file for form item", e);
                return null;
            }
        }
        else
        {
            return fileItem.getFile();
        }
    }

    private static java.util.Optional<Path> formValueFilePath(final HttpServerExchange exchange, final String name) {

        return formValueFileItem(exchange, name).map(Extractors::formValueFileItemPath);

    }

    private static java.util.Optional<Stream<Path>> formValueFilePaths(final HttpServerExchange exchange, final String name) {

        return formValueFileItems(exchange, name).map(items -> items.map(Extractors::formValueFileItemPath));

    }

    private static java.util.Optional<Map<String, Path>> formValuePathMap(final HttpServerExchange exchange, final String name) {

        return java.util.Optional.ofNullable(exchange.getAttachment(FormDataParser.FORM_DATA).get(name))
                                 .map(deque -> deque.stream().filter(fv -> fv.getFileItem() != null).collect(Collectors.toMap(FormData.FormValue::getFileName, fv -> formValueFileItemPath(fv.getFileItem()))));

    }

    private static java.util.Optional<Map<String, File>> formValueFileMap(final HttpServerExchange exchange, final String name) {

        return java.util.Optional.ofNullable(exchange.getAttachment(FormDataParser.FORM_DATA).get(name))
                                 .map(deque -> deque.stream().filter(fv -> fv.getFileItem() != null).collect(Collectors.toMap(FormData.FormValue::getFileName, fv -> formValueFileItemPath(fv.getFileItem()).toFile())));

    }

    private static java.util.Optional<ByteBuffer> formValueBuffer(final HttpServerExchange exchange, final String name)
    {

      FormData formData =  exchange.getAttachment(FormDataParser.FORM_DATA);

      //  

        Iterator<String> itr = formData.iterator();

        while(itr.hasNext())
        {
            String s = itr.next();

           // 

           Deque<FormData.FormValue> deque = formData.get(s);

           var values = deque.stream().map(v -> {


               if(v.isFileItem())
               {
                   return Map.of("headers",v.getHeaders(),"fileItem",v.getFileItem(),"fileName",v.getFileName());
               }
               else

               {
                   return Map.of("headers",v.getHeaders(),"value",v.getValue());
               }


           }).collect(Collectors.toList());

        //   

        }


        return java.util.Optional.ofNullable(formData.get(name)).map(Deque::getFirst).map(fi -> {

            try
            {

                if (fi.isFileItem())
                {
                    FormData.FileItem fileItem = fi.getFileItem();

                  log.trace("fileItem: {} {} ",fileItem,fileItem.getFile());

                    if(fileItem.isInMemory())
                    {
                        log.trace("fileItem: {} is in memory {}", fileItem, fileItem.getFileSize());
                    }
                    return DataUtilities.fileItemToBuffer(fileItem);
                }

            } catch (Exception e)
            {
                log.error("Failed to parse buffer for name {}", name, e);
            }

            return null;

        });

    }

    private static java.util.Optional<FormData.FormValue> formValue(final HttpServerExchange exchange, final String name)
    {

        return java.util.Optional.ofNullable(exchange.getAttachment(FormDataParser.FORM_DATA).get(name)).map(Deque::getFirst);

    }

    private static java.util.Optional<FormData.FileItem> formValueFileItem(final HttpServerExchange exchange, final String name)
    {

        return java.util.Optional.ofNullable(exchange.getAttachment(FormDataParser.FORM_DATA).get(name)).map(Deque::getFirst).map(FormData.FormValue::getFileItem);

    }

    private static java.util.Optional<Stream<FormData.FileItem>> formValueFileItems(final HttpServerExchange exchange, final String name)
    {

        return java.util.Optional.ofNullable(exchange.getAttachment(FormDataParser.FORM_DATA).get(name)).map(deque -> deque.stream().map(FormData.FormValue::getFileItem));
    }

    private static <T> T formValueModel(final FormData.FormValue formValue, final TypeReference<T> type, final String name)
    {

        if (formValue.getHeaders().get(Headers.CONTENT_TYPE) != null && XML_PATTERN.matcher(formValue.getHeaders().getFirst(Headers.CONTENT_TYPE)).matches())
        {
            if (formValue.isFileItem())
            {
                try
                {
                    ByteBuffer byteBuffer = DataUtilities.fileItemToBuffer(formValue.getFileItem());

                    return parseTypedXML(type, byteBuffer.array());

                } catch (Exception e)
                {
                    log.error("Failed to parse buffered XML for {}", name, e);
                    return null;
                }
            }
            else
            {
                try
                {
                    return parseTypedXML(type, formValue.getValue().getBytes());
                } catch (Exception e)
                {
                    log.error("Failed to parse XML for {}", name, e);
                    return null;
                }
            }
        }
        else if (formValue.getHeaders().get(Headers.CONTENT_TYPE) == null || (formValue.getHeaders().get(Headers.CONTENT_TYPE) != null && JSON_PATTERN.matcher(formValue.getHeaders().getFirst(Headers.CONTENT_TYPE)).matches()))
        {
            if (formValue.isFileItem())
            {
                try
                {
                    ByteBuffer byteBuffer = DataUtilities.fileItemToBuffer(formValue.getFileItem());

                    return parseTypedJson(type, byteBuffer.array());

                } catch (Exception e)
                {
                    log.error("Failed to parse buffered json for {}", name, e);
                    return null;
                }
            }
            else
            {
                return parseTypedJson(type, formValue.getValue().getBytes());
            }
        }
        else
        {
            log.warn("FormValue for {} is not a file item", name);
            return null;
        }
    }

    private static <T> T formValueModel(final FormData.FormValue formValue, final Class<T> type, final String name)
    {

        if (formValue.getHeaders().get(Headers.CONTENT_TYPE) != null && XML_PATTERN.matcher(formValue.getHeaders().getFirst(Headers.CONTENT_TYPE)).matches())
        {
            if (formValue.isFileItem())
            {
                try
                {
                    ByteBuffer byteBuffer = DataUtilities.fileItemToBuffer(formValue.getFileItem());

                    return parseTypedXML(type, byteBuffer.array());

                } catch (Exception e)
                {
                    log.error("Failed to parse buffered XML for {}", name, e);
                    return null;
                }
            }
            else
            {
                try
                {
                    return parseTypedXML(type, formValue.getValue().getBytes());
                } catch (Exception e)
                {
                    log.error("Failed to parse XML for {}", name, e);
                    return null;
                }
            }
        }
        else if (formValue.getHeaders().get(Headers.CONTENT_TYPE) == null || (formValue.getHeaders().get(Headers.CONTENT_TYPE) != null && JSON_PATTERN.matcher(formValue.getHeaders().getFirst(Headers.CONTENT_TYPE)).matches()))
        {
            if (formValue.isFileItem())
            {
                try
                {
                    ByteBuffer byteBuffer = DataUtilities.fileItemToBuffer(formValue.getFileItem());

                    return parseTypedJson(type, byteBuffer.array());

                } catch (Exception e)
                {
                    log.error("Failed to parse buffered json for {}", name, e);
                    return null;
                }
            }
            else
            {
                return parseTypedJson(type, formValue.getValue().getBytes());
            }
        }
        else
        {
            log.warn("FormValue for {} is not a file item", name);
            return null;
        }
    }

    public static class Header {

        public static String string(final HttpServerExchange exchange, final String name) throws IllegalArgumentException
        {

            return java.util.Optional.ofNullable(exchange.getRequestHeaders().get(name)).map(HeaderValues::getFirst).orElseThrow(() -> new IllegalArgumentException("Invalid parameter " + name));
        }

        public static class Optional {

            public static java.util.Optional<String> string(final HttpServerExchange exchange, final String name)
            {

                return java.util.Optional.ofNullable(exchange.getRequestHeaders().get(name)).map(HeaderValues::getFirst);
            }

        }

    }

    public static class Optional {

        public static <T> java.util.Optional<T> extractWithFunction(final HttpServerExchange exchange, final String name, Function<String, T> function)
        {

            return string(exchange, name).map(function);
        }

        public static java.util.Optional<JsonNode> namedJsonNode(final HttpServerExchange exchange)
        {

            return jsonModel(exchange, JsonNode.class);
        }

        public static java.util.Optional<JsonNode> namedJsonNode(final HttpServerExchange exchange, final String name)
        {

            return formValue(exchange, name).map(fv -> formValueModel(fv, JsonNode.class, name));
        }

        public static <T> java.util.Optional<T> model(final HttpServerExchange exchange, final TypeReference<T> type)
        {

            if (ServerPredicates.XML_PREDICATE.resolve(exchange))
            {
                return xmlModel(exchange, type);
            }
            else
            {
                return jsonModel(exchange, type);
            }
        }

        public static <T> java.util.Optional<T> model(final HttpServerExchange exchange, final Class<T> type)
        {

            if (ServerPredicates.XML_PREDICATE.resolve(exchange))
            {
                return xmlModel(exchange, type);
            }
            else
            {
                return jsonModel(exchange, type);
            }
        }

        public static <T> java.util.Optional<T> namedModel(final HttpServerExchange exchange, final Class<T> type, final String name)
        {

            return formValue(exchange, name).map(fv -> formValueModel(fv, type, name));
        }

        public static <T> java.util.Optional<T> namedModel(final HttpServerExchange exchange, final TypeReference<T> type, final String name)
        {

            return formValue(exchange, name).map(fv -> formValueModel(fv, type, name));

        }

        public static <T> java.util.Optional<T> jsonModel(final HttpServerExchange exchange, final TypeReference<T> type)
        {

            return java.util.Optional.ofNullable(exchange.getAttachment(ServerRequest.BYTE_BUFFER_KEY)).map(ByteBuffer::array).map(b -> parseTypedJson(type, b));
        }

        public static <T> java.util.Optional<T> jsonModel(final HttpServerExchange exchange, final Class<T> type)
        {

            return java.util.Optional.ofNullable(exchange.getAttachment(ServerRequest.BYTE_BUFFER_KEY)).map(ByteBuffer::array).map(b -> parseTypedJson(type, b));
        }

        public static <T> java.util.Optional<T> xmlModel(final HttpServerExchange exchange, final TypeReference<T> type)
        {

            return java.util.Optional.ofNullable(exchange.getAttachment(ServerRequest.BYTE_BUFFER_KEY)).map(ByteBuffer::array).map(b -> parseTypedXML(type, b));
        }

        public static <T> java.util.Optional<T> xmlModel(final HttpServerExchange exchange, final Class<T> type)
        {

            return java.util.Optional.ofNullable(exchange.getAttachment(ServerRequest.BYTE_BUFFER_KEY)).map(ByteBuffer::array).map(b -> parseTypedXML(type, b));
        }

        public static java.util.Optional<Date> date(final HttpServerExchange exchange, final String name)
        {

            return string(exchange, name).map(OffsetDateTime::parse).map(OffsetDateTime::toInstant).map(Date::from);

        }

        public static java.util.Optional<OffsetDateTime> offsetDateTime(final HttpServerExchange exchange, final String name)
        {

            return string(exchange, name).map(OffsetDateTime::parse);

        }

        public static java.util.Optional<ZonedDateTime> zonedDateTime(final HttpServerExchange exchange, final String name)
        {

            return string(exchange, name).map(ZonedDateTime::parse);
        }

        public static java.util.Optional<Instant> instant(final HttpServerExchange exchange, final String name)
        {

            return string(exchange, name).map(Instant::parse);
        }

        public static java.util.Optional<Integer> integerValue(final HttpServerExchange exchange, final String name)
        {

            return string(exchange, name).map(Integer::parseInt);
        }

        public static java.util.Optional<Short> shortValue(final HttpServerExchange exchange, final String name)
        {

            return string(exchange, name).map(Short::parseShort);
        }

        public static java.util.Optional<Float> floatValue(final HttpServerExchange exchange, final String name)
        {

            return string(exchange, name).map(Float::parseFloat);
        }

        public static java.util.Optional<Double> doubleValue(final HttpServerExchange exchange, final String name)
        {

            return string(exchange, name).map(Double::parseDouble);
        }

        public static java.util.Optional<BigDecimal> bigDecimalValue(final HttpServerExchange exchange, final String name)
        {

            return string(exchange, name).map(BigDecimal::new);
        }

        public static java.util.Optional<Long> longValue(final HttpServerExchange exchange, final String name)
        {

            return string(exchange, name).map(Long::parseLong);
        }

        public static java.util.Optional<Boolean> booleanValue(final HttpServerExchange exchange, final String name)
        {

            return string(exchange, name).map(Boolean::parseBoolean);
        }

//		public static  <E extends Enum<E>> java.util.Optional<E> enumValue(final HttpServerExchange exchange, final Class<E> clazz, final String name)
//		{
//			return string(exchange, name).map(e -> Enum.valueOf(clazz, name));
//		}

        public static java.util.Optional<String> string(final HttpServerExchange exchange, final String name)
        {

            return java.util.Optional.ofNullable(exchange.getQueryParameters().get(name)).map(Deque::getFirst);
        }

        public static java.util.Optional<Path> filePath(final HttpServerExchange exchange, final String name)
        {

            return formValueFilePath(exchange, name);
        }

        public static java.util.Optional<File> file(final HttpServerExchange exchange, final String name)
        {

            return formValueFilePath(exchange, name).map(Path::toFile);
        }

        public static java.util.Optional<ByteBuffer> byteBuffer(final HttpServerExchange exchange) throws IOException
        {

            return java.util.Optional.ofNullable(exchange.getAttachment(ServerRequest.BYTE_BUFFER_KEY));
        }

        public static java.util.Optional<ByteBuffer> namedByteBuffer(final HttpServerExchange exchange, final String name) throws IOException
        {

            return formValueBuffer(exchange, name);
        }

    }

    public static Date date(final HttpServerExchange exchange, final String name) throws IllegalArgumentException
    {

        return Date.from(OffsetDateTime.parse(string(exchange, name)).toInstant());
    }

    public static ZonedDateTime zonedDateTime(final HttpServerExchange exchange, final String name) throws IllegalArgumentException
    {

        return ZonedDateTime.parse(string(exchange, name));

    }

    public static OffsetDateTime offsetDateTime(final HttpServerExchange exchange, final String name) throws IllegalArgumentException
    {

        return OffsetDateTime.parse(string(exchange, name));

    }

    public static Path filePath(final HttpServerExchange exchange, final String name) throws IllegalArgumentException
    {

        return formValueFilePath(exchange, name).orElseThrow(() -> new IllegalArgumentException("Invalid parameter " + name));
    }

    public static List<Path> pathList(final HttpServerExchange exchange, final String name) throws IllegalArgumentException
    {

        return formValueFilePaths(exchange, name).map(s -> s.collect(Collectors.toList())).orElse(new ArrayList<>());
    }

    public static List<File> fileList(final HttpServerExchange exchange, final String name) throws IllegalArgumentException
    {

        return formValueFilePaths(exchange, name).map(s -> s.map(Path::toFile).collect(Collectors.toList())).orElse(new ArrayList<>());
    }

    public static Map<String, Path> pathMap(final HttpServerExchange exchange, final String name) throws IllegalArgumentException
    {

        return formValuePathMap(exchange, name).orElse(new HashMap<>());
    }

    public static Map<String, File> fileMap(final HttpServerExchange exchange, final String name) throws IllegalArgumentException
    {

        return formValueFileMap(exchange, name).orElse(new HashMap<>());
    }

    public static File file(final HttpServerExchange exchange, final String name) throws IllegalArgumentException
    {

        return formValueFilePath(exchange, name).map(Path::toFile).orElseThrow(() -> new IllegalArgumentException("Invalid parameter " + name));
    }

    public static ByteBuffer byteBuffer(final HttpServerExchange exchange) throws IOException
    {

        return exchange.getAttachment(ServerRequest.BYTE_BUFFER_KEY);
    }

    public static ByteBuffer namedByteBuffer(final HttpServerExchange exchange, final String name) throws IOException
    {

        return formValueBuffer(exchange, name).orElseThrow(() -> new IllegalArgumentException("Invalid parameter " + name));
    }

    public static String string(final HttpServerExchange exchange, final String name) throws IllegalArgumentException
    {

        try
        {
            return exchange.getQueryParameters().get(name).getFirst();
        } catch (NullPointerException e)
        {
            throw new IllegalArgumentException("Invalid parameter " + name, e);
        }
    }

    public static <T> T extractWithFunction(final HttpServerExchange exchange, final String name, Function<String, T> function) throws IllegalArgumentException
    {

        return function.apply(string(exchange, name));
    }

    public static Float floatValue(final HttpServerExchange exchange, final String name) throws IllegalArgumentException
    {

        return Float.parseFloat(string(exchange, name));
    }

    public static Double doubleValue(final HttpServerExchange exchange, final String name) throws IllegalArgumentException
    {

        return Double.parseDouble(string(exchange, name));
    }

    public static BigDecimal bigDecimalValue(final HttpServerExchange exchange, final String name)
    {

        return new BigDecimal(string(exchange, name));
    }

    public static Long longValue(final HttpServerExchange exchange, final String name) throws IllegalArgumentException
    {

        return Long.parseLong(string(exchange, name));
    }

    public static Instant instant(final HttpServerExchange exchange, final String name) throws IllegalArgumentException
    {

        return Instant.parse(string(exchange, name));
    }

    public static Integer integerValue(final HttpServerExchange exchange, final String name) throws IllegalArgumentException
    {

        return Integer.parseInt(string(exchange, name));

    }

    public static Short shortValue(final HttpServerExchange exchange, final String name) throws IllegalArgumentException
    {

        return Short.parseShort(string(exchange, name));

    }

    public static Boolean booleanValue(final HttpServerExchange exchange, final String name) throws IllegalArgumentException
    {

        return Boolean.parseBoolean(string(exchange, name));
    }

    public static <T> T jsonModel(final HttpServerExchange exchange, final TypeReference<T> type) throws IllegalArgumentException
    {

        return parseTypedJson(type, exchange.getAttachment(ServerRequest.BYTE_BUFFER_KEY).array());

    }

    public static <T> T jsonModel(final HttpServerExchange exchange, final Class<T> type) throws IllegalArgumentException
    {

        return parseTypedJson(type, exchange.getAttachment(ServerRequest.BYTE_BUFFER_KEY).array());

    }

    public static <T> T xmlModel(final HttpServerExchange exchange, final Class<T> type) throws IllegalArgumentException
    {

        return parseTypedXML(type, exchange.getAttachment(ServerRequest.BYTE_BUFFER_KEY).array());

    }

    public static <T> T xmlModel(final HttpServerExchange exchange, final TypeReference<T> type) throws IllegalArgumentException
    {

        return parseTypedXML(type, exchange.getAttachment(ServerRequest.BYTE_BUFFER_KEY).array());

    }

    public static JsonNode any(final HttpServerExchange exchange)
    {

        return parseJson(exchange.getAttachment(ServerRequest.BYTE_BUFFER_KEY).array());
    }

    public static JsonNode jsonNode(final HttpServerExchange exchange)
    {

        return parseJson(exchange.getAttachment(ServerRequest.BYTE_BUFFER_KEY).array());
    }

    public static JsonNode namedJsonNode(final HttpServerExchange exchange, final String name)
    {

        return formValue(exchange, name).map(fv -> formValueModel(fv, JsonNode.class, name)).orElseThrow(() -> new IllegalArgumentException("Invalid parameter " + name));
    }

    public static <T> T model(final HttpServerExchange exchange, final TypeReference<T> type) throws IllegalArgumentException
    {

        if (ServerPredicates.XML_PREDICATE.resolve(exchange))
        {
            return xmlModel(exchange, type);
        }
        else
        {
            return jsonModel(exchange, type);
        }
    }

    public static <T> T model(final HttpServerExchange exchange, final Class<T> type) throws IllegalArgumentException
    {

        if (ServerPredicates.XML_PREDICATE.resolve(exchange))
        {
            return xmlModel(exchange, type);
        }
        else
        {
            return jsonModel(exchange, type);
        }
    }

    public static <T> T namedModel(final HttpServerExchange exchange, final TypeReference<T> type, final String name) throws IllegalArgumentException
    {

        return formValue(exchange, name).map(fv -> formValueModel(fv, type, name)).orElseThrow(() -> new IllegalArgumentException("Invalid parameter " + name));
    }

    public static <T> T namedModel(final HttpServerExchange exchange, final Class<T> type, final String name) throws IllegalArgumentException
    {

        return formValue(exchange, name).map(fv -> formValueModel(fv, type, name)).orElseThrow(() -> new IllegalArgumentException("Invalid parameter " + name));
    }

    public static Function<Method, HttpString> httpMethodFromMethod = (m) ->
            Arrays.stream(m.getDeclaredAnnotations()).map(a -> {

                if (a instanceof javax.ws.rs.POST)
                {
                    return Methods.POST;
                }
                else if (a instanceof javax.ws.rs.GET)
                {
                    return Methods.GET;
                }
                else if (a instanceof javax.ws.rs.PUT)
                {
                    return Methods.PUT;
                }
                else if (a instanceof javax.ws.rs.DELETE)
                {
                    return Methods.DELETE;
                }
                else if (a instanceof javax.ws.rs.OPTIONS)
                {
                    return Methods.OPTIONS;
                }
                else if (a instanceof javax.ws.rs.HEAD)
                {
                    return Methods.HEAD;
                }
                else if (a instanceof javax.ws.rs.PATCH)
                {
                    return Methods.PATCH;
                }
                else
                {
                    return null;
                }

            }).filter(Objects::nonNull).findFirst().get();

    public static Function<Method, String> pathTemplateFromMethod = (m) ->
    {
        javax.ws.rs.Path childPath = m.getDeclaredAnnotation(javax.ws.rs.Path.class);

        javax.ws.rs.Path parentPath = m.getDeclaringClass().getDeclaredAnnotation(javax.ws.rs.Path.class);

        if (!childPath.value().equals("/"))
        {
            return (String.format("%s/%s", parentPath.value(), childPath.value())).replaceAll("//", "\\/");
        }

        return (parentPath.value());

    };

}
