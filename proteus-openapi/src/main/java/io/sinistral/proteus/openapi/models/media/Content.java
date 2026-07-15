package io.sinistral.proteus.openapi.models.media;

import java.util.LinkedHashMap;
import java.util.Objects;

public class Content extends LinkedHashMap<String, MediaType> {
    public Content addMediaType(String key, MediaType mediaType) {
        this.put(key, mediaType);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Content)) return false;
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode());
    }
}
