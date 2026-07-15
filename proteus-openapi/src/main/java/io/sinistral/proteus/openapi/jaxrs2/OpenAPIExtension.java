package io.sinistral.proteus.openapi.jaxrs2;

import io.sinistral.proteus.openapi.models.Operation;
import com.fasterxml.jackson.annotation.JsonView;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Extension interface for OpenAPI processing
 */
public interface OpenAPIExtension {

    /**
     * Extract parameters from annotations
     *
     * @param annotations annotations to process
     * @param type parameter type
     * @param typesToSkip types to skip during processing
     * @param components OpenAPI components
     * @param classConsumes class-level @Consumes
     * @param methodConsumes method-level @Consumes
     * @param includeRequestBody whether to include request body
     * @param jsonViewAnnotation JsonView annotation if present
     * @param chain iterator for chaining extensions
     * @return resolved parameter
     */
    ResolvedParameter extractParameters(
        List<Annotation> annotations,
        Type type,
        Set<Type> typesToSkip,
        io.sinistral.proteus.openapi.models.Components components,
        jakarta.ws.rs.Consumes classConsumes,
        jakarta.ws.rs.Consumes methodConsumes,
        boolean includeRequestBody,
        JsonView jsonViewAnnotation,
        Iterator<OpenAPIExtension> chain
    );

    /**
     * Decorate an operation
     *
     * @param operation operation to decorate
     * @param method method being processed
     * @param chain iterator for chaining extensions
     */
    void decorateOperation(
        Operation operation,
        Method method,
        Iterator<OpenAPIExtension> chain
    );
}
