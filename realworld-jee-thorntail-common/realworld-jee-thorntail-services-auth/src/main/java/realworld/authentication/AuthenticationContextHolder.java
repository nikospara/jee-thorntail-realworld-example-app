package realworld.authentication;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;

/**
 * Producer of the {@link AuthenticationContext}.
 */
@RequestScoped
public class AuthenticationContextHolder {

	private AuthenticationContext authenticationContext;

	/**
	 * Get and produce the {@code AuthenticationContext}.
	 * 
	 * @return The current {@code AuthenticationContext}
	 */
	@Produces @RequestScoped
	public AuthenticationContext getAuthenticationContext() {
		return authenticationContext;
	}

	/**
	 * Set the current {@code AuthenticationContext}.
	 * 
	 * @param authenticationContext The current {@code AuthenticationContext}
	 */
	public void setAuthenticationContext(AuthenticationContext authenticationContext) {
		this.authenticationContext = authenticationContext;
	}
}
