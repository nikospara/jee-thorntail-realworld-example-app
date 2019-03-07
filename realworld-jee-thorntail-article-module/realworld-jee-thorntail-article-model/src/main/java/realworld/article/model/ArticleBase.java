package realworld.article.model;

import java.time.LocalDateTime;

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
	LocalDateTime getCreatedAt();

	/**
	 * The last modification date.
	 *
	 * @return The last modification date
	 */
	LocalDateTime getUpdatedAt();

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
}
