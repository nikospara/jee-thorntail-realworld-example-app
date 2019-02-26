package realworld.comments.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Comment that has links to its related entities, i.e. contains only their ids.
 */
public interface CommentWithLinks extends CommentBase {

	/**
	 * The author id.
	 *
	 * @return The author id
	 */
	String getAuthorId();

	static CommentWithLinks make(String id, String body, Date createdAt, Date updatedAt, String authorId) {
		return new Impl(id, body, createdAt, updatedAt, authorId);
	}

	/**
	 * Simple, immutable implementation of the {@link CommentWithLinks}.
	 */
	class Impl extends CommentBase.Impl implements CommentWithLinks, Serializable {

		private static final long serialVersionUID = 1L;

		private final String authorId;

		/**
		 * Full constructor.
		 *
		 * @param id             The id
		 * @param body
		 * @param createdAt
		 * @param updatedAt
		 * @param authorId
		 */
		Impl(String id, String body, Date createdAt, Date updatedAt, String authorId) {
			super(id, body, createdAt, updatedAt);
			this.authorId = authorId;
		}

		@Override
		public String getAuthorId() {
			return authorId;
		}
	}
}
