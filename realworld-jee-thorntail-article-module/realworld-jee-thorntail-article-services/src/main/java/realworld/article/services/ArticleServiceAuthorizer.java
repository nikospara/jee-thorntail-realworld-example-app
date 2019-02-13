package realworld.article.services;

import static javax.interceptor.Interceptor.Priority.APPLICATION;

import javax.annotation.Priority;
import javax.decorator.Decorator;
import javax.decorator.Delegate;
import javax.inject.Inject;

import realworld.article.model.ArticleCreationData;
import realworld.article.model.ArticleData;
import realworld.authorization.Authorization;

/**
 * Security for the {@link ArticleService} implementation.
 */
@Decorator
@Priority(APPLICATION)
class ArticleServiceAuthorizer implements ArticleService {

	private ArticleService delegate;

	private Authorization authorization;

	/**
	 * Injection constructor.
	 *
	 * @param delegate      The delegate of this decorator
	 * @param authorization The authorization utilities
	 */
	@Inject
	public ArticleServiceAuthorizer(@Delegate ArticleService delegate, Authorization authorization) {
		this.delegate = delegate;
		this.authorization = authorization;
	}

	@Override
	public ArticleData findArticleBySlug(String slug) {
		return delegate.findArticleBySlug(slug);
	}

	@Override
	public ArticleData create(ArticleCreationData creationData) {
		authorization.requireLogin();
		return delegate.create(creationData);
	}

	@Override
	public String makeSlug(String title) {
		return delegate.makeSlug(title);
	}
}
