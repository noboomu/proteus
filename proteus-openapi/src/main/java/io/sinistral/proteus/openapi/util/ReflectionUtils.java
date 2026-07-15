package io.sinistral.proteus.openapi.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Reflection utilities for OpenAPI processing
 */
public class ReflectionUtils {

    /**
     * Get annotation from class
     */
    public static <T extends Annotation> T getAnnotation(Class<?> cls, Class<T> annotationClass) {
        if (cls == null || annotationClass == null) {
            return null;
        }
        return cls.getAnnotation(annotationClass);
    }

    /**
     * Get annotation from method
     */
    public static <T extends Annotation> T getAnnotation(Method method, Class<T> annotationClass) {
        if (method == null || annotationClass == null) {
            return null;
        }
        return method.getAnnotation(annotationClass);
    }

    /**
     * Get repeatable annotations from class
     */
    public static <T extends Annotation> List<T> getRepeatableAnnotations(Class<?> cls, Class<T> annotationClass) {
        if (cls == null || annotationClass == null) {
            return new ArrayList<>();
        }
        T[] annotations = cls.getAnnotationsByType(annotationClass);
        return Arrays.asList(annotations);
    }

    /**
     * Get repeatable annotations from method
     */
    public static <T extends Annotation> List<T> getRepeatableAnnotations(Method method, Class<T> annotationClass) {
        if (method == null || annotationClass == null) {
            return new ArrayList<>();
        }
        T[] annotations = method.getAnnotationsByType(annotationClass);
        return Arrays.asList(annotations);
    }

    /**
     * Get repeatable annotations as array from class
     */
    public static <T extends Annotation> T[] getRepeatableAnnotationsArray(Class<?> cls, Class<T> annotationClass) {
        if (cls == null || annotationClass == null) {
            return null;
        }
        return cls.getAnnotationsByType(annotationClass);
    }

    /**
     * Get repeatable annotations as array from method
     */
    public static <T extends Annotation> T[] getRepeatableAnnotationsArray(Method method, Class<T> annotationClass) {
        if (method == null || annotationClass == null) {
            return null;
        }
        return method.getAnnotationsByType(annotationClass);
    }

    /**
     * Check if a method is overridden
     */
    public static boolean isOverriddenMethod(Method method, Class<?> cls) {
        if (method == null || cls == null) {
            return false;
        }

        Class<?> declaringClass = method.getDeclaringClass();
        if (declaringClass.equals(cls)) {
            return false;
        }

        // Check if the class or any superclass has this method
        Class<?> current = cls;
        while (current != null && !current.equals(declaringClass)) {
            try {
                current.getDeclaredMethod(method.getName(), method.getParameterTypes());
                return true;
            } catch (NoSuchMethodException e) {
                current = current.getSuperclass();
            }
        }
        return false;
    }

    /**
     * Get the overridden method if it exists
     */
    public static Method getOverriddenMethod(Method method) {
        if (method == null) {
            return null;
        }

        Class<?> declaringClass = method.getDeclaringClass();
        Class<?> superClass = declaringClass.getSuperclass();

        if (superClass != null) {
            try {
                return superClass.getMethod(method.getName(), method.getParameterTypes());
            } catch (NoSuchMethodException e) {
                // No overridden method
            }
        }
        return null;
    }
}
