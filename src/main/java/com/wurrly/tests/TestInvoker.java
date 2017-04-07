/**
 * 
 */
package com.wurrly.tests;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Throwables;
import com.wurrly.controllers.Users;
import com.wurrly.server.ServerRequest;

import static java.lang.invoke.MethodHandles.lookup;

import java.lang.invoke.CallSite;
import java.lang.invoke.LambdaMetafactory;

/**
 * @author jbauer
 *
 */
public class TestInvoker
{
	private final static Method biConsumerAccept;
    private final static Method consumerAccept;
    private final static Method quadConsumerAccept;

    private final static Method functionApply;
    private final static Method biFunctionApply;

private static Logger Logger = LoggerFactory.getLogger(TestInvoker.class.getCanonicalName());
	
	interface QuadConsumer<R, T, U, V> {
		  void accept(R r,T t, U u, V v);
		}
	
//	public QuadConsumer<Users, ServerRequest,Long,Optional<String>> handleLambda = 
//			(Users users, ServerRequest req, Long userId, Optional<String> context) -> {
//				
//				users.user(req, userId, context);
//				
//			};
//			
    static {
        try {
            biConsumerAccept = BiConsumer.class.getMethod("accept", Object.class, Object.class);
            consumerAccept = Consumer.class.getMethod("accept", Object.class);
            functionApply = Function.class.getMethod("apply", Object.class);
            biFunctionApply = BiFunction.class.getMethod("apply", Object.class, Object.class);
            quadConsumerAccept = QuadConsumer.class.getMethod("accept", Object.class, Object.class, Object.class, Object.class);

        } catch (NoSuchMethodException e) {
            throw Throwables.propagate(e);
        }
    }
    
	 

public static void main(String[] args)  
{

	try
	{
        MethodHandles.Lookup lookup = MethodHandles.lookup();

 		Users users = new Users();
 		
		Method[] methods = Users.class.getDeclaredMethods();
		
		for( Method m : methods )
		{
			Logger.debug("method: " + m);
			
//			MethodHandle mh = lookup.unreflect(m);
//			
//			MethodType mt = MethodType.fromMethodDescriptorString(m.toString(),ClassLoader.getSystemClassLoader());
//			
//			Logger.debug("method type: " + mt);
			
			 final MethodHandles.Lookup caller = lookup.in(m.getDeclaringClass());
		        final MethodHandle implementationMethod;

		        try {
		            implementationMethod = caller.unreflect(m);
		        } catch (IllegalAccessException e) {
		           throw Throwables.propagate(e);
		        }

		        final MethodType factoryMethodType = MethodType.methodType(consumerAccept.getDeclaringClass());

			final Class<?> methodReturn = m.getReturnType();

			final Class<?>[] methodParams = m.getParameterTypes();
			
			for( Class<?> c : methodParams )
			{
				System.out.println("method param: " + c);
				 
			}
			
	        final MethodType functionMethodType = MethodType.methodType(methodReturn, methodParams);

			System.out.println("functionMethodType: " + functionMethodType);

			QuadConsumer.class.getMethod("accept", Object.class, Object.class, Object.class, Object.class);
			
			//invokeMethodHandler(users,mh);
			
			QuadConsumer<Users,ServerRequest, Long, Optional<String>> consumer = produceLambda(m,quadConsumerAccept);
			
			System.out.println("consumer: " + consumer); 
			
			consumer.accept( users, new ServerRequest(), 2501L,   Optional.of("default"));

		}
		
	
			
			

	} catch (Exception e)
	{
		e.printStackTrace();
	}
}

public static <T> T produceLambda(final Method sourceMethod, final Method targetMethod) {
    MethodHandles.Lookup lookup = lookup();
    sourceMethod.setAccessible(true);
    final MethodHandles.Lookup caller = lookup.in(sourceMethod.getDeclaringClass());
    final MethodHandle implementationMethod;

    try {
        implementationMethod = caller.unreflect(sourceMethod);
    } catch (IllegalAccessException e) {
       throw Throwables.propagate(e);
    }

    final MethodType factoryMethodType = MethodType.methodType(targetMethod.getDeclaringClass());

    final Class<?> methodReturn = targetMethod.getReturnType();
    final Class<?>[] methodParams = targetMethod.getParameterTypes();

    final MethodType functionMethodType = MethodType.methodType(methodReturn, methodParams);

    final CallSite lambdaFactory;
    try {
        lambdaFactory = LambdaMetafactory.metafactory(
                lookup,
                targetMethod.getName(),
                factoryMethodType,
                functionMethodType,
                implementationMethod,
                implementationMethod.type()
        );

        final MethodHandle factoryInvoker = lambdaFactory.getTarget();

        return (T) factoryInvoker.invoke();
        
    } catch (Throwable e) {
        // TODO: fallback to classic reflection method if lambda generation fails.
        throw new InternalError(String.format("Unable to generate lambda for method %s. %s",
                sourceMethod.getDeclaringClass().getName() + "." + sourceMethod.getName(),
                e.getMessage()));
    }
} 
	
	public static void invokeMethodHandler(Object source,MethodHandle handle)  
	{
		try
		{
			handle.bindTo(source).invokeWithArguments(new ServerRequest(), 2501L,   Optional.of("default"));

		} catch ( Throwable e)
		{
			 
		}
 
	}
 
}
