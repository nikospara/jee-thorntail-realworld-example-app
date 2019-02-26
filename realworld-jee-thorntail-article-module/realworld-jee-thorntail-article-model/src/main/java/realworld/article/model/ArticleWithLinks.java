package realworld.article.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Article that has links to its related entities, i.e. contains only their ids.
 */
public interface ArticleWithLinks extends ArticleBase {

	/**
	 * The author id.
	 *
	 * @return The author id
	 */
	String getAuthorId();

	static ArticleWithLinks make(String id, String slug, String title, String description, String body, Date createdAt, Date updatedAt, boolean favorited, int favoritesCount, String authorId) {
		return new Impl(id, slug, title, description, body, createdAt, updatedAt, favorited, favoritesCount, authorId);
	}

	/**
	 * Simple, immutable implementation of the {@link ArticleWithLinks}.
	 */
	class Impl extends ArticleBase.Impl implements ArticleWithLinks, Serializable {

		private static final long serialVersionUID = 1L;

		private final String authorId;

		/**
		 * Full constructor.
		 *
		 * @param id             The id
		 * @param slug
		 * @param title
		 * @param description
		 * @param body
		 * @param createdAt
		 * @param updatedAt
		 * @param favorited
		 * @param favoritesCount
		 * @param authorId
		 */
		Impl(String id, String slug, String title, String description, String body, Date createdAt, Date updatedAt, boolean favorited, int favoritesCount, String authorId) {
			super(id, slug, title, description, body, createdAt, updatedAt, favorited, favoritesCount);
			this.authorId = authorId;
		}

		@Override
		public String getAuthorId() {
			return authorId;
		}
	}
}
