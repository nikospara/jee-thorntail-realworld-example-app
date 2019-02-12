package realworld.jaxrs.sys.authentication;

import javax.enterprise.inject.Vetoed;

import realworld.authentication.User;

/**
 * {@code Principal} implementation.
 */
@Vetoed
public class UserImpl implements User {
	
	private String name;
	private String uniqueId;
	
	/**
	 * Default constructor.
	 */
	public UserImpl() {
		// NO OP
	}
	
	/**
	 * Full constructor.
	 * 
	 * @param name     The user name
	 * @param uniqueId The unique id
	 */
	public UserImpl(String name, String uniqueId) {
		this.name = name;
		this.uniqueId = uniqueId;
	}

	@Override
	public String getName() {
		return name;
	}

	/**
	 * Set the name of this user.
	 * 
	 * @param name The name
	 */
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getUniqueId() {
		return uniqueId;
	}
	
	/**
	 * Set the unique id.
	 * 
	 * @param uniqueId The unique id
	 */
	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
	}
}
