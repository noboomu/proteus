package io.sinistral.proteus.openapi.models.security;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class OAuthFlow {
    private String authorizationUrl;
    private String tokenUrl;
    private String refreshUrl;
    private Map<String, String> scopes;
    private Map<String, Object> extensions;

    public String getAuthorizationUrl() {
        return authorizationUrl;
    }

    public void setAuthorizationUrl(String authorizationUrl) {
        this.authorizationUrl = authorizationUrl;
    }

    public String getTokenUrl() {
        return tokenUrl;
    }

    public void setTokenUrl(String tokenUrl) {
        this.tokenUrl = tokenUrl;
    }

    public String getRefreshUrl() {
        return refreshUrl;
    }

    public void setRefreshUrl(String refreshUrl) {
        this.refreshUrl = refreshUrl;
    }

    public Map<String, String> getScopes() {
        return scopes;
    }

    public void setScopes(Map<String, String> scopes) {
        this.scopes = scopes;
    }

    public void addScope(String name, String description) {
        if (this.scopes == null) {
            this.scopes = new LinkedHashMap<>();
        }
        this.scopes.put(name, description);
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
        if (!(o instanceof OAuthFlow)) return false;
        OAuthFlow oAuthFlow = (OAuthFlow) o;
        return Objects.equals(authorizationUrl, oAuthFlow.authorizationUrl) &&
            Objects.equals(tokenUrl, oAuthFlow.tokenUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(authorizationUrl, tokenUrl);
    }
}
