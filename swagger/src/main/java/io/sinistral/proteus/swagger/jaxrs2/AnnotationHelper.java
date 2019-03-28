
/**
 *
 */
package io.sinistral.proteus.swagger.jaxrs2;

import io.swagger.annotations.ApiParam;
import io.swagger.annotations.Example;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;

/**
 * @author jbauer
 */
public class AnnotationHelper
{
    public static ApiParam createApiParam(Parameter parameter)
    {
        return new ApiParam()
        {
            @Override
            public Class<? extends Annotation> annotationType()
            {
                return ApiParam.class;
            }
            @Override
            public String name()
            {
                QueryParam queryParam = parameter.getAnnotation(QueryParam.class);
                FormParam formParam = parameter.getAnnotation(FormParam.class);
                PathParam pathParam = parameter.getAnnotation(PathParam.class);

                if (queryParam != null)
                {
                    return queryParam.value();
                }
                else if (pathParam != null)
                {
                    return pathParam.value();
                }
                else if (formParam != null)
                {
                    return formParam.value();
                }
                else
                {
                    return parameter.getName();
                }
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
                try {

                    DefaultValue defaultValue = parameter.getAnnotation(DefaultValue.class);

                    return defaultValue.value();

                } catch (NullPointerException e) {}

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

    public static FormParam createFormParam(Parameter parameter)
    {
        return new FormParam()
        {
            @Override
            public String value()
            {
                FormParam annotation = parameter.getAnnotation(FormParam.class);

                if (annotation != null)
                {
                    return annotation.value();
                }

                return parameter.getName();
            }
            @Override
            public Class<? extends Annotation> annotationType()
            {
                return FormParam.class;
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
                PathParam annotation = parameter.getAnnotation(PathParam.class);

                if (annotation != null)
                {
                    return annotation.value();
                }

                return parameter.getName();
            }
            @Override
            public Class<? extends Annotation> annotationType()
            {
                return PathParam.class;
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
                QueryParam annotation = parameter.getAnnotation(QueryParam.class);

                if (annotation != null)
                {
                    return annotation.value();
                }

                return parameter.getName();
            }
            @Override
            public Class<? extends Annotation> annotationType()
            {
                return QueryParam.class;
            }
        };
    }
}



