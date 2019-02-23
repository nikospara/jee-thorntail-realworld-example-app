package realworld.authorization;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import realworld.authentication.AuthenticationContext;

/**
 * Authorization implementation.
 */
@ApplicationScoped
class AuthorizationImpl implements Authorization {
	
	private AuthenticationContext authenticationContext;
	
	/**
	 * Default constructor required by the infrastructure.
	 */
	AuthorizationImpl() {
		// NO OP
	}

	/**
	 * Injection constructor.
	 * 
	 * @param authenticationContext The authentication context
	 */
	@Inject
	public AuthorizationImpl(AuthenticationContext authenticationContext) {
		this.authenticationContext = authenticationContext;
	}

	@Override
	public void requireLogin() throws AppSecurityException {
		if( authenticationContext.getUserPrincipal() == null ) {
			throw new NotAuthenticatedException();
		}
	}
}
