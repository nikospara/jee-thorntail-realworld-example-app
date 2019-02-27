package realworld.jaxrs.impl.article;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * Customize the serialization of {@link realworld.article.model.ArticleData}.
 */
@JsonTypeName("article")
@JsonTypeInfo(include = JsonTypeInfo.As.WRAPPER_OBJECT ,use = JsonTypeInfo.Id.NAME)
public interface ArticleMixin {
	@SuppressWarnings("unused")
	@JsonProperty("tagList")
	Set<String> getTags();
}
