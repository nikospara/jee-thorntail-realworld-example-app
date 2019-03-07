package realworld.user.model;

import org.immutables.value.Value;

/**
 * User profile full data.
 */
@Value.Immutable
public interface UserData {

	/** Get the user id. */
	@Nullable
	String getId();

	/** Get the user name. */
	String getUsername();

	/** Get the user email. */
	String getEmail();

	/** Get the user biography. */
	@Nullable
	String getBio();

	/** Get the link to the user image. */
	@Nullable
	String getImage();
}
