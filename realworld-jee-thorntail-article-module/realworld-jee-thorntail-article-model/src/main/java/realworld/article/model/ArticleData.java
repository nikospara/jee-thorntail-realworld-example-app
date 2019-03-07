package realworld.article.model;

import java.util.Set;

import org.immutables.value.Value;
import realworld.user.model.ProfileData;

/**
 * Article full data.
 *
 * @see <a href="https://github.com/gothinkster/realworld/tree/master/api#single-article" target="_top">Specification</a>
 */
@Value.Immutable
public interface ArticleData extends ArticleBase {

	/**
	 * The profile of the author.
	 *
	 * @return The author
	 */
	ProfileData getAuthor();

	/**
	 * The tags.
	 *
	 * @return The tags
	 */
	Set<String> getTags();
}
