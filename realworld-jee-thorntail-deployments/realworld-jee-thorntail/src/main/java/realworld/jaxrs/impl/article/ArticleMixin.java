package realworld.jaxrs.impl.article;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Customize the serialization of {@link realworld.article.model.ArticleData}.
 */
public interface ArticleMixin {
	@SuppressWarnings("unused")
	@JsonProperty("tagList")
	Set<String> getTags();
}
