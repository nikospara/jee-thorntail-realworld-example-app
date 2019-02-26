package realworld.comments.model;

import java.io.Serializable;
import java.util.Date;

import realworld.user.model.ProfileData;

/**
 * Comment full data.
 *
 * @see <a href="https://github.com/gothinkster/realworld/tree/master/api#single-comment" target="_top">Specification</a>
 */
public interface CommentData extends CommentBase {

	/**
	 * The profile of the author.
	 *
	 * @return The author
	 */
	ProfileData getAuthor();

	static CommentData make(CommentBase commentBase, ProfileData author) {
		return new Impl(commentBase.getId(), commentBase.getBody(), commentBase.getCreatedAt(), commentBase.getUpdatedAt(), author);
	}

	/**
	 * Simple, immutable implementation of the {@link CommentData}.
	 */
	class Impl extends CommentBase.Impl implements CommentData, Serializable {

		private static final long serialVersionUID = 1L;

		private final ProfileData author;

		/**
		 * Full constructor.
		 *
		 * @param id             The id
		 * @param body
		 * @param createdAt
		 * @param updatedAt
		 * @param author         The author
		 */
		Impl(String id, String body, Date createdAt, Date updatedAt, ProfileData author) {
			super(id, body, createdAt, updatedAt);
			this.author = author;
		}

		@Override
		public ProfileData getAuthor() {
			return author;
		}
	}
}
