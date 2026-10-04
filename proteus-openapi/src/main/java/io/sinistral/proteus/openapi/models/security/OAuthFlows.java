package io.sinistral.proteus.openapi.models.security;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/** OpenAPI OAuth flows object holding all flow configurations. */
public class OAuthFlows {

    /** Creates the object. */
    public OAuthFlows() {}
    /** The implicit. */
    private OAuthFlow implicit;
    /** The password. */
    private OAuthFlow password;
    /** The client credentials. */
    private OAuthFlow clientCredentials;
    /** The authorization code. */
    private OAuthFlow authorizationCode;
    /** The extensions. */
    private Map<String, Object> extensions;

    /**
     * Returns the implicit flow.
     *
     * @return the implicit flow, or null when unset
     */
    public OAuthFlow getImplicit() {
        return implicit;
    }

    /**
     * Sets the implicit flow.
     *
     * @param implicit the implicit flow
     */
    public void setImplicit(OAuthFlow implicit) {
        this.implicit = implicit;
    }

    /**
     * Returns the password flow.
     *
     * @return the password flow, or null when unset
     */
    public OAuthFlow getPassword() {
        return password;
    }

    /**
     * Sets the password flow.
     *
     * @param password the password flow
     */
    public void setPassword(OAuthFlow password) {
        this.password = password;
    }

    /**
     * Returns the client credentials flow.
     *
     * @return the client credentials flow, or null when unset
     */
    public OAuthFlow getClientCredentials() {
        return clientCredentials;
    }

    /**
     * Sets the client credentials flow.
     *
     * @param clientCredentials the client credentials flow
     */
    public void setClientCredentials(OAuthFlow clientCredentials) {
        this.clientCredentials = clientCredentials;
    }

    /**
     * Returns the authorization code flow.
     *
     * @return the authorization code flow, or null when unset
     */
    public OAuthFlow getAuthorizationCode() {
        return authorizationCode;
    }

    /**
     * Sets the authorization code flow.
     *
     * @param authorizationCode the authorization code flow
     */
    public void setAuthorizationCode(OAuthFlow authorizationCode) {
        this.authorizationCode = authorizationCode;
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
        if (!(o instanceof OAuthFlows)) return false;
        OAuthFlows that = (OAuthFlows) o;
        return Objects.equals(implicit, that.implicit) &&
            Objects.equals(password, that.password) &&
            Objects.equals(clientCredentials, that.clientCredentials) &&
            Objects.equals(authorizationCode, that.authorizationCode);
    }

    /**
     * Processes this element.
    *
    * @return the result
     */
    @Override
    public int hashCode() {
        return Objects.hash(implicit, password, clientCredentials, authorizationCode);
    }
}
