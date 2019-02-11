package realworld.user.model;

import java.io.Serializable;

/**
 * Data of another user profile, including whether the current user is following.
 *
 * @see <a href="https://github.com/gothinkster/realworld/tree/master/api#profile">Specification</a>
 */
public interface ProfileData {

	/** Get the user name. */
	String getUsername();

	/** Get the user biography. */
	String getBio();

	/** Get the link to the user image. */
	String getImage();

	/** Get whether the current user is following this user. */
	boolean isFollowing();

	/**
	 * Create an instance from the full data.
	 *
	 * @param username  The user name
	 * @param bio       The user biography
	 * @param image     Link to the user image
	 * @param following Whether the current user is following this user
	 * @return An immutable instance
	 */
	static ProfileData make(String username, String bio, String image, boolean following) {
		return new Impl(username, bio, image, following);
	}

	class Impl implements ProfileData, Serializable {

		private static final long serialVersionUID = 1L;

		private String username;
		private String bio;
		private String image;
		private boolean following;

		/**
		 * Full constructor.
		 *
		 * @param username  The user name
		 * @param bio       The user biography
		 * @param image     Link to the user image
		 * @param following Whether the current user is following this user
		 */
		public Impl(String username, String bio, String image, boolean following) {
			this.username = username;
			this.bio = bio;
			this.image = image;
			this.following = following;
		}

		@Override
		public String getUsername() {
			return username;
		}

		@Override
		public String getBio() {
			return bio;
		}

		@Override
		public String getImage() {
			return image;
		}

		@Override
		public boolean isFollowing() {
			return following;
		}
	}
}
