package realworld.user.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import static javax.validation.constraints.Pattern.Flag.CASE_INSENSITIVE;

/**
 * Data required for user registration.
 *
 * @see <a href="https://github.com/gothinkster/realworld/tree/master/api#registration">Specification</a>
 */
public interface UserRegistrationData {

	/** Get the user name. */
	@NotNull
	@Size(min=5)
	String getUsername();

	/** Get the user email. */
	@NotNull
	@Pattern(regexp="^.+@.+\\.[a-z]+$", flags=CASE_INSENSITIVE)
	String getEmail();

	/** Get the password. */
	@NotNull
	@Size(min=5)
	String getPassword();
}
