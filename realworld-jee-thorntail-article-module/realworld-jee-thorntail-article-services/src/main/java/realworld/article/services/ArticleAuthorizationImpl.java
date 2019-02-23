package realworld.article.services;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import realworld.authentication.AuthenticationContext;
import realworld.authorization.AppSecurityException;
import realworld.authorization.NotAuthenticatedException;
import realworld.authorization.NotAuthorizedException;

/**
 * Implementation of the {@link ArticleAuthorization}.
 */
@ApplicationScoped
class ArticleAuthorizationImpl implements ArticleAuthorization {

	private AuthenticationContext authenticationContext;

	private ArticleDao articleDao;

	/**
	 * Default constructor for frameworks.
	 */
	ArticleAuthorizationImpl() {
		// NOOP
	}

	/**
	 * Injection constructor.
	 *
	 * @param authenticationContext The authentication context
	 * @param articleDao            The article DAO
	 */
	@Inject
	public ArticleAuthorizationImpl(AuthenticationContext authenticationContext, ArticleDao articleDao) {
		this.authenticationContext = authenticationContext;
		this.articleDao = articleDao;
	}

	@Override
	public void requireCurrentUserToBeAuthorOf(String slug) throws AppSecurityException {
		if( authenticationContext.getUserPrincipal() == null ) {
			throw new NotAuthenticatedException();
		}
		if( !articleDao.checkArticleAuthor(slug, authenticationContext.getUserPrincipal().getUniqueId()) ) {
			throw new NotAuthorizedException();
		}
	}
}
