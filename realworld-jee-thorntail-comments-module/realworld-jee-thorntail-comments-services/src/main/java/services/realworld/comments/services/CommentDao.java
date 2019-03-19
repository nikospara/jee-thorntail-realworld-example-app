package services.realworld.comments.services;

import java.time.LocalDateTime;
import java.util.List;

import realworld.comments.model.CommentCreationData;
import realworld.comments.model.CommentWithLinks;

/**
 * DAO for the comment entity.
 */
public interface CommentDao {

	/**
	 * Create a comment.
	 *
	 * @param creationData The comment creation data
	 * @param authorId     The author id
	 * @param createdAt    The creation timestamp
	 * @return The new comment
	 */
	CommentWithLinks create(CommentCreationData creationData, String authorId, LocalDateTime createdAt);

	/**
	 * Find the comments with the given ids.
	 *
	 * @param ids The ids to look for
	 * @return A list of comment data
	 */
	List<CommentWithLinks> findCommentsWithIds(List<String> ids);

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
