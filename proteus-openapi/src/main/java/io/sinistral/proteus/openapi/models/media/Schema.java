package io.sinistral.proteus.openapi.models.media;

import io.sinistral.proteus.openapi.models.ExternalDocumentation;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Schema object for OpenAPI 3.1
 */
public class Schema<T> {
    private String type;
    private String format;
    private String title;
    private String description;
    private T _default;
    private BigDecimal multipleOf;
    private BigDecimal maximum;
    private Boolean exclusiveMaximum;
    private BigDecimal minimum;
    private Boolean exclusiveMinimum;
    private Integer maxLength;
    private Integer minLength;
    private String pattern;
    private Integer maxItems;
    private Integer minItems;
    private Boolean uniqueItems;
    private Integer maxProperties;
    private Integer minProperties;
    private List<String> required;
    private List<T> _enum;
    private List<Schema> allOf;
    private List<Schema> anyOf;
    private List<Schema> oneOf;
    private Schema not;
    private Map<String, Schema> properties;
    private Object additionalProperties;
    private Schema items;
    private Boolean readOnly;
    private Boolean writeOnly;
    private Boolean deprecated;
    private ExternalDocumentation externalDocs;
    private T example;
    private List<T> examples;
    private Boolean nullable;
    private Discriminator discriminator;
    private XML xml;
    private String $ref;
    private Map<String, Object> extensions;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Schema<T> type(String type) {
        this.type = type;
        return this;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public Schema<T> format(String format) {
        this.format = format;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Schema<T> title(String title) {
        this.title = title;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Schema<T> description(String description) {
        this.description = description;
        return this;
    }

    public T getDefault() {
        return _default;
    }

    public void setDefault(T _default) {
        this._default = _default;
    }

    public Schema<T> _default(T _default) {
        this._default = _default;
        return this;
    }

    public BigDecimal getMultipleOf() {
        return multipleOf;
    }

    public void setMultipleOf(BigDecimal multipleOf) {
        this.multipleOf = multipleOf;
    }

    public BigDecimal getMaximum() {
        return maximum;
    }

    public void setMaximum(BigDecimal maximum) {
        this.maximum = maximum;
    }

    public Boolean getExclusiveMaximum() {
        return exclusiveMaximum;
    }

    public void setExclusiveMaximum(Boolean exclusiveMaximum) {
        this.exclusiveMaximum = exclusiveMaximum;
    }

    public BigDecimal getMinimum() {
        return minimum;
    }

    public void setMinimum(BigDecimal minimum) {
        this.minimum = minimum;
    }

    public Boolean getExclusiveMinimum() {
        return exclusiveMinimum;
    }

    public void setExclusiveMinimum(Boolean exclusiveMinimum) {
        this.exclusiveMinimum = exclusiveMinimum;
    }

    public Integer getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(Integer maxLength) {
        this.maxLength = maxLength;
    }

    public Integer getMinLength() {
        return minLength;
    }

    public void setMinLength(Integer minLength) {
        this.minLength = minLength;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public Integer getMaxItems() {
        return maxItems;
    }

    public void setMaxItems(Integer maxItems) {
        this.maxItems = maxItems;
    }

    public Integer getMinItems() {
        return minItems;
    }

    public void setMinItems(Integer minItems) {
        this.minItems = minItems;
    }

    public Boolean getUniqueItems() {
        return uniqueItems;
    }

    public void setUniqueItems(Boolean uniqueItems) {
        this.uniqueItems = uniqueItems;
    }

    public Integer getMaxProperties() {
        return maxProperties;
    }

    public void setMaxProperties(Integer maxProperties) {
        this.maxProperties = maxProperties;
    }

    public Integer getMinProperties() {
        return minProperties;
    }

    public void setMinProperties(Integer minProperties) {
        this.minProperties = minProperties;
    }

    public List<String> getRequired() {
        return required;
    }

    public void setRequired(List<String> required) {
        this.required = required;
    }

    public void addRequiredItem(String requiredItem) {
        if (this.required == null) {
            this.required = new ArrayList<>();
        }
        this.required.add(requiredItem);
    }

    public List<T> getEnum() {
        return _enum;
    }

    public void setEnum(List<T> _enum) {
        this._enum = _enum;
    }

    public void addEnumItemObject(T _enumItem) {
        if (this._enum == null) {
            this._enum = new ArrayList<>();
        }
        this._enum.add(_enumItem);
    }

    public List<Schema> getAllOf() {
        return allOf;
    }

    public void setAllOf(List<Schema> allOf) {
        this.allOf = allOf;
    }

    public List<Schema> getAnyOf() {
        return anyOf;
    }

    public void setAnyOf(List<Schema> anyOf) {
        this.anyOf = anyOf;
    }

    public List<Schema> getOneOf() {
        return oneOf;
    }

    public void setOneOf(List<Schema> oneOf) {
        this.oneOf = oneOf;
    }

    public Schema getNot() {
        return not;
    }

    public void setNot(Schema not) {
        this.not = not;
    }

    public Map<String, Schema> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, Schema> properties) {
        this.properties = properties;
    }

    public void addProperties(String key, Schema propertiesItem) {
        if (this.properties == null) {
            this.properties = new LinkedHashMap<>();
        }
        this.properties.put(key, propertiesItem);
    }

    public Object getAdditionalProperties() {
        return additionalProperties;
    }

    public void setAdditionalProperties(Object additionalProperties) {
        this.additionalProperties = additionalProperties;
    }

    public Schema getItems() {
        return items;
    }

    public void setItems(Schema items) {
        this.items = items;
    }

    public Schema<T> items(Schema items) {
        this.items = items;
        return this;
    }

    public Boolean getReadOnly() {
        return readOnly;
    }

    public void setReadOnly(Boolean readOnly) {
        this.readOnly = readOnly;
    }

    public Boolean getWriteOnly() {
        return writeOnly;
    }

    public void setWriteOnly(Boolean writeOnly) {
        this.writeOnly = writeOnly;
    }

    public Boolean getDeprecated() {
        return deprecated;
    }

    public void setDeprecated(Boolean deprecated) {
        this.deprecated = deprecated;
    }

    public ExternalDocumentation getExternalDocs() {
        return externalDocs;
    }

    public void setExternalDocs(ExternalDocumentation externalDocs) {
        this.externalDocs = externalDocs;
    }

    public T getExample() {
        return example;
    }

    public void setExample(T example) {
        this.example = example;
    }

    public Schema<T> example(T example) {
        this.example = example;
        return this;
    }

    public List<T> getExamples() {
        return examples;
    }

    public void setExamples(List<T> examples) {
        this.examples = examples;
    }

    public Boolean getNullable() {
        return nullable;
    }

    public void setNullable(Boolean nullable) {
        this.nullable = nullable;
    }

    public Discriminator getDiscriminator() {
        return discriminator;
    }

    public void setDiscriminator(Discriminator discriminator) {
        this.discriminator = discriminator;
    }

    public XML getXml() {
        return xml;
    }

    public void setXml(XML xml) {
        this.xml = xml;
    }

    public String get$ref() {
        return $ref;
    }

    public void set$ref(String $ref) {
        this.$ref = $ref;
    }

    public Schema<T> $ref(String $ref) {
        this.$ref = $ref;
        return this;
    }

    public Map<String, Object> getExtensions() {
        return extensions;
    }

    public void setExtensions(Map<String, Object> extensions) {
        this.extensions = extensions;
    }

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
        if (!(o instanceof Schema)) return false;
        Schema<?> schema = (Schema<?>) o;
        return Objects.equals(type, schema.type) &&
            Objects.equals(format, schema.format) &&
            Objects.equals(title, schema.title) &&
            Objects.equals(description, schema.description) &&
            Objects.equals(_default, schema._default) &&
            Objects.equals($ref, schema.$ref);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, format, title, description, _default, $ref);
    }

    @Override
    public String toString() {
        return "Schema{" +
            "type='" + type + '\'' +
            ", format='" + format + '\'' +
            ", $ref='" + $ref + '\'' +
            '}';
    }
}
