package realworld.jaxrs.impl.article;

import com.fasterxml.jackson.annotation.JsonRootName;

/**
 * Customize the serialization of {@link realworld.article.model.ArticleData}.
 */
@JsonRootName("article")
public class ArticleMixin {
}
