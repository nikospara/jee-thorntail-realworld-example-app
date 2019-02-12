package realworld.user.jaxrs;

import java.io.Serializable;
import java.util.EnumSet;

import com.fasterxml.jackson.annotation.JsonRootName;
import realworld.user.model.UserUpdateData;

/**
 * Information for updating a user.
 *
 * @see <a href="https://github.com/gothinkster/realworld/tree/master/api#update-user" target="_top">Specification</a>
 */
@JsonRootName("user")
public class UpdateParam implements Serializable, UserUpdateData {

	private static final long serialVersionUID = 1L;

	private String username;
	private String email;
	private String password;
	private String image;
	private String bio;
	private EnumSet<PropName> explicitlySetProps = EnumSet.noneOf(PropName.class);

	/** Get the username. */
	@Override
	public String getUsername() {
		return username;
	}

	/** Set the username. */
	public void setUsername(String username) {
		explicitlySetProps.add(PropName.USERNAME);
		this.username = username;
	}

	/** Get the email. */
	@Override
	public String getEmail() {
		return email;
	}

	/** Set the email. */
	public void setEmail(String email) {
		explicitlySetProps.add(PropName.EMAIL);
		this.email = email;
	}

	/** Get the password. */
	@Override
	public String getPassword() {
		return password;
	}

	/** Set the password. */
	public void setPassword(String password) {
		explicitlySetProps.add(PropName.PASSWORD);
		this.password = password;
	}

	/** Get the image. */
	@Override
	public String getImage() {
		return image;
	}

	/** Set the image. */
	public void setImage(String image) {
		explicitlySetProps.add(PropName.IMAGE);
		this.image = image;
	}

	/** Get the biography. */
	@Override
	public String getBio() {
		return bio;
	}

	/** Set the biography. */
	public void setBio(String bio) {
		explicitlySetProps.add(PropName.BIO);
		this.bio = bio;
	}

	@Override
	public boolean isExplicitlySet(PropName prop) {
		return explicitlySetProps.contains(prop);
	}
}
