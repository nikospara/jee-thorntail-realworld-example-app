package realworld.article.services;

import static javax.interceptor.Interceptor.Priority.APPLICATION;

import javax.annotation.Priority;
import javax.decorator.Decorator;
import javax.decorator.Delegate;
import javax.inject.Inject;
import java.util.List;

import realworld.article.model.ArticleCreationData;
import realworld.article.model.ArticleData;
import realworld.article.model.ArticleResult;
import realworld.article.model.ArticleUpdateData;
import realworld.authorization.Authorization;
import realworld.comments.model.CommentCreationData;
import realworld.comments.model.CommentData;

/**
 * Security for the {@link ArticleService} implementation.
 */
@Decorator
@Priority(APPLICATION)
class ArticleServiceAuthorizer implements ArticleService {

	private ArticleService delegate;

	private Authorization authorization;

	private ArticleAuthorization articleAuthorization;

	/**
	 * Injection constructor.
	 *
	 * @param delegate              The delegate of this decorator
	 * @param authorization         The authorization utilities
	 * @param articleAuthorization  The article authorization
	 */
	@Inject
	public ArticleServiceAuthorizer(@Delegate ArticleService delegate, Authorization authorization, ArticleAuthorization articleAuthorization) {
		this.delegate = delegate;
		this.authorization = authorization;
		this.articleAuthorization = articleAuthorization;
	}

	@Override
	public ArticleResult<ArticleData> find(ArticleSearchCriteria criteria) {
		return delegate.find(criteria);
	}

	@Override
	public ArticleResult<ArticleData> feed(ArticleSearchCriteria criteria) {
		authorization.requireLogin();
		return delegate.feed(criteria);
	}

	@Override
	public ArticleData findArticleBySlug(String slug) {
		return delegate.findArticleBySlug(slug);
	}

	@Override
	public String findArticleIdBySlug(String slug) {
		return delegate.findArticleIdBySlug(slug);
	}

	@Override
	public ArticleData create(ArticleCreationData creationData) {
		authorization.requireLogin();
		return delegate.create(creationData);
	}

	@Override
	public ArticleData update(String slug, ArticleUpdateData updateData) {
		articleAuthorization.requireCurrentUserToBeAuthorOf(slug);
		return delegate.update(slug, updateData);
	}

	@Override
	public void delete(String slug) {
		articleAuthorization.requireCurrentUserToBeAuthorOf(slug);
		delegate.delete(slug);
	}

	@Override
	public String makeSlug(String title) {
		return delegate.makeSlug(title);
	}

	@Override
	public ArticleData favorite(String slug) {
		authorization.requireLogin();
		return delegate.favorite(slug);
	}

	@Override
	public ArticleData unfavorite(String slug) {
		authorization.requireLogin();
		return delegate.unfavorite(slug);
	}

	@Override
	public CommentData comment(String articleSlug, CommentCreationData creationData) {
		authorization.requireLogin();
		return delegate.comment(articleSlug, creationData);
	}

	@Override
	public List<CommentData> findArticleComments(String slug) {
		return delegate.findArticleComments(slug);
	}

	@Override
	public void deleteArticleComment(String slug, String commentId) {
		delegate.deleteArticleComment(slug, commentId);
	}
}
