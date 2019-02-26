package realworld.jaxrs.impl.article;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import realworld.article.jaxrs.ArticleFeedResource;
import realworld.article.model.ArticleData;
import realworld.article.model.ArticleResult;
import realworld.article.services.ArticleSearchCriteria;
import realworld.article.services.ArticleService;

/**
 * Article feed implementation.
 */
@ApplicationScoped
public class ArticleFeedResourceImpl implements ArticleFeedResource {

	@Inject
	private ArticleService articleService;

	@Override
	public ArticleResult<ArticleData> feed(Integer limit, Integer offset) {
		return articleService.feed(ArticleSearchCriteria.builder()
				.withLimit(limit)
				.withOffset(offset)
				.build()
		);
	}
}
