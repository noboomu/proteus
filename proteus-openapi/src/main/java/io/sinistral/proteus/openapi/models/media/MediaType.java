package io.sinistral.proteus.openapi.models.media;

import io.sinistral.proteus.openapi.models.examples.Example;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/** OpenAPI media type object with examples and encoding. */
public class MediaType {

    /** Creates the object. */
    public MediaType() {}
    /** the schema. */
    private Schema schema;
    /** the example. */
    private Object example;
    /** the examples. */
    private Map<String, Example> examples;
    /** the encoding. */
    private Map<String, Encoding> encoding;
    /** the extensions. */
    private Map<String, Object> extensions;

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
    public MediaType schema(Schema schema) {
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
     * Adds an entry to the example map.
     *
     * @param key the entry
     * @param examplesItem the entry
     */
    public void addExamples(String key, Example examplesItem) {
        if (this.examples == null) {
            this.examples = new LinkedHashMap<>();
        }
        this.examples.put(key, examplesItem);
    }

    /**
     * Returns the encoding value.
     *
     * @return the encoding value, or null when unset
     */
    public Map<String, Encoding> getEncoding() {
        return encoding;
    }

    /**
     * Sets the encoding value.
     *
     * @param encoding the encoding value
     */
    public void setEncoding(Map<String, Encoding> encoding) {
        this.encoding = encoding;
    }

    /**
     * Adds an entry to the encoding value.
     *
     * @param key the entry
     * @param encodingItem the entry
     */
    public void addEncoding(String key, Encoding encodingItem) {
        if (this.encoding == null) {
            this.encoding = new LinkedHashMap<>();
        }
        this.encoding.put(key, encodingItem);
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
        if (!(o instanceof MediaType)) return false;
        MediaType mediaType = (MediaType) o;
        return Objects.equals(schema, mediaType.schema) &&
            Objects.equals(example, mediaType.example);
    }

    /**
     * Processes this element.
    *
    * @return the result
     */
    @Override
    public int hashCode() {
        return Objects.hash(schema, example);
    }
}
