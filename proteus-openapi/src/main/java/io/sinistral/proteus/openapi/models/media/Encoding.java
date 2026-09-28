package io.sinistral.proteus.openapi.models.media;

import io.sinistral.proteus.openapi.models.headers.Header;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/** OpenAPI encoding object describing how a multipart body part is serialized. */
public class Encoding {

    /** Creates an empty encoding object. */
    public Encoding() {}
    /** the content type. */
    private String contentType;
    /** the headers. */
    private Map<String, Header> headers;
    /** the style. */
    private StyleEnum style;
    /** the explode. */
    private Boolean explode;
    /** the allow reserved. */
    private Boolean allowReserved;
    /** the extensions. */
    private Map<String, Object> extensions;

    /** Encoding serialization style selector. */
    public enum StyleEnum {
        /** The form value. */
        FORM("form"),
        /** The spaceDelimited value. */
        SPACEDELIMITED("spaceDelimited"),
        /** The pipeDelimited value. */
        PIPEDELIMITED("pipeDelimited"),
        /** The deepObject value. */
        DEEPOBJECT("deepObject");

        /** the value. */
        private final String value;

        StyleEnum(String value) {
            this.value = value;
        }

        /**
         * Returns the value.
         *
         * @return the value, or null when unset
         */
        public String getValue() {
            return value;
        }

        @Override
        /**
         * Processes this element.
        *
        * @return the result
         */
        public String toString() {
            return String.valueOf(value);
        }
    }

    /**
     * Returns the to string value.
     *
     * @return the to string value, or null when unset
     */
    public String getContentType() {
        return contentType;
    }

    /**
     * Sets the content type value.
     *
     * @param contentType the content type value
     */
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    /**
     * Returns the header map.
     *
     * @return the header map, or null when unset
     */
    public Map<String, Header> getHeaders() {
        return headers;
    }

    /**
     * Sets the header map.
     * @param headers the value to set
     *
     */
    public void setHeaders(Map<String, Header> headers) {
        this.headers = headers;
    }

    /**
     * Returns the style.
     *
     * @return the style, or null when unset
     */
    public StyleEnum getStyle() {
        return style;
    }

    /**
     * Sets the style.
     * @param style the value to set
     *
     */
    public void setStyle(StyleEnum style) {
        this.style = style;
    }

    /**
     * Returns the explode flag.
     *
     * @return the explode flag, or null when unset
     */
    public Boolean getExplode() {
        return explode;
    }

    /**
     * Sets the explode flag.
     * @param explode the value to set
     *
     */
    public void setExplode(Boolean explode) {
        this.explode = explode;
    }

    /**
     * Returns the allowReserved flag.
     *
     * @return the allowReserved flag, or null when unset
     */
    public Boolean getAllowReserved() {
        return allowReserved;
    }

    /**
     * Sets the allowReserved flag.
     * @param allowReserved the value to set
     *
     */
    public void setAllowReserved(Boolean allowReserved) {
        this.allowReserved = allowReserved;
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
     * @param extensions the value to set
     *
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
        if (!(o instanceof Encoding)) return false;
        Encoding encoding = (Encoding) o;
        return Objects.equals(contentType, encoding.contentType) &&
            Objects.equals(style, encoding.style);
    }

    /**
     * Processes this element.
    *
    * @return the result
     */
    @Override
    public int hashCode() {
        return Objects.hash(contentType, style);
    }
}
