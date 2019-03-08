package realworld.article.services;

import java.util.List;

import realworld.EntityDoesNotExistException;
import realworld.article.model.ArticleData;
import realworld.article.model.ArticleCreationData;
import realworld.article.model.ArticleResult;
import realworld.article.model.ArticleUpdateData;
import realworld.comments.model.CommentCreationData;
import realworld.comments.model.CommentData;

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
	 * Find article id by slug.
	 *
	 * @param slug The slug
	 * @return The article id
	 * @throws EntityDoesNotExistException If an article with the given slug does not exist
	 */
	String findArticleIdBySlug(String slug) throws EntityDoesNotExistException;

	/**
	 * Create an article.
	 *
	 * @param creationData ArticleData creation data
	 * @return The new article
	 */
	ArticleData create(ArticleCreationData creationData);

	/**
	 * Update the article identified by the given slug.
	 *
	 * @param slug       The slug
	 * @param updateData The data to update
	 * @return The full article
	 * @throws EntityDoesNotExistException If an article with the given slug does not exist
	 */
	ArticleData update(String slug, ArticleUpdateData updateData) throws EntityDoesNotExistException;

	/**
	 * Delete the article.
	 *
	 * @param slug The slug
	 * @throws EntityDoesNotExistException If the article does not exist
	 */
	void delete(String slug) throws EntityDoesNotExistException;

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

	/**
	 * Add a comment to an article as the current user.
	 *
	 * @param slug         The slug of the article
	 * @param creationData The comment creation data
	 * @return The full comment data
	 * @throws EntityDoesNotExistException If an article with the given slug does not exist
	 */
	CommentData comment(String slug, CommentCreationData creationData) throws EntityDoesNotExistException;

	/**
	 * Fetch the comments of an article.
	 *
	 * @param slug  The slug of the article
	 * @return The list of full comment data
	 * @throws EntityDoesNotExistException If an article with the given slug does not exist
	 */
	List<CommentData> findArticleComments(String slug) throws EntityDoesNotExistException;

	/**
	 * Delete the comment from the given article.
	 *
	 * @param slug      Article slug
	 * @param commentId Comment id
	 */
	void deleteArticleComment(String slug, String commentId);
}
