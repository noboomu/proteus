package io.sinistral.proteus.openapi.models.servers;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ServerVariable {
    private List<String> _enum;
    private String _default;
    private String description;
    private Map<String, Object> extensions;

    public List<String> getEnum() {
        return _enum;
    }

    public void setEnum(List<String> _enum) {
        this._enum = _enum;
    }

    public void addEnumItem(String _enumItem) {
        if (this._enum == null) {
            this._enum = new ArrayList<>();
        }
        this._enum.add(_enumItem);
    }

    public String getDefault() {
        return _default;
    }

    public void setDefault(String _default) {
        this._default = _default;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
        if (!(o instanceof ServerVariable)) return false;
        ServerVariable that = (ServerVariable) o;
        return Objects.equals(_default, that._default) &&
            Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_default, description);
    }
}
