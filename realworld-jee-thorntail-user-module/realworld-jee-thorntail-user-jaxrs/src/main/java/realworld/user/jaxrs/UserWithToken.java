package realworld.user.jaxrs;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonRootName;

/**
 * Full user representation, including token to access APIs that require login.
 *
 * @see <a href="https://github.com/gothinkster/realworld/tree/master/api#users-for-authentication" target="_top">Specification</a>
 */
@JsonRootName("user")
public class UserWithToken implements Serializable {

	private static final long serialVersionUID = 1L;

	private String username;
	private String email;
	private String bio;
	private String image;
	private String token;

	/**
	 * Full constructor.
	 *
	 * @param username The user name
	 * @param email    The user email
	 * @param bio      The biography
	 * @param image    Link to the user image
	 * @param token    The token to use when accessing protected APIs
	 */
	public UserWithToken(String username, String email, String bio, String image, String token) {
		this.username = username;
		this.email = email;
		this.bio = bio;
		this.image = image;
		this.token = token;
	}

	/** Get the user name. */
	public String getUsername() {
		return username;
	}
	/** Get the user email. */
	public String getEmail() {
		return email;
	}
	/** Get the user biography. */
	public String getBio() {
		return bio;
	}
	/** Get the link to the user image. */
	public String getImage() {
		return image;
	}
	/** Get the token. */
	public String getToken() {
		return token;
	}
}
