
/**
 *
 */
package io.sinistral.proteus.server.tools.swagger;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

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
    public ServerParameterExtension()
    {
        super();
    }

    @Override
    protected JavaType constructType(Type type)
    {
        if (type.getTypeName().contains("java.nio.ByteBuffer") || type.getTypeName().contains("java.nio.file.Path"))
        {
            type = java.io.File.class;
        }

        return super.constructType(type);
    }

    @Override
    public List<Parameter> extractParameters(List<Annotation> annotations, Type type, Set<Type> typesToSkip, Iterator<SwaggerExtension> chain)
    {
        if (type.getTypeName().contains("java.nio.ByteBuffer") || type.getTypeName().contains("java.nio.file.Path"))
        {
            type = java.io.File.class;
        }

        return super.extractParameters(annotations, type, typesToSkip, chain);
    }

    @Override
    protected boolean shouldIgnoreType(Type type, Set<Type> typesToSkip)
    {
        if (type.getTypeName().contains("io.sinistral.proteus.server.ServerRequest")
                || type.getTypeName().contains("HttpServerExchange")
                || type.getTypeName().contains("HttpHandler")
                || type.getTypeName().contains("io.sinistral.proteus.server.ServerResponse")
                || type.getTypeName().contains("io.undertow.server.session"))
        {
            return true;
        }

        return super.shouldIgnoreType(type, typesToSkip);
    }
}



