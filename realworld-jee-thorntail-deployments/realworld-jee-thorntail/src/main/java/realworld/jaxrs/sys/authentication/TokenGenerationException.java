package realworld.jaxrs.sys.authentication;

/**
 * Indicates some error in the token generation process.
 */
public class TokenGenerationException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public TokenGenerationException(Throwable cause) {
		super(cause);
	}
}
