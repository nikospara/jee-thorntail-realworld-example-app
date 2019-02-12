package realworld.user.jaxrs;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonRootName;
import realworld.user.model.UserRegistrationData;

/**
 * Information required to register.
 *
 * @see <a href="https://github.com/gothinkster/realworld/tree/master/api#registration" target="_top">Specification</a>
 */
@JsonRootName("user")
public class RegisterParam implements UserRegistrationData, Serializable {

	private static final long serialVersionUID = 1L;

	private String username;
	private String email;
	private String password;

	@Override
	public String getUsername() {
		return username;
	}

	/** Set the user name. */
	public void setUsername(String username) {
		this.username = username;
	}

	@Override
	public String getEmail() {
		return email;
	}

	/** Set the user email. */
	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String getPassword() {
		return password;
	}

	/** Set the password. */
	public void setPassword(String password) {
		this.password = password;
	}
}
