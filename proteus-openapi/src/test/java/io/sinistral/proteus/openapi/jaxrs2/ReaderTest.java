package io.sinistral.proteus.openapi.jaxrs2;

import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.sinistral.proteus.openapi.test.controllers.OpenAPITests;
import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.filter.OpenAPI31SpecFilter;
import io.swagger.v3.core.filter.SpecFilter;
import io.swagger.v3.core.util.Json;
import io.swagger.v3.core.util.Json31;
import io.swagger.v3.jaxrs2.ext.OpenAPIExtensions;
import io.swagger.v3.jaxrs2.integration.JaxrsApplicationAndAnnotationScanner;
import io.swagger.v3.oas.integration.GenericOpenApiContext;
import io.swagger.v3.oas.integration.SwaggerConfiguration;
import io.swagger.v3.oas.integration.api.OpenApiContext;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.SpecVersion;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.*;
import java.util.*;
import java.util.stream.Collectors;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ReaderTest
{

    private static final Logger logger = LoggerFactory.getLogger(io.sinistral.proteus.openapi.jaxrs2.ReaderTest.class.getName());

    public class CustomModelNameGenerator
    {

        public String generate(AnnotatedType type)
        {
            // Recursively build a name for generics
            if (type.getType() instanceof java.lang.reflect.ParameterizedType pt) {
                StringBuilder sb = new StringBuilder();
                sb.append(((Class<?>) pt.getRawType()).getSimpleName());
                for (Type arg : pt.getActualTypeArguments()) {
                    sb.append(getTypeName(arg));
                }
                return sb.toString();
            }
            return type.getType().getTypeName().replaceAll("[^\\w]", "");
        }

        private String getTypeName(Type type)
        {
            if (type instanceof Class<?> clazz) {
                return clazz.getSimpleName();
            } else if (type instanceof java.lang.reflect.ParameterizedType pt) {
                StringBuilder sb = new StringBuilder();
                sb.append(pt.getRawType() instanceof Class<?> c ? c.getSimpleName() : pt.getRawType().getTypeName());
                for (Type arg : pt.getActualTypeArguments()) {
                    sb.append(getTypeName(arg));
                }
                return sb.toString();
            }
            return type.getTypeName().replaceAll("[^\\w]", "");
        }
    }

    public static String generateTypeName(Type type)
    {
        if (type instanceof Class<?>) {
            Class<?> clazz = (Class<?>) type;
            if (clazz.isArray()) {
                return "ArrayOf" + generateTypeName(clazz.getComponentType());
            }
            return clazz.getSimpleName();
        } else if (type instanceof ParameterizedType) {
            ParameterizedType pt = (ParameterizedType) type;
            StringBuilder sb = new StringBuilder();
            sb.append(((Class<?>) pt.getRawType()).getSimpleName());
            for (Type arg : pt.getActualTypeArguments()) {
                sb.append(generateTypeName(arg));
            }
            return sb.toString();
        } else if (type instanceof GenericArrayType) {
            return "ArrayOf" + generateTypeName(((GenericArrayType) type).getGenericComponentType());
        } else if (type instanceof TypeVariable) {
            return ((TypeVariable<?>) type).getName();
        } else if (type instanceof WildcardType) {
            return "Wildcard";
        } else {
            // fallback for unknown types
            return type.getTypeName().replaceAll("[^\\w]", "");
        }
    }


    @Test
    void processFile() throws Exception
    {

        Class<?> clazz = OpenAPITests.class;

        io.swagger.v3.jaxrs2.Reader reader = new io.swagger.v3.jaxrs2.Reader();

        OpenAPI api = reader.read(clazz);


        logger.info("OpenAPI spec: {}", Json.pretty().writeValueAsString(api));

        OpenAPI openAPI = new SpecFilter().filter(api, new OpenAPI31SpecFilter(), null, null, null);

        logger.info("OpenAPI spec: {}", Json31.pretty().writeValueAsString(openAPI));

        logger.info("OpenAPI spec: {}", Json31.pretty().writeValueAsString(openAPI.getPaths()));

    }

    public Map<String, Map<String, List<String>>> testFunction()
    {
        return null;
    }

    public Long testLongFunction()
    {
        return null;
    }

    public static String getSchemaTypeName(JavaType type)
    {
        if (type.isCollectionLikeType()) {
            JavaType contentType = type.getContentType();
            String innerName = getSchemaTypeName(contentType);
            return "ListOf" + innerName;
        } else if (type.isMapLikeType()) {
            JavaType keyType = type.getKeyType();
            JavaType valueType = type.getContentType();
            String keyName = getSchemaTypeName(keyType);
            String valueName = getSchemaTypeName(valueType);
            return "MapOf" + keyName + "To" + valueName;
        } else {
            // Simple type
            return type.getRawClass().getSimpleName();
        }
    }


    @Test
    void reflect()
    {
        ObjectMapper mapper = new ObjectMapper();

        Method[] methods = this.getClass().getDeclaredMethods();

        for (Method method : methods) {
            logger.info("Method: {}", method.getName());

            Type[] genericParameterTypes = method.getGenericParameterTypes();

            JavaType genericReturnType = mapper.getTypeFactory().constructType(method.getGenericReturnType());

            JavaType returnType = mapper.getTypeFactory().constructType(method.getReturnType());

            logger.info("Generic Return Type: {} Return: {}", genericReturnType.getTypeName(), returnType.getTypeName());

            logger.info("Generic Return Type Name: {} Return: {}", getSchemaTypeName(genericReturnType), getSchemaTypeName(returnType));


            for (Type type : genericParameterTypes) {
                if (type instanceof ParameterizedType) {
                    ParameterizedType pt = (ParameterizedType) type;
                    logger.info("  Parameterized Type: {}", pt.getRawType().getTypeName());
                    for (Type arg : pt.getActualTypeArguments()) {
                        logger.info("    Argument Type: {}", arg.getTypeName());
                    }
                } else {
                    logger.info("  Type: {}", type.getTypeName());
                }
            }
        }


    }

    @Test
    void reader2() throws Exception
    {
        OpenAPIExtensions.setExtensions(Collections.singletonList(new ServerParameterExtension()));

        OpenAPI openApi = new OpenAPI(SpecVersion.V31);

        ObjectMapper jsonMapper = Json31.mapper();

        openApi.setOpenapi("3.1.1");

        Info info = new Info().title("Test API").version("1.0.0").description("This is a test API for OpenAPI 3.1.1");

        openApi.setInfo(info);


        if (openApi.getComponents() == null) {
            openApi.setComponents(new Components());
        }


        List<Server> servers = new ArrayList<>();
        Server server = new Server();
        server.setUrl("http://localhost:8080/api");
        server.setDescription("Local development server");
        servers.add(server);

        openApi.setServers(servers);

        SwaggerConfiguration config = new SwaggerConfiguration().resourceClasses(Set.of(OpenAPITests.class.getName())).openAPI(openApi);


        if (config.getUserDefinedOptions() == null) {
            config.setUserDefinedOptions(new HashMap<>());
        }

        config.getUserDefinedOptions().put("jsonViewQueryParameterName", "context");


        Set<String> modelConverterClasses = new HashSet<>();

        modelConverterClasses.add(ServerModelResolver.class.getName());


        config.setModelConverterClassess(modelConverterClasses);

        OpenApiContext ctx = new GenericOpenApiContext().openApiConfiguration(config).openApiReader(new Reader2(config)).openApiScanner(new JaxrsApplicationAndAnnotationScanner().openApiConfiguration(config)).init();

        openApi = ctx.read();

        logger.info("OpenAPI spec: {}", Json.pretty().writeValueAsString(openApi));

        OpenAPI openAPI = new SpecFilter().filter(openApi, new OpenAPI31SpecFilter(), null, null, null);

        logger.info("OpenAPI spec: {}", Json31.pretty().writeValueAsString(openAPI));

        logger.info("OpenAPI spec: {}", Json31.pretty().writeValueAsString(openAPI.getPaths()));
    }


}