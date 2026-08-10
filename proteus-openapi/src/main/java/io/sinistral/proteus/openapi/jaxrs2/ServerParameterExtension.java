/**
 *
 */
package io.sinistral.proteus.openapi.jaxrs2;

import io.sinistral.proteus.openapi.models.Components;
import io.sinistral.proteus.openapi.models.parameters.Parameter;
import io.sinistral.proteus.openapi.util.Json;
import io.sinistral.proteus.openapi.util.ParameterProcessor;
import jakarta.ws.rs.*;
import org.apache.commons.lang3.StringUtils;
import com.fasterxml.jackson.annotation.JsonView;
import tools.jackson.databind.BeanDescription;
import tools.jackson.databind.JavaType;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.SerializationConfig;
import tools.jackson.databind.introspect.AnnotatedField;
import tools.jackson.databind.introspect.AnnotatedMethod;
import tools.jackson.databind.introspect.BeanPropertyDefinition;
import tools.jackson.databind.introspect.ClassIntrospector;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.*;

/**
 * @author jbauer
 */
public class ServerParameterExtension extends AbstractOpenAPIExtension {

    private static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(
        ServerParameterExtension.class.getCanonicalName()
    );
    private static String QUERY_PARAM = "query";
    private static String HEADER_PARAM = "header";
    private static String COOKIE_PARAM = "cookie";
    private static String PATH_PARAM = "path";
    private static String FORM_PARAM = "form";

    private final ObjectMapper mapper;

    public ServerParameterExtension() {
        this(Json.mapper());
    }

    public ServerParameterExtension(ObjectMapper mapper) {
        this.mapper = Objects.requireNonNull(mapper, "mapper");
    }

    @Override
    public ResolvedParameter extractParameters(
        List<Annotation> annotations,
        Type type,
        Set<Type> typesToSkip,
        Components components,
        jakarta.ws.rs.Consumes classConsumes,
        jakarta.ws.rs.Consumes methodConsumes,
        boolean includeRequestBody,
        JsonView jsonViewAnnotation,
        Iterator<OpenAPIExtension> chain
    ) {
        if (shouldIgnoreType(type, typesToSkip)) {
            return new ResolvedParameter();
        }

        JavaType javaType = constructType(type);
        boolean isRequired = true;

        if (isOptionalType(javaType)) {
            isRequired = false;
        }

        Parameter parameter = null;
        List<Parameter> additionalParameters = new ArrayList<>();
        List<Parameter> additionalFormParameters = new ArrayList<>();

        for (Annotation annotation : annotations) {
            if (annotation instanceof QueryParam) {
                QueryParam param = (QueryParam) annotation;
                Parameter qp = new Parameter();

                qp.setIn(QUERY_PARAM);
                qp.setName(param.value());

                parameter = qp;
            } else if (annotation instanceof PathParam) {
                PathParam param = (PathParam) annotation;
                Parameter pp = new Parameter();

                pp.setIn(PATH_PARAM);
                pp.setName(param.value());

                parameter = pp;
            } else if (annotation instanceof MatrixParam) {
                MatrixParam param = (MatrixParam) annotation;
                Parameter pp = new Parameter();

                pp.setIn(PATH_PARAM);
                pp.setStyle(Parameter.StyleEnum.MATRIX);
                pp.setName(param.value());

                parameter = pp;
            } else if (annotation instanceof HeaderParam) {
                HeaderParam param = (HeaderParam) annotation;
                Parameter pp = new Parameter();

                pp.setIn(HEADER_PARAM);
                pp.setName(param.value());

                parameter = pp;
            } else if (annotation instanceof CookieParam) {
                CookieParam param = (CookieParam) annotation;
                Parameter pp = new Parameter();

                pp.setIn(COOKIE_PARAM);
                pp.setName(param.value());

                parameter = pp;
            } else if (
                annotation instanceof io.swagger.v3.oas.annotations.Parameter
            ) {
                if (
                    ((io.swagger.v3.oas.annotations.Parameter) annotation).hidden()
                ) {
                    return new ResolvedParameter();
                }

                if (parameter == null) {
                    parameter = new Parameter();
                }
            } else {
                handleAdditionalAnnotation(
                    additionalParameters,
                    additionalFormParameters,
                    annotation,
                    type,
                    typesToSkip,
                    classConsumes,
                    methodConsumes,
                    components,
                    includeRequestBody,
                    jsonViewAnnotation
                );
            }
        }

        List<Parameter> parameters = new ArrayList<>();
        ResolvedParameter extractParametersResult = new ResolvedParameter();
        extractParametersResult.parameters.addAll(additionalParameters);
        extractParametersResult.formParameters.addAll(additionalFormParameters);

        if ((parameter != null) && StringUtils.isNotBlank(parameter.getIn())) {
            parameter.setRequired(isRequired);
            parameters.add(parameter);
        } else if (includeRequestBody) {
            Parameter unknownParameter = ParameterProcessor.applyAnnotations(
                null,
                type,
                annotations,
                components,
                (classConsumes == null) ? new String[0] : classConsumes.value(),
                (methodConsumes == null)
                    ? new String[0]
                    : methodConsumes.value(),
                jsonViewAnnotation
            );

            if (unknownParameter != null) {
                if (
                    StringUtils.isNotBlank(unknownParameter.getIn()) &&
                    !"form".equals(unknownParameter.getIn())
                ) {
                    extractParametersResult.parameters.add(unknownParameter);
                } else if ("form".equals(unknownParameter.getIn())) {
                    unknownParameter.setIn(null);
                    extractParametersResult.formParameters.add(
                        unknownParameter
                    );
                } else {
                    // return as request body
                    extractParametersResult.requestBody = unknownParameter;
                }
            }
        }

        for (Parameter p : parameters) {
            Parameter processedParameter = ParameterProcessor.applyAnnotations(
                p,
                type,
                annotations,
                components,
                (classConsumes == null) ? new String[0] : classConsumes.value(),
                (methodConsumes == null)
                    ? new String[0]
                    : methodConsumes.value(),
                jsonViewAnnotation
            );

            if (processedParameter != null) {
                extractParametersResult.parameters.add(processedParameter);
            }
        }

        return extractParametersResult;
    }

    /**
     * Adds additional annotation processing support
     * @param parameters
     * @param annotation
     * @param type
     * @param typesToSkip
     */
    private boolean handleAdditionalAnnotation(
        List<Parameter> parameters,
        List<Parameter> formParameters,
        Annotation annotation,
        final Type type,
        Set<Type> typesToSkip,
        jakarta.ws.rs.Consumes classConsumes,
        jakarta.ws.rs.Consumes methodConsumes,
        Components components,
        boolean includeRequestBody,
        JsonView jsonViewAnnotation
    ) {
        boolean processed = false;

        if (BeanParam.class.isAssignableFrom(annotation.getClass())) {
            // Use Jackson's logic for processing Beans
            JavaType javaType = constructType(type);

            SerializationConfig serializationConfig = mapper.serializationConfig();
            ClassIntrospector introspector = serializationConfig.classIntrospectorInstance();
            BeanDescription beanDescription = introspector.introspectForSerialization(
                javaType,
                introspector.introspectClassAnnotations(javaType)
            );
            final List<BeanPropertyDefinition> properties = beanDescription.findProperties();

            for (final BeanPropertyDefinition propDef : properties) {
                final AnnotatedField field = propDef.getField();
                final AnnotatedMethod setter = propDef.getSetter();
                final AnnotatedMethod getter = propDef.getGetter();
                final List<Annotation> paramAnnotations = new ArrayList<
                    Annotation
                >();
                final Iterator<OpenAPIExtension> extensions =
                    OpenAPIExtensions.chain();
                Type paramType = null;

                // Gather the field's details
                if (field != null) {
                    paramType = field.getType();

                    // Get annotations from field
                    field.annotations().forEach(fieldAnnotation -> {
                        if (!paramAnnotations.contains(fieldAnnotation)) {
                            paramAnnotations.add(fieldAnnotation);
                        }
                    });
                }

                // Gather the setter's details but only the ones we need
                if (setter != null) {
                    // Do not set the param class/type from the setter if the
                    // values are already identified
                    if (paramType == null) {
                        // paramType will stay null if there is no parameter
                        paramType = setter.getParameterType(0);
                    }

                    // Get annotations from setter
                    setter.annotations().forEach(fieldAnnotation -> {
                        if (!paramAnnotations.contains(fieldAnnotation)) {
                            paramAnnotations.add(fieldAnnotation);
                        }
                    });
                }

                // Gather the getter's details but only the ones we need
                if (getter != null) {
                    // Do not set the param class/type from the getter if the
                    // values are already identified
                    if (paramType == null) {
                        paramType = getter.getType();
                    }

                    // Get annotations from getter
                    getter.annotations().forEach(fieldAnnotation -> {
                        if (!paramAnnotations.contains(fieldAnnotation)) {
                            paramAnnotations.add(fieldAnnotation);
                        }
                    });
                }

                if (paramType == null) {
                    continue;
                }

                // Re-process all Bean fields and let the default
                // swagger-jaxrs/swagger-jersey-jaxrs processors do their thing
                ResolvedParameter resolvedParameter = extensions
                    .next()
                    .extractParameters(
                        paramAnnotations,
                        paramType,
                        typesToSkip,
                        components,
                        classConsumes,
                        methodConsumes,
                        includeRequestBody,
                        jsonViewAnnotation,
                        extensions
                    );
                List<Parameter> extractedParameters =
                    resolvedParameter.parameters;

                for (Parameter p : extractedParameters) {
                    Parameter processedParam =
                        ParameterProcessor.applyAnnotations(
                            p,
                            paramType,
                            paramAnnotations,
                            components,
                            (classConsumes == null)
                                ? new String[0]
                                : classConsumes.value(),
                            (methodConsumes == null)
                                ? new String[0]
                                : methodConsumes.value(),
                            jsonViewAnnotation
                        );

                    if (processedParam != null) {
                        log.debug("added new parameters: " + processedParam);
                        parameters.add(processedParam);
                    }
                }

                List<Parameter> extractedFormParameters =
                    resolvedParameter.formParameters;

                for (Parameter p : extractedFormParameters) {
                    Parameter processedParam =
                        ParameterProcessor.applyAnnotations(
                            p,
                            paramType,
                            paramAnnotations,
                            components,
                            (classConsumes == null)
                                ? new String[0]
                                : classConsumes.value(),
                            (methodConsumes == null)
                                ? new String[0]
                                : methodConsumes.value(),
                            jsonViewAnnotation
                        );

                    if (processedParam != null) {
                        formParameters.add(processedParam);
                    }
                }

                processed = true;
            }
        }

        return processed;
    }

    @Override
    protected boolean shouldIgnoreClass(Class<?> cls) {
        return (
            cls.getName().startsWith("javax.ws.rs.") ||
            cls.getName().startsWith("io.undertow")
        );
    }

    public boolean isOptionalType(JavaType propType) {
        return Arrays.asList(
            "com.google.common.base.Optional",
            "java.util.Optional"
        ).contains(propType.getRawClass().getCanonicalName());
    }
}
