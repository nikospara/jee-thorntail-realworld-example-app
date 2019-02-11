package realworld.authentication;

import java.security.Principal;

/**
 * A user of this application.
 */
public interface User extends Principal {

	/**
	 * Get the unique id.
	 * 
	 * @return The unique id
	 */
	String getUniqueId();
}
