package realworld.jaxrs.sys.authentication;

import javax.enterprise.inject.Vetoed;

import realworld.authentication.AuthenticationContext;

/**
 * {@link AuthenticationContext} implementation.
 */
@Vetoed
public class AuthenticationContextImpl implements AuthenticationContext {
	
	private UserImpl userPrincipal;
	
	/**
	 * Create an {@code AuthenticationContextImpl} for the unauthenticated case.
	 * 
	 * @return The authentication context for the unauthenticated case
	 */
	public static AuthenticationContextImpl unauthenticated() {
		return new AuthenticationContextImpl();
	}
	
	/**
	 * Create an {@code AuthenticationContextImpl} for the given user.
	 * 
	 * @param userPrincipal The user
	 * @return The authentication context
	 */
	public static AuthenticationContextImpl forUser(UserImpl userPrincipal) {
		AuthenticationContextImpl result = new AuthenticationContextImpl();
		result.userPrincipal = userPrincipal;
		return result;
	}

	@Override
	public UserImpl getUserPrincipal() {
		return userPrincipal;
	}
}
