package io.sinistral.proteus.openapi.models.info;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/** OpenAPI contact object with name, URL, and email details. */
public class Contact {

    /** Creates an empty contact object. */
    public Contact() {}
    /** the name. */
    private String name;
    /** the url. */
    private String url;
    /** the email. */
    private String email;
    /** the extensions. */
    private Map<String, Object> extensions;

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
     * @param name the value to set
     *
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the URL.
     *
     * @return the URL, or null when unset
     */
    public String getUrl() {
        return url;
    }

    /**
     * Sets the URL.
     * @param url the value to set
     *
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * Returns the email address.
     *
     * @return the email address, or null when unset
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email address.
     * @param email the value to set
     *
     */
    public void setEmail(String email) {
        this.email = email;
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
        if (!(o instanceof Contact)) return false;
        Contact contact = (Contact) o;
        return Objects.equals(name, contact.name) &&
            Objects.equals(email, contact.email);
    }

    /**
     * Processes this element.
    *
    * @return the result
     */
    @Override
    public int hashCode() {
        return Objects.hash(name, email);
    }
}
