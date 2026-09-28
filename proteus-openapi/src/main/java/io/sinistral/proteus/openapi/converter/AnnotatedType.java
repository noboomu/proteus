package io.sinistral.proteus.openapi.converter;

import com.fasterxml.jackson.annotation.JsonView;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Type wrapper with annotations for model resolution.
 */
public class AnnotatedType {
    /** the type. */
    private Type type;
    /** the ctx annotations. */
    private List<Annotation> ctxAnnotations;
    /** the annotations. */
    private Annotation[] annotations;
    /** the name. */
    private String name;
    /** the resolve as ref. */
    private boolean resolveAsRef = false;
    /** the schema from annotation. */
    private boolean schemaFromAnnotation = false;
    /** the json view annotation. */
    private JsonView jsonViewAnnotation;
    /** the property name. */
    private String propertyName;
    /** the skip override. */
    private Set<String> skipOverride = new LinkedHashSet<>();

    /** Creates an empty annotated type. */
    public AnnotatedType() {
    }

    /**
     * Creates an annotated type wrapping the given type.
     *
     * @param type the wrapped type
     */
    public AnnotatedType(Type type) {
        this.type = type;
    }

    /**
     * Returns the wrapped type.
     *
     * @return the type, or null when unset
     */
    public Type getType() {
        return type;
    }

    /**
     * Sets the wrapped type.
     *
     * @param type the type to wrap
     */
    public void setType(Type type) {
        this.type = type;
    }

    /**
     * Sets the wrapped type, fluent style.
     *
     * @param type the type to wrap
     * @return this instance
     */
    public AnnotatedType type(Type type) {
        this.type = type;
        return this;
    }

    /**
     * Returns the context annotations of the wrapped element.
     *
     * @return the context annotations, or null when unset
     */
    public List<Annotation> getCtxAnnotations() {
        return ctxAnnotations;
    }

    /**
     * Sets the context annotations of the wrapped element.
     *
     * @param ctxAnnotations the context annotations
     */
    public void setCtxAnnotations(List<Annotation> ctxAnnotations) {
        this.ctxAnnotations = ctxAnnotations;
    }

    /**
     * Sets the context annotations, fluent style.
     *
     * @param ctxAnnotations the context annotations
     * @return this instance
     */
    public AnnotatedType ctxAnnotations(List<Annotation> ctxAnnotations) {
        this.ctxAnnotations = ctxAnnotations;
        return this;
    }

    /**
     * Returns the annotations attached to the type use.
     *
     * @return the annotations, or null when unset
     */
    public Annotation[] getAnnotations() {
        return annotations;
    }

    /**
     * Sets the annotations attached to the type use.
     *
     * @param annotations the annotations
     */
    public void setAnnotations(Annotation[] annotations) {
        this.annotations = annotations;
    }

    /**
     * Sets the type-use annotations, fluent style.
     *
     * @param annotations the annotations
     * @return this instance
     */
    public AnnotatedType annotations(Annotation[] annotations) {
        this.annotations = annotations;
        return this;
    }

    /**
     * Returns the model name.
     *
     * @return the name, or null when unset
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the model name.
     *
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the model name, fluent style.
     *
     * @param name the name
     * @return this instance
     */
    public AnnotatedType name(String name) {
        this.name = name;
        return this;
    }

    /**
     * Returns true when the type resolves as a schema reference.
     *
     * @return the resolve-as-ref flag
     */
    public boolean isResolveAsRef() {
        return resolveAsRef;
    }

    /**
     * Sets whether the type resolves as a schema reference.
     *
     * @param resolveAsRef the resolve-as-ref flag
     */
    public void setResolveAsRef(boolean resolveAsRef) {
        this.resolveAsRef = resolveAsRef;
    }

    /**
     * Sets the resolve-as-ref flag, fluent style.
     *
     * @param resolveAsRef the resolve-as-ref flag
     * @return this instance
     */
    public AnnotatedType resolveAsRef(boolean resolveAsRef) {
        this.resolveAsRef = resolveAsRef;
        return this;
    }

    /**
     * Returns true when the schema is taken from an explicit annotation.
     *
     * @return the schema-from-annotation flag
     */
    public boolean isSchemaFromAnnotation() {
        return schemaFromAnnotation;
    }

    /**
     * Sets whether the schema is taken from an explicit annotation.
     *
     * @param schemaFromAnnotation the schema-from-annotation flag
     */
    public void setSchemaFromAnnotation(boolean schemaFromAnnotation) {
        this.schemaFromAnnotation = schemaFromAnnotation;
    }

    /**
     * Sets the schema-from-annotation flag, fluent style.
     *
     * @param schemaFromAnnotation the schema-from-annotation flag
     * @return this instance
     */
    public AnnotatedType schemaFromAnnotation(boolean schemaFromAnnotation) {
        this.schemaFromAnnotation = schemaFromAnnotation;
        return this;
    }

    /**
     * Returns the Jackson view annotation applied during resolution.
     *
     * @return the view annotation, or null when unset
     */
    public JsonView getJsonViewAnnotation() {
        return jsonViewAnnotation;
    }

    /**
     * Sets the Jackson view annotation applied during resolution.
     *
     * @param jsonViewAnnotation the view annotation
     */
    public void setJsonViewAnnotation(JsonView jsonViewAnnotation) {
        this.jsonViewAnnotation = jsonViewAnnotation;
    }

    /**
     * Sets the view annotation, fluent style.
     *
     * @param jsonViewAnnotation the view annotation
     * @return this instance
     */
    public AnnotatedType jsonViewAnnotation(JsonView jsonViewAnnotation) {
        this.jsonViewAnnotation = jsonViewAnnotation;
        return this;
    }

    /**
     * Returns the property name the type is bound to.
     *
     * @return the property name, or null when unset
     */
    public String getPropertyName() {
        return propertyName;
    }

    /**
     * Sets the property name the type is bound to.
     *
     * @param propertyName the property name
     */
    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    /**
     * Sets the property name, fluent style.
     *
     * @param propertyName the property name
     * @return this instance
     */
    public AnnotatedType propertyName(String propertyName) {
        this.propertyName = propertyName;
        return this;
    }

    /**
     * Returns the property names whose schema fields cannot be overridden.
     *
     * @return the skip override set, never null
     */
    public Set<String> getSkipOverride() {
        return skipOverride;
    }

    /**
     * Sets the property names whose schema fields cannot be overridden.
     *
     * @param skipOverride the skip override set
     */
    public void setSkipOverride(Set<String> skipOverride) {
        this.skipOverride = skipOverride;
    }

    /**
     * Sets the skip override set, fluent style.
     *
     * @param skipOverride the skip override set
     * @return this instance
     */
    public AnnotatedType skipOverride(Set<String> skipOverride) {
        this.skipOverride = skipOverride;
        return this;
    }

    /**
     * Processes this element.
    *
    * @param o the value
    * @return the result
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AnnotatedType)) return false;
        AnnotatedType that = (AnnotatedType) o;
        if (type != null ? !type.equals(that.type) : that.type != null) return false;
        if (ctxAnnotations != null ? !ctxAnnotations.equals(that.ctxAnnotations) : that.ctxAnnotations != null)
            return false;
        if (!Arrays.equals(annotations, that.annotations)) return false;
        return name != null ? name.equals(that.name) : that.name == null;
    }

    /**
     * Processes this element.
    *
    * @return the result
     */
    @Override
    public int hashCode() {
        int result = type != null ? type.hashCode() : 0;
        result = 31 * result + (ctxAnnotations != null ? ctxAnnotations.hashCode() : 0);
        result = 31 * result + Arrays.hashCode(annotations);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    /**
     * Processes this element.
    *
    * @return the result
     */
    @Override
    public String toString() {
        return "AnnotatedType{" +
            "type=" + type +
            ", name='" + name + '\'' +
            '}';
    }
}
