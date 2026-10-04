package io.sinistral.proteus.openapi.models.media;

import io.sinistral.proteus.openapi.models.ExternalDocumentation;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Schema object for OpenAPI 3.1.
 *
 * @param <T> the Java type described by this schema
 */
public class Schema<T> {

    /** Creates the object. */
    public Schema() {}
    /** The type. */
    private String type;
    /** The types. */
    private Set<String> types;
    /** The format. */
    private String format;
    /** The title. */
    private String title;
    /** The description. */
    private String description;
    /** The default. */
    private T _default;
    /** The multiple of. */
    private BigDecimal multipleOf;
    /** The maximum. */
    private BigDecimal maximum;
    /** The exclusive maximum. */
    private Boolean exclusiveMaximum;
    /** The minimum. */
    private BigDecimal minimum;
    /** The exclusive minimum. */
    private Boolean exclusiveMinimum;
    /** The max length. */
    private Integer maxLength;
    /** The min length. */
    private Integer minLength;
    /** The pattern. */
    private String pattern;
    /** The max items. */
    private Integer maxItems;
    /** The min items. */
    private Integer minItems;
    /** The unique items. */
    private Boolean uniqueItems;
    /** The max properties. */
    private Integer maxProperties;
    /** The min properties. */
    private Integer minProperties;
    /** The required. */
    private List<String> required;
    /** The enum. */
    private List<T> _enum;
    /** The all of. */
    private List<Schema> allOf;
    /** The any of. */
    private List<Schema> anyOf;
    /** The one of. */
    private List<Schema> oneOf;
    /** The prefix items. */
    private List<Schema> prefixItems;
    /** The not. */
    private Schema not;
    /** The properties. */
    private Map<String, Schema> properties;
    /** The pattern properties. */
    private Map<String, Schema> patternProperties;
    /** The additional properties. */
    private Object additionalProperties;
    /** The items. */
    private Schema items;
    /** The contains. */
    private Schema contains;
    /** The max contains. */
    private Integer maxContains;
    /** The min contains. */
    private Integer minContains;
    /** The additional items. */
    private Schema additionalItems;
    /** The unevaluated items. */
    private Schema unevaluatedItems;
    /** The unevaluated properties. */
    private Schema unevaluatedProperties;
    /** The content schema. */
    private Schema contentSchema;
    /** The property names. */
    private Schema propertyNames;
    /** The if. */
    private Schema _if;
    /** The else. */
    private Schema _else;
    /** The then. */
    private Schema then;
    /** The dependent schemas. */
    private Map<String, Schema> dependentSchemas;
    /** The dependent required. */
    private Map<String, List<String>> dependentRequired;
    /** The exclusive maximum value. */
    private BigDecimal exclusiveMaximumValue;
    /** The exclusive minimum value. */
    private BigDecimal exclusiveMinimumValue;
    /** The $id. */
    private String $id;
    /** The $schema. */
    private String $schema;
    /** The $anchor. */
    private String $anchor;
    /** The $vocabulary. */
    private String $vocabulary;
    /** The $dynamic anchor. */
    private String $dynamicAnchor;
    /** The $dynamic ref. */
    private String $dynamicRef;
    /** The $comment. */
    private String $comment;
    /** The content encoding. */
    private String contentEncoding;
    /** The content media type. */
    private String contentMediaType;
    /** The const. */
    private T _const;
    /** The read only. */
    private Boolean readOnly;
    /** The write only. */
    private Boolean writeOnly;
    /** The deprecated. */
    private Boolean deprecated;
    /** The external docs. */
    private ExternalDocumentation externalDocs;
    /** The example. */
    private T example;
    /** The examples. */
    private List<T> examples;
    /** The nullable. */
    private Boolean nullable;
    /** The discriminator. */
    private Discriminator discriminator;
    /** The xml. */
    private XML xml;
    /** The reference. */
    private String $ref;
    /** The extensions. */
    private Map<String, Object> extensions;

    /**
     * Returns the type.
     *
     * @return the type, or null when unset
     */
    @com.fasterxml.jackson.annotation.JsonIgnore
    public String getType() {
        return type;
    }

    /**
     * Sets the type.
     *
     * @param type the type
     */
    @com.fasterxml.jackson.annotation.JsonIgnore
    public void setType(String type) {
        this.type = type;
        this.types = null;
    }

    /**
     * Sets the type and returns this instance.
     *
     * @param type the type
     * @return this instance
     */
    public Schema<T> type(String type) {
        setType(type);
        return this;
    }

    /**
     * Returns the type set.
     *
     * @return the type set, or null when unset
     */
    @com.fasterxml.jackson.annotation.JsonIgnore
    public Set<String> getTypes() {
        return types;
    }

    /**
     * Sets the type set.
     *
     * @param types the type set
     */
    @com.fasterxml.jackson.annotation.JsonIgnore
    public void setTypes(Set<String> types) {
        this.types = types == null ? null : new LinkedHashSet<>(types);
        if (this.types != null && !this.types.isEmpty()) this.type = null;
    }

    /**
     * Sets the types and returns this instance.
     *
     * @param types the type set
     * @return this instance
     */
    public Schema<T> types(Set<String> types) {
        setTypes(types);
        return this;
    }

    /**
     * Adds an entry to the type.
     *
     * @param type the entry
    * @return the result
     */
    public Schema<T> addType(String type) {
        if (types == null) types = new LinkedHashSet<>();
        types.add(type);
        this.type = null;
        return this;
    }

    /**
     * Returns the type value.
     *
     * @return the type value, or null when unset
     */
    @com.fasterxml.jackson.annotation.JsonProperty("type")
    public Object getJsonType() {
        return types != null && !types.isEmpty() ? types : type;
    }

    /**
     * Sets the json type value.
     *
     * @param value the value value
     */
    @com.fasterxml.jackson.annotation.JsonProperty("type")
    public void setJsonType(Object value) {
        if (value instanceof String stringValue) {
            setType(stringValue);
        } else if (value instanceof Collection<?> collection) {
            LinkedHashSet<String> values = new LinkedHashSet<>();
            collection.forEach(item -> values.add(String.valueOf(item)));
            setTypes(values);
        }
    }

    /**
     * Returns the format.
     *
     * @return the format, or null when unset
     */
    public String getFormat() {
        return format;
    }

    /**
     * Sets the format.
     *
     * @param format the format
     */
    public void setFormat(String format) {
        this.format = format;
    }

    /**
     * Sets the format and returns this instance.
     *
     * @param format the format
     * @return this instance
     */
    public Schema<T> format(String format) {
        this.format = format;
        return this;
    }

    /**
     * Returns the title.
     *
     * @return the title, or null when unset
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title.
     *
     * @param title the title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Sets the title and returns this instance.
     *
     * @param title the title
     * @return this instance
     */
    public Schema<T> title(String title) {
        this.title = title;
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
     * Sets the description and returns this instance.
     *
     * @param description the description
     * @return this instance
     */
    public Schema<T> description(String description) {
        this.description = description;
        return this;
    }

    /**
     * Returns the default value.
     *
     * @return the default value, or null when unset
     */
    public T getDefault() {
        return _default;
    }

    /**
     * Sets the default value.
     *
     * @param _default the default value
     */
    public void setDefault(T _default) {
        this._default = _default;
    }

    /**
     * _default and returns this instance.
     *
     * @param _default the default value
     * @return this instance
     */
    public Schema<T> _default(T _default) {
        this._default = _default;
        return this;
    }

    /**
     * Returns the multiple of.
     *
     * @return the multipleOf constraint, or null when unset
     */
    public BigDecimal getMultipleOf() {
        return multipleOf;
    }

    /**
     * Sets the multipleOf constraint.
     *
     * @param multipleOf the multipleOf constraint
     */
    public void setMultipleOf(BigDecimal multipleOf) {
        this.multipleOf = multipleOf;
    }

    /**
     * Returns the maximum.
     *
     * @return the maximum, or null when unset
     */
    public BigDecimal getMaximum() {
        return maximum;
    }

    /**
     * Sets the maximum.
     *
     * @param maximum the maximum
     */
    public void setMaximum(BigDecimal maximum) {
        this.maximum = maximum;
    }

    /**
     * Returns the exclusiveMaximum flag.
     *
     * @return the exclusiveMaximum flag, or null when unset
     */
    public Boolean getExclusiveMaximum() {
        return exclusiveMaximum;
    }

    /**
     * Sets the exclusiveMaximum flag.
     *
     * @param exclusiveMaximum the exclusiveMaximum flag
     */
    public void setExclusiveMaximum(Boolean exclusiveMaximum) {
        this.exclusiveMaximum = exclusiveMaximum;
    }

    /**
     * Returns the minimum.
     *
     * @return the minimum, or null when unset
     */
    public BigDecimal getMinimum() {
        return minimum;
    }

    /**
     * Sets the minimum.
     *
     * @param minimum the minimum
     */
    public void setMinimum(BigDecimal minimum) {
        this.minimum = minimum;
    }

    /**
     * Returns the exclusiveMinimum flag.
     *
     * @return the exclusiveMinimum flag, or null when unset
     */
    public Boolean getExclusiveMinimum() {
        return exclusiveMinimum;
    }

    /**
     * Sets the exclusiveMinimum flag.
     *
     * @param exclusiveMinimum the exclusiveMinimum flag
     */
    public void setExclusiveMinimum(Boolean exclusiveMinimum) {
        this.exclusiveMinimum = exclusiveMinimum;
    }

    /**
     * Returns the maxLength constraint.
     *
     * @return the maxLength constraint, or null when unset
     */
    public Integer getMaxLength() {
        return maxLength;
    }

    /**
     * Sets the maxLength constraint.
     *
     * @param maxLength the maxLength constraint
     */
    public void setMaxLength(Integer maxLength) {
        this.maxLength = maxLength;
    }

    /**
     * Returns the minLength constraint.
     *
     * @return the minLength constraint, or null when unset
     */
    public Integer getMinLength() {
        return minLength;
    }

    /**
     * Sets the minLength constraint.
     *
     * @param minLength the minLength constraint
     */
    public void setMinLength(Integer minLength) {
        this.minLength = minLength;
    }

    /**
     * Returns the pattern.
     *
     * @return the pattern, or null when unset
     */
    public String getPattern() {
        return pattern;
    }

    /**
     * Sets the pattern.
     *
     * @param pattern the pattern
     */
    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    /**
     * Returns the maxItems constraint.
     *
     * @return the maxItems constraint, or null when unset
     */
    public Integer getMaxItems() {
        return maxItems;
    }

    /**
     * Sets the maxItems constraint.
     *
     * @param maxItems the maxItems constraint
     */
    public void setMaxItems(Integer maxItems) {
        this.maxItems = maxItems;
    }

    /**
     * Returns the minItems constraint.
     *
     * @return the minItems constraint, or null when unset
     */
    public Integer getMinItems() {
        return minItems;
    }

    /**
     * Sets the minItems constraint.
     *
     * @param minItems the minItems constraint
     */
    public void setMinItems(Integer minItems) {
        this.minItems = minItems;
    }

    /**
     * Returns the uniqueItems flag.
     *
     * @return the uniqueItems flag, or null when unset
     */
    public Boolean getUniqueItems() {
        return uniqueItems;
    }

    /**
     * Sets the uniqueItems flag.
     *
     * @param uniqueItems the uniqueItems flag
     */
    public void setUniqueItems(Boolean uniqueItems) {
        this.uniqueItems = uniqueItems;
    }

    /**
     * Returns the maxProperties constraint.
     *
     * @return the maxProperties constraint, or null when unset
     */
    public Integer getMaxProperties() {
        return maxProperties;
    }

    /**
     * Sets the maxProperties constraint.
     *
     * @param maxProperties the maxProperties constraint
     */
    public void setMaxProperties(Integer maxProperties) {
        this.maxProperties = maxProperties;
    }

    /**
     * Returns the minProperties constraint.
     *
     * @return the minProperties constraint, or null when unset
     */
    public Integer getMinProperties() {
        return minProperties;
    }

    /**
     * Sets the minProperties constraint.
     *
     * @param minProperties the minProperties constraint
     */
    public void setMinProperties(Integer minProperties) {
        this.minProperties = minProperties;
    }

    /**
     * Returns the required flag.
     *
     * @return the required flag, or null when unset
     */
    public List<String> getRequired() {
        return required;
    }

    /**
     * Sets the required flag.
     *
     * @param required the required flag
     */
    public void setRequired(List<String> required) {
        this.required = required;
    }

    /**
     * Adds an entry to the required value.
     *
     * @param requiredItem the entry
     */
    public void addRequiredItem(String requiredItem) {
        if (this.required == null) {
            this.required = new ArrayList<>();
        }
        this.required.add(requiredItem);
    }

    /**
     * Returns the enum values.
     *
     * @return the enum values, or null when unset
     */
    public List<T> getEnum() {
        return _enum;
    }

    /**
     * Sets the enum values.
     *
     * @param _enum the enum values
     */
    public void setEnum(List<T> _enum) {
        this._enum = _enum;
    }

    /**
     * Adds an entry to the enum value.
     *
     * @param _enumItem the entry
     */
    public void addEnumItemObject(T _enumItem) {
        if (this._enum == null) {
            this._enum = new ArrayList<>();
        }
        this._enum.add(_enumItem);
    }

    /**
     * Returns the all of.
     *
     * @return the allOf schemas, or null when unset
     */
    public List<Schema> getAllOf() {
        return allOf;
    }

    /**
     * Sets the allOf schemas.
     *
     * @param allOf the allOf schemas
     */
    public void setAllOf(List<Schema> allOf) {
        this.allOf = allOf;
    }

    /**
     * Returns the any of.
     *
     * @return the anyOf schemas, or null when unset
     */
    public List<Schema> getAnyOf() {
        return anyOf;
    }

    /**
     * Sets the anyOf schemas.
     *
     * @param anyOf the anyOf schemas
     */
    public void setAnyOf(List<Schema> anyOf) {
        this.anyOf = anyOf;
    }

    /**
     * Returns the one of.
     *
     * @return the oneOf schemas, or null when unset
     */
    public List<Schema> getOneOf() {
        return oneOf;
    }

    /**
     * Sets the oneOf schemas.
     *
     * @param oneOf the oneOf schemas
     */
    public void setOneOf(List<Schema> oneOf) {
        this.oneOf = oneOf;
    }

    /**
     * Returns the not schema.
     *
     * @return the not schema, or null when unset
     */
    public Schema getNot() {
        return not;
    }

    /**
     * Sets the not schema.
     *
     * @param not the not schema
     */
    public void setNot(Schema not) {
        this.not = not;
    }

    /**
     * Returns the properties value.
     *
     * @return the properties value, or null when unset
     */
    public Map<String, Schema> getProperties() {
        return properties;
    }

    /**
     * Sets the properties value.
     *
     * @param properties the properties value
     */
    public void setProperties(Map<String, Schema> properties) {
        this.properties = properties;
    }

    /**
     * Adds an entry to the properties value.
     *
     * @param key the entry
     * @param propertiesItem the entry
     */
    public void addProperties(String key, Schema propertiesItem) {
        if (this.properties == null) {
            this.properties = new LinkedHashMap<>();
        }
        this.properties.put(key, propertiesItem);
    }

    /**
     * Returns the additional properties.
     *
     * @return the additionalProperties flag or schema, or null when unset
     */
    public Object getAdditionalProperties() {
        return additionalProperties;
    }

    /**
     * Sets the additionalProperties flag or schema.
     *
     * @param additionalProperties the additionalProperties flag or schema
     */
    public void setAdditionalProperties(Object additionalProperties) {
        this.additionalProperties = additionalProperties;
    }

    /**
     * Returns the items schema.
     *
     * @return the items schema, or null when unset
     */
    public Schema getItems() {
        return items;
    }

    /**
     * Sets the items schema.
     *
     * @param items the items schema
     */
    public void setItems(Schema items) {
        this.items = items;
    }

    /**
     * Sets the items and returns this instance.
     *
     * @param items the items schema
     * @return this instance
     */
    public Schema<T> items(Schema items) {
        this.items = items;
        return this;
    }

    /**
     * Returns the readOnly flag.
     *
     * @return the readOnly flag, or null when unset
     */
    public Boolean getReadOnly() {
        return readOnly;
    }

    /**
     * Sets the readOnly flag.
     *
     * @param readOnly the readOnly flag
     */
    public void setReadOnly(Boolean readOnly) {
        this.readOnly = readOnly;
    }

    /**
     * Returns the writeOnly flag.
     *
     * @return the writeOnly flag, or null when unset
     */
    public Boolean getWriteOnly() {
        return writeOnly;
    }

    /**
     * Sets the writeOnly flag.
     *
     * @param writeOnly the writeOnly flag
     */
    public void setWriteOnly(Boolean writeOnly) {
        this.writeOnly = writeOnly;
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
     * Returns the external documentation.
     *
     * @return the external documentation, or null when unset
     */
    public ExternalDocumentation getExternalDocs() {
        return externalDocs;
    }

    /**
     * Sets the external documentation.
     *
     * @param externalDocs the external documentation
     */
    public void setExternalDocs(ExternalDocumentation externalDocs) {
        this.externalDocs = externalDocs;
    }

    /**
     * Returns the example.
     *
     * @return the example, or null when unset
     */
    public T getExample() {
        return example;
    }

    /**
     * Sets the example.
     *
     * @param example the example
     */
    public void setExample(T example) {
        this.example = example;
    }

    /**
     * Sets the example and returns this instance.
     *
     * @param example the example
     * @return this instance
     */
    public Schema<T> example(T example) {
        this.example = example;
        return this;
    }

    /**
     * Returns the example map.
     *
     * @return the example map, or null when unset
     */
    public List<T> getExamples() {
        return examples;
    }

    /**
     * Sets the example map.
     *
     * @param examples the example map
     */
    public void setExamples(List<T> examples) {
        this.examples = examples;
    }

    /**
     * Returns the nullable flag.
     *
     * @return the nullable flag, or null when unset
     */
    public Boolean getNullable() {
        return nullable;
    }

    /**
     * Sets the nullable flag.
     *
     * @param nullable the nullable flag
     */
    public void setNullable(Boolean nullable) {
        this.nullable = nullable;
    }

    /**
     * Returns the discriminator.
     *
     * @return the discriminator, or null when unset
     */
    public Discriminator getDiscriminator() {
        return discriminator;
    }

    /**
     * Sets the discriminator.
     *
     * @param discriminator the discriminator
     */
    public void setDiscriminator(Discriminator discriminator) {
        this.discriminator = discriminator;
    }

    /**
     * Returns the XML metadata.
     *
     * @return the XML metadata, or null when unset
     */
    public XML getXml() {
        return xml;
    }

    /**
     * Sets the XML metadata.
     *
     * @param xml the XML metadata
     */
    public void setXml(XML xml) {
        this.xml = xml;
    }

    /**
     * Returns the reference value.
     *
     * @return the reference value, or null when unset
     */
    @com.fasterxml.jackson.annotation.JsonProperty("$ref")
    public String get$ref() {
        return $ref;
    }

    /**
     * Sets the reference value.
     *
     * @param $ref the reference value
     */
    @com.fasterxml.jackson.annotation.JsonProperty("$ref")
    public void set$ref(String $ref) {
        this.$ref = $ref;
    }

    /**
     * Processes this element.
    *
    * @param $ref the value
    * @return the result
     */
    public Schema<T> $ref(String $ref) {
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
        if (!(o instanceof Schema)) return false;
        Schema<?> schema = (Schema<?>) o;
        return Objects.equals(type, schema.type) &&
            Objects.equals(format, schema.format) &&
            Objects.equals(title, schema.title) &&
            Objects.equals(description, schema.description) &&
            Objects.equals(_default, schema._default) &&
            Objects.equals($ref, schema.$ref);
    }

    /**
     * Processes this element.
    *
    * @return the result
     */
    @Override
    public int hashCode() {
        return Objects.hash(type, format, title, description, _default, $ref);
    }

    /**
     * Processes this element.
    *
    * @return the result
     */
    @Override
    public String toString() {
        return "Schema{" +
            "type='" + type + '\'' +
            ", format='" + format + '\'' +
            ", $ref='" + $ref + '\'' +
            '}';
    }
}
