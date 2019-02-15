package realworld.article.model;

import java.io.Serializable;
import java.util.Date;

import realworld.user.model.ProfileData;

/**
 * Article full data.
 *
 * @see <a href="https://github.com/gothinkster/realworld/tree/master/api#single-article" target="_top">Specification</a>
 */
public interface ArticleData extends ArticleBase {

	/**
	 * The profile of the author.
	 *
	 * @return The author
	 */
	ProfileData getAuthor();

	static ArticleData make(String id, String slug, String title, String description, String body, Date createdAt, Date updatedAt, boolean favorited, int favoritesCount, ProfileData author) {
		return new Impl(id, slug, title, description, body, createdAt, updatedAt, favorited, favoritesCount, author);
	}

	static ArticleData make(ArticleBase base, ProfileData author) {
		return new Impl(base.getId(), base.getSlug(), base.getTitle(), base.getDescription(), base.getBody(), base.getCreatedAt(), base.getUpdatedAt(), base.isFavorited(), base.getFavoritesCount(), author);
	}

	static ArticleData justFavorited(ArticleBase base, ProfileData author) {
		return new Impl(base.getId(), base.getSlug(), base.getTitle(), base.getDescription(), base.getBody(), base.getCreatedAt(), base.getUpdatedAt(), true, base.getFavoritesCount()+1, author);
	}

	static ArticleData justUnfavorited(ArticleBase base, ProfileData author) {
		return new Impl(base.getId(), base.getSlug(), base.getTitle(), base.getDescription(), base.getBody(), base.getCreatedAt(), base.getUpdatedAt(), false, base.getFavoritesCount()-1, author);
	}

	/**
	 * Simple, immutable implementation of the {@link ArticleData}.
	 */
	class Impl extends ArticleBase.Impl implements ArticleData, Serializable {

		private static final long serialVersionUID = 1L;

		private ProfileData author;

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
		 * @param author
		 */
		Impl(String id, String slug, String title, String description, String body, Date createdAt, Date updatedAt, boolean favorited, int favoritesCount, ProfileData author) {
			super(id, slug, title, description, body, createdAt, updatedAt, favorited, favoritesCount);
			this.author = author;
		}

		@Override
		public ProfileData getAuthor() {
			return author;
		}
	}
}
