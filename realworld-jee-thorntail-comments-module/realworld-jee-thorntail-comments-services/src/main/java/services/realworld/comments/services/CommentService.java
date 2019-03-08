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
	 * Create a comment as the current user.
	 *
	 * @param creationData The comment creation data
	 * @return The full comment data
	 */
	CommentData create(CommentCreationData creationData);

	/**
	 * Delete the comment with the given id.
	 *
	 * @param id The id
	 */
	void delete(String id);

	/**
	 * Find the comments with the given ids.
	 *
	 * @param ids The ids to look for
	 * @return A list of comment data
	 */
	List<CommentData> findCommentsWithIds(List<String> ids);
}
