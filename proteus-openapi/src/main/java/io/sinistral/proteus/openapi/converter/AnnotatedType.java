package io.sinistral.proteus.openapi.converter;

import com.fasterxml.jackson.annotation.JsonView;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Type wrapper with annotations for model resolution
 */
public class AnnotatedType {
    private Type type;
    private List<Annotation> ctxAnnotations;
    private Annotation[] annotations;
    private String name;
    private boolean resolveAsRef = false;
    private boolean schemaFromAnnotation = false;
    private JsonView jsonViewAnnotation;
    private String propertyName;
    private Set<String> skipOverride = new LinkedHashSet<>();

    public AnnotatedType() {
    }

    public AnnotatedType(Type type) {
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public AnnotatedType type(Type type) {
        this.type = type;
        return this;
    }

    public List<Annotation> getCtxAnnotations() {
        return ctxAnnotations;
    }

    public void setCtxAnnotations(List<Annotation> ctxAnnotations) {
        this.ctxAnnotations = ctxAnnotations;
    }

    public AnnotatedType ctxAnnotations(List<Annotation> ctxAnnotations) {
        this.ctxAnnotations = ctxAnnotations;
        return this;
    }

    public Annotation[] getAnnotations() {
        return annotations;
    }

    public void setAnnotations(Annotation[] annotations) {
        this.annotations = annotations;
    }

    public AnnotatedType annotations(Annotation[] annotations) {
        this.annotations = annotations;
        return this;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AnnotatedType name(String name) {
        this.name = name;
        return this;
    }

    public boolean isResolveAsRef() {
        return resolveAsRef;
    }

    public void setResolveAsRef(boolean resolveAsRef) {
        this.resolveAsRef = resolveAsRef;
    }

    public AnnotatedType resolveAsRef(boolean resolveAsRef) {
        this.resolveAsRef = resolveAsRef;
        return this;
    }

    public boolean isSchemaFromAnnotation() {
        return schemaFromAnnotation;
    }

    public void setSchemaFromAnnotation(boolean schemaFromAnnotation) {
        this.schemaFromAnnotation = schemaFromAnnotation;
    }

    public AnnotatedType schemaFromAnnotation(boolean schemaFromAnnotation) {
        this.schemaFromAnnotation = schemaFromAnnotation;
        return this;
    }

    public JsonView getJsonViewAnnotation() {
        return jsonViewAnnotation;
    }

    public void setJsonViewAnnotation(JsonView jsonViewAnnotation) {
        this.jsonViewAnnotation = jsonViewAnnotation;
    }

    public AnnotatedType jsonViewAnnotation(JsonView jsonViewAnnotation) {
        this.jsonViewAnnotation = jsonViewAnnotation;
        return this;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public AnnotatedType propertyName(String propertyName) {
        this.propertyName = propertyName;
        return this;
    }

    public Set<String> getSkipOverride() {
        return skipOverride;
    }

    public void setSkipOverride(Set<String> skipOverride) {
        this.skipOverride = skipOverride;
    }

    public AnnotatedType skipOverride(Set<String> skipOverride) {
        this.skipOverride = skipOverride;
        return this;
    }

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

    @Override
    public int hashCode() {
        int result = type != null ? type.hashCode() : 0;
        result = 31 * result + (ctxAnnotations != null ? ctxAnnotations.hashCode() : 0);
        result = 31 * result + Arrays.hashCode(annotations);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "AnnotatedType{" +
            "type=" + type +
            ", name='" + name + '\'' +
            '}';
    }
}
