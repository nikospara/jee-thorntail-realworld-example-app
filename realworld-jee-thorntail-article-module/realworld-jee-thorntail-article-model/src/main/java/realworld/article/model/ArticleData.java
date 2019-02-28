package realworld.article.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

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

	/**
	 * The tags.
	 *
	 * @return The tags
	 */
	Set<String> getTags();

	static ArticleData make(String id, String slug, String title, String description, String body, LocalDateTime createdAt, LocalDateTime updatedAt, boolean favorited, int favoritesCount, ProfileData author, Set<String> tags) {
		return new Impl(id, slug, title, description, body, createdAt, updatedAt, favorited, favoritesCount, author, tags);
	}

	static ArticleData make(ArticleBase base, ProfileData author, Set<String> tags) {
		return new Impl(base.getId(), base.getSlug(), base.getTitle(), base.getDescription(), base.getBody(), base.getCreatedAt(), base.getUpdatedAt(), base.isFavorited(), base.getFavoritesCount(), author, tags);
	}

	static ArticleData justFavorited(ArticleBase base, ProfileData author, Set<String> tags) {
		return new Impl(base.getId(), base.getSlug(), base.getTitle(), base.getDescription(), base.getBody(), base.getCreatedAt(), base.getUpdatedAt(), true, base.getFavoritesCount()+1, author, tags);
	}

	static ArticleData justUnfavorited(ArticleBase base, ProfileData author, Set<String> tags) {
		return new Impl(base.getId(), base.getSlug(), base.getTitle(), base.getDescription(), base.getBody(), base.getCreatedAt(), base.getUpdatedAt(), false, base.getFavoritesCount()-1, author, tags);
	}

	/**
	 * Simple, immutable implementation of the {@link ArticleData}.
	 */
	class Impl extends ArticleBase.Impl implements ArticleData, Serializable {

		private static final long serialVersionUID = 1L;

		private final ProfileData author;
		private final Set<String> tags;

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
		 * @param tags           The tags
		 */
		Impl(String id, String slug, String title, String description, String body, LocalDateTime createdAt, LocalDateTime updatedAt, boolean favorited, int favoritesCount, ProfileData author, Set<String> tags) {
			super(id, slug, title, description, body, createdAt, updatedAt, favorited, favoritesCount);
			this.author = author;
			this.tags = tags;
		}

		@Override
		public ProfileData getAuthor() {
			return author;
		}

		@Override
		public Set<String> getTags() {
			return tags;
		}
	}
}
