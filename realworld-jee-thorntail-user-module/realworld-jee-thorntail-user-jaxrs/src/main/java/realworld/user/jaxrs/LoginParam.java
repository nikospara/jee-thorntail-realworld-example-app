package realworld.user.jaxrs;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonRootName;
import realworld.user.model.UserLoginData;

/**
 * Information required to log in.
 *
 * @see <a href="https://github.com/gothinkster/realworld/tree/master/api#authentication" target="_top">Specification</a>
 */
@JsonRootName("user")
public class LoginParam implements UserLoginData, Serializable {

	private static final long serialVersionUID = 1L;

	private String email;
	private String password;

	/** Get the user email. */
	@Override
	public String getEmail() {
		return email;
	}

	/** Set the user email. */
	public void setEmail(String email) {
		this.email = email;
	}

	/** Get the password. */
	@Override
	public String getPassword() {
		return password;
	}

	/** Set the password. */
	public void setPassword(String password) {
		this.password = password;
	}
}
