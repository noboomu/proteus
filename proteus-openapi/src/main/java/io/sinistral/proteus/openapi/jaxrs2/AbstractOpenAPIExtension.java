package io.sinistral.proteus.openapi.jaxrs2;

import io.sinistral.proteus.openapi.models.Components;
import io.sinistral.proteus.openapi.models.Operation;
import io.sinistral.proteus.openapi.util.Json;
import com.fasterxml.jackson.annotation.JsonView;
import tools.jackson.databind.JavaType;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Base class for OpenAPI extensions.
 */
public abstract class AbstractOpenAPIExtension implements OpenAPIExtension {

    /** Creates the object. */
    public AbstractOpenAPIExtension() {}

    /**
     * Delegates parameter extraction to the next extension in the chain, returning an empty result at the end.
    *
    * @param annotations the value
    * @param type the value
    * @param typesToSkip the value
    * @param components the value
    * @param classConsumes the value
    * @param methodConsumes the value
    * @param includeRequestBody the value
    * @param jsonViewAnnotation the value
    * @param chain the value
    * @return the result
     */
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
        if (chain.hasNext()) {
            return chain.next().extractParameters(
                annotations,
                type,
                typesToSkip,
                components,
                classConsumes,
                methodConsumes,
                includeRequestBody,
                jsonViewAnnotation,
                chain
            );
        }
        return new ResolvedParameter();
    }

    /**
     * Delegates operation decoration to the next extension in the chain.
    *
    * @param operation the value
    * @param method the value
    * @param chain the value
     */
    @Override
    public void decorateOperation(
        Operation operation,
        Method method,
        Iterator<OpenAPIExtension> chain
    ) {
        if (chain.hasNext()) {
            chain.next().decorateOperation(operation, method, chain);
        }
    }

    /**
     * Returns true when the type is listed to skip or its class should be ignored.
     *
     * @param type the candidate type
     * @param typesToSkip the skip set
     * @return true when the type should be ignored
     */
    protected boolean shouldIgnoreType(Type type, Set<Type> typesToSkip) {
        if (typesToSkip.contains(type)) {
            return true;
        }
        if (shouldIgnoreClass(type.getTypeName())) {
            return true;
        }
        return false;
    }

    /**
     * Returns true for framework and void types that never become OpenAPI parameters.
     *
     * @param className the fully qualified class name
     * @return true when the class should be ignored
     */
    protected boolean shouldIgnoreClass(String className) {
        if (className == null || className.isEmpty()) {
            return true;
        }
        boolean ignore = false;
        ignore = ignore || className.startsWith("javax.ws.rs.");
        ignore = ignore || className.startsWith("jakarta.ws.rs.");
        ignore = ignore || className.equalsIgnoreCase("void");
        ignore = ignore || className.startsWith("io.undertow");
        ignore = ignore || className.startsWith("java.lang.Void");
        return ignore;
    }

    /**
     * Returns true when the class should be ignored.
     *
     * @param cls the candidate class, may be null
     * @return true when the class is null or should be ignored by name
     */
    protected boolean shouldIgnoreClass(Class<?> cls) {
        if (cls == null) {
            return true;
        }
        return shouldIgnoreClass(cls.getName());
    }

    /**
     * Constructs a Jackson JavaType for the given reflection type.
     *
     * @param type the reflection type
     * @return the resolved Jackson type
     */
    protected JavaType constructType(Type type) {
        return Json.mapper().getTypeFactory().constructType(type);
    }
}
