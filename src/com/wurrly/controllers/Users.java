/**
 * 
 */
package com.wurrly.controllers;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
 
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.jsoniter.any.Any;
import com.jsoniter.output.JsonStream;
import com.typesafe.config.Config;
import com.wurrly.models.User;
import com.wurrly.utilities.ServerRequest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * User API
 */
@Api(tags="users",produces="application/json", consumes="application/json")
@Path("/api/users")
@Produces(("application/json")) 
@Singleton
public class Users  
{

	@Inject
	@Named("es.index.name")
	protected String esIndexName;
	
	@Inject
	protected Config configuration;
	
	private static Logger log = LoggerFactory.getLogger(Users.class.getCanonicalName());
 

	public Users()
	{

	}

	 
	@GET
	@Path("/{userId}")
	@ApiOperation(value = "Find users by id", nickname = "user", httpMethod = "GET", response = JsonNode.class)
	public Any user(final ServerRequest serverRequest, @PathParam("userId") final Long userId, @QueryParam("context") Optional<String> context)
	{
//		
// 	log.debug("esIndexName: " + esIndexName);
// 	log.debug("configuration: " + configuration);

//		log.debug("context: " + context);
//
//				
				return Any.wrap(new User(userId));

	}
 
	 
	@POST
	@Path("/")
//	@ApiImplicitParams({ @ApiImplicitParam(dataType = "com.wurrly.models.User", name = "user", paramType = "body", required = false, allowMultiple = false) })
	@ApiOperation(value = "Find users by id", nickname = "user", httpMethod = "POST", response = JsonNode.class)
	public User createUser(final ServerRequest serverRequest,  @QueryParam("context") Optional<String> context, final User user  )
	{
//		
 
//	log.debug("context: " + context); 
//	log.debug("request: " + serverRequest); 
//	log.debug("file: " + user); 
		
		 return user;
 
		 

	}
	
	@PUT
	@Path("/{userId}/username")
//	@ApiImplicitParams({ @ApiImplicitParam(dataType = "com.wurrly.models.User", name = "user", paramType = "body", required = false, allowMultiple = false) })
	@ApiOperation(value = "Update a user's name", nickname = "updateUsername", httpMethod = "PUT", response = JsonNode.class)
	public Any updateUsername(final ServerRequest serverRequest,  @QueryParam("context") Optional<String> context, final User user  )
	{
//		
	log.debug("esIndexName: " + esIndexName); 
	log.debug("context: " + context); 
	log.debug("request: " + serverRequest); 
	log.debug("file: " + user); 

 
				return  Any.wrap(user);

	}



}
