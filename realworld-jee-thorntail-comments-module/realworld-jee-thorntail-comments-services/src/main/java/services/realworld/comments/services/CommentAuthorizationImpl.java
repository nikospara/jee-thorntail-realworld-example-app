package services.realworld.comments.services;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import realworld.authentication.AuthenticationContext;
import realworld.authorization.NotAuthenticatedException;
import realworld.authorization.NotAuthorizedException;

/**
 * Implementation of {@link CommentAuthorization}.
 */
@ApplicationScoped
class CommentAuthorizationImpl implements CommentAuthorization {

	private AuthenticationContext authenticationContext;

	private CommentDao commentDao;

	/**
	 * Default constructor for frameworks.
	 */
	CommentAuthorizationImpl() {
		// NOOP
	}

	/**
	 * Injection constructor.
	 *
	 * @param authenticationContext The authentication context
	 * @param commentDao            The comment DAO
	 */
	@Inject
	public CommentAuthorizationImpl(AuthenticationContext authenticationContext, CommentDao commentDao) {
		this.authenticationContext = authenticationContext;
		this.commentDao = commentDao;
	}

	@Override
	public void requireCurrentUserToBeAuthorOf(String id) {
		if( authenticationContext.getUserPrincipal() == null ) {
			throw new NotAuthenticatedException();
		}
		if( !commentDao.checkAuthor(id, authenticationContext.getUserPrincipal().getUniqueId()) ) {
			throw new NotAuthorizedException();
		}
	}
}
