package realworld.authorization;

/**
 * Helper methods for authorizing the access to services.
 */
public interface Authorization {

	/**
	 * Require a logged-in user.
	 * 
	 * @throws AppSecurityException If the requirement is not met
	 */
	void requireLogin() throws AppSecurityException;
}
