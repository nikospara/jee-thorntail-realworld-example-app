package realworld.authorization;

/**
 * An exception during authorization indicating that the client does
 * not have access rights to the requested resource or operation.
 * This exception should not be thrown to indicate system problems.
 */
public class AuthorizationException extends RuntimeException {

	private static final long serialVersionUID = -6122838389218067566L;

	/**
	 * Default constructor.
	 */
	public AuthorizationException() {
		super();
	}

	/**
	 * Construct an exception with a message.
	 * 
	 * @param message The message
	 */
	public AuthorizationException(String message) {
		super(message);
	}

	/**
	 * Construct an exception with a cause.
	 * 
	 * @param cause
	 */
	public AuthorizationException(Throwable cause) {
		super(cause);
	}

	/**
	 * Construct an exception with a message and a cause.
	 * 
	 * @param message
	 * @param cause
	 */
	public AuthorizationException(String message, Throwable cause) {
		super(message, cause);
	}
}
