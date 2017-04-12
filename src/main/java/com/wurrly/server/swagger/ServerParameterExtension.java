/**
 * 
 */
package com.wurrly.server.swagger;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JavaType;

import io.swagger.jaxrs.DefaultParameterExtension;
import io.swagger.jaxrs.ext.SwaggerExtension;
import io.swagger.models.parameters.Parameter;

/**
 * @author jbauer
 *
 */
public class ServerParameterExtension extends DefaultParameterExtension
{
	private static Logger log = LoggerFactory.getLogger(ServerParameterExtension.class.getCanonicalName());

	public ServerParameterExtension()
	{
		super();
		
		
	}
 
	@Override
	public List<Parameter> extractParameters(List<Annotation> annotations, Type type, Set<Type> typesToSkip, Iterator<SwaggerExtension> chain)
	{
 
		if(type.getTypeName().contains("java.nio.ByteBuffer") || type.getTypeName().contains("java.nio.file.Path"))
	      {
	      	type = java.io.File.class;
 
	      }
		 
		return  super.extractParameters(annotations, type, typesToSkip, chain);
	  
	}

 
	@Override
	protected boolean shouldIgnoreType(Type type, Set<Type> typesToSkip)
	{ 
 
		if( type.getTypeName().contains("ServerRequest"))
		{
			return true;
		}
		
		return super.shouldIgnoreType(type, typesToSkip);
	}

 
	@Override
	protected JavaType constructType(Type type)
	{ 
		
		if(type.getTypeName().contains("java.nio.ByteBuffer") || type.getTypeName().contains("java.nio.file.Path"))
	      {
	      	type = java.io.File.class; 

	      }

		return super.constructType(type);

	}

}
