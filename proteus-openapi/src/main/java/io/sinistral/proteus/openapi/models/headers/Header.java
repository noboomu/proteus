package io.sinistral.proteus.openapi.models.headers;

import io.sinistral.proteus.openapi.models.examples.Example;
import io.sinistral.proteus.openapi.models.media.Content;
import io.sinistral.proteus.openapi.models.media.Schema;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/** OpenAPI header object describing a single response or parameter header. */
public class Header {

    /** Creates the object. */
    public Header() {}
    /** The description. */
    private String description;
    /** The required. */
    private Boolean required;
    /** The deprecated. */
    private Boolean deprecated;
    /** The style. */
    private StyleEnum style;
    /** The explode. */
    private Boolean explode;
    /** The schema. */
    private Schema schema;
    /** The example. */
    private Object example;
    /** The examples. */
    private Map<String, Example> examples;
    /** The content. */
    private Content content;
    /** The reference. */
    private String $ref;
    /** The extensions. */
    private Map<String, Object> extensions;

    /** Header serialization style selector. */
    public enum StyleEnum {
        /** The simple value. */
        SIMPLE("simple");

        /** The value. */
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
         * Operates on the value.
        *
        * @return the result
         */
        public String toString() {
            return String.valueOf(value);
        }
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
     * Returns the value.
     *
     * @return the value, or null when unset
     */
    public Object getExample() {
        return example;
    }

    /**
     * Sets the value.
     *
     * @param example the value
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
     * Returns the value.
     *
     * @return the value, or null when unset
     */
    public String get$ref() {
        return $ref;
    }

    /**
     * Sets the value.
     *
     * @param $ref the value
     */
    public void set$ref(String $ref) {
        this.$ref = $ref;
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
     * Adds a vendor extension entry, ignoring names that are not {@code x-} prefixed.
     *
     * @param name the extension name
     * @param value the extension value
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
     * Operates on the value.
    *
    * @param o the value
    * @return the result
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Header)) return false;
        Header header = (Header) o;
        return Objects.equals(description, header.description) &&
            Objects.equals($ref, header.$ref);
    }

    /**
     * Operates on the value.
    *
    * @return the result
     */
    @Override
    public int hashCode() {
        return Objects.hash(description, $ref);
    }
}
