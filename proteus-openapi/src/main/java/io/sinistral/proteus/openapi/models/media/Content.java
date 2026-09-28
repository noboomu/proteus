package io.sinistral.proteus.openapi.models.media;

import java.util.LinkedHashMap;
import java.util.Objects;

/** OpenAPI content map from media type ranges to media type objects. */
public class Content extends LinkedHashMap<String, MediaType> {

    /** Creates an empty content map. */
    public Content() {}
    /**
     * Adds an entry to the media type.
     * @param key the value to set
     * @param mediaType the value to set
     *
    * @return the result
     */
    public Content addMediaType(String key, MediaType mediaType) {
        this.put(key, mediaType);
        return this;
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
        if (!(o instanceof Content)) return false;
        return super.equals(o);
    }

    /**
     * Processes this element.
    *
    * @return the result
     */
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode());
    }
}
