package realworld.authorization;

/**
 * No authentication information could be retrieved or authentication failed.
 */
public class NotAuthenticatedException extends AuthorizationException {

	private static final long serialVersionUID = -4393652987864432259L;

	/**
	 * 
	 */
	public NotAuthenticatedException() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 */
	public NotAuthenticatedException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param cause
	 */
	public NotAuthenticatedException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 */
	public NotAuthenticatedException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}
}
