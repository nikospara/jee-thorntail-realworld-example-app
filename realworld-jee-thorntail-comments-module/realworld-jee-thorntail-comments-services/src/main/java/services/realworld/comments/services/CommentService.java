package services.realworld.comments.services;

import java.util.List;

import realworld.EntityDoesNotExistException;
import realworld.comments.model.CommentCreationData;
import realworld.comments.model.CommentData;

/**
 * Comment services.
 */
public interface CommentService {

	/**
	 * Add a comment to an article as the current user.
	 *
	 * @param articleSlug  The slug of the article
	 * @param creationData The comment creation data
	 * @return The full comment data
	 * @throws EntityDoesNotExistException If an article with the given slug does not exist
	 */
	CommentData add(String articleSlug, CommentCreationData creationData) throws EntityDoesNotExistException;

	/**
	 * Fetch the comments of an article.
	 *
	 * @param articleSlug  The slug of the article
	 * @return The list of full comment data
	 * @throws EntityDoesNotExistException If an article with the given slug does not exist
	 */
	List<CommentData> findArticleComments(String articleSlug) throws EntityDoesNotExistException;

	/**
	 * Delete the comment with the given id.
	 *
	 * @param id The id
	 */
	void delete(String id);
}
