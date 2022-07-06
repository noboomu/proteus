/**
 *
 */
package io.sinistral.proteus.server.handlers;

import com.squareup.javapoet.MethodSpec;
import io.sinistral.proteus.server.handlers.HandlerGenerator.StatementParameterType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.ws.rs.*;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;

/**
 * Enum that assists in code generation for different method parameter types
 */

public enum TypeHandler {

    LongType("Long $L = $T.longValue(exchange,$S)", false, StatementParameterType.LITERAL, io.sinistral.proteus.server.Extractors.class, StatementParameterType.STRING),
    IntegerType("Integer $L = $T.integerValue(exchange,$S)", false, StatementParameterType.LITERAL, io.sinistral.proteus.server.Extractors.class, StatementParameterType.STRING),
    StringType("String $L =  $T.string(exchange,$S)", false, StatementParameterType.LITERAL, io.sinistral.proteus.server.Extractors.class, StatementParameterType.STRING),
    BooleanType("Boolean $L =  $T.booleanValue(exchange,$S)", false, StatementParameterType.LITERAL, io.sinistral.proteus.server.Extractors.class, StatementParameterType.STRING),
    FilePathType("$T $L = $T.filePath(exchange,$S)", true, java.nio.file.Path.class, StatementParameterType.LITERAL, io.sinistral.proteus.server.Extractors.class, StatementParameterType.STRING),

    AnyType("$T $L = $T.any(exchange)", true, com.fasterxml.jackson.databind.JsonNode.class, StatementParameterType.LITERAL, io.sinistral.proteus.server.Extractors.class),
    JsonNodeType("$T $L = $T.jsonNode(exchange)", true, com.fasterxml.jackson.databind.JsonNode.class, StatementParameterType.LITERAL, io.sinistral.proteus.server.Extractors.class),
    NamedJsonNodeType("$T $L = $T.namedJsonNode(exchange,$S)", true, com.fasterxml.jackson.databind.JsonNode.class, StatementParameterType.LITERAL, io.sinistral.proteus.server.Extractors.class, StatementParameterType.STRING),
    ModelType("$T $L = io.sinistral.proteus.server.Extractors.model(exchange,$L)", true, StatementParameterType.TYPE, StatementParameterType.LITERAL, StatementParameterType.LITERAL),
    NamedModelType("$T $L = io.sinistral.proteus.server.Extractors.namedModel(exchange,$L,$S)", true, StatementParameterType.TYPE, StatementParameterType.LITERAL, StatementParameterType.LITERAL, StatementParameterType.STRING),

    // EnumType("$T $L = $T.enumValue(exchange,$T.class,$S)", true,
    // StatementParameterType.TYPE,
    // StatementParameterType.LITERAL,io.sinistral.proteus.server.Extractors.class,
    // StatementParameterType.TYPE, StatementParameterType.STRING),

    FileType("$T $L =  $T.file(exchange,$S)", true, java.io.File.class, StatementParameterType.LITERAL, io.sinistral.proteus.server.Extractors.class, StatementParameterType.STRING),

    ByteBufferType("$T $L =  $T.byteBuffer(exchange)", true, java.nio.ByteBuffer.class, StatementParameterType.LITERAL, io.sinistral.proteus.server.Extractors.class),
    NamedByteBufferType("$T $L =  $T.namedByteBuffer(exchange,$S)", true, java.nio.ByteBuffer.class, StatementParameterType.LITERAL, io.sinistral.proteus.server.Extractors.class, StatementParameterType.STRING),

    DateType("$T $L =  $T.date(exchange,$S)", false, java.util.Date.class, StatementParameterType.LITERAL, io.sinistral.proteus.server.Extractors.class, StatementParameterType.STRING),
    ZonedDateTimeType("$T $L = $T.zonedDateTime(exchange,$S)", false, java.time.ZonedDateTime.class, StatementParameterType.LITERAL, io.sinistral.proteus.server.Extractors.class, StatementParameterType.STRING),
    OffsetDateTimeType("$T $L = $T.offsetDateTime(exchange,$S)", false, java.time.OffsetDateTime.class, StatementParameterType.LITERAL, io.sinistral.proteus.server.Extractors.class, StatementParameterType.STRING),

    InstantType("$T $L = $T.instant(exchange,$S)", false, java.time.Instant.class, StatementParameterType.LITERAL, io.sinistral.proteus.server.Extractors.class, StatementParameterType.STRING),

    FloatType("Float $L = $T.floatValue(exchange,$S)", false, StatementParameterType.LITERAL, io.sinistral.proteus.server.Extractors.class, StatementParameterType.STRING),
    DoubleType("Double $L = $T.doubleValue(exchange,$S)", false, StatementParameterType.LITERAL, io.sinistral.proteus.server.Extractors.class, StatementParameterType.STRING),
    BigDecimalType("BigDecimal $L = $T.bigDecimalValue(exchange,$S)", false, StatementParameterType.LITERAL, io.sinistral.proteus.server.Extractors.class, StatementParameterType.STRING),

    ValueOfType("$T $L = $T.valueOf($T.string(exchange,$S))", false, StatementParameterType.TYPE, StatementParameterType.LITERAL, StatementParameterType.TYPE, io.sinistral.proteus.server.Extractors.class, StatementParameterType.STRING),
    FromStringType("$T $L = $T.fromString($T.string(exchange,$S))", false, StatementParameterType.TYPE, StatementParameterType.LITERAL, StatementParameterType.TYPE, io.sinistral.proteus.server.Extractors.class, StatementParameterType.STRING),

    QueryListValueOfType("$T<$T> $L = exchange.getQueryParameters().get($S).stream().map($T::valueOf).collect(java.util.stream.Collectors.toList())", false, java.util.List.class, StatementParameterType.RAW, StatementParameterType.LITERAL, StatementParameterType.STRING, StatementParameterType.RAW),
    QueryListFromStringType("$T<$T> $L = exchange.getQueryParameters().get($S).stream().map($T::fromString).collect(java.util.stream.Collectors.toList())", false, java.util.List.class, StatementParameterType.RAW, StatementParameterType.LITERAL, StatementParameterType.STRING, StatementParameterType.RAW),

    QuerySetValueOfType("$T<$T> $L = exchange.getQueryParameters().get($S).stream().map($T::valueOf).collect(java.util.stream.Collectors.toSet())", false, java.util.Set.class, StatementParameterType.RAW, StatementParameterType.LITERAL, StatementParameterType.STRING, StatementParameterType.RAW),
    QuerySetFromStringType("$T<$T> $L = exchange.getQueryParameters().get($S).stream().map($T::fromString).collect(java.util.stream.Collectors.toSet())", false, java.util.Set.class, StatementParameterType.RAW, StatementParameterType.LITERAL, StatementParameterType.STRING, StatementParameterType.RAW),

    // BeanListValueOfType("$T<$T> $L =
    // $T.string(exchange,$S).map($T::valueOf).collect(java.util.stream.Collectors.toList())",
    // true, java.util.List.class, StatementParameterType.RAW,
    // StatementParameterType.LITERAL,
    // io.sinistral.proteus.server.Extractors.class,
    // StatementParameterType.LITERAL, StatementParameterType.RAW),

    BeanListValueOfType("$T $L = io.sinistral.proteus.server.Extractors.model(exchange,$L)", true, StatementParameterType.TYPE, StatementParameterType.LITERAL, StatementParameterType.LITERAL),
    BeanListFromStringType("$T $L = io.sinistral.proteus.server.Extractors.model(exchange,$L)", true, StatementParameterType.TYPE, StatementParameterType.LITERAL, StatementParameterType.LITERAL),

    FileListType("$T $L = io.sinistral.proteus.server.Extractors.fileList(exchange,$S)", true, StatementParameterType.TYPE, StatementParameterType.LITERAL, StatementParameterType.STRING),
    PathListType("$T $L = io.sinistral.proteus.server.Extractors.pathList(exchange,$S)", true, StatementParameterType.TYPE, StatementParameterType.LITERAL, StatementParameterType.STRING),

    FileMapType("$T $L = io.sinistral.proteus.server.Extractors.fileMap(exchange,$S)", true, StatementParameterType.TYPE, StatementParameterType.LITERAL, StatementParameterType.STRING),
    PathMapType("$T $L = io.sinistral.proteus.server.Extractors.pathMap(exchange,$S)", true, StatementParameterType.TYPE, StatementParameterType.LITERAL, StatementParameterType.STRING),

    HeaderValueOfType("$T $L = $T.valueOf($T.string(exchange,$S))", false, StatementParameterType.TYPE, StatementParameterType.LITERAL, StatementParameterType.TYPE, io.sinistral.proteus.server.Extractors.Header.class, StatementParameterType.STRING),
    HeaderFromStringType("$T $L = $T.fromString($T.string(exchange,$S))", false, StatementParameterType.TYPE, StatementParameterType.LITERAL, StatementParameterType.TYPE, io.sinistral.proteus.server.Extractors.Header.class, StatementParameterType.STRING),
    HeaderStringType("$T $L = $T.string(exchange,$S)", false, String.class, StatementParameterType.LITERAL, io.sinistral.proteus.server.Extractors.Header.class, StatementParameterType.STRING),

    OptionalHeaderValueOfType("$T<$T> $L = $T.string(exchange,$S).map($T::valueOf)", false, Optional.class, StatementParameterType.RAW, StatementParameterType.LITERAL, io.sinistral.proteus.server.Extractors.Header.Optional.class, StatementParameterType.STRING, StatementParameterType.RAW),
    OptionalHeaderFromStringType("$T<$T> $L = $T.string(exchange,$S).map($T::fromString)", false, Optional.class, StatementParameterType.RAW, StatementParameterType.LITERAL, io.sinistral.proteus.server.Extractors.Header.Optional.class, StatementParameterType.STRING, StatementParameterType.RAW),
    OptionalHeaderStringType("$T<$T> $L = $T.string(exchange,$S)", false, Optional.class, String.class, StatementParameterType.LITERAL, io.sinistral.proteus.server.Extractors.Header.Optional.class, StatementParameterType.STRING),

    QueryOptionalListValueOfType("$T $L = java.util.Optional.ofNullable(exchange.getQueryParameters().get($S)).map(java.util.Deque::stream).map( p -> p.map($T::valueOf).collect(java.util.stream.Collectors.toList()))", false, StatementParameterType.RAW, StatementParameterType.LITERAL, StatementParameterType.STRING, StatementParameterType.RAW),
    QueryOptionalListFromStringType("$T $L = java.util.Optional.ofNullable(exchange.getQueryParameters().get($S)).map(java.util.Deque::stream).map( p -> p.map($T::fromString).collect(java.util.stream.Collectors.toList()))", false, StatementParameterType.RAW, StatementParameterType.LITERAL, StatementParameterType.STRING, StatementParameterType.RAW),

    QueryOptionalSetValueOfType("$T $L = java.util.Optional.ofNullable(exchange.getQueryParameters().get($S)).map(java.util.Deque::stream).map( p -> p.map($T::valueOf).collect(java.util.stream.Collectors.toSet()))", false, StatementParameterType.RAW, StatementParameterType.LITERAL, StatementParameterType.STRING, StatementParameterType.RAW),
    QueryOptionalSetFromStringType("$T $L = java.util.Optional.ofNullable(exchange.getQueryParameters().get($S)).map(java.util.Deque::stream).map( p -> p.map($T::fromString).collect(java.util.stream.Collectors.toSet()))", false, StatementParameterType.RAW, StatementParameterType.LITERAL, StatementParameterType.STRING, StatementParameterType.RAW),

    OptionalBeanListValueOfType("java.util.Optional<$L> $L = $T.model(exchange,$L)", false, StatementParameterType.TYPE, StatementParameterType.LITERAL, io.sinistral.proteus.server.Extractors.Optional.class, StatementParameterType.LITERAL),
    OptionalBeanListFromStringType("java.util.Optional<$L> $L = $T.model(exchange,$L)", false, StatementParameterType.TYPE, StatementParameterType.LITERAL, io.sinistral.proteus.server.Extractors.Optional.class, StatementParameterType.LITERAL),

    OptionalJsonNodeType("$T<$T> $L = $T.jsonNode(exchange)", true, Optional.class, com.fasterxml.jackson.databind.JsonNode.class, StatementParameterType.LITERAL, io.sinistral.proteus.server.Extractors.Optional.class),
    OptionalAnyType("$T<$T> $L = $T.any(exchange)", true, Optional.class, com.fasterxml.jackson.databind.JsonNode.class, StatementParameterType.LITERAL, io.sinistral.proteus.server.Extractors.Optional.class),
    OptionalStringType("$T<String> $L = $T.string(exchange,$S)", false, Optional.class, StatementParameterType.LITERAL, io.sinistral.proteus.server.Extractors.Optional.class, StatementParameterType.STRING),
    OptionalLongType("$T<Long> $L = $T.longValue(exchange,$S)", false, Optional.class, StatementParameterType.LITERAL, io.sinistral.proteus.server.Extractors.Optional.class, StatementParameterType.STRING),
    OptionalIntegerType("$T<Integer> $L = $T.integerValue(exchange,$S)", false, Optional.class, StatementParameterType.LITERAL, io.sinistral.proteus.server.Extractors.Optional.class, StatementParameterType.STRING),
    OptionalBooleanType("$T<Boolean> $L = $T.booleanValue(exchange,$S)", false, Optional.class, StatementParameterType.LITERAL, io.sinistral.proteus.server.Extractors.Optional.class, StatementParameterType.STRING),
    OptionalFilePathType("$T<$T> $L = $T.filePath(exchange,$S)", true, Optional.class, java.nio.file.Path.class, StatementParameterType.LITERAL, io.sinistral.proteus.server.Extractors.Optional.class, StatementParameterType.STRING),

    OptionalNamedByteBufferType("$T<$T> $L = $T.namedByteBuffer(exchange,$S)", true, Optional.class, java.nio.ByteBuffer.class, StatementParameterType.LITERAL, io.sinistral.proteus.server.Extractors.Optional.class, StatementParameterType.STRING),

    OptionalByteBufferType("$T<$T> $L = $T.byteBuffer(exchange)", true, Optional.class, java.nio.ByteBuffer.class, StatementParameterType.LITERAL, io.sinistral.proteus.server.Extractors.Optional.class),

    OptionalFileType("$T<$T> $L = $T.file(exchange,$S)", true, Optional.class, java.io.File.class, StatementParameterType.LITERAL, io.sinistral.proteus.server.Extractors.Optional.class, StatementParameterType.STRING),

    OptionalFloatType("$T<Float> $L = $T.floatValue(exchange,$S)", false, Optional.class, StatementParameterType.LITERAL, io.sinistral.proteus.server.Extractors.Optional.class, StatementParameterType.STRING),
    OptionalDoubleType("$T<Double> $L = $T.doubleValue(exchange,$S)", false, Optional.class, StatementParameterType.LITERAL, io.sinistral.proteus.server.Extractors.Optional.class, StatementParameterType.STRING),
    OptionalBigDecimalType("$T<BigDecimal> $L = $T.bigDecimalValue(exchange,$S)", false, Optional.class, StatementParameterType.LITERAL, io.sinistral.proteus.server.Extractors.Optional.class, StatementParameterType.STRING),

    OptionalDateType("$T<$T> $L = $T.date(exchange,$S)", false, Optional.class, java.util.Date.class, StatementParameterType.LITERAL, io.sinistral.proteus.server.Extractors.Optional.class, StatementParameterType.STRING),
    OptionalInstantType("$T<$T> $L = $T.instant(exchange,$S)", false, Optional.class, java.time.Instant.class, StatementParameterType.LITERAL, io.sinistral.proteus.server.Extractors.Optional.class, StatementParameterType.STRING),
    OptionalZonedDateTimeType("$T<$T> $L = $T.zonedDateTime(exchange,$S)", false, Optional.class, java.time.ZonedDateTime.class, StatementParameterType.LITERAL, io.sinistral.proteus.server.Extractors.Optional.class, StatementParameterType.STRING),
    OptionalOffsetDateTimeType("$T<$T> $L = $T.offsetDateTime(exchange,$S)", false, Optional.class, java.time.OffsetDateTime.class, StatementParameterType.LITERAL, io.sinistral.proteus.server.Extractors.Optional.class, StatementParameterType.STRING),

    OptionalModelType("java.util.Optional<$L> $L = $T.model(exchange,$L)", false, StatementParameterType.LITERAL, StatementParameterType.LITERAL, io.sinistral.proteus.server.Extractors.Optional.class, StatementParameterType.LITERAL),

    OptionalNamedJsonNodeType("$T<$T> $L = $T.namedJsonNode(exchange,$s)", true, Optional.class, com.fasterxml.jackson.databind.JsonNode.class, StatementParameterType.LITERAL, io.sinistral.proteus.server.Extractors.Optional.class, StatementParameterType.STRING),
    OptionalNamedModelType("java.util.Optional<$L> $L = $T.namedModel(exchange,$L,$S)", false, StatementParameterType.LITERAL, StatementParameterType.LITERAL, io.sinistral.proteus.server.Extractors.Optional.class, StatementParameterType.LITERAL),

    OptionalValueOfType("$T<$T> $L = $T.string(exchange,$S).map($T::valueOf)", false, Optional.class, StatementParameterType.RAW, StatementParameterType.LITERAL, io.sinistral.proteus.server.Extractors.Optional.class, StatementParameterType.STRING, StatementParameterType.RAW),
    OptionalFromStringType("$T<$T> $L = $T.string(exchange,$S).map($T::fromString)", false, Optional.class, StatementParameterType.RAW, StatementParameterType.LITERAL, io.sinistral.proteus.server.Extractors.Optional.class, StatementParameterType.STRING, StatementParameterType.RAW),

    OptionalFileMapType("$T $L = io.sinistral.proteus.server.Extractors.fileMap(exchange,$S)", true, StatementParameterType.TYPE, StatementParameterType.LITERAL, StatementParameterType.STRING),
    OptionalPathMapType("$T $L = io.sinistral.proteus.server.Extractors.pathMap(exchange,$S)", true, StatementParameterType.TYPE, StatementParameterType.LITERAL, StatementParameterType.STRING),
    // OptionalEnumType("$T $L = $T.enumValue(exchange,$T.class,$S)", true,
    // StatementParameterType.TYPE, StatementParameterType.LITERAL,
    // io.sinistral.proteus.server.Extractors.Optional.class,
    // StatementParameterType.RAW, StatementParameterType.STRING),

    ;

    private static final Logger log = LoggerFactory.getLogger(TypeHandler.class.getName());

    public boolean isBlocking()
    {

        return this.isBlocking;
    }

    public String statement()
    {

        return this.statement;
    }

    /**
     * The template statement for the
     * {@link com.squareup.javapoet.MethodSpec.Builder} to use
     */
    final String statement;

    /**
     * If the <code>TypeReference</code> requires the
     * {@link io.undertow.server.HttpHandler} to block
     */
    final private boolean isBlocking;

    /**
     * An {@code Object} array that is passed to the {@code statement}
     */
    final private Object[] parameterTypes;

    TypeHandler(String statement, boolean isBlocking, Object... types)
    {

        this.statement = statement;
        this.isBlocking = isBlocking;
        this.parameterTypes = types;
    }

    /**
     * Helper function to bind values to a
     * {@link com.squareup.javapoet.MethodSpec.Builder}
     *
     * @param builder
     * @param parameter
     * @param handler
     * @throws Exception
     */
    public static void addStatement(MethodSpec.Builder builder, Parameter parameter, TypeHandler handler) throws Exception
    {

        Object[] args = new Object[handler.parameterTypes.length];

        String pName = parameter.getName();

        for (int i = 0; i < handler.parameterTypes.length; i++)
        {

            if (handler.parameterTypes[i] instanceof StatementParameterType)
            {

                if (parameter.isAnnotationPresent(QueryParam.class))
                {
                    QueryParam qp = parameter.getAnnotation(QueryParam.class);
                    pName = qp.value();
                }
                else if (parameter.isAnnotationPresent(HeaderParam.class))
                {
                    HeaderParam hp = parameter.getAnnotation(HeaderParam.class);
                    pName = hp.value();
                }
                else if (parameter.isAnnotationPresent(PathParam.class))
                {
                    PathParam pp = parameter.getAnnotation(PathParam.class);
                    pName = pp.value();
                }
                else if (parameter.isAnnotationPresent(CookieParam.class))
                {
                    CookieParam cp = parameter.getAnnotation(CookieParam.class);
                    pName = cp.value();
                }
                else if (parameter.isAnnotationPresent(FormParam.class))
                {
                    FormParam fp = parameter.getAnnotation(FormParam.class);
                    pName = fp.value();
                }

                StatementParameterType pType = (StatementParameterType) handler.parameterTypes[i];
                switch (pType)
                {
                    case LITERAL:
                        args[i] = parameter.getName();
                        break;
                    case STRING:
                        args[i] = pName;
                        break;
                    case TYPE:
                        args[i] = parameter.getParameterizedType();
                        break;
                    case RAW:
                    {
                        Type type = parameter.getParameterizedType();
                        type = HandlerGenerator.extractErasedType(type);
                        args[i] = type;
                        break;
                    }
                    default:
                        break;
                }
            }
            else if (handler.parameterTypes[i] instanceof Class)
            {
                Class<?> clazz = (Class<?>) handler.parameterTypes[i];

                args[i] = clazz;

            }
        }

        if (handler.equals(BeanListValueOfType))
        {
            HandlerGenerator.log.debug(parameter.getName() + " " + Arrays.toString(args) + " " + handler);
        }

        builder.addStatement(handler.statement, args);

        Max max = parameter.isAnnotationPresent(Max.class) ? parameter.getAnnotationsByType(Max.class)[0] : null;

        Min min = parameter.isAnnotationPresent(Min.class) ? parameter.getAnnotationsByType(Min.class)[0] : null;

        if (max != null || min != null)
        {
            if (max != null && min != null)
            {
                long maxValue = min.value();
                long minValue = min.value();

                builder.beginControlFlow("if( $L < $L )", pName, minValue);
                builder.addStatement("throw new io.sinistral.proteus.server.exceptions.ServerException($S,javax.ws.rs.core.Response.Status.BAD_REQUEST)", min.message()
                                                                                                                                                             .equals("{javax.validation.constraints.Min.message}") ? "must be greater than or equal to " + minValue : min.message());
                builder.endControlFlow();
                builder.beginControlFlow("else if( $L > $L )", pName, maxValue);
                builder.addStatement("throw new io.sinistral.proteus.server.exceptions.ServerException($S,javax.ws.rs.core.Response.Status.BAD_REQUEST)", max.message()
                                                                                                                                                             .equals("{javax.validation.constraints.Max.message}") ? "must be less than or equal to " + maxValue : max.message());
                builder.endControlFlow();

            }
            else if (max != null)
            {
                long maxValue = max.value();

                builder.beginControlFlow("if( $L > $L )", pName, maxValue);
                builder.addStatement("throw new io.sinistral.proteus.server.exceptions.ServerException($S,javax.ws.rs.core.Response.Status.BAD_REQUEST)", max.message()
                                                                                                                                                             .equals("{javax.validation.constraints.Max.message}") ? "must be less than or equal to " + maxValue : max.message());
                builder.endControlFlow();
            }
            else
            {
                long minValue = min.value();

                builder.beginControlFlow("if( $L < $L )", pName, minValue);
                builder.addStatement("throw new io.sinistral.proteus.server.exceptions.ServerException($S,javax.ws.rs.core.Response.Status.BAD_REQUEST)", min.message()
                                                                                                                                                             .equals("{javax.validation.constraints.Min.message}") ? "must be greater than or equal to " + minValue : min.message());
                builder.endControlFlow();
            }
        }
    }

    /**
     * Helper function to bind a {@link Parameter} to a
     * {@link com.squareup.javapoet.MethodSpec.Builder}
     *
     * @param builder
     * @param parameter
     * @throws Exception
     */
    public static void addStatement(MethodSpec.Builder builder, Parameter parameter) throws Exception
    {

        BeanParam beanParam = parameter.getAnnotation(BeanParam.class);

        boolean isBeanParameter = beanParam != null;

        TypeHandler handler = TypeHandler.forType(parameter.getParameterizedType(), isBeanParameter);

        FormParam formParam = parameter.getAnnotation(FormParam.class);

        if (formParam != null)
        {
            switch (handler)
            {
                case JsonNodeType:
                {
                    handler = NamedJsonNodeType;
                    break;
                }
                case ByteBufferType:
                {
                    handler = NamedByteBufferType;
                    break;
                }
                case OptionalJsonNodeType:
                {
                    handler = OptionalNamedJsonNodeType;
                    break;
                }
                case OptionalByteBufferType:
                {
                    handler = OptionalNamedByteBufferType;
                    break;
                }
            }
        }

//			if(handler.equals(TypeHandler.ModelType))
//			{
//				HandlerGenerator.log.warn("found modeltype for " + parameter.getParameterizedType());
//			}
        addStatement(builder, parameter, handler);

    }

    public static TypeHandler forType(Type type)
    {

        return forType(type, false);
    }

    /**
     * Lookup the <code>TypeHandler</code> for a {@link Type}
     */
    public static TypeHandler forType(Type type, Boolean isBeanParam)
    {

        boolean hasValueOf = false;
        boolean hasFromString = false;
        boolean isOptional = type.getTypeName().contains("java.util.Optional");
        boolean isArray = type.getTypeName().contains("java.util.List");
        boolean isSet = type.getTypeName().contains("java.util.Set");
        boolean isMap = type.getTypeName().contains("java.util.Map");
        boolean isParameterized = type instanceof ParameterizedType;

        if (!isOptional && !isArray && !isSet && !isMap && !isParameterized)
        {
            try
            {


                Class<?> clazz = Class.forName(type.getTypeName());

                hasValueOf = HandlerGenerator.hasValueOfMethod(clazz);

                hasFromString = HandlerGenerator.hasFromStringMethod(clazz);

            } catch (Exception e)
            {
                HandlerGenerator.log.error("failed to get class {}", type.getTypeName() , e);

            }
        }

        if (isArray && !isOptional)
        {
            try
            {
                Class<?> erasedType = (Class<?>) HandlerGenerator.extractErasedType(type);

                if (HandlerGenerator.hasValueOfMethod(erasedType))
                {
                    if (!isBeanParam)
                    {
                        return QueryListValueOfType;
                    }
                    else
                    {

                        return BeanListValueOfType;

                    }
                }
                else if (HandlerGenerator.hasFromStringMethod(erasedType))
                {
                    if (!isBeanParam)
                    {
                        return QueryListFromStringType;

                    }
                    else
                    {
                        return BeanListFromStringType;
                    }
                }
                else if (erasedType.getTypeName().contains("java.nio.file.Path"))
                {
                    return PathListType;
                }
                else if (erasedType.getTypeName().contains("java.io.File"))
                {
                    return FileListType;
                }
                else
                {
                    return ModelType;
                }

            } catch (Exception e)
            {
                log.error(e.getMessage(), e);
                throw e;
            }
        }

        if (isMap && !isOptional)
        {
            try
            {

                ParameterizedType erasedType = (ParameterizedType) HandlerGenerator.extractErasedType(type);

                Type[] types = erasedType.getActualTypeArguments();

                if (types[1].getTypeName().contains("java.nio.file.Path"))
                {
                    return PathMapType;
                }
                else if (types[1].getTypeName().contains("java.io.File"))
                {
                    return FileMapType;
                }
                else
                {
                    return ModelType;
                }

            } catch (Exception e)
            {
                HandlerGenerator.log.error(e.getMessage(), e);
                throw e;
            }
        }

        if (isSet && !isOptional)
        {
            try
            {
                Class<?> erasedType = (Class<?>) HandlerGenerator.extractErasedType(type);

                if (HandlerGenerator.hasValueOfMethod(erasedType))
                {
                    if (!isBeanParam)
                    {
                        return QuerySetValueOfType;

                    }
                    else
                    {
                        return BeanListValueOfType;
                    }
                }
                else if (HandlerGenerator.hasFromStringMethod(erasedType))
                {
                    if (!isBeanParam)
                    {
                        return QuerySetFromStringType;

                    }
                    else
                    {
                        return BeanListFromStringType;
                    }
                }
                else
                {
                    return ModelType;
                }

            } catch (Exception e)
            {
                HandlerGenerator.log.error(e.getMessage(), e);
                throw e;

            }
        }
        else if (isArray)
        {
            try
            {

                if (type instanceof ParameterizedType)
                {
                    ParameterizedType pType = (ParameterizedType) type;
                    type = pType.getActualTypeArguments()[0];
                }

                Class<?> erasedType = (Class<?>) HandlerGenerator.extractErasedType(type);

                if (HandlerGenerator.hasValueOfMethod(erasedType))
                {
                    if (!isBeanParam)
                    {
                        return QueryOptionalListValueOfType;

                    }
                    else
                    {
                        return OptionalBeanListValueOfType;
                    }

                }
                else if (HandlerGenerator.hasFromStringMethod(erasedType))
                {
                    if (!isBeanParam)
                    {
                        return QueryOptionalListFromStringType;

                    }
                    else
                    {
                        return OptionalBeanListFromStringType;
                    }
                }
                else
                {
                    return ModelType;
                }

            } catch (Exception e)
            {
                HandlerGenerator.log.error(e.getMessage(), e);
                throw e;
            }
        }
        else if (isMap)
        {
            try
            {

                return ModelType;

            } catch (Exception e)
            {
                HandlerGenerator.log.error(e.getMessage(), e);
                throw e;
            }
        }
        else if (isSet)
        {
            try
            {

                if (type instanceof ParameterizedType)
                {
                    ParameterizedType pType = (ParameterizedType) type;
                    type = pType.getActualTypeArguments()[0];
                }

                Class<?> erasedType = (Class<?>) HandlerGenerator.extractErasedType(type);

                if (HandlerGenerator.hasValueOfMethod(erasedType))
                {
                    if (!isBeanParam)
                    {
                        return QueryOptionalSetValueOfType;

                    }
                    else
                    {
                        return OptionalBeanListValueOfType;
                    }

                }
                else if (HandlerGenerator.hasFromStringMethod(erasedType))
                {
                    if (!isBeanParam)
                    {
                        return QueryOptionalSetFromStringType;

                    }
                    else
                    {
                        return OptionalBeanListFromStringType;
                    }
                }
                else
                {
                    return ModelType;
                }

            } catch (Exception e)
            {
                HandlerGenerator.log.error(e.getMessage(), e);
                throw e;

            }
        }

        if (type.equals(Long.class))
        {
            return LongType;
        }
        else if (type.equals(Integer.class))
        {
            return IntegerType;
        }
        else if (type.equals(Float.class))
        {
            return FloatType;
        }
        else if (type.equals(Double.class))
        {
            return DoubleType;
        }
        else if (type.equals(BigDecimal.class))
        {
            return BigDecimalType;
        }
        else if (type.equals(java.nio.ByteBuffer.class))
        {
            return ByteBufferType;
        }
        else if (type.equals(Boolean.class))
        {
            return BooleanType;
        }
        else if (type.equals(String.class))
        {
            return StringType;
        }
        else if (type.equals(java.nio.file.Path.class))
        {
            return FilePathType;
        }
        else if (type.equals(java.io.File.class))
        {
            return FileType;
        }
        else if (type.equals(java.time.Instant.class))
        {
            return InstantType;
        }
        else if (type.equals(java.util.Date.class))
        {
            return DateType;
        }
        else if (type.equals(java.time.ZonedDateTime.class))
        {
            return ZonedDateTimeType;
        }
        else if (type.equals(java.time.OffsetDateTime.class))
        {
            return OffsetDateTimeType;
        }
        else if (type.equals(com.fasterxml.jackson.databind.JsonNode.class))
        {
            return JsonNodeType;
        }
        else if (type.equals(com.fasterxml.jackson.databind.JsonNode.class))
        {
            return JsonNodeType;
        }
        else if (isOptional)
        {
            if (type.getTypeName().contains("java.lang.Long"))
            {
                return OptionalLongType;
            }
            else if (type.getTypeName().contains("java.lang.String"))
            {
                return OptionalStringType;
            }
            else if (type.getTypeName().contains("java.util.Date"))
            {
                return OptionalDateType;
            }
            else if (type.getTypeName().contains("java.time.OffsetDateTime"))
            {
                return OptionalOffsetDateTimeType;
            }
            else if (type.getTypeName().contains("java.time.Instant"))
            {
                return OptionalInstantType;
            }
            else if (type.getTypeName().contains("java.time.ZonedDateTime"))
            {
                return ZonedDateTimeType;
            }
            else if (type.getTypeName().contains("java.lang.Boolean"))
            {
                return OptionalBooleanType;
            }
            else if (type.getTypeName().contains("java.lang.Float"))
            {
                return OptionalFloatType;
            }
            else if (type.getTypeName().contains("java.lang.Double"))
            {
                return OptionalDoubleType;
            }
            else if (type.getTypeName().contains("java.math.BigDecimal"))
            {
                return OptionalBigDecimalType;
            }
            else if (type.getTypeName().contains("java.lang.Integer"))
            {
                return OptionalIntegerType;
            }
            else if (type.getTypeName().contains("java.nio.file.Path"))
            {
                return OptionalFilePathType;
            }
            else if (type.getTypeName().contains("java.nio.ByteBuffer"))
            {
                return OptionalByteBufferType;
            }
            else if (type.getTypeName().contains("java.io.File"))
            {
                return OptionalFileType;
            }
            else
            {
                try
                {

                    Class<?> erasedType = (Class<?>) HandlerGenerator.extractErasedType(type);

                    if (HandlerGenerator.hasValueOfMethod(erasedType))
                    {
                        return OptionalValueOfType;

                    }
                    else if (HandlerGenerator.hasFromStringMethod(erasedType))
                    {
                        return OptionalFromStringType;
                    }

                    if (isBeanParam)
                    {
                        return OptionalModelType;
                    }

                } catch (Exception e)
                {
                    HandlerGenerator.log.error("error : " + e.getMessage(), e);
                    return OptionalStringType;
                }

                return OptionalStringType;
            }
        }
        else if (hasValueOf)
        {
            return ValueOfType;
        }
        else if (hasFromString)
        {
            return FromStringType;
        }
        else
        {
            return ModelType;
        }
    }
}