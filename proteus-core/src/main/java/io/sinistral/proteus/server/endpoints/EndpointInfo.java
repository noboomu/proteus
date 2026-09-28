/**
 *
 */
package io.sinistral.proteus.server.endpoints;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.undertow.util.HttpString;

/**
 * Endpoint metadata describing one generated route: method, path template, media types, and
 * originating controller element. Serialized with only non-null fields present.
 *
 * @author jbauer
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EndpointInfo implements Comparable<EndpointInfo> {

    /** the consumes. */
    private String consumes = "*/*";
    /** the produces. */
    private String produces = "*/*";
    /** the controller method. */
    private String controllerMethod = "*";
    /** the controller name. */
    private String controllerName = "_";
    /** the method. */
    private HttpString method;
    /** the path template. */
    private String pathTemplate;

    /** Default constructor for deserialization. */
    public EndpointInfo() {}

    private EndpointInfo(Builder builder) {
        this.method = builder.method;
        this.pathTemplate = builder.pathTemplate;
        this.consumes = builder.consumes;
        this.produces = builder.produces;
        this.controllerMethod = builder.controllerMethod;
        this.controllerName = builder.controllerName;
    }

    /**
     * Creates builder to build {@link EndpointInfo}.
     * @return created builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Orders by path template, then controller name, method name, and HTTP method, with null
     * components sorting first.
     *
     * @param other the endpoint to compare against
     * @return a negative integer, zero, or positive integer per the ordering
     */
    public int compareTo(EndpointInfo other) {
        int result = compareNullable(this.pathTemplate, other.pathTemplate);

        if (result != 0) {
            return result;
        }

        result = compareNullable(this.controllerName, other.controllerName);

        if (result != 0) {
            return result;
        }

        result = compareNullable(this.controllerMethod, other.controllerMethod);

        if (result != 0) {
            return result;
        }

        return compareNullable(
                this.method != null ? this.method.toString() : null,
                other.method != null ? other.method.toString() : null
        );
    }

    private static int compareNullable(String a, String b) {
        if (a == null && b == null) return 0;
        if (a == null) return -1;
        if (b == null) return 1;
        return a.compareTo(b);
    }

    /**
     * Returns the hash code.
     *
     * @return the hash code, or null when unset
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result =
            prime * result + ((consumes == null) ? 0 : consumes.hashCode());
        result =
            prime * result +
            ((controllerMethod == null) ? 0 : controllerMethod.hashCode());
        result =
            prime * result +
            ((controllerName == null) ? 0 : controllerName.hashCode());
        result = prime * result + ((method == null) ? 0 : method.hashCode());
        result =
            prime * result +
            ((pathTemplate == null) ? 0 : pathTemplate.hashCode());
        result =
            prime * result + ((produces == null) ? 0 : produces.hashCode());
        return result;
    }

    /**
     * Sets the equals, fluent style.
     *
     * @param obj the equals
     * @return this instance
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        EndpointInfo other = (EndpointInfo) obj;
        if (consumes == null) {
            if (other.consumes != null) return false;
        } else if (!consumes.equals(other.consumes)) return false;
        if (controllerMethod == null) {
            if (other.controllerMethod != null) return false;
        } else if (
            !controllerMethod.equals(other.controllerMethod)
        ) return false;
        if (controllerName == null) {
            if (other.controllerName != null) return false;
        } else if (!controllerName.equals(other.controllerName)) return false;
        if (method == null) {
            if (other.method != null) return false;
        } else if (!method.equals(other.method)) return false;
        if (pathTemplate == null) {
            if (other.pathTemplate != null) return false;
        } else if (!pathTemplate.equals(other.pathTemplate)) return false;
        if (produces == null) {
            if (other.produces != null) return false;
        } else if (!produces.equals(other.produces)) return false;
        return true;
    }

    /**
     * Returns the to string.
     *
     * @return the to string, or null when unset
     */
    @Override
    public String toString() {
        return String.format(
            "\t%-8s %-40s %-26s %-26s %s",
            this.method,
            this.pathTemplate,
            "[" + this.consumes + "]",
            "[" + this.produces + "]",
            "(" + this.controllerName + "." + this.controllerMethod + ")"
        );
    }

    /**
     * Returns the consumed media type, defaulting to {@code APPLICATION_WILDCARD}.
     *
     * @return the consumes value
     */
    public String getConsumes() {
        return consumes;
    }

    /**
     * Sets the consumed media type.
     *
     * @param consumes the consumes value to set
     */
    public void setConsumes(String consumes) {
        this.consumes = consumes;
    }

    /**
     * Returns the controller method name, defaulting to {@code *}.
     *
     * @return the controller method name
     */
    public String getControllerMethod() {
        return controllerMethod;
    }

    /**
     * Sets the controller method name.
     *
     * @param controllerMethod the controller method name to set
     */
    public void setControllerMethod(String controllerMethod) {
        this.controllerMethod = controllerMethod;
    }

    /**
     * Returns the controller class name, defaulting to {@code _}.
     *
     * @return the controller class name
     */
    public String getControllerName() {
        return controllerName;
    }

    /**
     * Sets the controller class name; a null value becomes the empty string.
     *
     * @param controllerName the controller class name to set
     */
    public void setControllerName(String controllerName) {
        this.controllerName = controllerName;

        if (this.controllerName == null) {
            this.controllerName = "";
        }
    }

    /**
     * Returns the HTTP method.
     *
     * @return the HTTP method
     */
    public HttpString getMethod() {
        return method;
    }

    /**
     * Sets the HTTP method.
     *
     * @param method the HTTP method to set
     */
    public void setMethod(HttpString method) {
        this.method = method;
    }

    /**
     * Returns the route path template.
     *
     * @return the path template
     */
    public String getPathTemplate() {
        return pathTemplate;
    }

    /**
     * Sets the route path template.
     *
     * @param pathTemplate the path template to set
     */
    public void setPathTemplate(String pathTemplate) {
        this.pathTemplate = pathTemplate;
    }

    /**
     * Returns the produced media type, defaulting to {@code APPLICATION_WILDCARD}.
     *
     * @return the produces value
     */
    public String getProduces() {
        return produces;
    }

    /**
     * Sets the produced media type.
     *
     * @param produces the produces value to set
     */
    public void setProduces(String produces) {
        this.produces = produces;
    }

    /**
     * Builder to build {@link EndpointInfo}.
     */
    public static final class Builder {

        /** the consumes. */
        private String consumes = "*/*";
        /** the produces. */
        private String produces = "*/*";
        /** the controller method. */
        private String controllerMethod = "_";
        /** the controller name. */
        private String controllerName = "_";
        /** the method. */
        private HttpString method;
        /** the path template. */
        private String pathTemplate;

        private Builder() {}

        /**
         * Builds the endpoint info from the configured values.
         *
         * @return a new endpoint info
         */
        public EndpointInfo build() {
            return new EndpointInfo(this);
        }

        /**
         * Sets the consumed media type.
         *
         * @param consumes the consumes value
         * @return this builder
         */
        public Builder withConsumes(String consumes) {
            this.consumes = consumes;

            return this;
        }

        /**
         * Sets the controller method name.
         *
         * @param controllerMethod the controller method name
         * @return this builder
         */
        public Builder withControllerMethod(String controllerMethod) {
            this.controllerMethod = controllerMethod;

            return this;
        }

        /**
         * Sets the controller class name.
         *
         * @param controllerName the controller class name
         * @return this builder
         */
        public Builder withControllerName(String controllerName) {
            this.controllerName = controllerName;

            return this;
        }

        /**
         * Sets the HTTP method.
         *
         * @param method the HTTP method
         * @return this builder
         */
        public Builder withMethod(HttpString method) {
            this.method = method;

            return this;
        }

        /**
         * Sets the route path template.
         *
         * @param pathTemplate the path template
         * @return this builder
         */
        public Builder withPathTemplate(String pathTemplate) {
            this.pathTemplate = pathTemplate;

            return this;
        }

        /**
         * Sets the produced media type.
         *
         * @param produces the produces value
         * @return this builder
         */
        public Builder withProduces(String produces) {
            this.produces = produces;

            return this;
        }
    }
}
