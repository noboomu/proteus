package io.sinistral.proteus.openapi.models.servers;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/** OpenAPI server variable object with default and enum values. */
public class ServerVariable {

    /** Creates the object. */
    public ServerVariable() {}
    /** The enum. */
    private List<String> _enum;
    /** The default. */
    private String _default;
    /** The description. */
    private String description;
    /** The extensions. */
    private Map<String, Object> extensions;

    /**
     * Returns the enum values.
     *
     * @return the enum values, or null when unset
     */
    public List<String> getEnum() {
        return _enum;
    }

    /**
     * Sets the enum values.
     *
     * @param _enum the enum values
     */
    public void setEnum(List<String> _enum) {
        this._enum = _enum;
    }

    /**
     * Adds an entry to the enum value.
     *
     * @param _enumItem the entry
     */
    public void addEnumItem(String _enumItem) {
        if (this._enum == null) {
            this._enum = new ArrayList<>();
        }
        this._enum.add(_enumItem);
    }

    /**
     * Returns the default value.
     *
     * @return the default value, or null when unset
     */
    public String getDefault() {
        return _default;
    }

    /**
     * Sets the default value.
     *
     * @param _default the default value
     */
    public void setDefault(String _default) {
        this._default = _default;
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
        if (!(o instanceof ServerVariable)) return false;
        ServerVariable that = (ServerVariable) o;
        return Objects.equals(_default, that._default) &&
            Objects.equals(description, that.description);
    }

    /**
     * Processes this element.
    *
    * @return the result
     */
    @Override
    public int hashCode() {
        return Objects.hash(_default, description);
    }
}
