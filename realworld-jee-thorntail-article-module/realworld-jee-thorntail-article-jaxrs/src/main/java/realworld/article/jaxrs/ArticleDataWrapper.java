package realworld.article.jaxrs;

import realworld.article.model.ArticleData;

/**
 * Wrap an {@link ArticleData} instance.
 * <p>
 * This class is necessary because of the unfortunate choice of the Realworld API
 * to wrap every object in a root, except for the {@link realworld.article.model.ArticleResult},
 * which is serialized as is.
 * Using {@code @JsonRootName} and {@code objectMapper.enable(SerializationFeature.WRAP_ROOT_VALUE)}
 * does not work, because it cannot make the exception for the {@code ArticleResult}.
 * Thus we are forced to use {@code @JsonTypeName}+{@code @JsonTypeInfo}.
 * If we annotate the {@code ArticleData} mixin however,
 * the {@code List<ArticleData>} in the {@code ArticleResult} is serialized as nested objects,
 * which violates the Realworld API.
 * <p>
 * We choose to serialize the {@code ArticleData} as is and manually wrap it in this class,
 * when returned directly.
 */
public class ArticleDataWrapper {

	private ArticleData article;

	/**
	 * Wrap an article.
	 *
	 * @param article The wrapped article
	 */
	public ArticleDataWrapper(ArticleData article) {
		this.article = article;
	}

	/**
	 * Get the wrapped article.
	 *
	 * @return The wrapped article
	 */
	public ArticleData getArticle() {
		return article;
	}
}
