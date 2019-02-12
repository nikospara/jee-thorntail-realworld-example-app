package realworld.jaxrs.sys.authentication;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.SignedJWT;
import realworld.user.model.UserData;

/**
 * JWT-related services.
 */
public interface JwtService {

	/**
	 * Create a JWT from the given user object.
	 *
	 * @param user The user object
	 * @return The token
	 */
	String toToken(UserData user);

	/**
	 * Create a signed JWT that reflects the changes to the current user data,
	 * but has the same expiration time as the current token.
	 *
	 * @param user         The updated user data
	 * @param currentToken The current token
	 * @return The updated token
	 */
	String updateUser(UserData user, String currentToken);

	/**
	 * Verify the given JWT.
	 *
	 * @param jwt The JWT to verify
	 * @return Whether the given JWT verifies successfully
	 * @throws JOSEException On error
	 */
	boolean verify(SignedJWT jwt) throws JOSEException;
}
