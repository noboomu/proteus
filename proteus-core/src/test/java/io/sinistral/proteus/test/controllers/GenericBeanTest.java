package io.sinistral.proteus.test.controllers;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeBindings;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeVariableName;
import io.sinistral.proteus.server.handlers.HandlerGenerator;
import org.apache.commons.lang3.reflect.TypeUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.reflections.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GenericBeanTest {

    private static final Logger logger = LoggerFactory.getLogger(GenericBean.class.getName());

    static ObjectMapper objectMapper = new ObjectMapper();

    public static class GenericClass {

        enum Range {
            far, close;
        }

        public io.sinistral.proteus.test.controllers.GenericBean<Long> bean = new io.sinistral.proteus.test.controllers.GenericBean<>();

        private Range range = Range.far;

        public Range getRange() {

            return range;
        }

        public void setRange(Range range) {

            this.range = range;
        }

        public GenericClass() {

        }

        public GenericClass(GenericBean<Long> bean) {

            this.bean = bean;
        }

        public GenericBean<Long> getBean() {

            return bean;
        }

        public GenericBean<Long> getLongBean() {

            return new GenericBean<>(10L);
        }

        public void setBean(GenericBean<Long> bean) {

            this.bean = bean;
        }

        public List<GenericBean<Long>> getGenericClassBean(Collection<Long> items)
        {

            return items.stream().map(GenericBean::new).collect(Collectors.toList());
        }

        public List<Map<String, Set<Long>>> getComplex()
        {

            Random random = new Random();

            return List.of(Map.of("a", random.longs(5).boxed().collect(Collectors.toSet())), Map.of("b", random.longs(15).boxed().collect(Collectors.toSet())));

        }

        public Optional<Long> getOptionalBean()
        {

            return Optional.ofNullable(this.bean).map(GenericBean::getValue);
        }

    }

    @BeforeAll
    protected void setUp()
    {

    }

    public static <T> Class<T> resolveGenericType(Class<?> declaredClass) {

        if (declaredClass.isAssignableFrom(ParameterizedType.class))
        {
            ParameterizedType parameterizedType = (ParameterizedType) declaredClass.getGenericSuperclass();
            Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
            return (Class<T>) actualTypeArguments[0];
        }
        else
        {
            return null;
        }
    }

    private JavaType constructJavaType(final Type type) {

        if (type instanceof ParameterizedType)
        {
            JavaType[] javaTypeArgs = new JavaType[((ParameterizedType) type).getActualTypeArguments().length];
            for (int i = 0; i != ((ParameterizedType) type).getActualTypeArguments().length; ++i)
            {
                javaTypeArgs[i] = constructJavaType(((ParameterizedType) type).getActualTypeArguments()[i]);
            }
            return objectMapper.getTypeFactory().constructType(type,
                    TypeBindings.create((Class<?>) ((ParameterizedType) type).getRawType(), javaTypeArgs));
        }
        else
        {
            return objectMapper.getTypeFactory().constructType(type);
        }
    }

    public   JavaType generateTypeReference(Class<?> type)
    {

        return constructJavaType(type);

    }

    @Test
    void genericTypeHandler() throws Exception
    {

        Set<Method> methods = ReflectionUtils.get(ReflectionUtils.Methods.get(GenericClass.class));

        for (Method m : methods)
        {

            // get rawType (container)
            // getActualTypeArguments (inside brackets) by name

            // method sdignature is string version
            /**
             *  method.genericInfo
             *      returnType
             *
             */

            logger.info("method: {} {}", m, m.getName());
            if (m.getName().contains("get"))
            {
                Class<?> genericReturnType = m.getReturnType();

                Class<?> genericType = resolveGenericType(genericReturnType);

          //      

                Type type = m.getGenericReturnType();
String s = "";
                if (type instanceof ParameterizedType)
                {
                      s = HandlerGenerator.typeReferenceNameForParameterizedType((ParameterizedType) type);



                 //   
                }



                Type erasedType = HandlerGenerator.extractErasedType(type);

               // 

                logger.info("generateTypeReference: {}", generateTypeReference(m.getReturnType()));



                Map<TypeVariable<?>, Type> typeArgs = new HashMap<>();

                if(type instanceof ParameterizedType)
                {
                    typeArgs = TypeUtils.getTypeArguments((ParameterizedType)type);
                }

                MethodSpec.Builder methodSpecBuilder = MethodSpec.methodBuilder("someMethod");

                methodSpecBuilder.addTypeVariables(typeArgs.keySet().stream().map(TypeVariableName::get).collect(Collectors.toList()));

                methodSpecBuilder.addStatement("$T $L = new $T();",m.getGenericReturnType(),s, m.getGenericReturnType() );


                logger.info("args: {}\nmethod: {}",typeArgs,methodSpecBuilder.build().toString());
                StringBuilder sb1 = new StringBuilder();
                sb1.append("\n").append("type: ").append(type).append(" => ").append(genericReturnType.getTypeName()).append("\n");
                typeArgs.forEach((k, v) -> {
                    sb1.append("name: ").append(k.getName()).append(" typeName: ").append(k.getTypeName()).append(" bounds: ").append(Arrays.toString(k.getBounds())).append("\n");
                    sb1.append("value: ").append(v).append(" typeName: ").append(v.getTypeName()).append("\n");
                });

                logger.info("{}", sb1.toString());


                Class<?> clazz = TypeUtils.getRawType(type, type);


                Type superclass = genericReturnType.getGenericSuperclass();
                 List<TypeVariable<?>> genericParameterTypes = Arrays.stream(genericReturnType.getTypeParameters()).collect(Collectors.toList());

                StringBuilder sb = new StringBuilder().append("\n").append("generic return type: ").append(genericReturnType).append("\t").append(genericReturnType.getTypeName()).append("\n")
                                                      .append("getGenericReturnType:\t").append(m.getGenericReturnType()).append("\n")
                                                      .append("type parameters: ").append(genericParameterTypes);

                logger.info(sb.toString());
                sb = new StringBuilder();

                for (TypeVariable<?> t : genericParameterTypes)
                {

                    Class<?> t1 = TypeUtils.getRawType(t, type);

                    Class<?> t2 = TypeUtils.getRawType(type, type);

                    sb.append("type variable: ").append(t).append("\n").append("t: ").append(t).append("\ntype: ").append(type).append("\n").append("t1: ").append(t1).append("\n").append("t2: ").append(t2);

                    logger.info(sb.toString());

                    Type cachedType = typeArgs.get(t);

                    logger.info("cached: {}", cachedType);
                    sb = new StringBuilder();
                }

                logger.info("generic return type: {}\n return type: {}", m.getGenericReturnType(), m.getReturnType());

                logger.info("result:\n{}", sb);
            }
        }

        logger.info("methods: {} ", methods);

    }

}