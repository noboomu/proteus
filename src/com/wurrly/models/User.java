/**
 * 
 */
package com.wurrly.models;

public class User
{
	private Long id = 0l;
	
	public User()
	{
		
	}
	
	public User(Long id)
	{
		this.id = id;
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
	
	
	
}