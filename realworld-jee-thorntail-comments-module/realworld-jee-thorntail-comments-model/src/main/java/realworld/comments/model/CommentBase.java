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

	/**
	 * Simple, immutable implementation of the {@link CommentBase}.
	 */
	class Impl implements CommentBase, Serializable {

		private static final long serialVersionUID = 1L;

		private final String id;
		private final String body;
		private final LocalDateTime createdAt;
		private final LocalDateTime updatedAt;

		/**
		 * Full constructor.
		 *
		 * @param id             The id
		 * @param body           The body
		 * @param createdAt      The creation datetime
		 * @param updatedAt      The last updated datetime
		 */
		Impl(String id, String body, LocalDateTime createdAt, LocalDateTime updatedAt) {
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
		public LocalDateTime getCreatedAt() {
			return createdAt;
		}

		@Override
		public LocalDateTime getUpdatedAt() {
			return updatedAt;
		}
	}
}
