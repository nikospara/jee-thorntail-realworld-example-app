package realworld.article.jaxrs;

import java.util.ArrayList;
import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonRootName;
import realworld.article.model.ArticleData;

/**
 * A list of articles for serialization.
 */
@JsonRootName("articles")
public class ArticleList extends ArrayList<ArticleData> {

	/**
	 * Default constructor.
	 */
	public ArticleList() {
	}

	/**
	 * Copy the given collection.
	 *
	 * @param c The collection to copy
	 */
	public ArticleList(Collection<? extends ArticleData> c) {
		super(c);
	}
}
