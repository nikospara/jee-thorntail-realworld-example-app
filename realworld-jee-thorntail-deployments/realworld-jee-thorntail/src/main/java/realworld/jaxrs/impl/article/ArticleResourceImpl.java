package realworld.jaxrs.impl.article;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import realworld.article.jaxrs.ArticleResource;
import realworld.article.jaxrs.CreationParam;
import realworld.article.jaxrs.UpdateParam;
import realworld.article.model.ArticleData;
import realworld.article.model.ArticleResult;
import realworld.article.services.ArticleSearchCriteria;
import realworld.article.services.ArticleService;

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
	public ArticleData get(String slug) {
		return articleService.findArticleBySlug(slug);
	}

	@Override
	public ArticleData create(CreationParam param) {
		return articleService.create(param);
	}

	@Override
	public ArticleData update(String slug, UpdateParam updateParam) {
		return articleService.update(slug, updateParam);
	}

	@Override
	public void delete(String slug) {
		articleService.delete(slug);
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
