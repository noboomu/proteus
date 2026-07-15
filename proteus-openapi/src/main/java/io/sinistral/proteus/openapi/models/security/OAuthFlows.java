package io.sinistral.proteus.openapi.models.security;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class OAuthFlows {
    private OAuthFlow implicit;
    private OAuthFlow password;
    private OAuthFlow clientCredentials;
    private OAuthFlow authorizationCode;
    private Map<String, Object> extensions;

    public OAuthFlow getImplicit() {
        return implicit;
    }

    public void setImplicit(OAuthFlow implicit) {
        this.implicit = implicit;
    }

    public OAuthFlow getPassword() {
        return password;
    }

    public void setPassword(OAuthFlow password) {
        this.password = password;
    }

    public OAuthFlow getClientCredentials() {
        return clientCredentials;
    }

    public void setClientCredentials(OAuthFlow clientCredentials) {
        this.clientCredentials = clientCredentials;
    }

    public OAuthFlow getAuthorizationCode() {
        return authorizationCode;
    }

    public void setAuthorizationCode(OAuthFlow authorizationCode) {
        this.authorizationCode = authorizationCode;
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
        if (!(o instanceof OAuthFlows)) return false;
        OAuthFlows that = (OAuthFlows) o;
        return Objects.equals(implicit, that.implicit) &&
            Objects.equals(password, that.password) &&
            Objects.equals(clientCredentials, that.clientCredentials) &&
            Objects.equals(authorizationCode, that.authorizationCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(implicit, password, clientCredentials, authorizationCode);
    }
}
