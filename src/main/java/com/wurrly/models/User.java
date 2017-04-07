/**
 * 
 */
package com.wurrly.models;

import com.jsoniter.annotation.JsonIgnore;

public class User
{
	public enum UserType
	{
		GUEST,MEMBER,ADMIN
	}
	
	private Long id = 0L;
	
	@JsonIgnore
	private UserType type = UserType.GUEST;

	public User()
	{
		
	}
	
	public User(Long id)
	{
		this.id = id;
	}
	
	public User(Long id, UserType type)
	{
		this.id = id;
		this.type = type;
	}


	/**
	 * @return the id
	 */
	public Long getId()
	{
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id)
	{
		this.id = id;
	}

//	/**
//	 * @return the type
//	 */
//	public UserType getType()
//	{
//		return type;
//	}

	/**
	 * @param type the type to set
	 */
	public void setType(UserType type)
	{
		this.type = type;
	}
	
	
	
}