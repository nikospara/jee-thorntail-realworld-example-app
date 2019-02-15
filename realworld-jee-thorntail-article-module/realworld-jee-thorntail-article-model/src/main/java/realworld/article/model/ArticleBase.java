package realworld.article.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Base article, containing only its own fields, not related entities.
 */
public interface ArticleBase {

	/**
	 * The id.
	 *
	 * @return The id.
	 */
	String getId();

	/**
	 * The slug.
	 *
	 * @return The slug
	 */
	String getSlug();

	/**
	 * The title.
	 *
	 * @return The title
	 */
	String getTitle();

	/**
	 * The description.
	 *
	 * @return The description
	 */
	String getDescription();

	/**
	 * The body.
	 *
	 * @return The body
	 */
	String getBody();

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
	 * Whether the current user has favorited this article.
	 *
	 * @return {@code true} if favorited by the current user
	 */
	boolean isFavorited();

	/**
	 * The favorites count.
	 *
	 * @return The favorites count
	 */
	int getFavoritesCount();

	/**
	 * Simple, immutable implementation of the {@link ArticleBase}.
	 */
	class Impl implements ArticleBase, Serializable {

		private static final long serialVersionUID = 1L;

		private String id;
		private String slug;
		private String title;
		private String description;
		private String body;
		private Date createdAt;
		private Date updatedAt;
		private boolean favorited;
		private int favoritesCount;

		/**
		 * Full constructor.
		 *
		 * @param id             The id
		 * @param slug           The slug
		 * @param title
		 * @param description
		 * @param body
		 * @param createdAt
		 * @param updatedAt
		 * @param favorited
		 * @param favoritesCount
		 */
		Impl(String id, String slug, String title, String description, String body, Date createdAt, Date updatedAt, boolean favorited, int favoritesCount) {
			this.id = id;
			this.slug = slug;
			this.title = title;
			this.description = description;
			this.body = body;
			this.createdAt = createdAt;
			this.updatedAt = updatedAt;
			this.favorited = favorited;
			this.favoritesCount = favoritesCount;
		}

		@Override
		public String getId() {
			return id;
		}

		@Override
		public String getSlug() {
			return slug;
		}

		@Override
		public String getTitle() {
			return title;
		}

		@Override
		public String getDescription() {
			return description;
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

		@Override
		public boolean isFavorited() {
			return favorited;
		}

		@Override
		public int getFavoritesCount() {
			return favoritesCount;
		}
	}
}
