package io.sinistral.proteus.openapi.models.security;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/** OpenAPI OAuth flow object with authorization URLs and scopes. */
public class OAuthFlow {

    /** Creates the object. */
    public OAuthFlow() {}
    /** The authorization url. */
    private String authorizationUrl;
    /** The token url. */
    private String tokenUrl;
    /** The refresh url. */
    private String refreshUrl;
    /** The scopes. */
    private Map<String, String> scopes;
    /** The extensions. */
    private Map<String, Object> extensions;

    /**
     * Returns the authorization URL.
     *
     * @return the authorization URL, or null when unset
     */
    public String getAuthorizationUrl() {
        return authorizationUrl;
    }

    /**
     * Sets the authorization URL.
     *
     * @param authorizationUrl the authorization URL
     */
    public void setAuthorizationUrl(String authorizationUrl) {
        this.authorizationUrl = authorizationUrl;
    }

    /**
     * Returns the token URL.
     *
     * @return the token URL, or null when unset
     */
    public String getTokenUrl() {
        return tokenUrl;
    }

    /**
     * Sets the token URL.
     *
     * @param tokenUrl the token URL
     */
    public void setTokenUrl(String tokenUrl) {
        this.tokenUrl = tokenUrl;
    }

    /**
     * Returns the refresh URL.
     *
     * @return the refresh URL, or null when unset
     */
    public String getRefreshUrl() {
        return refreshUrl;
    }

    /**
     * Sets the refresh URL.
     *
     * @param refreshUrl the refresh URL
     */
    public void setRefreshUrl(String refreshUrl) {
        this.refreshUrl = refreshUrl;
    }

    /**
     * Returns the scopes map.
     *
     * @return the scopes map, or null when unset
     */
    public Map<String, String> getScopes() {
        return scopes;
    }

    /**
     * Sets the scopes map.
     *
     * @param scopes the scopes map
     */
    public void setScopes(Map<String, String> scopes) {
        this.scopes = scopes;
    }

    /**
     * Adds an entry to the scope value.
     *
     * @param name the entry
     * @param description the entry
     */
    public void addScope(String name, String description) {
        if (this.scopes == null) {
            this.scopes = new LinkedHashMap<>();
        }
        this.scopes.put(name, description);
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
        if (!(o instanceof OAuthFlow)) return false;
        OAuthFlow oAuthFlow = (OAuthFlow) o;
        return Objects.equals(authorizationUrl, oAuthFlow.authorizationUrl) &&
            Objects.equals(tokenUrl, oAuthFlow.tokenUrl);
    }

    /**
     * Processes this element.
    *
    * @return the result
     */
    @Override
    public int hashCode() {
        return Objects.hash(authorizationUrl, tokenUrl);
    }
}
