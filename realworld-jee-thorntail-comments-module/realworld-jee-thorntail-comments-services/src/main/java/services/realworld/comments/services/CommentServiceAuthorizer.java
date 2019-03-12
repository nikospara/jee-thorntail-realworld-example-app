package services.realworld.comments.services;

import static javax.interceptor.Interceptor.Priority.APPLICATION;

import javax.annotation.Priority;
import javax.decorator.Decorator;
import javax.decorator.Delegate;
import javax.inject.Inject;
import java.util.List;

import realworld.authorization.Authorization;
import realworld.comments.model.CommentCreationData;
import realworld.comments.model.CommentData;

/**
 * Security for the {@link CommentService} implementation.
 */
@Decorator
@Priority(APPLICATION)
public class CommentServiceAuthorizer implements CommentService {

	private CommentService delegate;

	private Authorization authorization;

	private CommentAuthorization commentAuthorization;

	/**
	 * Injection constructor.
	 *
	 * @param delegate              The delegate of this decorator
	 * @param authorization         The authorization utilities
	 * @param commentAuthorization  The comment authorization utilities
	 */
	@Inject
	public CommentServiceAuthorizer(@Delegate CommentService delegate, Authorization authorization, CommentAuthorization commentAuthorization) {
		this.delegate = delegate;
		this.authorization = authorization;
		this.commentAuthorization = commentAuthorization;
	}

	@Override
	public CommentData create(CommentCreationData creationData) {
		authorization.requireLogin();
		return delegate.create(creationData);
	}

	@Override
	public void delete(String id) {
		commentAuthorization.requireCurrentUserToBeAuthorOf(id);
		delegate.delete(id);
	}

	@Override
	public List<CommentData> findCommentsWithIds(List<String> ids) {
		return delegate.findCommentsWithIds(ids);
	}
}
