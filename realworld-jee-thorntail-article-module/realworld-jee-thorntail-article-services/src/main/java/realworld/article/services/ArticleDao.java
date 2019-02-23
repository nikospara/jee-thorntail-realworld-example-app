package realworld.article.services;

import java.util.Date;
import java.util.Set;

import realworld.EntityDoesNotExistException;
import realworld.article.model.ArticleCreationData;
import realworld.article.model.ArticleResult;
import realworld.article.model.ArticleWithLinks;

/**
 * DAO interface for the ArticleData entity.
 */
public interface ArticleDao {

	/**
	 * Search for articles.
	 *
	 * @param userId          The current user id, to calculate {@code favorited}
	 * @param criteria        The search criteria
	 * @param defaultCriteria Default values for the search criteria
	 * @return The results
	 */
	ArticleResult<ArticleWithLinks> find(String userId, ArticleSearchCriteria criteria, ArticleSearchCriteria defaultCriteria);

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
	 * Find the tags of the given article.
	 *
	 * @param articleId The article id
	 * @return The set of tags
	 * @throws EntityDoesNotExistException If the article does not exist
	 */
	Set<String> findTags(String articleId) throws EntityDoesNotExistException;

	/**
	 * Create a new article.
	 *
	 * @param creationData
	 * @param slug         The slug to use
	 * @param creationDate The creation date
	 * @param authorId     The author id
	 * @param tags         The tags
	 * @return
	 */
	ArticleWithLinks create(ArticleCreationData creationData, String slug, Date creationDate, String authorId, Set<String> tags);

	/**
	 * Update the article having the given id.
	 *
	 * @param id          The article id
	 * @param title       The new title
	 * @param slug        The new slug
	 * @param description The new description
	 * @param body        The new body
	 * @param tags        The new tags, set to {@code null} to leave unchanged!
	 * @param updatedAt   The time of update
	 * @throws EntityDoesNotExistException If the article does not exist
	 */
	void update(String id, String title, String slug, String description, String body, Set<String> tags, Date updatedAt) throws EntityDoesNotExistException;

	/**
	 * Delete the article with the given slug.
	 *
	 * @param slug The slug
	 * @throws EntityDoesNotExistException If the article does not exist
	 */
	void delete(String slug) throws EntityDoesNotExistException;

	/**
	 * Add a favorite entry for the user and article.
	 *
	 * @param userId      The user id
	 * @param articleId   Article id
	 * @throws EntityDoesNotExistException If the article does not exist
	 */
	void addFavorite(String userId, String articleId) throws EntityDoesNotExistException;

	/**
	 * Remove the favorite entry for the user and article, if any.
	 *
	 * @param userId      The user id
	 * @param articleId   Article id
	 * @throws EntityDoesNotExistException If the article does not exist
	 */
	void removeFavorite(String userId, String articleId) throws EntityDoesNotExistException;

	/**
	 * Check that the article with the given slug is written by the user with the given id.
	 *
	 * @param slug   Article slug
	 * @param userId Id of author
	 * @return {@code true}   If the given article is written by the given author
	 */
	boolean checkArticleAuthor(String slug, String userId);
}
