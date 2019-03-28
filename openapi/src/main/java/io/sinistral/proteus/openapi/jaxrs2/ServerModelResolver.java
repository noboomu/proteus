
/**
 *
 */
package io.sinistral.proteus.openapi.jaxrs2;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.type.TypeFactory;
import io.sinistral.proteus.server.ServerResponse;
import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverter;
import io.swagger.v3.core.converter.ModelConverterContext;
import io.swagger.v3.core.util.Json;
import io.swagger.v3.oas.models.media.Schema;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * @author jbauer
 */
public class ServerModelResolver extends io.swagger.v3.core.jackson.ModelResolver
{
    private static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(ServerModelResolver.class.getCanonicalName());

    public ServerModelResolver()
    {
        super(Json.mapper());
    }

    /**
     * @param mapper
     */
    public ServerModelResolver(ObjectMapper mapper)
    {
        super(mapper);
    }

    /*
     * (non-Javadoc)
     * @see io.swagger.v3.core.jackson.ModelResolver#resolve(io.swagger.v3.core.
     * converter.AnnotatedType,
     * io.swagger.v3.core.converter.ModelConverterContext, java.util.Iterator)
     */
    @Override
    public Schema resolve(AnnotatedType annotatedType, ModelConverterContext context, Iterator<ModelConverter> next)
    {
        JavaType classType = TypeFactory.defaultInstance().constructType(annotatedType.getType());
        Class<?> rawClass = classType.getRawClass();
        JavaType resolvedType = classType;

        if ((rawClass != null) &&!resolvedType.isPrimitive())
        {
            // log.debug("resolvedType in " + resolvedType);
            if (rawClass.isAssignableFrom(ServerResponse.class))
            {
                resolvedType = classType.containedType(0);
            }
            else if (rawClass.isAssignableFrom(CompletableFuture.class))
            {
                Class<?> futureCls = classType.containedType(0).getRawClass();

                if (futureCls.isAssignableFrom(ServerResponse.class))
                {
                    // log.debug("class is assignable from ServerResponse");
                    final JavaType futureType = TypeFactory.defaultInstance().constructType(classType.containedType(0));

                    resolvedType = futureType.containedType(0);
                }
                else
                {
                    // log.debug("class is NOT assignable from ServerResponse");
                    resolvedType = classType.containedType(0);
                }
            }

            if (resolvedType != null)
            {
                if (resolvedType.getTypeName().contains("java.lang.Void"))
                {
                    resolvedType = TypeFactory.defaultInstance().constructFromCanonical(Void.class.getName());
                }
                else if (resolvedType.getTypeName().contains("Optional"))
                {
                    if (resolvedType.getTypeName().contains("java.nio.file.Path"))
                    {
                        resolvedType = TypeFactory.defaultInstance().constructParametricType(Optional.class, File.class);
                    }

                    if (resolvedType.getTypeName().contains("ByteBuffer"))
                    {
                        resolvedType = TypeFactory.defaultInstance().constructParametricType(Optional.class, File.class);
                    }
                }
                else
                {
                    if (resolvedType.getTypeName().contains("java.nio.file.Path"))
                    {
                        resolvedType = TypeFactory.defaultInstance().constructFromCanonical(File.class.getName());
                    }

                    if (resolvedType.getTypeName().contains("ByteBuffer"))
                    {
                        resolvedType = TypeFactory.defaultInstance().constructFromCanonical(File.class.getName());
                    }
                }

                annotatedType.setType(resolvedType);

                // log.debug("resolvedType out " + resolvedType);
            }
        }

        try {

            // log.info("Processing " + annotatedType + " " + classType + " " + annotatedType.getName());
            return super.resolve(annotatedType, context, next);

        } catch (Exception e) {

            log.error("Error processing " + annotatedType + " " + classType + " " + annotatedType.getName(), e);

            return null;
        }
    }

    /*
     * (non-Javadoc)
     * @see
     * io.swagger.v3.core.jackson.ModelResolver#resolveRequiredProperties(com.
     * fasterxml.jackson.databind.introspect.Annotated,
     * java.lang.annotation.Annotation[],
     * io.swagger.v3.oas.annotations.media.Schema)
     */
    @Override
    protected List<String> resolveRequiredProperties(Annotated a, Annotation[] annotations, io.swagger.v3.oas.annotations.media.Schema schema)
    {
        // TODO Auto-generated method stub
        return super.resolveRequiredProperties(a, annotations, schema);
    }

    /*
     * (non-Javadoc)
     * @see
     * io.swagger.v3.core.jackson.ModelResolver#shouldIgnoreClass(java.lang.
     * reflect.Type)
     */
    @Override
    protected boolean shouldIgnoreClass(Type type)
    {
        // System.out.println("should ignore " + type);
        JavaType classType = TypeFactory.defaultInstance().constructType(type);
        String canonicalName = classType.toCanonical();

        if (canonicalName.startsWith("io.undertow")
                || canonicalName.startsWith("org.xnio")
                || canonicalName.equals("io.sinistral.proteus.server.ServerRequest")
                || canonicalName.contains(Void.class.getName()))
        {
            return true;
        }

        // TODO Auto-generated method stub
        return super.shouldIgnoreClass(type);
    }
}



