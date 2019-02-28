package services.realworld.comments.services;

import java.time.LocalDateTime;
import java.util.List;

import realworld.EntityDoesNotExistException;
import realworld.comments.model.CommentCreationData;
import realworld.comments.model.CommentWithLinks;

/**
 * DAO for the comment entity.
 */
public interface CommentDao {

	/**
	 * Add a comment to an article.
	 *
	 * @param creationData The comment creation data
	 * @param articleId    The article id
	 * @param authorId     The author id
	 * @param createdAt    The creation timestamp
	 * @return The new comment
	 */
	CommentWithLinks add(CommentCreationData creationData, String articleId, String authorId, LocalDateTime createdAt) throws EntityDoesNotExistException;

	/**
	 * Fetch the comments of an article.
	 *
	 * @param articleId  The id of the article
	 * @return The list of comment data
	 * @throws EntityDoesNotExistException If an article with the given slug does not exist
	 */
	List<CommentWithLinks> findArticleComments(String articleId) throws EntityDoesNotExistException;

	/**
	 * Delete the comment with the given id.
	 *
	 * @param id The id
	 */
	void delete(String id);

	/**
	 * Check that the comment having the given id is written by the given user.
	 *
	 * @param id       The comment id
	 * @param userId   The user id
	 * @return
	 */
	boolean checkAuthor(String id, String userId);
}
