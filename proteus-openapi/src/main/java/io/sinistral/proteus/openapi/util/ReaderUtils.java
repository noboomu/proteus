package io.sinistral.proteus.openapi.util;

import io.sinistral.proteus.openapi.integration.OpenAPIConfiguration;
import io.sinistral.proteus.openapi.jaxrs2.OpenAPIExtensions;
import io.sinistral.proteus.openapi.models.Components;
import io.sinistral.proteus.openapi.models.parameters.Parameter;
import jakarta.ws.rs.Consumes;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Reader utilities for OpenAPI processing
 */
public class ReaderUtils {

    /**
     * Get path from JAX-RS annotations
     */
    public static String getPath(
        jakarta.ws.rs.Path classPath,
        jakarta.ws.rs.Path methodPath,
        String parentPath,
        boolean isSubresource
    ) {
        StringBuilder path = new StringBuilder();

        if (parentPath != null && !parentPath.isEmpty()) {
            path.append(parentPath);
        }

        if (classPath != null && classPath.value() != null) {
            path.append(classPath.value());
        }

        if (methodPath != null && methodPath.value() != null) {
            if (path.length() > 0 && !path.toString().endsWith("/")) {
                path.append("/");
            }
            path.append(methodPath.value());
        }

        String result = path.toString();
        // Normalize path
        result = result.replaceAll("//+", "/");
        if (result.isEmpty()) {
            result = "/";
        }

        return result;
    }

    /**
     * Check if a path should be ignored
     */
    public static boolean isIgnored(String path, OpenAPIConfiguration config) {
        // Basic implementation - can be enhanced
        return false;
    }

    /**
     * Extract HTTP method from JAX-RS method annotations
     */
    public static String extractOperationMethod(Method method, Object chain) {
        if (method.isAnnotationPresent(jakarta.ws.rs.GET.class)) {
            return "get";
        } else if (method.isAnnotationPresent(jakarta.ws.rs.POST.class)) {
            return "post";
        } else if (method.isAnnotationPresent(jakarta.ws.rs.PUT.class)) {
            return "put";
        } else if (method.isAnnotationPresent(jakarta.ws.rs.DELETE.class)) {
            return "delete";
        } else if (method.isAnnotationPresent(jakarta.ws.rs.PATCH.class)) {
            return "patch";
        } else if (method.isAnnotationPresent(jakarta.ws.rs.HEAD.class)) {
            return "head";
        } else if (method.isAnnotationPresent(jakarta.ws.rs.OPTIONS.class)) {
            return "options";
        }
        return null;
    }

    /**
     * Collect constructor parameters
     */
    public static List<Parameter> collectConstructorParameters(
        Class<?> cls,
        Components components,
        Consumes classConsumes,
        Object jsonView
    ) {
        // Basic implementation - returns empty list
        // Can be enhanced to introspect constructor parameters
        return new ArrayList<>();
    }

    /**
     * Collect field parameters
     */
    public static List<Parameter> collectFieldParameters(
        Class<?> cls,
        Components components,
        Consumes classConsumes,
        Object jsonView
    ) {
        // Basic implementation - returns empty list
        // Can be enhanced to introspect field annotations
        return new ArrayList<>();
    }

    /**
     * Get string list from string array
     */
    public static Optional<List<String>> getStringListFromStringArray(String[] array) {
        if (array == null || array.length == 0) {
            return Optional.empty();
        }
        List<String> list = new ArrayList<>();
        for (String s : array) {
            if (s != null && !s.isEmpty()) {
                list.add(s);
            }
        }
        return list.isEmpty() ? Optional.empty() : Optional.of(list);
    }
}
