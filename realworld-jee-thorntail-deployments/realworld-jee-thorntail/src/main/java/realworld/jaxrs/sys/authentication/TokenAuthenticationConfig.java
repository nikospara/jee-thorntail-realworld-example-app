package realworld.jaxrs.sys.authentication;

import java.net.URL;

/**
 * Configuration for token authentication.
 */
public interface TokenAuthenticationConfig {

	/**
	 * The JWT secret.
	 * 
	 * @return The JWT secret
	 */
	String getJwtSecret();
	
	/**
	 * The JWT session validity time in seconds.
	 * 
	 * @return The JWT session validity time
	 */
	Long getJwtSessionTime();

	/**
	 * Unused in the current configuration.
	 */
	URL getJwkUrl();

	/**
	 * The field of the JWT that maps to the user name.
	 *
	 * @return The field of the JWT that maps to the user name.
	 */
	String getUsernameFieldInJwt();
}
