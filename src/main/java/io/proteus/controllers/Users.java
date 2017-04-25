/**
 * 
 */
package io.proteus.controllers;

import static io.proteus.server.ServerResponse.response;

import java.nio.ByteBuffer;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableMap;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.jsoniter.any.Any;
import com.jsoniter.output.JsonStream;
import com.typesafe.config.Config;

import io.proteus.models.User;
import io.proteus.server.ServerRequest;
import io.proteus.server.ServerResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.HttpString; 
/**
 * User API
 */
@Api(tags="users")
@Path("/api/users")
@Produces((MediaType.APPLICATION_JSON)) 
@Consumes((MediaType.APPLICATION_JSON)) 
@Singleton
public class Users  
{
	  
	@Inject
	protected Config config;
	
	private static Logger log = LoggerFactory.getLogger(Users.class.getCanonicalName());
 

	public Users()
	{

	}

	@GET
	@Path("/{userId}/type")
	@ApiOperation(value = "Find users by id with type", httpMethod = "GET", response = User.class)
	public ServerResponse<User> userType(
	                    @ApiParam(hidden=true)final ServerRequest serverRequest, @PathParam("userId") final Long userId, 
	                    @QueryParam("optionalQueryString")  Optional<String> optionalQueryString, 
	                    @QueryParam("optionalLong")  Optional<Long> optionalLong, 
	                    @QueryParam("longValue")   Long  longValue, 
	                    @QueryParam("dateValue") @ApiParam(defaultValue="2014-04-23T04:30:45.123+01:00", format="date")  Optional<Date>  optionalDate, 
	                    @QueryParam("numbers")    List<Integer>  numbers, 

	                    @ApiParam(defaultValue="01234567-9ABC-DEF0-1234-56789ABCDEF0", format="uuid")  @QueryParam("queryFromString") UUID queryFromString, 
	                    @ApiParam(defaultValue="01234567-9ABC-DEF0-1234-56789ABCDEF0", format="uuid") @QueryParam("optionalQueryFromString") Optional<UUID> optionalQueryFromString, 
	                    @ApiParam(defaultValue="01234567-9ABC-DEF0-1234-56789ABCDEF0") @HeaderParam("headerFromString") Optional<UUID> headerFromString,
	                    @ApiParam(defaultValue="123") @HeaderParam("headerString") String headerString,
	                    @HeaderParam("optionalHeaderString") Optional<String> optionalHeaderString,
	                    @QueryParam("queryEnum") User.UserType queryEnum, 
	                    @QueryParam("optionalQueryEnum") Optional<User.UserType> optionalQueryEnum
	                    
	                    )
	{
 		
				 log.debug("optionalQueryFromString: " + optionalQueryFromString);
				 log.debug("queryFromString: " + queryFromString);
				 log.debug("optionalQueryString: " + optionalQueryString);
				 log.debug("headerFromString: " + headerFromString);
				 log.debug("headerString: " + headerString);
				 log.debug("optionalHeaderString: " + optionalHeaderString);
				 log.debug("queryEnum: " + queryEnum);
				 log.debug("optionalQueryEnum: " + optionalQueryEnum);
				 log.debug("userId: " + userId);
				 log.debug("numbers: " + numbers);
				 log.debug("optionalDate: " + optionalDate);

 		
				return response(new User(232343L))
						.ok() 
						.header(HttpString.tryFromString("TestHeader"), "57475475");

	}
	
	@POST
	@Path("/form/{userId}")
 	@Consumes("*/*")
	@ApiOperation(value = "Post a complex form",   httpMethod = "POST", response = User.class)
	public ServerResponse<Any> userForm(  final ServerRequest serverRequest, 
	                    @ApiParam(name="userId",required=true) @PathParam("userId") final Long userId,
	                    @ApiParam(name="context",required=false) @QueryParam("context") Optional<String> context, 
	                    @ApiParam(name="type",required=true) @QueryParam("type") User.UserType type, 
	                    ByteBuffer testFile
	                    )
	{
//		
// 	log.debug("esIndexName: " + esIndexName);
// 	log.debug("configuration: " + configuration);

 	log.debug("testFile: " + testFile);
//
//				
				return response(Any.wrap(new User(userId,type))).ok();

	}
	 
	 
	@GET
	@Path("/{userId}")
	@ApiOperation(value = "Find users by id",   httpMethod = "GET", response = User.class)
	public ServerResponse<User> user( final ServerRequest serverRequest, 
	                @ApiParam(name="userId", required=true) @PathParam("userId") final Long userId, 
	                @ApiParam(name="context", required=false) @QueryParam("context") Optional<String> context
	                )
	{
//		
// 	log.debug("esIndexName: " + esIndexName);
// 	log.debug("configuration: " + configuration);

//		log.debug("context: " + context);
//
//				
		return response(User.class)
				.ok()
				.applicationJson()
				.body(JsonStream.serialize(new User(userId)));
				 

	}
 
	 
	@SuppressWarnings("rawtypes")
	@POST
	@Path("/")
	//@Consumes("multipart/form-data")
//	@ApiImplicitParams({ @ApiImplicitParam(dataType = "com.wurrly.models.User", name = "user", paramType = "body", required = false, allowMultiple = false) })
	@ApiOperation(value = "Create a user",   httpMethod = "POST", response = User.class)
	public ServerResponse createUser( final ServerRequest serverRequest,  
	                                     @QueryParam("context") Optional<String> context, 
	                                     final User user, 
	                                     List<String> stringArgs,
	                                     Optional<List<String>> optionalStringArgs 
	                                     )
	{
 		 
 			ServerResponse response = null;
 			
 			if( user != null )
 			{
 	 			return  response(User.class).ok().entity(user) ;

 			}
 			else
 			{
 	 			return response().throwable(new Exception("No user found")) ;
 				
 			}
 			 
	}
	
	@PUT
	@Path("/username")
	@Consumes(MediaType.APPLICATION_JSON + "," + MediaType.APPLICATION_XML) 
	@Produces(MediaType.APPLICATION_JSON + "," + MediaType.APPLICATION_XML) 
//	@ApiImplicitParams({ @ApiImplicitParam(dataType = "com.wurrly.models.User", name = "user", paramType = "body", required = false, allowMultiple = false) })
	@ApiOperation(value = "Update a user's name",   httpMethod = "PUT", response = User.class)
	public CompletableFuture<ServerResponse<List<User>>> updateUsername(@ApiParam(hidden=true)final ServerRequest serverRequest,  @QueryParam("context") Optional<String> context, final List<User> users  )
	{
//		 
	log.debug("context: " + context); 
	log.debug("request: " + serverRequest); 
	log.debug("file: " + users); 

 
		return CompletableFuture.completedFuture(response(users));

	}

	@GET
	@Path("/empty")
//	@ApiImplicitParams({ @ApiImplicitParam(dataType = "com.wurrly.models.User", name = "user", paramType = "body", required = false, allowMultiple = false) })
	@ApiOperation(value = "Test an empty control endpoint",   httpMethod = "GET" )
	public CompletableFuture<ServerResponse<ImmutableMap>> empty()
	{
//		 
 
		return CompletableFuture.completedFuture(response(ImmutableMap.class).entity( ImmutableMap.of("empty", "success") ));

	}
	


}
