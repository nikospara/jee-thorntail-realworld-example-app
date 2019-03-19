package realworld.article.services;

import java.time.LocalDateTime;
import java.util.List;
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
	ArticleWithLinks findArticleBySlug(String userId, String slug);

	/**
	 * Find an article id by slug.
	 *
	 * @param slug The slug to search
	 * @return The article id
	 * @throws EntityDoesNotExistException If the article does not exist
	 */
	String findArticleIdBySlug(String slug);

	/**
	 * Find the tags of the given article.
	 *
	 * @param articleId The article id
	 * @return The set of tags
	 * @throws EntityDoesNotExistException If the article does not exist
	 */
	Set<String> findTags(String articleId);

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
	ArticleWithLinks create(ArticleCreationData creationData, String slug, LocalDateTime creationDate, String authorId, Set<String> tags);

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
	void update(String id, String title, String slug, String description, String body, Set<String> tags, LocalDateTime updatedAt);

	/**
	 * Delete the article with the given slug.
	 *
	 * @param slug The slug
	 * @throws EntityDoesNotExistException If the article does not exist
	 */
	void delete(String slug);

	/**
	 * Add a favorite entry for the user and article.
	 *
	 * @param userId      The user id
	 * @param articleId   Article id
	 * @throws EntityDoesNotExistException If the article does not exist
	 */
	void addFavorite(String userId, String articleId);

	/**
	 * Remove the favorite entry for the user and article, if any.
	 *
	 * @param userId      The user id
	 * @param articleId   Article id
	 * @throws EntityDoesNotExistException If the article does not exist
	 */
	void removeFavorite(String userId, String articleId);

	/**
	 * Check that the article with the given slug is written by the user with the given id.
	 *
	 * @param slug   Article slug
	 * @param userId Id of author
	 * @return {@code true}   If the given article is written by the given author
	 */
	boolean checkArticleAuthor(String slug, String userId);

	/**
	 * Link the comment with the given id to this article.
	 *
	 * @param articleId        The article id
	 * @param commentId        The comment id
	 * @param commentCreatedAt The comment creation timestamp
	 * @throws EntityDoesNotExistException If the article does not exist
	 */
	void comment(String articleId, String commentId, LocalDateTime commentCreatedAt);

	/**
	 * Find the ids of comments attached to the given article.
	 *
	 * @param articleId The article id
	 * @return The list of comment ids
	 * @throws EntityDoesNotExistException If the article does not exist
	 */
	List<String> findCommentIds(String articleId);

	/**
	 * Delete the comment from the given article.
	 *
	 * @param slug      Article slug
	 * @param commentId Comment id
	 */
	void deleteComment(String slug, String commentId);
}
