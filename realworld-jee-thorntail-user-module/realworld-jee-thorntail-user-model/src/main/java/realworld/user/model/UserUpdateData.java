package realworld.user.model;

import static javax.validation.constraints.Pattern.Flag.CASE_INSENSITIVE;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * Data required for updating a user.
 *
 * @see <a href="https://github.com/gothinkster/realworld/tree/master/api#update-user">Specification</a>
 */
public interface UserUpdateData {

	/**
	 * Used by {@link UserUpdateData#isExplicitlySet(PropName)} to specify properties
	 * of a {@code UserUpdateData} object that have been changed.
	 */
	enum PropName {
		USERNAME,
		EMAIL,
		PASSWORD,
		IMAGE,
		BIO
	}

	/** Get the user name. */
	@Size(min=5)
	String getUsername();

	/** Get the user email. */
	@Pattern(regexp="^.+@.+\\.[a-z]+$", flags=CASE_INSENSITIVE)
	String getEmail();

	/** Get the password. */
	@Size(min=5)
	String getPassword();

	/** Get the image. */
	String getImage();

	/** Get the biography. */
	String getBio();

	/**
	 * Check if the given property is explicitly set. If not its value should be disregarded.
	 *
	 * @param prop The property to check
	 * @return Whether the property is explicitly set and should not be disregarded
	 */
	boolean isExplicitlySet(PropName prop);
}
