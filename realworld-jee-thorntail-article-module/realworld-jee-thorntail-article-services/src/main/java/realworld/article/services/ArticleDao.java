package realworld.article.services;

import java.util.Date;

import realworld.EntityDoesNotExistException;
import realworld.article.model.ArticleCreationData;
import realworld.article.model.ArticleWithLinks;

/**
 * DAO interface for the ArticleData entity.
 */
public interface ArticleDao {
	/**
	 * Find an article plus accompanying statistics by slug.
	 * 
	 * @param userId The id of the current user (to check favorite)
	 * @param slug The slug to search
	 * @return The article
	 * @throws EntityDoesNotExistException If the article does not exist
	 */
	ArticleWithLinks findArticleBySlug(String userId, String slug) throws EntityDoesNotExistException;

	/**
	 * Create a new article.
	 *
	 * @param creationData
	 * @param slug         The slug to use
	 * @param creationDate The creation date
	 * @param authorId     The author id
	 * @return
	 */
	ArticleWithLinks create(ArticleCreationData creationData, String slug, Date creationDate, String authorId);
}
