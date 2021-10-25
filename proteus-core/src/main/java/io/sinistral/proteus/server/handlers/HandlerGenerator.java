/**
 *
 */
package io.sinistral.proteus.server.handlers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import io.sinistral.proteus.annotations.Blocking;
import io.sinistral.proteus.annotations.Debug;
import io.sinistral.proteus.server.Extractors;
import io.sinistral.proteus.server.ServerRequest;
import io.sinistral.proteus.server.ServerResponse;
import io.sinistral.proteus.server.endpoints.EndpointInfo;
import io.undertow.server.HandlerWrapper;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.RoutingHandler;
import io.undertow.server.handlers.form.FormEncodedDataDefinition;
import io.undertow.server.handlers.form.MultiPartParserDefinition;
import io.undertow.util.Headers;
import io.undertow.util.HttpString;
import net.openhft.compiler.CachedCompiler;
import org.apache.commons.lang3.StringUtils;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.lang.model.element.Modifier;
import javax.ws.rs.BeanParam;
import javax.ws.rs.FormParam;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Generates code and compiles a <code>Supplier<RoutingHandler></code> class
 * from the target class's methods that are annotated with a JAX-RS method
 * annotation (i.e. <code>javax.ws.rs.GET</code>)
 * @author jbauer
 */
public class HandlerGenerator {

    static Logger log = LoggerFactory.getLogger(HandlerGenerator.class.getCanonicalName());

    private static final Pattern TYPE_NAME_PATTERN = Pattern.compile("(java\\.util\\.[A-Za-z]+)<([^>]+)", Pattern.DOTALL | Pattern.UNIX_LINES);
    private static final Pattern CONCURRENT_TYPE_NAME_PATTERN = Pattern.compile("(java\\.util\\.concurrent\\.[A-Za-z]+)<([^>]+)", Pattern.DOTALL | Pattern.UNIX_LINES);

    public enum StatementParameterType {
        STRING, LITERAL, TYPE, RAW
    }

    @Inject
    @Named("application.path")
    protected String applicationPath;

    protected String packageName;
    protected String className;
    protected String sourceString;

    @Inject
    @Named("registeredEndpoints")
    protected Set<EndpointInfo> registeredEndpoints;

    protected Class<?> controllerClass;

    protected Set<String> injectedHandlerWrappers = new HashSet<>();

    @Inject
    @Named("registeredHandlerWrappers")
    protected Map<String, HandlerWrapper> registeredHandlerWrappers;

    protected Map<String, Class<? extends HandlerWrapper>> registeredWrapperTypes = new HashMap<>();

    protected Map<Class<? extends HandlerWrapper>, String> typeLevelHandlerWrapperMap = new LinkedHashMap<>();

    /**
     * Create a new {@code HandlerGenerator} instance used to generate a
     * {@code Supplier<RoutingHandler>} class
     * @param packageName
     *            generated class package name
     * @param controllerClass
     *            the class handlers will be generated from this class
     */
    public HandlerGenerator(String packageName, Class<?> controllerClass)
    {

        this.packageName = packageName;
        this.controllerClass = controllerClass;
        this.className = controllerClass.getSimpleName() + "RouteSupplier";
    }

    /**
     * Compiles the generated source into a new {@link Class}
     * @return a new {@code Supplier<RoutingHandler>} class
     */
    public Class<? extends Supplier<RoutingHandler>> compileClass() throws Exception
    {

        try
        {
            this.generateRoutes();

            log.debug("\n\nGenerated Class Source:\n\n{}", this.sourceString);

            CachedCompiler cachedCompiler = new CachedCompiler(null, null);

            return cachedCompiler.loadFromJava(packageName + "." + className, this.sourceString);

        } catch (Exception e)
        {
            log.error("Failed to compile {}\nSource:\n{}", packageName + "." + className, this.sourceString, e);
            throw e;
        }
    }

    /**
     * Generates the routing Java source code
     */
    protected void generateRoutes() throws Exception
    {

        TypeSpec.Builder typeBuilder = TypeSpec.classBuilder(className).addModifiers(Modifier.PUBLIC)
                                               .addSuperinterface(ParameterizedTypeName.get(Supplier.class, RoutingHandler.class));

        ClassName extractorClass = ClassName.get("io.sinistral.proteus.server", "Extractors");

        ClassName injectClass = ClassName.get("com.google.inject", "Inject");

        MethodSpec.Builder constructor = MethodSpec.constructorBuilder().addModifiers(Modifier.PUBLIC).addAnnotation(injectClass);

        String className = this.controllerClass.getSimpleName().toLowerCase() + "Controller";

        typeBuilder.addField(this.controllerClass, className, Modifier.PROTECTED, Modifier.FINAL);

        ClassName wrapperClass = ClassName.get("io.undertow.server", "HandlerWrapper");
        ClassName stringClass = ClassName.get("java.lang", "String");
        ClassName mapClass = ClassName.get("java.util", "Map");

        TypeName mapOfWrappers = ParameterizedTypeName.get(mapClass, stringClass, wrapperClass);

        TypeName annotatedMapOfWrappers = mapOfWrappers
                .annotated(AnnotationSpec.builder(com.google.inject.name.Named.class).addMember("value", "$S", "registeredHandlerWrappers").build());

        typeBuilder.addField(mapOfWrappers, "registeredHandlerWrappers", Modifier.PROTECTED, Modifier.FINAL);

        constructor.addParameter(this.controllerClass, className);
        constructor.addParameter(annotatedMapOfWrappers, "registeredHandlerWrappers");

        constructor.addStatement("this.$N = $N", className, className);
        constructor.addStatement("this.$N = $N", "registeredHandlerWrappers", "registeredHandlerWrappers");

        addClassMethodHandlers(typeBuilder, this.controllerClass);

        registeredWrapperTypes.forEach((key, value) -> {

            TypeName typeName = TypeName.get(value);

            typeBuilder.addField(typeName, key, Modifier.PROTECTED, Modifier.FINAL);

            constructor.addParameter(typeName, key);

            constructor.addStatement("this.$N = $N", key, key);
        });

        typeBuilder.addMethod(constructor.build());

        JavaFile javaFile = JavaFile.builder(packageName, typeBuilder.build()).addStaticImport(extractorClass, "*").build();

        StringBuilder sb = new StringBuilder();

        javaFile.writeTo(sb);

        this.sourceString = sb.toString();

    }

    protected void addClassMethodHandlers(TypeSpec.Builder typeBuilder, Class<?> clazz) throws Exception
    {

        ClassName httpHandlerClass = ClassName.get("io.undertow.server", "HttpHandler");

        String controllerName = clazz.getSimpleName().toLowerCase() + "Controller";

        int handlerWrapperIndex = 1;

        HashSet<String> handlerNameSet = new HashSet<>();

        MethodSpec.Builder initBuilder = MethodSpec.methodBuilder("get").addModifiers(Modifier.PUBLIC).returns(RoutingHandler.class)
                                                   .addStatement("final $T router = new $T()", io.undertow.server.RoutingHandler.class, io.undertow.server.RoutingHandler.class);

        final Map<Type, String> parameterizedLiteralsNameMap = Arrays.stream(clazz.getDeclaredMethods())
                                                                     .filter(m -> m.getAnnotation(Path.class) != null)
                                                                     .flatMap(
                                                                             m -> Arrays.stream(m.getParameters()).map(Parameter::getParameterizedType)
                                                                                        .filter(t -> t.getTypeName().contains("<") && !t.getTypeName().contains("concurrent")))
                                                                     .distinct().filter(t ->
                {

                    TypeHandler handler = TypeHandler.forType(t);
                    return (handler.equals(TypeHandler.ModelType) || handler.equals(TypeHandler.OptionalModelType) || handler.equals(TypeHandler.NamedModelType) || handler.equals(TypeHandler.OptionalNamedModelType));

                }).collect(Collectors.toMap(java.util.function.Function.identity(), HandlerGenerator::typeReferenceNameForParameterizedType));

        Arrays.stream(clazz.getDeclaredMethods())
              .filter(m -> m.getAnnotation(Path.class) != null)
              .flatMap(m -> Arrays.stream(m.getParameters()))
              .forEach(p ->
              {

                  BeanParam beanParam = p.getAnnotation(BeanParam.class);

                  boolean isBeanParameter = beanParam != null;

                  if (isBeanParameter)
                  {
                      TypeHandler handler = TypeHandler.forType(p.getParameterizedType(), true);

                      if (handler.equals(TypeHandler.BeanListValueOfType)
                              || handler.equals(TypeHandler.BeanListFromStringType)
                              || handler.equals(TypeHandler.OptionalBeanListValueOfType)
                              || handler.equals(TypeHandler.OptionalBeanListFromStringType))
                      {
                          parameterizedLiteralsNameMap.put(p.getParameterizedType(), HandlerGenerator.typeReferenceNameForParameterizedType(p.getParameterizedType()));
                      }
                  }

              });

        final Map<Type, String> literalsNameMap = Arrays.stream(clazz.getDeclaredMethods())
                                                        .filter(m -> m.getAnnotation(Path.class) != null)
                                                        .flatMap(m -> Arrays.stream(m.getParameters())
                                                                            .map(Parameter::getParameterizedType)).filter(t ->
                {

                    if (t.getTypeName().contains("java.util"))
                    {
                        return false;
                    }

                    try
                    {
                        Class<?> optionalType = (Class<?>) extractErasedType(t);

                        if (optionalType != null)
                        {
                            t = optionalType;
                        }

                    } catch (Exception ignored)
                    {

                    }

                    if (t.getTypeName().contains("java.lang"))
                    {
                        return false;
                    }
                    else if (t.getTypeName().contains("java.nio"))
                    {
                        return false;
                    }
                    else if (t.getTypeName().contains("java.io"))
                    {
                        return false;
                    }
                    else if (t.getTypeName().contains("java.util"))
                    {
                        return false;
                    }
                    else if (t.equals(HttpServerExchange.class) || t.equals(ServerRequest.class))
                    {
                        return false;
                    }

                    if (t instanceof Class)
                    {
                        Class<?> pClazz = (Class<?>) t;
                        if (pClazz.isPrimitive())
                        {
                            return false;
                        }
                        return !pClazz.isEnum();

                    }

                    return true;

                })
                                                        .distinct()
                                                        .collect(Collectors.toMap(java.util.function.Function.identity(), HandlerGenerator::typeReferenceNameForType));

        parameterizedLiteralsNameMap
                .forEach((t, n) -> initBuilder.addStatement("final $T<$L> $LTypeReference = new $T<$L>(){}", TypeReference.class, t, n, TypeReference.class, t));

        literalsNameMap.forEach((t, n) -> initBuilder.addStatement("final $T<$T> $LTypeReference = new $T<$T>(){}", TypeReference.class, t, n, TypeReference.class, t));

        Optional<io.sinistral.proteus.annotations.Chain> typeLevelWrapAnnotation = Optional.ofNullable(clazz.getAnnotation(io.sinistral.proteus.annotations.Chain.class));

        /*
        CLASS LEVEL WRAPPERS
         */

        if (typeLevelWrapAnnotation.isPresent())
        {
            io.sinistral.proteus.annotations.Chain w = typeLevelWrapAnnotation.get();

            Class<? extends HandlerWrapper>[] wrapperClasses = w.value();

            for (Class<? extends HandlerWrapper> wrapperClass : wrapperClasses)
            {

                String wrapperName = generateFieldName(wrapperClass.getCanonicalName());

                registeredWrapperTypes.put(wrapperName, wrapperClass);

                typeLevelHandlerWrapperMap.put(wrapperClass, wrapperName);
            }
        }

        initBuilder.addStatement("$T currentHandler = $L", HttpHandler.class, "null");

        initBuilder.addCode("$L", "\n");

        List<String> consumesContentTypes;
        List<String> producesContentTypes;

        /*
         * Controller Level Authorization
         */

        List<String> typeLevelSecurityDefinitions = new ArrayList<>();

        if (Optional.ofNullable(clazz.getAnnotation(Path.class)).isPresent())
        {

            Annotation[] annotations = clazz.getAnnotations();

            Annotation securityRequirementAnnotation = Arrays.stream(annotations).filter(a -> a.getClass().getName().contains("SecurityRequirement" +
                    "")).findFirst().orElse(null);

            if (securityRequirementAnnotation != null)
            {

                if (securityRequirementAnnotation != null)
                {

                    try
                    {
                        Field nameField = securityRequirementAnnotation.getClass().getField("name");

                        if (nameField != null)
                        {
                            Object securityRequirement = nameField.get(securityRequirementAnnotation);
                            typeLevelSecurityDefinitions.add(securityRequirement.toString());
                        }

                    } catch (Exception e)
                    {
                        log.warn("No name field on security requirement");
                    }

                }

            }
        }

        log.debug("Scanning methods for class {}", clazz.getName());

        int nameIndex = 1;

        for (Method m : clazz.getDeclaredMethods())
        {

            if (!Optional.ofNullable(m.getAnnotation(javax.ws.rs.Path.class)).isPresent())
            {
                continue;
            }

            log.debug("\n\nScanning method: {}\n", m.getName());

            EndpointInfo endpointInfo = new EndpointInfo();

            String producesContentType = "*/*";
            String consumesContentType = "*/*";

            boolean isBlocking = false;
            boolean isDebug = false;

            Optional<Blocking> blockingAnnotation = Optional.ofNullable(m.getAnnotation(Blocking.class));

            if (blockingAnnotation.isPresent())
            {
                isBlocking = blockingAnnotation.get().value();
            }

            Optional<Debug> debugAnnotation = Optional.ofNullable(m.getAnnotation(Debug.class));

            if (debugAnnotation.isPresent())
            {
                isDebug = debugAnnotation.get().value();
            }

            Optional<javax.ws.rs.Produces> producesAnnotation = Optional.ofNullable(m.getAnnotation(javax.ws.rs.Produces.class));

            if (!producesAnnotation.isPresent())
            {
                producesAnnotation = Optional.ofNullable(clazz.getAnnotation(javax.ws.rs.Produces.class));

                if (producesAnnotation.isPresent())
                {

                    producesContentTypes = Arrays.stream(producesAnnotation.get().value()).flatMap(v -> Arrays.stream((v.split(",")))).collect(Collectors.toList());

                    producesContentType = String.join(",", producesContentTypes);
                }

            }
            else
            {

                producesContentTypes = Arrays.stream(producesAnnotation.get().value()).flatMap(v -> Arrays.stream((v.split(",")))).collect(Collectors.toList());

                producesContentType = producesContentTypes.stream().collect(Collectors.joining(","));
            }

            endpointInfo.setProduces(producesContentType);

            Optional<javax.ws.rs.Consumes> consumesAnnotation = Optional.ofNullable(m.getAnnotation(javax.ws.rs.Consumes.class));

            if (!consumesAnnotation.isPresent())
            {
                consumesAnnotation = Optional.ofNullable(clazz.getAnnotation(javax.ws.rs.Consumes.class));

                if (consumesAnnotation.isPresent())
                {
                    consumesContentTypes = Arrays.stream(consumesAnnotation.get().value()).flatMap(v -> Arrays.stream((v.split(",")))).collect(Collectors.toList());

                    consumesContentType = String.join(",", consumesContentTypes);
                }
            }
            else
            {
                consumesContentTypes = Arrays.stream(consumesAnnotation.get().value()).flatMap(v -> Arrays.stream((v.split(",")))).collect(Collectors.toList());

                consumesContentType = String.join(",", consumesContentTypes);
            }

            endpointInfo.setControllerName(clazz.getSimpleName());

            String methodPath;

            try
            {
                methodPath = Extractors.pathTemplateFromMethod.apply(m).replaceAll("\\/\\/", "\\/");
            } catch (Exception e)
            {
                log.error("Error parsing method path for {}",m.getName(), e);
                continue;
            }

            methodPath = applicationPath + methodPath;

            HttpString httpMethod = Extractors.httpMethodFromMethod.apply(m);

            endpointInfo.setMethod(httpMethod);

            endpointInfo.setConsumes(consumesContentType);

            //The handler for these two inputs types is blocking, so we set the flag
            if (endpointInfo.getConsumes().contains(FormEncodedDataDefinition.APPLICATION_X_WWW_FORM_URLENCODED)
                    || endpointInfo.getConsumes().contains(MultiPartParserDefinition.MULTIPART_FORM_DATA))
            {
                isBlocking = true;
            }

            endpointInfo.setPathTemplate(methodPath);

            endpointInfo.setControllerMethod(m.getName());

            String handlerName = String.format("%c%s%sHandler_%s", Character.toLowerCase(clazz.getSimpleName().charAt(0)), clazz.getSimpleName()
                                                                                                                                .substring(1), StringUtils.capitalize(m.getName()), String.valueOf(nameIndex++));

            handlerNameSet.add(handlerName);

            TypeSpec.Builder handlerClassBuilder = TypeSpec.anonymousClassBuilder("").addSuperinterface(httpHandlerClass);

            /**
             * @TODO
             * Rewrite with lambdas or method references.
             *
             * 		final io.undertow.server.HttpHandler benchmarksDbPostgresHandler = (final HttpServerExchange exchange) ->
             *        {
             * 			benchmarksController.dbPostgres;
             *        });
             *
             * 	OR
             *
             * 	  final io.undertow.server.HttpHandler benchmarksDbPostgresHandler = benchmarksController::dbPostgres;
             **/

            MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder("handleRequest").addModifiers(Modifier.PUBLIC).addException(ClassName.get("java.lang", "Exception"))
                                                         .addAnnotation(Override.class)
                                                         .addParameter(ParameterSpec.builder(HttpServerExchange.class, "exchange", Modifier.FINAL).build());

            for (Parameter p : m.getParameters())
            {

                if (p.getParameterizedType().equals(ServerRequest.class)
                        || p.getParameterizedType().equals(HttpServerExchange.class)
                        || p.getParameterizedType().equals(HttpHandler.class))
                {
                    continue;
                }

                try
                {
                    BeanParam beanParam = p.getAnnotation(BeanParam.class);

                    boolean isBeanParameter = beanParam != null;

                    TypeHandler t = TypeHandler.forType(p.getParameterizedType(), isBeanParameter);

                    if (t.isBlocking())
                    {
                        isBlocking = true;
                        break;
                    }

                } catch (Exception e)
                {
                    log.error("Error processing path parameter {} for method {}",p.getName(),m.getName(),e);
                    throw e;
                }
            }

            log.debug("parameterizedLiteralsNameMap: " + parameterizedLiteralsNameMap);

            if (isBlocking)
            {

                methodBuilder.addStatement("exchange.startBlocking()");

                methodBuilder.beginControlFlow("if (exchange.isInIoThread())");

                methodBuilder.addStatement("exchange.dispatch(this)");

                methodBuilder.nextControlFlow("else");

            }

            List<Parameter> parameters = Arrays.stream(m.getParameters()).collect(Collectors.toList());

            for(Parameter p : parameters)
            {

                Type type = p.getParameterizedType();

                try
                {

                    log.debug("Method {} parameter {} type {}", m.getName(), p.getName() , type);

                    if (p.getType().equals(ServerRequest.class))
                    {
                        methodBuilder.addStatement("$T $L = new $T(exchange)", ServerRequest.class, p.getName(), ServerRequest.class);

                    }
                    else if (p.getType().equals(HttpHandler.class))
                    {
                        methodBuilder.addStatement("$T $L = this", HttpHandler.class, p.getName());
                    }
                    else if (!p.getType().equals(HttpServerExchange.class))
                    {
                        if (p.isAnnotationPresent(HeaderParam.class))
                        {

                            TypeHandler handler = TypeHandler.forType(type);

                            if (handler.equals(TypeHandler.OptionalStringType))
                            {
                                handler = TypeHandler.OptionalHeaderStringType;

                                TypeHandler.addStatement(methodBuilder, p, handler);

                            }
                            else if (handler.equals(TypeHandler.OptionalValueOfType))
                            {
                                handler = TypeHandler.OptionalHeaderValueOfType;

                                TypeHandler.addStatement(methodBuilder, p, handler);

                            }
                            else if (handler.equals(TypeHandler.OptionalFromStringType))
                            {
                                handler = TypeHandler.OptionalHeaderFromStringType;
                                TypeHandler.addStatement(methodBuilder, p, handler);

                            }
                            else if (handler.equals(TypeHandler.StringType))
                            {
                                handler = TypeHandler.HeaderStringType;
                                TypeHandler.addStatement(methodBuilder, p, handler);

                            }
                            else if (handler.equals(TypeHandler.ValueOfType))
                            {
                                handler = TypeHandler.HeaderValueOfType;
                                TypeHandler.addStatement(methodBuilder, p, handler);

                            }
                            else if (handler.equals(TypeHandler.FromStringType))
                            {
                                handler = TypeHandler.HeaderFromStringType;
                                TypeHandler.addStatement(methodBuilder, p, handler);
                            }
                            else
                            {
                                handler = TypeHandler.HeaderStringType;

                                TypeHandler.addStatement(methodBuilder, p, handler);
                            }

                        }
                        else
                        {
                            BeanParam beanParam = p.getAnnotation(BeanParam.class);

                            FormParam formParam = p.getAnnotation(FormParam.class);

                            boolean isBeanParameter = beanParam != null;

                            TypeHandler t = TypeHandler.forType(type, isBeanParameter);

                            if (formParam != null)
                            {
                                switch (t)
                                {
                                    case ModelType:
                                    {
                                        t = TypeHandler.NamedModelType;
                                        break;
                                    }
                                    case JsonNodeType:
                                    {
                                        t = TypeHandler.NamedJsonNodeType;
                                        break;
                                    }
                                    case ByteBufferType:
                                    {
                                        t = TypeHandler.NamedByteBufferType;
                                        break;
                                    }
                                    case OptionalModelType:
                                    {
                                        t = TypeHandler.OptionalNamedModelType;
                                        break;
                                    }
                                    case OptionalJsonNodeType:
                                    {
                                        t = TypeHandler.OptionalNamedJsonNodeType;
                                        break;
                                    }
                                    case OptionalByteBufferType:
                                    {
                                        t = TypeHandler.OptionalNamedByteBufferType;
                                        break;
                                    }
                                }
                            }

                            log.debug("beanParam handler: {}",t);

                            if (t.equals(TypeHandler.OptionalModelType) || t.equals(TypeHandler.ModelType))
                            {
                                String interfaceType = parameterizedLiteralsNameMap.get(type);

                                String typeName = type.getTypeName();

                                if (typeName.contains("$"))
                                {
                                    typeName = typeName.replace("$", ".");
                                }

                                if (t.equals(TypeHandler.OptionalModelType))
                                {
                                    ParameterizedType pType = (ParameterizedType) type;

                                    if (type instanceof ParameterizedType)
                                    {
                                        pType = (ParameterizedType) type;
                                        type = pType.getActualTypeArguments()[0];
                                    }

                                    String pTypeName = type.getTypeName() + ".class";

                                    methodBuilder.addStatement(t.statement, type.getTypeName(), p.getName(), io.sinistral.proteus.server.Extractors.Optional.class, pTypeName);

                                }
                                else
                                {
                                    String pType = interfaceType != null ? interfaceType + "TypeReference" : typeName + ".class";
                                    methodBuilder.addStatement(t.statement, type, p.getName(), pType);
                                }

                            }
                            else if (t.equals(TypeHandler.OptionalNamedModelType) || t.equals(TypeHandler.NamedModelType))
                            {
                                String interfaceType = parameterizedLiteralsNameMap.get(type);

                                String typeName = type.getTypeName();

                                if (typeName.contains("$"))
                                {
                                    typeName = typeName.replace("$", ".");
                                }

                                String pType = interfaceType != null ? interfaceType + "TypeReference" : typeName + ".class";

                                methodBuilder.addStatement(t.statement, type, p.getName(), pType, p.getName());

                            }
                            else if (t.equals(TypeHandler.BeanListFromStringType) || t.equals(TypeHandler.BeanListValueOfType))
                            {
                                String interfaceType = parameterizedLiteralsNameMap.get(type);

                                String typeName = type.getTypeName();

                                if (typeName.contains("$"))
                                {
                                    typeName = typeName.replace("$", ".");
                                }

                                String pType = interfaceType != null ? interfaceType + "TypeReference" : typeName + ".class";

                                methodBuilder.addStatement(t.statement, type, p.getName(), pType);

                            }
                            else if (t.equals(TypeHandler.OptionalFromStringType) || t.equals(TypeHandler.OptionalValueOfType))
                            {

                                TypeHandler.addStatement(methodBuilder, p);
                            }
                            else if (t.equals(TypeHandler.QueryOptionalListFromStringType)
                                    || t.equals(TypeHandler.QueryOptionalListValueOfType)
                                    || t.equals(TypeHandler.QueryOptionalSetValueOfType)
                                    || t.equals(TypeHandler.QueryOptionalSetFromStringType))
                            {
                                ParameterizedType pType = (ParameterizedType) type;

                                if (type instanceof ParameterizedType)
                                {
                                    pType = (ParameterizedType) type;
                                    type = pType.getActualTypeArguments()[0];
                                }

                                Class<?> erasedType = (Class<?>) extractErasedType(type);

                                methodBuilder.addStatement(t.statement, pType, p.getName(), p.getName(), erasedType);

                            }
                            else if (t.equals(TypeHandler.OptionalBeanListFromStringType) || t.equals(TypeHandler.OptionalBeanListValueOfType))
                            {
                                ParameterizedType pType = (ParameterizedType) type;

                                if (type instanceof ParameterizedType)
                                {
                                    pType = (ParameterizedType) type;
                                    type = pType.getActualTypeArguments()[0];
                                }

                                Class<?> erasedType = (Class<?>) extractErasedType(type);

                                try
                                {

                                    methodBuilder.addStatement(t.statement, pType, p.getName(), p.getName(), erasedType);

                                } catch (Exception e)
                                {
                                    log.error("error adding statement to method {} for parameter {}:\nstatement: {}\ntype: {} erased type: {}\n",m.getName(),  p.getName(), t.statement,   pType, erasedType,e);
                                    throw e;
                                }

                            }
                            else
                            {
                                TypeHandler.addStatement(methodBuilder, p);
                            }
                        }
                    }

                } catch (Exception e)
                {
                    log.error("Failed to generate statement for method {}",m.getName(),e);
                    throw e;
                }

            }

            methodBuilder.addCode("$L", "\n");

            CodeBlock.Builder functionBlockBuilder = CodeBlock.builder();

            String controllerMethodArgs = Arrays.stream(m.getParameters()).map(Parameter::getName).collect(Collectors.joining(","));

            if (!m.getReturnType().toString().equalsIgnoreCase("void"))
            {
                if (m.getReturnType().getTypeName().contains("java.util.concurrent.CompletionStage")
                        || m.getReturnType().getTypeName().contains("java.util.concurrent.CompletableFuture"))
                {
                    Type futureType = m.getGenericReturnType();

                    functionBlockBuilder.add("$T $L = $L.$L($L);", futureType, "response", controllerName, m.getName(), controllerMethodArgs);

                }
                else
                {
                    functionBlockBuilder.add("$T $L = $L.$L($L);", m.getGenericReturnType(), "response", controllerName, m.getName(), controllerMethodArgs);
                }

                methodBuilder.addCode(functionBlockBuilder.build());

                methodBuilder.addCode("$L", "\n");

                if (m.getReturnType().equals(ServerResponse.class))
                {
                    methodBuilder.addStatement("$L.send($L)", "response", "exchange");

                }
                else if ((m.getGenericReturnType().toString().contains("java.util.concurrent.CompletionStage") && m.getGenericReturnType().toString().contains("ServerResponse"))
                        || (m.getGenericReturnType().toString().contains("java.util.concurrent.CompletableFuture") && m.getGenericReturnType().toString().contains("ServerResponse")))

                {
                    methodBuilder.addCode("exchange.dispatch( exchange.getConnection().getWorker(), () -> ");
                    methodBuilder.beginControlFlow("", "");

                    methodBuilder.addCode(
                            "$L.whenComplete( (r,ex) -> ",
                            "response");
                    methodBuilder.beginControlFlow("", "");

                    methodBuilder.beginControlFlow("if(ex != null)");
                    methodBuilder.addCode("\t\texchange.putAttachment(io.undertow.server.handlers.ExceptionHandler.THROWABLE, ex);");
                    methodBuilder.addCode("\t\texchange.setResponseCode(500);\n\t");
                    methodBuilder.addCode("\t\texchange.endExchange();\n\t");
                    methodBuilder.nextControlFlow("else");
                    methodBuilder.addCode("\t\tr.send($L);", "exchange");
                    methodBuilder.endControlFlow();
                    methodBuilder.endControlFlow(")", "");
                    methodBuilder.endControlFlow(")", "");

                }
                else if (m.getReturnType().getTypeName().contains("java.util.concurrent.CompletionStage")
                        || m.getReturnType().getTypeName().contains("java.util.concurrent.CompletableFuture"))
                {

                    String postProcess = ".";

                    if (!producesContentType.contains(","))
                    {
                        if (producesContentType.contains(MediaType.APPLICATION_JSON))
                        {
                            postProcess = ".applicationJson().";
                        }
                        else if (producesContentType.contains(MediaType.APPLICATION_XML))
                        {
                            postProcess = ".applicationXml().";
                        }
                        else if (producesContentType.contains(MediaType.TEXT_HTML))
                        {
                            postProcess = ".textHtml().";
                        }
                        else if (producesContentType != null)
                        {
                            postProcess = String.format(".contentType(\"%s\").", producesContentType);
                        }
                        else
                        {
                            postProcess = ".";
                        }
                    }

                    methodBuilder.addCode("exchange.dispatch( exchange.getConnection().getWorker(), () -> ");
                    methodBuilder.beginControlFlow("", "");

                    methodBuilder.addCode(
                            "$L.whenComplete( (r,ex) -> ",
                            "response");
                    methodBuilder.beginControlFlow("", "");

                    methodBuilder.beginControlFlow("if(ex != null)");
                    methodBuilder.addCode("\texchange.putAttachment(io.undertow.server.handlers.ExceptionHandler.THROWABLE, ex);\n");
                    methodBuilder.addCode("\texchange.setResponseCode(500);\n");
                    methodBuilder.addCode("\texchange.endExchange();\n");
                    methodBuilder.nextControlFlow("else");
                    methodBuilder.addCode("\t\tio.sinistral.proteus.server.ServerResponse.response(r)" + postProcess + "send($L);", "exchange");
                    methodBuilder.endControlFlow();
                    methodBuilder.endControlFlow(")", "");

                    methodBuilder.endControlFlow(")", "");

                }
                else
                {

                    methodBuilder.addStatement("exchange.getResponseHeaders().put($T.CONTENT_TYPE, $S)", Headers.class, producesContentType);

                    if (m.getReturnType().equals(String.class))
                    {
                        methodBuilder.addStatement("exchange.getResponseSender().send($L)", "response");
                    }
                    else
                    {
                        methodBuilder.addStatement("exchange.getResponseSender().send($L.toString())", "response");
                    }

                }

            }
            else
            {

                functionBlockBuilder.add("$L.$L($L);", controllerName, m.getName(), controllerMethodArgs);

                methodBuilder.addCode(functionBlockBuilder.build());

                methodBuilder.addCode("$L", "\n");

            }

            if (isBlocking)
            {
                methodBuilder.endControlFlow();

            }

            handlerClassBuilder.addMethod(methodBuilder.build());

            FieldSpec handlerField = FieldSpec.builder(httpHandlerClass, handlerName, Modifier.FINAL).initializer("$L", handlerClassBuilder.build()).build();

            initBuilder.addCode("$L\n", handlerField.toString());

            Optional<io.sinistral.proteus.annotations.Chain> wrapAnnotation = Optional.ofNullable(m.getAnnotation(io.sinistral.proteus.annotations.Chain.class));

            /*
             * Authorization
             */

            List<String> securityDefinitions = new ArrayList<>();

            /*
             * @TODO wrap blocking in BlockingHandler?
             */

            if (Optional.ofNullable(m.getAnnotation(Path.class)).isPresent())
            {

                Annotation[] annotations = clazz.getAnnotations();

                Annotation securityRequirementAnnotation = Arrays.stream(annotations).filter(a -> a.getClass().getName().contains("SecurityRequirement")).findFirst().orElse(null);

                if (securityRequirementAnnotation != null)
                {

                    try
                    {
                        Field nameField = securityRequirementAnnotation.getClass().getField("name");

                        if (nameField != null)
                        {
                            Object securityRequirement = nameField.get(securityRequirementAnnotation);
                            securityDefinitions.add(securityRequirement.toString());
                        }

                    } catch (Exception e)
                    {
                        log.warn("No name field on security requirement");
                    }

                }

            }

            if (securityDefinitions.isEmpty())
            {
                securityDefinitions.addAll(typeLevelSecurityDefinitions);
            }

            if (isBlocking && isDebug)
            {
                handlerName = "new io.undertow.server.handlers.RequestDumpingHandler(new io.undertow.server.handlers.RequestBufferingHandler.Wrapper(8).wrap(" + handlerName + "))";
            }
            else if (isBlocking)
            {
                handlerName = "new io.undertow.server.handlers.RequestBufferingHandler.Wrapper(8).wrap(" + handlerName + ")";

            }
            else if (isDebug)
            {
                handlerName = "new io.undertow.server.handlers.RequestDumpingHandler(" + handlerName + ")";

            }

            if (wrapAnnotation.isPresent() || typeLevelHandlerWrapperMap.size() > 0 || securityDefinitions.size() > 0)
            {
                initBuilder.addStatement("currentHandler = $L", handlerName);

                if (wrapAnnotation.isPresent())
                {
                    io.sinistral.proteus.annotations.Chain w = wrapAnnotation.get();

                    Class<? extends HandlerWrapper>[] wrapperClasses = w.value();

                    for (Class<? extends HandlerWrapper> wrapperClass : wrapperClasses)
                    {
                        String wrapperName = typeLevelHandlerWrapperMap.get(wrapperClass);

                        if (wrapperName == null)
                        {
                            wrapperName = String.format("%s_%d", generateFieldName(wrapperClass.getCanonicalName()), handlerWrapperIndex++);

                        }

                        initBuilder.addStatement("currentHandler = $L.wrap($L)", wrapperName, "currentHandler");

                        registeredWrapperTypes.put(wrapperName, wrapperClass);

                    }
                }

                for (Class<? extends HandlerWrapper> wrapperClass : typeLevelHandlerWrapperMap.keySet())
                {
                    String wrapperName = typeLevelHandlerWrapperMap.get(wrapperClass);
                    initBuilder.addStatement("currentHandler = $L.wrap($L)", wrapperName, "currentHandler");

                    registeredWrapperTypes.put(wrapperName, wrapperClass);

                }

                for (String securityDefinitionName : securityDefinitions)
                {
                    initBuilder.addStatement("currentHandler = registeredHandlerWrappers.get($S).wrap($L)", securityDefinitionName, "currentHandler");
                }

                initBuilder.addStatement("$L.add(io.undertow.util.Methods.$L,$S,$L)", "router", httpMethod, methodPath, "currentHandler");
            }
            else
            {
                initBuilder.addStatement("$L.add(io.undertow.util.Methods.$L,$S,$L)", "router", httpMethod, methodPath, handlerName);
            }

            initBuilder.addCode("$L", "\n");

            try
            {
                registeredEndpoints.add(endpointInfo);
            } catch (Exception e)
            {
                log.error("Failed to register endpoint {}", endpointInfo, e);
                throw e;
            }

        }

        initBuilder.addCode("$Lreturn router;\n", "\n");

        typeBuilder.addMethod(initBuilder.build());

    }

    /**
     * @return the packageName
     */
    public String getPackageName()
    {

        return packageName;
    }

    /**
     * @param packageName
     *            the packageName to set
     */
    public void setPackageName(String packageName)
    {

        this.packageName = packageName;
    }

    /**
     * @return the className
     */
    public String getClassName()
    {

        return className;
    }

    /**
     * @param className
     *            the className to set
     */
    public void setClassName(String className)
    {

        this.className = className;
    }

    protected static ArrayList<String> getClassNamesFromPackage(String packageName) throws Exception
    {

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        URL packageURL;
        ArrayList<String> names = new ArrayList<>();

        packageName = packageName.replace(".", "/");
        packageURL = classLoader.getResource(packageName);

        assert packageURL != null;
        URI uri = new URI(packageURL.toString());
        File folder = new File(uri.getPath());
        // won't work with path which contains blank (%20)
        // File folder = new File(packageURL.getFile());
        File[] contenuti = folder.listFiles();
        String entryName;
        assert contenuti != null;
        for (File actual : contenuti)
        {
            if (actual.isDirectory())
            {
                continue;
            }

            entryName = actual.getName();
            entryName = entryName.substring(0, entryName.lastIndexOf('.'));
            names.add(entryName);
        }

        return names;
    }

    protected static Set<Class<?>> getApiClasses(String basePath, Predicate<String> pathPredicate)
    {

        Reflections ref = new Reflections(basePath);
        Stream<Class<?>> stream = ref.getTypesAnnotatedWith(Path.class).stream();

        if (pathPredicate != null)
        {
            stream = stream.filter(clazz ->
            {

                Path annotation = clazz.getDeclaredAnnotation(Path.class);

                return annotation != null && pathPredicate.test(annotation.value());

            });
        }

        return stream.collect(Collectors.toSet());

    }

    protected static Type extractErasedType(Type type)
    {

        String typeName = type.getTypeName();

        Matcher matcher = TYPE_NAME_PATTERN.matcher(typeName);

        if (matcher.find())
        {

            int matches = matcher.groupCount();

            if (matches == 2)
            {

                String erasedType = matcher.group(2);

                String clearDollarType = erasedType.replaceAll("\\$", ".");

                try
                {
                    return Class.forName(clearDollarType);

                } catch (Exception e1)
                {
                    try
                    {

                        return Class.forName(erasedType);

                    } catch (Exception e2)
                    {
                        return type;
                    }
                }

            }
            else if (matches > 2)
            {
                String erasedType = matcher.group(3);

                String clearDollarType = erasedType.replaceAll("\\$", ".");

                try
                {
                    return Class.forName(clearDollarType);
                } catch (Exception e1)
                {
                    try
                    {
                        return Class.forName(erasedType);
                    } catch (Exception e2)
                    {
                        return type;
                    }
                }
            }
        }

        return null;
    }

    protected static String typeReferenceNameForParameterizedType(Type type)
    {

        String typeName = type.getTypeName();

        if (typeName.contains("Optional"))
        {
            log.warn("Type is for an optional named {}", typeName);
        }

        Matcher matcher = TYPE_NAME_PATTERN.matcher(typeName);

        if (matcher.find())
        {

            int matches = matcher.groupCount();

            if (matches == 2)
            {
                String genericInterface = matcher.group(1);
                String erasedType = matcher.group(2).replaceAll("\\$", ".");

                String[] genericParts = genericInterface.split("\\.");
                String[] erasedParts = erasedType.split("\\.");

                String genericTypeName = genericParts[genericParts.length - 1];
                String erasedTypeName;

                if (erasedParts.length > 1)
                {
                    erasedTypeName = erasedParts[erasedParts.length - 2] + erasedParts[erasedParts.length - 1];
                }
                else
                {
                    erasedTypeName = erasedParts[0];
                }

                typeName = String.format("%s%s%s", Character.toLowerCase(erasedTypeName.charAt(0)), erasedTypeName.substring(1), genericTypeName);

                return typeName;
            }

        }

        matcher = CONCURRENT_TYPE_NAME_PATTERN.matcher(typeName);

        if (matcher.find())
        {

            int matches = matcher.groupCount();

            if (matches == 2)
            {
                String genericInterface = matcher.group(1);
                String erasedType = matcher.group(2).replaceAll("\\$", ".");

                String[] genericParts = genericInterface.split("\\.");
                String[] erasedParts = erasedType.split("\\.");

                String genericTypeName = genericParts[genericParts.length - 1];
                String erasedTypeName;

                if (erasedParts.length > 1)
                {
                    erasedTypeName = erasedParts[erasedParts.length - 2] + erasedParts[erasedParts.length - 1];
                }
                else
                {
                    erasedTypeName = erasedParts[0];
                }

                typeName = String.format("%s%s%s", Character.toLowerCase(erasedTypeName.charAt(0)), erasedTypeName.substring(1), genericTypeName);
                return typeName;
            }

        }

        return typeName;
    }

    protected static String typeReferenceNameForType(Type type)
    {

        String typeName = type.getTypeName();

        String[] erasedParts = typeName.split("\\.");

        String erasedTypeName;

        if (erasedParts.length > 1)
        {
            erasedTypeName = erasedParts[erasedParts.length - 2] + erasedParts[erasedParts.length - 1];
        }
        else
        {
            erasedTypeName = erasedParts[0];
        }

        typeName = generateFieldName(erasedTypeName);

        return typeName;
    }

    protected static String generateFieldName(String name)
    {

        String[] parts = name.split("\\.");

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < parts.length; i++)
        {
            String part = parts[i];

            if (i == 0)
            {
                sb.append(String.format("%s%s", Character.toLowerCase(part.charAt(0)), part.substring(1)));
            }
            else
            {
                sb.append(String.format("%s%s", Character.toUpperCase(part.charAt(0)), part.substring(1)));
            }
        }

        return sb.toString();
    }

    protected static void generateTypeReference(MethodSpec.Builder builder, Type type, String name)
    {

        builder.addCode(
                CodeBlock
                        .of("\n\ncom.fasterxml.jackson.core.type.TypeReference<$T> $L = new com.fasterxml.jackson.core.type.TypeReference<$L>(){};\n\n", type, name, type));

    }

    protected static void generateParameterReference(MethodSpec.Builder builder, Class<?> clazz)
    {

        builder.addCode(CodeBlock.of("\n\nType $LType = $T.", clazz, clazz));

    }

    protected static boolean hasValueOfMethod(Class<?> clazz)
    {

        return Arrays.stream(clazz.getMethods()).anyMatch(m -> m.getName().equals("valueOf"));
    }

    protected static boolean hasFromStringMethod(Class<?> clazz)
    {

        return Arrays.stream(clazz.getMethods()).anyMatch(m -> m.getName().equals("fromString"));
    }

}
