package realworld.services;

import java.util.Collections;
import java.util.List;

/**
 * Simple validation exception that produces the same output as a full blown {@code ConstraintViolationException}
 * without the extra hassle.
 */
public class SimpleValidationException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	private final List<SimpleConstraintViolation> violations;

	public SimpleValidationException(List<SimpleConstraintViolation> violations) {
		this.violations = violations;
	}

	public SimpleValidationException(SimpleConstraintViolation violation) {
		this.violations = Collections.singletonList(violation);
	}

	public List<SimpleConstraintViolation> getViolations() {
		return violations;
	}
}
