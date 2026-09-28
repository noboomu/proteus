package io.sinistral.proteus.openapi.models.callbacks;

import io.sinistral.proteus.openapi.models.PathItem;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * OpenAPI callback object mapping callback expressions to path items.
 */
@tools.jackson.databind.annotation.JsonSerialize(using = CallbackSerializer.class)
@tools.jackson.databind.annotation.JsonDeserialize(using = CallbackDeserializer.class)
/** OpenAPI callback object mapping expressions to path items. */
public class Callback extends LinkedHashMap<String, PathItem> {

    /** Creates the object. */
    public Callback() {}
    /** the reference. */
    private String $ref;
    /** the extensions. */
    private Map<String, Object> extensions;

    /**
     * Adds one path item under the given callback expression.
     *
     * @param name the callback expression
     * @param item the path item
     * @return this instance
     */
    public Callback addPathItem(String name, PathItem item) {
        this.put(name, item);
        return this;
    }

    /**
     * Returns the reference target.
     *
     * @return the reference, or null when unset
     */
    public String get$ref() {
        return $ref;
    }

    /**
     * Sets the reference target.
     *
     * @param $ref the reference
     */
    public void set$ref(String $ref) {
        this.$ref = $ref;
    }

    /**
     * Sets the reference target, fluent style.
     *
     * @param $ref the reference
     * @return this instance
     */
    public Callback $ref(String $ref) {
        this.$ref = $ref;
        return this;
    }

    /**
     * Returns the extension properties.
     *
     * @return the extension map, or null when unset
     */
    public Map<String, Object> getExtensions() {
        return extensions;
    }

    /**
     * Sets the extension properties.
     *
     * @param extensions the extension map
     */
    public void setExtensions(Map<String, Object> extensions) {
        this.extensions = extensions;
    }

    /**
     * Adds one extension property; names not starting with {@code x-} are ignored.
     *
     * @param name the extension name
     * @param value the extension value
     */
    public void addExtension(String name, Object value) {
        if (name == null || name.isEmpty() || !name.startsWith("x-")) return;
        if (extensions == null) extensions = new LinkedHashMap<>();
        extensions.put(name, value);
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
        if (!(o instanceof Callback)) return false;
        if (!super.equals(o)) return false;
        Callback callback = (Callback) o;
        return Objects.equals($ref, callback.$ref);
    }

    /**
     * Processes this element.
    *
    * @return the result
     */
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), $ref);
    }
}
