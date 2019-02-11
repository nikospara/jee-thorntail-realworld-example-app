package realworld.user.persistence;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

/**
 * Id class of the {@link Follow} entity.
 */
@Embeddable
public class FollowId implements Serializable {

	private static final long serialVersionUID = 1L;

	private String follower;

	private String followed;

	/**
	 * Default constructor.
	 */
	public FollowId() {
		// NO OP
	}

	/**
	 * Full constructor, the {@code follower} follows the {@code followed}.
	 *
	 * @param follower The follower id
	 * @param followed The followed id
	 */
	public FollowId(String follower, String followed) {
		this.follower = follower;
		this.followed = followed;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		FollowId other = (FollowId) o;
		return Objects.equals(follower, other.follower) && Objects.equals(followed, other.followed);
	}

	@Override
	public int hashCode() {
		return Objects.hash(follower, followed);
	}

	public String getFollower() {
		return follower;
	}

	public void setFollower(String follower) {
		this.follower = follower;
	}

	public String getFollowed() {
		return followed;
	}

	public void setFollowed(String followed) {
		this.followed = followed;
	}
}
