package realworld.services;

/**
 * A simple validation error for custom validations that do not
 * require the complexity of a {@code ConstraintViolation}.
 */
public class SimpleConstraintViolation {

	private String fieldName;
	private String message;

	/**
	 * Full constructor.
	 *
	 * @param fieldName The name of the invalid field
	 * @param message   The validity message
	 */
	public SimpleConstraintViolation(String fieldName, String message) {
		this.fieldName = fieldName;
		this.message = message;
	}

	/**
	 * Get the name of the invalid field.
	 *
	 * @return The name of the invalid field
	 */
	public String getFieldName() {
		return fieldName;
	}

	/**
	 * Get the validity message.
	 *
	 * @return The validity message
	 */
	public String getMessage() {
		return message;
	}
}
