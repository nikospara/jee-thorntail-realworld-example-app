package realworld.jaxrs.impl.article;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Context;

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

	@Context
	private HttpServletResponse response;

	@Override
	public ArticleResult<ArticleData> feed(Integer limit, Integer offset) {
		response.setHeader("X-Realworld-API", "THIS RESPONSE VIOLATES THE REALWORLD API");
		return articleService.feed(ArticleSearchCriteria.builder()
				.withLimit(limit)
				.withOffset(offset)
				.build()
		);
	}
}
