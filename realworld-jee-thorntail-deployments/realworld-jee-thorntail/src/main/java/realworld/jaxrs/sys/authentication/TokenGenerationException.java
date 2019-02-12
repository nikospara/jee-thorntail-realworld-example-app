package realworld.jaxrs.sys.authentication;

/**
 * Indicates some error in the token generation process.
 */
public class TokenGenerationException extends RuntimeException {
	public TokenGenerationException(Throwable cause) {
		super(cause);
	}
}
