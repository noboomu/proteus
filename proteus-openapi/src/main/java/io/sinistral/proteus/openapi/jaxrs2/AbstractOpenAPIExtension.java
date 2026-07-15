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
 * Base class for OpenAPI extensions
 */
public abstract class AbstractOpenAPIExtension implements OpenAPIExtension {

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

    protected boolean shouldIgnoreType(Type type, Set<Type> typesToSkip) {
        if (typesToSkip.contains(type)) {
            return true;
        }
        if (shouldIgnoreClass(type.getTypeName())) {
            return true;
        }
        return false;
    }

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

    protected boolean shouldIgnoreClass(Class<?> cls) {
        if (cls == null) {
            return true;
        }
        return shouldIgnoreClass(cls.getName());
    }

    protected JavaType constructType(Type type) {
        return Json.mapper().getTypeFactory().constructType(type);
    }
}
