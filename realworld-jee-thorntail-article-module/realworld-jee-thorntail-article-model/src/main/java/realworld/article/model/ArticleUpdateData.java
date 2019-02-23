package realworld.article.model;

import javax.validation.constraints.Pattern;
import java.util.Set;

/**
 * Data required for updating an article.
 *
 * @see <a href="https://github.com/gothinkster/realworld/tree/master/api#update-article" target="_top">Specification</a>
 */
public interface ArticleUpdateData {

	/**
	 * Used by {@link ArticleUpdateData#isExplicitlySet(PropName)} to specify properties
	 * of a {@code ArticleUpdateData} object that have been changed.
	 */
	enum PropName {
		TITLE,
		DESCRIPTION,
		BODY,
		TAGS
	}

	/**
	 * The title.
	 *
	 * @return The title
	 */
	String getTitle();

	/**
	 * The description.
	 *
	 * @return The description
	 */
	String getDescription();

	/**
	 * The body.
	 *
	 * @return The body
	 */
	String getBody();

	/**
	 * The tags.
	 *
	 * @return The tags
	 */
	Set<String> getTags();

	/**
	 * Check if the given property is explicitly set. If not its value should be disregarded.
	 *
	 * @param prop The property to check
	 * @return Whether the property is explicitly set and should not be disregarded
	 */
	boolean isExplicitlySet(PropName prop);
}
