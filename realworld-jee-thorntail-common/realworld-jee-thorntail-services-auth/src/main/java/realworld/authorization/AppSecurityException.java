package realworld.authorization;

/**
 * Generic superclass of security-related exceptions in this application.
 */
public abstract class AppSecurityException extends RuntimeException {

	private static final long serialVersionUID = -6122838389218067566L;

	/**
	 * Default constructor.
	 */
	public AppSecurityException() {
		super();
	}

	/**
	 * Construct an exception with a message.
	 * 
	 * @param message The message
	 */
	public AppSecurityException(String message) {
		super(message);
	}

	/**
	 * Construct an exception with a cause.
	 * 
	 * @param cause
	 */
	public AppSecurityException(Throwable cause) {
		super(cause);
	}

	/**
	 * Construct an exception with a message and a cause.
	 * 
	 * @param message
	 * @param cause
	 */
	public AppSecurityException(String message, Throwable cause) {
		super(message, cause);
	}
}
