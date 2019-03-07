package realworld.comments.model;

import org.immutables.value.Value;
import realworld.user.model.ProfileData;

/**
 * Comment full data.
 *
 * @see <a href="https://github.com/gothinkster/realworld/tree/master/api#single-comment" target="_top">Specification</a>
 */
@Value.Immutable
public interface CommentData extends CommentBase {

	/**
	 * The profile of the author.
	 *
	 * @return The author
	 */
	ProfileData getAuthor();
}
