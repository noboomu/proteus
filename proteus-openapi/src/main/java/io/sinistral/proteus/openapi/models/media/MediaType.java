package io.sinistral.proteus.openapi.models.media;

import io.sinistral.proteus.openapi.models.examples.Example;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class MediaType {
    private Schema schema;
    private Object example;
    private Map<String, Example> examples;
    private Map<String, Encoding> encoding;
    private Map<String, Object> extensions;

    public Schema getSchema() {
        return schema;
    }

    public void setSchema(Schema schema) {
        this.schema = schema;
    }

    public MediaType schema(Schema schema) {
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

    public void addExamples(String key, Example examplesItem) {
        if (this.examples == null) {
            this.examples = new LinkedHashMap<>();
        }
        this.examples.put(key, examplesItem);
    }

    public Map<String, Encoding> getEncoding() {
        return encoding;
    }

    public void setEncoding(Map<String, Encoding> encoding) {
        this.encoding = encoding;
    }

    public void addEncoding(String key, Encoding encodingItem) {
        if (this.encoding == null) {
            this.encoding = new LinkedHashMap<>();
        }
        this.encoding.put(key, encodingItem);
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
        if (!(o instanceof MediaType)) return false;
        MediaType mediaType = (MediaType) o;
        return Objects.equals(schema, mediaType.schema) &&
            Objects.equals(example, mediaType.example);
    }

    @Override
    public int hashCode() {
        return Objects.hash(schema, example);
    }
}
