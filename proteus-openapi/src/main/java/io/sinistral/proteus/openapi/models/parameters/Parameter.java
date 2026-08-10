package io.sinistral.proteus.openapi.models.parameters;

import io.sinistral.proteus.openapi.models.ExternalDocumentation;
import io.sinistral.proteus.openapi.models.examples.Example;
import io.sinistral.proteus.openapi.models.media.Content;
import io.sinistral.proteus.openapi.models.media.Schema;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class Parameter {
    private String name;
    private String in;
    private String description;
    private Boolean required;
    private Boolean deprecated;
    private Boolean allowEmptyValue;
    private StyleEnum style;
    private Boolean explode;
    private Boolean allowReserved;
    private Schema schema;
    private Object example;
    private Map<String, Example> examples;
    private Content content;
    private String $ref;
    private Map<String, Object> extensions;

    public enum StyleEnum {
        MATRIX("matrix"),
        LABEL("label"),
        FORM("form"),
        SIMPLE("simple"),
        SPACEDELIMITED("spaceDelimited"),
        PIPEDELIMITED("pipeDelimited"),
        DEEPOBJECT("deepObject");

        private final String value;

        StyleEnum(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }

        public static StyleEnum fromValue(String value) {
            for (StyleEnum b : StyleEnum.values()) {
                if (b.value.equals(value)) {
                    return b;
                }
            }
            return null;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Parameter name(String name) {
        this.name = name;
        return this;
    }

    public String getIn() {
        return in;
    }

    public void setIn(String in) {
        this.in = in;
    }

    public Parameter in(String in) {
        this.in = in;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Parameter description(String description) {
        this.description = description;
        return this;
    }

    public Boolean getRequired() {
        return required;
    }

    public void setRequired(Boolean required) {
        this.required = required;
    }

    public Parameter required(Boolean required) {
        this.required = required;
        return this;
    }

    public Boolean getDeprecated() {
        return deprecated;
    }

    public void setDeprecated(Boolean deprecated) {
        this.deprecated = deprecated;
    }

    public Boolean getAllowEmptyValue() {
        return allowEmptyValue;
    }

    public void setAllowEmptyValue(Boolean allowEmptyValue) {
        this.allowEmptyValue = allowEmptyValue;
    }

    public Parameter allowEmptyValue(Boolean allowEmptyValue) {
        this.allowEmptyValue = allowEmptyValue;
        return this;
    }

    public StyleEnum getStyle() {
        return style;
    }

    public void setStyle(StyleEnum style) {
        this.style = style;
    }

    public Parameter style(StyleEnum style) {
        this.style = style;
        return this;
    }

    public Boolean getExplode() {
        return explode;
    }

    public void setExplode(Boolean explode) {
        this.explode = explode;
    }

    public Boolean getAllowReserved() {
        return allowReserved;
    }

    public void setAllowReserved(Boolean allowReserved) {
        this.allowReserved = allowReserved;
    }

    public Schema getSchema() {
        return schema;
    }

    public void setSchema(Schema schema) {
        this.schema = schema;
    }

    public Parameter schema(Schema schema) {
        this.schema = schema;
        return this;
    }

    public Object getExample() {
        return example;
    }

    public void setExample(Object example) {
        this.example = example;
    }

    public Map<String, Example> getExamples() {
        return examples;
    }

    public void setExamples(Map<String, Example> examples) {
        this.examples = examples;
    }

    public void addExample(String key, Example examplesItem) {
        if (this.examples == null) {
            this.examples = new LinkedHashMap<>();
        }
        this.examples.put(key, examplesItem);
    }

    public Content getContent() {
        return content;
    }

    public void setContent(Content content) {
        this.content = content;
    }

    public String get$ref() {
        return $ref;
    }

    public void set$ref(String $ref) {
        this.$ref = $ref;
    }

    public Parameter $ref(String $ref) {
        this.$ref = $ref;
        return this;
    }

    @com.fasterxml.jackson.annotation.JsonAnyGetter
    public Map<String, Object> getExtensions() {
        return extensions;
    }

    public void setExtensions(Map<String, Object> extensions) {
        this.extensions = extensions;
    }

    @com.fasterxml.jackson.annotation.JsonAnySetter
    public void addExtension(String name, Object value) {
        if (name == null || name.isEmpty() || !name.startsWith("x-")) {
            return;
        }
        if (this.extensions == null) {
            this.extensions = new LinkedHashMap<>();
        }
        this.extensions.put(name, value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Parameter)) return false;
        Parameter parameter = (Parameter) o;
        return Objects.equals(name, parameter.name) &&
            Objects.equals(in, parameter.in) &&
            Objects.equals($ref, parameter.$ref);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, in, $ref);
    }

    @Override
    public String toString() {
        return "Parameter{" +
            "name='" + name + '\'' +
            ", in='" + in + '\'' +
            ", required=" + required +
            '}';
    }
}
