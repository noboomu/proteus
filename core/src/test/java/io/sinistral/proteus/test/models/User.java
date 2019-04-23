/**
 * 
 */
package io.sinistral.proteus.test.models;

 
/**
 * @author jbauer
 *
 */
public class User
{
 
	public static class InnerUserModel {
		public Long id;
	}
	
	public enum UserType
	{
		GUEST,MEMBER,ADMIN
	}
	
	private Long id = 0L;
	 
	
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



	/**
	 * @param type the type to set
	 */
	public void setType(UserType type)
	{
		this.type = type;
	}

 
	/**
	 * @return the type
	 */
	public UserType getType()
	{
		return type;
	}
	
	public static User generateUser()
	{
		return new User((long)(Math.random()*1000)+1L, UserType.ADMIN);
	}
	
}
