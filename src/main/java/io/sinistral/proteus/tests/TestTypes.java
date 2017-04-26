/**
 * 
 */
package io.sinistral.proteus.tests;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

import io.sinistral.proteus.server.ServerResponse;

/**
 * @author jbauer
 *
 */
public class TestTypes
{
 
	interface ServiceCall<T> 
	{
		CompletableFuture<T> invoke(T request);
	}
	
	public static interface FutureSupplier extends ServiceCall<ServerResponse>
	{
		default CompletableFuture<ServerResponse> invoke()
		{
			return completedFutureSupplier.get(); //CompletableFuture.completedFuture(new ServerResponse()); 
		}
		
		public CompletableFuture<ServerResponse> invoke( CompletableFuture<ServerResponse> response );
	}
	
	 
	
	public static class RestCaller implements FutureSupplier
	{

		public RestCaller( UnaryOperator<CompletableFuture<ServerResponse>> initializer )
		{
			initializer.apply(this.invoke());
		}
	 
		@Override
		public CompletableFuture<ServerResponse> invoke(CompletableFuture<ServerResponse> response)
		{
			// TODO Auto-generated method stub
			return response;
		}

		/* (non-Javadoc)
		 * @see com.wurrly.tests.TestTypes.ServiceCall#invoke(java.lang.Object)
		 */
		@Override
		public CompletableFuture<ServerResponse> invoke(ServerResponse request)
		{
			// TODO Auto-generated method stub
			return null;
		}
		
	}
	
	private static Supplier<CompletableFuture<ServerResponse>> completedFutureSupplier = () ->
	{
		return CompletableFuture.completedFuture(new ServerResponse()); 
	};
	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		// TODO Auto-generated method stub
		try
		{
			 
			
			ServiceCall<ServerResponse> res = processRequest();
		
			
			CompletableFuture<ServerResponse> response = (res.invoke(new ServerResponse()));
			
			response.thenAccept( r -> 
			{ 
				 
					r.send(null, null);
			 
				
			}).exceptionally( ex ->  {      throw new CompletionException(ex); });
		 
			
			 CompletableFuture<String> future3 = CompletableFuture.supplyAsync(() -> "test");
		        future3.thenAccept(t -> {
		            throw new RuntimeException();
		        }).exceptionally(t -> {
		            t.printStackTrace();
		            throw new CompletionException(t);
		        });
			
			//FutureSupplier supplier = processRequest2();
 			 


		} catch (Exception e)
		{
			e.printStackTrace();
		}

	}
	
	public static ServiceCall<ServerResponse> processRequest()
	{
		return (response) -> {
			
			System.out.println(response);
			return CompletableFuture.completedFuture(response);
			
		};
	}
	
 

}
