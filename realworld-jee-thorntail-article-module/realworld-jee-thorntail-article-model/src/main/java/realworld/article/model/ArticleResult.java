package realworld.article.model;

import java.util.List;

/**
 * An article search result.
 *
 * @param <A> The type of article class
 */
public class ArticleResult<A extends ArticleBase> {

	private List<A> articles;
	private Long articlesCount;

	/**
	 * Default constructor.
	 */
	public ArticleResult() {
		// NOOP
	}

	/**
	 * Full constructor.
	 *
	 * @param articles      The results
	 * @param articlesCount The count
	 */
	public ArticleResult(List<A> articles, Long articlesCount) {
		this.articles = articles;
		this.articlesCount = articlesCount;
	}

	/**
	 * Get the page of results.
	 *
	 * @return A list of articles
	 */
	public List<A> getArticles() {
		return articles;
	}

	/**
	 * Set the page of results.
	 *
	 * @param articles The page of results
	 */
	public void setArticles(List<A> articles) {
		this.articles = articles;
	}

	/**
	 * Get the toal count of this query.
	 *
	 * @return The count
	 */
	public Long getArticlesCount() {
		return articlesCount;
	}

	/**
	 * Set the total count for this query.
	 *
	 * @param articlesCount The count
	 */
	public void setArticlesCount(Long articlesCount) {
		this.articlesCount = articlesCount;
	}
}
