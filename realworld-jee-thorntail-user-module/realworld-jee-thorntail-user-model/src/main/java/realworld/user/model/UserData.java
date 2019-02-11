package realworld.user.model;

import java.io.Serializable;

/**
 * User profile full data.
 */
public interface UserData {

	/** Get the user id. */
	String getId();

	/** Get the user name. */
	String getUsername();

	/** Get the user email. */
	String getEmail();

	/** Get the user biography. */
	String getBio();

	/** Get the link to the user image. */
	String getImage();

	/**
	 * Make an instance from the full data, but without id. Used when creating new user.
	 *
	 * @param username The user name
	 * @param email    The email
	 * @param bio      The biography
	 * @param image    The image
	 * @return An immutable instance
	 */
	static UserData make(String username, String email, String bio, String image) {
		return new Impl(null, username, email, bio, image);
	}

	/**
	 * Make an instance from the full data.
	 *
	 * @param id       The user id
	 * @param username The user name
	 * @param email    The email
	 * @param bio      The biography
	 * @param image    The image
	 * @return An immutable instance
	 */
	static UserData make(String id, String username, String email, String bio, String image) {
		return new Impl(id, username, email, bio, image);
	}

	/**
	 * Simple, immutable implementation of the {@link UserData}.
	 */
	class Impl implements UserData, Serializable {

		private static final long serialVersionUID = 1L;

		private String id;
		private String username;
		private String email;
		private String bio;
		private String image;

		/**
		 * Full constructor.
		 *
		 * @param id       The user id
		 * @param username The user name
		 * @param email    The email
		 * @param bio      The biography
		 * @param image    The image
		 */
		Impl(String id, String username, String email, String bio, String image) {
			this.id = id;
			this.username = username;
			this.email = email;
			this.bio = bio;
			this.image = image;
		}

		@Override
		public String getId() {
			return id;
		}

		@Override
		public String getUsername() {
			return username;
		}

		@Override
		public String getEmail() {
			return email;
		}

		@Override
		public String getBio() {
			return bio;
		}

		@Override
		public String getImage() {
			return image;
		}
	}
}
