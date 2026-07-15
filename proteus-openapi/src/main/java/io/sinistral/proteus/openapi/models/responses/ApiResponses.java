package io.sinistral.proteus.openapi.models.responses;

import java.util.LinkedHashMap;
import java.util.Objects;

public class ApiResponses extends LinkedHashMap<String, ApiResponse> {
    private ApiResponse _default;

    public ApiResponse getDefault() {
        return _default;
    }

    public void setDefault(ApiResponse _default) {
        this._default = _default;
    }

    public ApiResponses _default(ApiResponse _default) {
        this._default = _default;
        return this;
    }

    public ApiResponses addApiResponse(String name, ApiResponse item) {
        this.put(name, item);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ApiResponses)) return false;
        if (!super.equals(o)) return false;
        ApiResponses that = (ApiResponses) o;
        return Objects.equals(_default, that._default);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), _default);
    }
}
