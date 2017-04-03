/**
 * 
 */
package com.wurrly.server;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import io.undertow.util.HttpString;

/**
 * @author jbauer
 *
 */
public class RestRoute implements Comparable<RestRoute>
{
	private HttpString method;
	private String pathTemplate;
	private String consumes;
	private String produces;
	private String controllerMethod;
	private String controllerName;
	
	
	public RestRoute()
	{
		
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
	}
	
	
	public int hashCode()
	{
	     return new HashCodeBuilder(17, 37).
	    	       append(controllerName).
	    	       append(controllerMethod).
	    	       append(consumes).
	    	       append(produces).
	    	       append(method).
	    	       append(pathTemplate).
	    	       toHashCode();
	}
	
  public int compareTo(RestRoute o) {
	     RestRoute myClass = (RestRoute) o;
	     return new CompareToBuilder()
	       .append(this.controllerName, myClass.controllerName)
	       .append(this.method, myClass.method)
	       .append(this.controllerMethod, myClass.controllerMethod)
	       .toComparison();
	   }

	@Override
	public String toString()
	{
		return String.format("%-8s %-60s %-18s %-18s %s", this.method, this.pathTemplate, "[" + this.consumes + "]", "[" + this.produces+ "]", "("+this.controllerMethod+ ")");
	}

	 
	
}
