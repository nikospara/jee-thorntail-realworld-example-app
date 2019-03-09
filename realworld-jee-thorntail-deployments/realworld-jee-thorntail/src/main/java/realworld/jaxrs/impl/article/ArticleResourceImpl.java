package realworld.jaxrs.impl.article;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import realworld.article.jaxrs.ArticleDataWrapper;
import realworld.article.jaxrs.ArticleResource;
import realworld.article.jaxrs.CreationParam;
import realworld.article.jaxrs.UpdateParam;
import realworld.article.model.ArticleData;
import realworld.article.model.ArticleResult;
import realworld.article.services.ArticleSearchCriteria;
import realworld.article.services.ArticleService;
import realworld.comments.jaxrs.CommentCreationParam;
import realworld.comments.jaxrs.CommentDataWrapper;
import realworld.comments.jaxrs.CommentsList;

/**
 * Article operations implementation.
 */
@ApplicationScoped
public class ArticleResourceImpl implements ArticleResource {

	@Inject
	private ArticleService articleService;

	@Override
	public ArticleResult<ArticleData> find(String tag, String author, String favoritedBy, Integer limit, Integer offset) {
		return articleService.find(ArticleSearchCriteria.builder()
				.withTag(tag)
				.withAuthor(author)
				.favoritedBy(favoritedBy)
				.withLimit(limit)
				.withOffset(offset)
				.build()
		);
	}

	@Override
	public ArticleDataWrapper get(String slug) {
		return new ArticleDataWrapper(articleService.findArticleBySlug(slug));
	}

	@Override
	public ArticleDataWrapper create(CreationParam param) {
		return new ArticleDataWrapper(articleService.create(param));
	}

	@Override
	public ArticleDataWrapper update(String slug, UpdateParam updateParam) {
		return new ArticleDataWrapper(articleService.update(slug, updateParam));
	}

	@Override
	public void delete(String slug) {
		articleService.delete(slug);
	}

	@Override
	public ArticleDataWrapper favorite(String slug) {
		return new ArticleDataWrapper(articleService.favorite(slug));
	}

	@Override
	public ArticleDataWrapper unfavorite(String slug) {
		return new ArticleDataWrapper(articleService.unfavorite(slug));
	}

	@Override
	public CommentsList getComments(String slug) {
		return new CommentsList(articleService.findArticleComments(slug));
	}

	@Override
	public CommentDataWrapper comment(String slug, CommentCreationParam param) {
		return new CommentDataWrapper(articleService.comment(slug, param));
	}

	@Override
	public void delete(String slug, String commentId) {
		articleService.deleteArticleComment(slug, commentId);
	}
}
