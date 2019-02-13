package realworld.article.model;

/**
 * ArticleData creation data.
 *
 * @see <a href="https://github.com/gothinkster/realworld/tree/master/api#create-article">Specification</a>
 */
public interface ArticleCreationData {

	/**
	 * Title of the new article.
	 *
	 * @return The title
	 */
	String getTitle();

	/**
	 * Description of the new article.
	 *
	 * @return The description
	 */
	String getDescription();

	/**
	 * Body text of the new article.
	 *
	 * @return The body
	 */
	String getBody();
}
