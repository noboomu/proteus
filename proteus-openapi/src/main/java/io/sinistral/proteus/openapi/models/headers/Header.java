package io.sinistral.proteus.openapi.models.headers;

import io.sinistral.proteus.openapi.models.examples.Example;
import io.sinistral.proteus.openapi.models.media.Content;
import io.sinistral.proteus.openapi.models.media.Schema;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class Header {
    private String description;
    private Boolean required;
    private Boolean deprecated;
    private StyleEnum style;
    private Boolean explode;
    private Schema schema;
    private Object example;
    private Map<String, Example> examples;
    private Content content;
    private String $ref;
    private Map<String, Object> extensions;

    public enum StyleEnum {
        SIMPLE("simple");

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
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getRequired() {
        return required;
    }

    public void setRequired(Boolean required) {
        this.required = required;
    }

    public Boolean getDeprecated() {
        return deprecated;
    }

    public void setDeprecated(Boolean deprecated) {
        this.deprecated = deprecated;
    }

    public StyleEnum getStyle() {
        return style;
    }

    public void setStyle(StyleEnum style) {
        this.style = style;
    }

    public Boolean getExplode() {
        return explode;
    }

    public void setExplode(Boolean explode) {
        this.explode = explode;
    }

    public Schema getSchema() {
        return schema;
    }

    public void setSchema(Schema schema) {
        this.schema = schema;
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
        if (!(o instanceof Header)) return false;
        Header header = (Header) o;
        return Objects.equals(description, header.description) &&
            Objects.equals($ref, header.$ref);
    }

    @Override
    public int hashCode() {
        return Objects.hash(description, $ref);
    }
}
