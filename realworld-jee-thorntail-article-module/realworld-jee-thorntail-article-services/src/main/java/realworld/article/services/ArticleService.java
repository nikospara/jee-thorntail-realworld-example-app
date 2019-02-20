package realworld.article.services;

import realworld.EntityDoesNotExistException;
import realworld.article.model.ArticleData;
import realworld.article.model.ArticleCreationData;
import realworld.article.model.ArticleResult;

/**
 * Article services.
 */
public interface ArticleService {

	/**
	 * Find articles by criteria.
	 *
	 * @param criteria The search criteria
	 * @return The search results
	 */
	ArticleResult<ArticleData> find(ArticleSearchCriteria criteria);

	/**
	 * Find articles created by followed users, ordered by most recent first.
	 *
	 * @param criteria The search criteria
	 * @return The search results
	 */
	ArticleResult<ArticleData> feed(ArticleSearchCriteria criteria);

	/**
	 * Find article by slug.
	 *
	 * @param slug The slug
	 * @return The article
	 * @throws EntityDoesNotExistException If an article with the given slug does not exist
	 */
	ArticleData findArticleBySlug(String slug) throws EntityDoesNotExistException;

	/**
	 * Create an article.
	 *
	 * @param creationData ArticleData creation data
	 * @return The new article
	 */
	ArticleData create(ArticleCreationData creationData);

	/**
	 * Transform a title to a slug.
	 *
	 * @param title The title to transform
	 * @return The slug
	 */
	String makeSlug(String title);

	/**
	 * Favorite article.
	 *
	 * @param slug Article slug
	 * @return The article
	 * @throws EntityDoesNotExistException If the article does not exist
	 */
	ArticleData favorite(String slug) throws EntityDoesNotExistException;

	/**
	 * Remove article from favorites.
	 *
	 * @param slug Article slug
	 * @return The article
	 * @throws EntityDoesNotExistException If the article does not exist
	 */
	ArticleData unfavorite(String slug) throws EntityDoesNotExistException;
}
