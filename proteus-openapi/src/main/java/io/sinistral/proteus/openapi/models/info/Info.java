package io.sinistral.proteus.openapi.models.info;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/** OpenAPI info object with document metadata. */
public class Info {

    /** Creates the object. */
    public Info() {}
    /** The title. */
    private String title;
    /** The summary. */
    private String summary;
    /** The description. */
    private String description;
    /** The terms of service. */
    private String termsOfService;
    /** The contact. */
    private Contact contact;
    /** The license. */
    private License license;
    /** The version. */
    private String version;
    /** The extensions. */
    private Map<String, Object> extensions;

    /**
     * Returns the title.
     *
     * @return the title, or null when unset
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title.
     *
     * @param title the title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Operates on the title.
    *
    * @param title the value
    * @return the result
     */
    public Info title(String title) {
        this.title = title;
        return this;
    }

    /**
     * Returns the summary.
     *
     * @return the summary, or null when unset
     */
    public String getSummary() {
        return summary;
    }

    /**
     * Sets the summary.
     *
     * @param summary the summary
     */
    public void setSummary(String summary) {
        this.summary = summary;
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
     * Operates on the description.
    *
    * @param description the value
    * @return the result
     */
    public Info description(String description) {
        this.description = description;
        return this;
    }

    /**
     * Returns the terms of service.
     *
     * @return the terms of service URL, or null when unset
     */
    public String getTermsOfService() {
        return termsOfService;
    }

    /**
     * Sets the terms of service URL.
     *
     * @param termsOfService the terms of service URL
     */
    public void setTermsOfService(String termsOfService) {
        this.termsOfService = termsOfService;
    }

    /**
     * Returns the contact details.
     *
     * @return the contact details, or null when unset
     */
    public Contact getContact() {
        return contact;
    }

    /**
     * Sets the contact details.
     *
     * @param contact the contact details
     */
    public void setContact(Contact contact) {
        this.contact = contact;
    }

    /**
     * Operates on the contact details.
    *
    * @param contact the value
    * @return the result
     */
    public Info contact(Contact contact) {
        this.contact = contact;
        return this;
    }

    /**
     * Returns the license details.
     *
     * @return the license details, or null when unset
     */
    public License getLicense() {
        return license;
    }

    /**
     * Sets the license details.
     *
     * @param license the license details
     */
    public void setLicense(License license) {
        this.license = license;
    }

    /**
     * Operates on the license details.
    *
    * @param license the value
    * @return the result
     */
    public Info license(License license) {
        this.license = license;
        return this;
    }

    /**
     * Returns the version.
     *
     * @return the version, or null when unset
     */
    public String getVersion() {
        return version;
    }

    /**
     * Sets the version.
     *
     * @param version the version
     */
    public void setVersion(String version) {
        this.version = version;
    }

    /**
     * Operates on the version.
    *
    * @param version the value
    * @return the result
     */
    public Info version(String version) {
        this.version = version;
        return this;
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
     * Adds a vendor extension entry, ignoring names that are not {@code x-} prefixed.
     *
     * @param name the extension name
     * @param value the extension value
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
     * Operates on the value.
    *
    * @param o the value
    * @return the result
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Info)) return false;
        Info info = (Info) o;
        return Objects.equals(title, info.title) &&
            Objects.equals(version, info.version);
    }

    /**
     * Operates on the value.
    *
    * @return the result
     */
    @Override
    public int hashCode() {
        return Objects.hash(title, version);
    }

    /**
     * Operates on the value.
    *
    * @return the result
     */
    @Override
    public String toString() {
        return "Info{" +
            "title='" + title + '\'' +
            ", version='" + version + '\'' +
            '}';
    }
}
