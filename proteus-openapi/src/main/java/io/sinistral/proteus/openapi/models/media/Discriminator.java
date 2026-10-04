package io.sinistral.proteus.openapi.models.media;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/** OpenAPI discriminator object selecting a schema by property value. */
public class Discriminator {

    /** Creates an empty discriminator object. */
    public Discriminator() {}
    /** The property name. */
    private String propertyName;
    /** The mapping. */
    private Map<String, String> mapping;

    /**
     * Returns the discriminator property name.
     *
     * @return the discriminator property name, or null when unset
     */
    public String getPropertyName() {
        return propertyName;
    }

    /**
     * Sets the discriminator property name.
     * @param propertyName the value to set
     *
     */
    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    /**
     * Sets the property name and returns this instance.
     *
     * @param propertyName the discriminator property name
     * @return this instance
     */
    public Discriminator propertyName(String propertyName) {
        this.propertyName = propertyName;
        return this;
    }

    /**
     * Returns the mapping.
     *
     * @return the mapping, or null when unset
     */
    public Map<String, String> getMapping() {
        return mapping;
    }

    /**
     * Sets the mapping.
     * @param mapping the value to set
     *
     */
    public void setMapping(Map<String, String> mapping) {
        this.mapping = mapping;
    }

    /**
     * Adds an entry to the mapping.
     * @param key the value to set
     * @param value the value to set
     *
     */
    public void addMapping(String key, String value) {
        if (this.mapping == null) {
            this.mapping = new LinkedHashMap<>();
        }
        this.mapping.put(key, value);
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
        if (!(o instanceof Discriminator)) return false;
        Discriminator that = (Discriminator) o;
        return Objects.equals(propertyName, that.propertyName) &&
            Objects.equals(mapping, that.mapping);
    }

    /**
     * Processes this element.
    *
    * @return the result
     */
    @Override
    public int hashCode() {
        return Objects.hash(propertyName, mapping);
    }
}
