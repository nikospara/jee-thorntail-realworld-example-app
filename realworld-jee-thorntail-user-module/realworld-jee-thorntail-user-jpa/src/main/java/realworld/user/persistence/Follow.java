package realworld.user.persistence;

import static javax.persistence.FetchType.LAZY;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Mark that a user (the "follower") "follows" another (the "followed").
 */
@Entity
@IdClass(FollowId.class)
@Table(name = "RWL_USER_FOLLOW")
public class Follow {

	@Id
	@ManyToOne(fetch = LAZY, optional = false)
	@JoinColumn(name = "user_id")
	private User follower;

	@Id
	@ManyToOne(fetch = LAZY, optional = false)
	@JoinColumn(name = "follows_id")
	private User followed;

	public User getFollower() {
		return follower;
	}

	public void setFollower(User follower) {
		this.follower = follower;
	}

	public User getFollowed() {
		return followed;
	}

	public void setFollowed(User followed) {
		this.followed = followed;
	}
}
