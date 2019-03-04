package realworld.authentication;

/**
 * Authentication context.
 */
public interface AuthenticationContext {

	/**
	 * Returns principal containing the name of the current authenticated user.
	 * If the user has not been authenticated, the method returns {@code null}.
	 * 
	 * @return A principal containing the name of the current user or {@code null} if the user has not been authenticated
	 */
	User getUserPrincipal();
}
