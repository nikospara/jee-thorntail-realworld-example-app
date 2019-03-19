package services.realworld.comments.services;

import realworld.authorization.AppSecurityException;

/**
 * Comment module-specific methods for authorizing the access to comment services.
 */
public interface CommentAuthorization {

	/**
	 * Require that the current user is authenticated and has authored the comment with the given id.
	 *
	 * @param id The comment id
	 * @throws AppSecurityException If the check fails
	 */
	void requireCurrentUserToBeAuthorOf(String id);
}
