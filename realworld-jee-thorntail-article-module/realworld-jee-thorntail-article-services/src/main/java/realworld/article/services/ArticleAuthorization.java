package realworld.article.services;

import realworld.authorization.AppSecurityException;

/**
 * Article module-specific methods for authorizing the access to article services.
 */
public interface ArticleAuthorization {

	/**
	 * Require that the current user is authenticated and has authored the article with the given slug.
	 *
	 * @param slug The slug
	 * @throws AppSecurityException If the check fails
	 */
	void requireCurrentUserToBeAuthorOf(String slug) throws AppSecurityException;
}
