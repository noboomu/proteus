package io.sinistral.proteus.openapi.models.security;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/** OpenAPI security scheme object describing an auth mechanism. */
public class SecurityScheme {

    /** Creates the object. */
    public SecurityScheme() {}
    /** the type. */
    private Type type;
    /** the description. */
    private String description;
    /** the name. */
    private String name;
    /** the in. */
    private In in;
    /** the scheme. */
    private String scheme;
    /** the bearer format. */
    private String bearerFormat;
    /** the flows. */
    private OAuthFlows flows;
    /** the open id connect url. */
    private String openIdConnectUrl;
    /** the reference. */
    private String $ref;
    /** the extensions. */
    private Map<String, Object> extensions;

    /** Security scheme type selector. */
    public enum Type {
        /** The apiKey value. */
        APIKEY("apiKey"),
        /** The http value. */
        HTTP("http"),
        /** The oauth2 value. */
        OAUTH2("oauth2"),
        /** The openIdConnect value. */
        OPENIDCONNECT("openIdConnect"),
        /** The mutualTLS value. */
        MUTUALTLS("mutualTLS");

        /** the value. */
        private final String value;

        Type(String value) {
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

    /** Security scheme location selector. */
    public enum In {
        /** The query value. */
        QUERY("query"),
        /** The header value. */
        HEADER("header"),
        /** The cookie value. */
        COOKIE("cookie");

        /** the value. */
        private final String value;

        In(String value) {
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
     * Returns the type.
     *
     * @return the type, or null when unset
     */
    public Type getType() {
        return type;
    }

    /**
     * Sets the type.
     *
     * @param type the type
     */
    public void setType(Type type) {
        this.type = type;
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
     * Returns the name.
     *
     * @return the name, or null when unset
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name.
     *
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the location.
     *
     * @return the location, or null when unset
     */
    public In getIn() {
        return in;
    }

    /**
     * Sets the location.
     *
     * @param in the location
     */
    public void setIn(In in) {
        this.in = in;
    }

    /**
     * Returns the scheme.
     *
     * @return the scheme, or null when unset
     */
    public String getScheme() {
        return scheme;
    }

    /**
     * Sets the scheme.
     *
     * @param scheme the scheme
     */
    public void setScheme(String scheme) {
        this.scheme = scheme;
    }

    /**
     * Returns the bearer format.
     *
     * @return the bearer format, or null when unset
     */
    public String getBearerFormat() {
        return bearerFormat;
    }

    /**
     * Sets the bearer format.
     *
     * @param bearerFormat the bearer format
     */
    public void setBearerFormat(String bearerFormat) {
        this.bearerFormat = bearerFormat;
    }

    /**
     * Returns the OAuth flows.
     *
     * @return the OAuth flows, or null when unset
     */
    public OAuthFlows getFlows() {
        return flows;
    }

    /**
     * Sets the OAuth flows.
     *
     * @param flows the OAuth flows
     */
    public void setFlows(OAuthFlows flows) {
        this.flows = flows;
    }

    /**
     * Returns the OpenID Connect URL.
     *
     * @return the OpenID Connect URL, or null when unset
     */
    public String getOpenIdConnectUrl() {
        return openIdConnectUrl;
    }

    /**
     * Sets the OpenID Connect URL.
     *
     * @param openIdConnectUrl the OpenID Connect URL
     */
    public void setOpenIdConnectUrl(String openIdConnectUrl) {
        this.openIdConnectUrl = openIdConnectUrl;
    }

    /**
     * Returns the reference value.
     *
     * @return the reference value, or null when unset
     */
    public String get$ref() {
        return $ref;
    }

    /**
     * Sets the reference value.
     *
     * @param $ref the reference value
     */
    public void set$ref(String $ref) {
        this.$ref = $ref;
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
        if (!(o instanceof SecurityScheme)) return false;
        SecurityScheme that = (SecurityScheme) o;
        return type == that.type &&
            Objects.equals(name, that.name) &&
            Objects.equals($ref, that.$ref);
    }

    /**
     * Processes this element.
    *
    * @return the result
     */
    @Override
    public int hashCode() {
        return Objects.hash(type, name, $ref);
    }
}
