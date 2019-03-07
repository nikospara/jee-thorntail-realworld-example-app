package realworld.user.model;

import org.immutables.value.Value;

/**
 * Data of another user profile, including whether the current user is following.
 *
 * @see <a href="https://github.com/gothinkster/realworld/tree/master/api#profile">Specification</a>
 */
@Value.Immutable
public interface ProfileData {

	/** Get the user name. */
	String getUsername();

	/** Get the user biography. */
	@Nullable
	String getBio();

	/** Get the link to the user image. */
	@Nullable
	String getImage();

	/** Get whether the current user is following this user. */
	boolean isFollowing();
}
