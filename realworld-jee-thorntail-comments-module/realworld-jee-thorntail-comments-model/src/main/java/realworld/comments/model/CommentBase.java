package realworld.comments.model;

import java.io.Serializable;
import java.util.Date;

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
	Date getCreatedAt();

	/**
	 * The last modification date.
	 *
	 * @return The last modification date
	 */
	Date getUpdatedAt();

	/**
	 * The body.
	 *
	 * @return The body
	 */
	String getBody();

	/**
	 * Simple, immutable implementation of the {@link CommentBase}.
	 */
	class Impl implements CommentBase, Serializable {

		private static final long serialVersionUID = 1L;

		private final String id;
		private final String body;
		private final Date createdAt;
		private final Date updatedAt;

		/**
		 * Full constructor.
		 *
		 * @param id             The id
		 * @param body
		 * @param createdAt
		 * @param updatedAt
		 */
		Impl(String id, String body, Date createdAt, Date updatedAt) {
			this.id = id;
			this.body = body;
			this.createdAt = createdAt;
			this.updatedAt = updatedAt;
		}

		@Override
		public String getId() {
			return id;
		}

		@Override
		public String getBody() {
			return body;
		}

		@Override
		public Date getCreatedAt() {
			return createdAt;
		}

		@Override
		public Date getUpdatedAt() {
			return updatedAt;
		}
	}
}
