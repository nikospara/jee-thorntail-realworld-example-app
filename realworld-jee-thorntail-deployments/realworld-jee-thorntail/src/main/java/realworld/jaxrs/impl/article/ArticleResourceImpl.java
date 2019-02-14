package realworld.jaxrs.impl.article;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import realworld.article.jaxrs.ArticleResource;
import realworld.article.jaxrs.CreationParam;
import realworld.article.model.ArticleData;
import realworld.article.services.ArticleService;

/**
 * Article operations implementation.
 */
@ApplicationScoped
public class ArticleResourceImpl implements ArticleResource {

	@Inject
	private ArticleService articleService;

	@Override
	public ArticleData get(String slug) {
		return articleService.findArticleBySlug(slug);
	}

	@Override
	public ArticleData create(CreationParam param) {
		return articleService.create(param);
	}

	@Override
	public ArticleData favorite(String slug) {
		return articleService.favorite(slug);
	}

	@Override
	public ArticleData unfavorite(String slug) {
		return articleService.unfavorite(slug);
	}
}
