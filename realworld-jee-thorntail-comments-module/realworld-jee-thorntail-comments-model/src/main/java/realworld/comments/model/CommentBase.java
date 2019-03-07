package realworld.comments.model;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Base comment, containing only its own fields, not related entities.
 */
public interface CommentBase {

	/**
	 * Get the comment id.
	 *
	 * @return The comment id
	 */
	String getId();


	/**
	 * The creation date.
	 *
	 * @return the creation date
	 */
	LocalDateTime getCreatedAt();

	/**
	 * The last modification date.
	 *
	 * @return The last modification date
	 */
	LocalDateTime getUpdatedAt();

	/**
	 * The body.
	 *
	 * @return The body
	 */
	String getBody();
}
