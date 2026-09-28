package io.sinistral.proteus.openapi.models.parameters;

import io.sinistral.proteus.openapi.models.ExternalDocumentation;
import io.sinistral.proteus.openapi.models.examples.Example;
import io.sinistral.proteus.openapi.models.media.Content;
import io.sinistral.proteus.openapi.models.media.Schema;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/** OpenAPI parameter object describing one operation parameter. */
public class Parameter {

    /** Creates the object. */
    public Parameter() {}
    /** the name. */
    private String name;
    /** the in. */
    private String in;
    /** the description. */
    private String description;
    /** the required. */
    private Boolean required;
    /** the deprecated. */
    private Boolean deprecated;
    /** the allow empty value. */
    private Boolean allowEmptyValue;
    /** the style. */
    private StyleEnum style;
    /** the explode. */
    private Boolean explode;
    /** the allow reserved. */
    private Boolean allowReserved;
    /** the schema. */
    private Schema schema;
    /** the example. */
    private Object example;
    /** the examples. */
    private Map<String, Example> examples;
    /** the content. */
    private Content content;
    /** the reference. */
    private String $ref;
    /** the extensions. */
    private Map<String, Object> extensions;

    /** Parameter serialization style selector. */
    public enum StyleEnum {
        /** The matrix value. */
        MATRIX("matrix"),
        /** The label value. */
        LABEL("label"),
        /** The form value. */
        FORM("form"),
        /** The simple value. */
        SIMPLE("simple"),
        /** The spaceDelimited value. */
        SPACEDELIMITED("spaceDelimited"),
        /** The pipeDelimited value. */
        PIPEDELIMITED("pipeDelimited"),
        /** The deepObject value. */
        DEEPOBJECT("deepObject");

        /** the value. */
        private final String value;

        StyleEnum(String value) {
            this.value = value;
        }

        /**
         * Returns the value.
         *
         * @return the value, or null when unset
         */
        public String getValue() {
            return value;
        }

        @Override
        /**
         * Processes this element.
        *
        * @return the result
         */
        public String toString() {
            return String.valueOf(value);
        }

        /**
         * Processes this element.
        *
        * @param value the value
        * @return the result
         */
        public static StyleEnum fromValue(String value) {
            for (StyleEnum b : StyleEnum.values()) {
                if (b.value.equals(value)) {
                    return b;
                }
            }
            return null;
        }
    }

    /**
     * Returns the name.
     *
     * @return the name, or null when unset
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name.
     *
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the name, fluent style.
     *
     * @param name the name
     * @return this instance
     */
    public Parameter name(String name) {
        this.name = name;
        return this;
    }

    /**
     * Returns the location.
     *
     * @return the location, or null when unset
     */
    public String getIn() {
        return in;
    }

    /**
     * Sets the location.
     *
     * @param in the location
     */
    public void setIn(String in) {
        this.in = in;
    }

    /**
     * Sets the location, fluent style.
     *
     * @param in the location
     * @return this instance
     */
    public Parameter in(String in) {
        this.in = in;
        return this;
    }

    /**
     * Returns the description.
     *
     * @return the description, or null when unset
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description.
     *
     * @param description the description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Sets the description, fluent style.
     *
     * @param description the description
     * @return this instance
     */
    public Parameter description(String description) {
        this.description = description;
        return this;
    }

    /**
     * Returns the required flag.
     *
     * @return the required flag, or null when unset
     */
    public Boolean getRequired() {
        return required;
    }

    /**
     * Sets the required flag.
     *
     * @param required the required flag
     */
    public void setRequired(Boolean required) {
        this.required = required;
    }

    /**
     * Sets the required flag, fluent style.
     *
     * @param required the required flag
     * @return this instance
     */
    public Parameter required(Boolean required) {
        this.required = required;
        return this;
    }

    /**
     * Returns the deprecated flag.
     *
     * @return the deprecated flag, or null when unset
     */
    public Boolean getDeprecated() {
        return deprecated;
    }

    /**
     * Sets the deprecated flag.
     *
     * @param deprecated the deprecated flag
     */
    public void setDeprecated(Boolean deprecated) {
        this.deprecated = deprecated;
    }

    /**
     * Returns the deprecated value.
     *
     * @return the deprecated value, or null when unset
     */
    public Boolean getAllowEmptyValue() {
        return allowEmptyValue;
    }

    /**
     * Sets the allow empty value value.
     *
     * @param allowEmptyValue the allow empty value value
     */
    public void setAllowEmptyValue(Boolean allowEmptyValue) {
        this.allowEmptyValue = allowEmptyValue;
    }

    /**
     * Processes this element.
    *
    * @param allowEmptyValue the value
    * @return the result
     */
    public Parameter allowEmptyValue(Boolean allowEmptyValue) {
        this.allowEmptyValue = allowEmptyValue;
        return this;
    }

    /**
     * Returns the style.
     *
     * @return the style, or null when unset
     */
    public StyleEnum getStyle() {
        return style;
    }

    /**
     * Sets the style.
     *
     * @param style the style
     */
    public void setStyle(StyleEnum style) {
        this.style = style;
    }

    /**
     * Sets the style, fluent style.
     *
     * @param style the style
     * @return this instance
     */
    public Parameter style(StyleEnum style) {
        this.style = style;
        return this;
    }

    /**
     * Returns the explode flag.
     *
     * @return the explode flag, or null when unset
     */
    public Boolean getExplode() {
        return explode;
    }

    /**
     * Sets the explode flag.
     *
     * @param explode the explode flag
     */
    public void setExplode(Boolean explode) {
        this.explode = explode;
    }

    /**
     * Returns the allowReserved flag.
     *
     * @return the allowReserved flag, or null when unset
     */
    public Boolean getAllowReserved() {
        return allowReserved;
    }

    /**
     * Sets the allowReserved flag.
     *
     * @param allowReserved the allowReserved flag
     */
    public void setAllowReserved(Boolean allowReserved) {
        this.allowReserved = allowReserved;
    }

    /**
     * Returns the schema.
     *
     * @return the schema, or null when unset
     */
    public Schema getSchema() {
        return schema;
    }

    /**
     * Sets the schema.
     *
     * @param schema the schema
     */
    public void setSchema(Schema schema) {
        this.schema = schema;
    }

    /**
     * Sets the schema, fluent style.
     *
     * @param schema the schema
     * @return this instance
     */
    public Parameter schema(Schema schema) {
        this.schema = schema;
        return this;
    }

    /**
     * Returns the example.
     *
     * @return the example, or null when unset
     */
    public Object getExample() {
        return example;
    }

    /**
     * Sets the example.
     *
     * @param example the example
     */
    public void setExample(Object example) {
        this.example = example;
    }

    /**
     * Returns the example map.
     *
     * @return the example map, or null when unset
     */
    public Map<String, Example> getExamples() {
        return examples;
    }

    /**
     * Sets the example map.
     *
     * @param examples the example map
     */
    public void setExamples(Map<String, Example> examples) {
        this.examples = examples;
    }

    /**
     * Adds an entry to the example.
     *
     * @param key the entry
     * @param examplesItem the entry
     */
    public void addExample(String key, Example examplesItem) {
        if (this.examples == null) {
            this.examples = new LinkedHashMap<>();
        }
        this.examples.put(key, examplesItem);
    }

    /**
     * Returns the content.
     *
     * @return the content, or null when unset
     */
    public Content getContent() {
        return content;
    }

    /**
     * Sets the content.
     *
     * @param content the content
     */
    public void setContent(Content content) {
        this.content = content;
    }

    /**
     * Returns the reference value.
     *
     * @return the reference value, or null when unset
     */
    public String get$ref() {
        return $ref;
    }

    /**
     * Sets the reference value.
     *
     * @param $ref the reference value
     */
    public void set$ref(String $ref) {
        this.$ref = $ref;
    }

    /**
     * Processes this element.
    *
    * @param $ref the value
    * @return the result
     */
    public Parameter $ref(String $ref) {
        this.$ref = $ref;
        return this;
    }

    /**
     * Returns the extension map.
     *
     * @return the extension map, or null when unset
     */
    @com.fasterxml.jackson.annotation.JsonAnyGetter
    public Map<String, Object> getExtensions() {
        return extensions;
    }

    /**
     * Sets the extension map.
     *
     * @param extensions the extension map
     */
    public void setExtensions(Map<String, Object> extensions) {
        this.extensions = extensions;
    }

    /**
     * Adds an entry to the extension value.
     *
     * @param name the entry
     * @param value the entry
     */
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

    /**
     * Processes this element.
    *
    * @param o the value
    * @return the result
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Parameter)) return false;
        Parameter parameter = (Parameter) o;
        return Objects.equals(name, parameter.name) &&
            Objects.equals(in, parameter.in) &&
            Objects.equals($ref, parameter.$ref);
    }

    /**
     * Processes this element.
    *
    * @return the result
     */
    @Override
    public int hashCode() {
        return Objects.hash(name, in, $ref);
    }

    /**
     * Processes this element.
    *
    * @return the result
     */
    @Override
    public String toString() {
        return "Parameter{" +
            "name='" + name + '\'' +
            ", in='" + in + '\'' +
            ", required=" + required +
            '}';
    }
}
