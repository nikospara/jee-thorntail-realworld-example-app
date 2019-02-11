package realworld.user.model;

import static javax.validation.constraints.Pattern.Flag.CASE_INSENSITIVE;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * Data required for user login.
 *
 * @see <a href="https://github.com/gothinkster/realworld/tree/master/api#authentication">Specification</a>
 */
public interface UserLoginData {

	/** Get the user email. */
	@NotNull
	@Pattern(regexp="^.+@.+\\.[a-z]+$", flags=CASE_INSENSITIVE)
	String getEmail();

	/** Get the password. */
	@NotNull
	@Size(min=5)
	String getPassword();
}
