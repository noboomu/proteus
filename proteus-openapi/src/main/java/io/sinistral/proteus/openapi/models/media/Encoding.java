package io.sinistral.proteus.openapi.models.media;

import io.sinistral.proteus.openapi.models.headers.Header;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class Encoding {
    private String contentType;
    private Map<String, Header> headers;
    private StyleEnum style;
    private Boolean explode;
    private Boolean allowReserved;
    private Map<String, Object> extensions;

    public enum StyleEnum {
        FORM("form"),
        SPACEDELIMITED("spaceDelimited"),
        PIPEDELIMITED("pipeDelimited"),
        DEEPOBJECT("deepObject");

        private final String value;

        StyleEnum(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Map<String, Header> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, Header> headers) {
        this.headers = headers;
    }

    public StyleEnum getStyle() {
        return style;
    }

    public void setStyle(StyleEnum style) {
        this.style = style;
    }

    public Boolean getExplode() {
        return explode;
    }

    public void setExplode(Boolean explode) {
        this.explode = explode;
    }

    public Boolean getAllowReserved() {
        return allowReserved;
    }

    public void setAllowReserved(Boolean allowReserved) {
        this.allowReserved = allowReserved;
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
        if (!(o instanceof Encoding)) return false;
        Encoding encoding = (Encoding) o;
        return Objects.equals(contentType, encoding.contentType) &&
            Objects.equals(style, encoding.style);
    }

    @Override
    public int hashCode() {
        return Objects.hash(contentType, style);
    }
}
