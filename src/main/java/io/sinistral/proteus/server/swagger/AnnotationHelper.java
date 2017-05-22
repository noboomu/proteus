/**
 * 
 */
package io.sinistral.proteus.server.swagger;

import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;

import javax.ws.rs.FormParam;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

import io.swagger.annotations.ApiParam;
import io.swagger.annotations.Example;

/**
 * @author jbauer
 */
public class AnnotationHelper
{
	public static FormParam createFormParam(Parameter parameter)
	{

		return new FormParam()
		{

			@Override
			public String value()
			{
				return parameter.getName();
			}

			@Override
			public Class<? extends Annotation> annotationType()
			{
				return FormParam.class;
			}

		};
	}

	public static QueryParam createQueryParam(Parameter parameter)
	{

		return new QueryParam()
		{

			@Override
			public String value()
			{
				return parameter.getName();
			}

			@Override
			public Class<? extends Annotation> annotationType()
			{
				return QueryParam.class;
			}
		};
	}

	public static PathParam createPathParam(Parameter parameter)
	{

		return new PathParam()
		{

			@Override
			public String value()
			{
				return parameter.getName();
			}

			@Override
			public Class<? extends Annotation> annotationType()
			{
				return PathParam.class;
			}
		};
	}

	public static ApiParam createApiParam(Parameter parameter)
	{

		return new ApiParam()
		{

			@Override
			public Class<? extends Annotation> annotationType()
			{
				// TODO Auto-generated method stub
				return ApiParam.class;
			}

			@Override
			public String name()
			{
				return parameter.getName();
			}

			@Override
			public String value()
			{
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public String defaultValue()
			{
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public String allowableValues()
			{
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public boolean required()
			{
				return !parameter.getParameterizedType().getTypeName().contains("java.util.Optional");
			}

			@Override
			public String access()
			{
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public boolean allowMultiple()
			{
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean hidden()
			{
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public String example()
			{
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Example examples()
			{
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public String type()
			{
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public String format()
			{
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public boolean allowEmptyValue()
			{
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean readOnly()
			{
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public String collectionFormat()
			{
				// TODO Auto-generated method stub
				return null;
			}

		};
	}
}
