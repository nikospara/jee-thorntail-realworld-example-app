package realworld.article.services;

import realworld.EntityDoesNotExistException;
import realworld.article.model.ArticleData;
import realworld.article.model.ArticleCreationData;

/**
 * ArticleData services.
 */
public interface ArticleService {

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
}
