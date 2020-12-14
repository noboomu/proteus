/**
 *
 */
package io.sinistral.proteus.server.endpoints;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.undertow.util.HttpString;

/**
 * @author jbauer
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EndpointInfo implements Comparable<EndpointInfo>
{
    private String consumes = "*/*";
    private String produces = "*/*";
    private String controllerMethod = "*";
    private String controllerName = "_";
    private HttpString method;
    private String pathTemplate;

    public EndpointInfo()
    {
    }

    private EndpointInfo(Builder builder)
    {
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
    public static Builder builder()
    {
        return new Builder();
    }

    public int compareTo(EndpointInfo other)
    {
        int result = this.pathTemplate.compareTo(other.pathTemplate);

        if (result != 0) {
            return result;
        }

        result = this.controllerName.compareTo(other.controllerName);

        if (result != 0) {
            return result;
        }

        result = this.controllerMethod.compareTo(other.controllerMethod);

        if (result != 0) {
            return result;
        }

        return this.method.compareTo(other.method);

    }


    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((consumes == null) ? 0 : consumes.hashCode());
        result = prime * result + ((controllerMethod == null) ? 0 : controllerMethod.hashCode());
        result = prime * result + ((controllerName == null) ? 0 : controllerName.hashCode());
        result = prime * result + ((method == null) ? 0 : method.hashCode());
        result = prime * result + ((pathTemplate == null) ? 0 : pathTemplate.hashCode());
        result = prime * result + ((produces == null) ? 0 : produces.hashCode());
        return result;
    }


    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        EndpointInfo other = (EndpointInfo) obj;
        if (consumes == null) {
            if (other.consumes != null)
                return false;
        } else if (!consumes.equals(other.consumes))
            return false;
        if (controllerMethod == null) {
            if (other.controllerMethod != null)
                return false;
        } else if (!controllerMethod.equals(other.controllerMethod))
            return false;
        if (controllerName == null) {
            if (other.controllerName != null)
                return false;
        } else if (!controllerName.equals(other.controllerName))
            return false;
        if (method == null) {
            if (other.method != null)
                return false;
        } else if (!method.equals(other.method))
            return false;
        if (pathTemplate == null) {
            if (other.pathTemplate != null)
                return false;
        } else if (!pathTemplate.equals(other.pathTemplate))
            return false;
        if (produces == null) {
            if (other.produces != null)
                return false;
        } else if (!produces.equals(other.produces))
            return false;
        return true;
    }

    @Override
    public String toString()
    {
        return String.format("\t%-8s %-40s %-26s %-26s %s",
                this.method,
                this.pathTemplate,
                "[" + this.consumes + "]",
                "[" + this.produces + "]",
                "(" + this.controllerName + "." + this.controllerMethod + ")");
    }

    /**
     * @return the consumes
     */
    public String getConsumes()
    {
        return consumes;
    }

    /**
     * @param consumes the consumes to set
     */
    public void setConsumes(String consumes)
    {
        this.consumes = consumes;
    }

    /**
     * @return the controllerMethod
     */
    public String getControllerMethod()
    {
        return controllerMethod;
    }

    /**
     * @param controllerMethod the controllerMethod to set
     */
    public void setControllerMethod(String controllerMethod)
    {
        this.controllerMethod = controllerMethod;
    }

    /**
     * @return the controllerName
     */
    public String getControllerName()
    {
        return controllerName;
    }

    /**
     * @param controllerName the controllerName to set
     */
    public void setControllerName(String controllerName)
    {
        this.controllerName = controllerName;

        if (this.controllerName == null) {
            this.controllerName = "";
        }
    }

    /**
     * @return the method
     */
    public HttpString getMethod()
    {
        return method;
    }

    /**
     * @param method the method to set
     */
    public void setMethod(HttpString method)
    {
        this.method = method;
    }

    /**
     * @return the pathTemplate
     */
    public String getPathTemplate()
    {
        return pathTemplate;
    }

    /**
     * @param pathTemplate the pathTemplate to set
     */
    public void setPathTemplate(String pathTemplate)
    {
        this.pathTemplate = pathTemplate;
    }

    /**
     * @return the produces
     */
    public String getProduces()
    {
        return produces;
    }

    /**
     * @param produces the produces to set
     */
    public void setProduces(String produces)
    {
        this.produces = produces;
    }

    /**
     * Builder to build {@link EndpointInfo}.
     */
    public static final class Builder
    {
        private String consumes = "*/*";
        private String produces = "*/*";
        private String controllerMethod = "_";
        private String controllerName = "_";
        private HttpString method;
        private String pathTemplate;

        private Builder()
        {
        }

        public EndpointInfo build()
        {
            return new EndpointInfo(this);
        }

        public Builder withConsumes(String consumes)
        {
            this.consumes = consumes;

            return this;
        }

        public Builder withControllerMethod(String controllerMethod)
        {
            this.controllerMethod = controllerMethod;

            return this;
        }

        public Builder withControllerName(String controllerName)
        {
            this.controllerName = controllerName;

            return this;
        }

        public Builder withMethod(HttpString method)
        {
            this.method = method;

            return this;
        }

        public Builder withPathTemplate(String pathTemplate)
        {
            this.pathTemplate = pathTemplate;

            return this;
        }

        public Builder withProduces(String produces)
        {
            this.produces = produces;

            return this;
        }
    }
}



