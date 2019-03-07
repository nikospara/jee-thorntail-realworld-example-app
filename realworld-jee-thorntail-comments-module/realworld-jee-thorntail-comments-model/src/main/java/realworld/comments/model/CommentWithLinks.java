package realworld.comments.model;

import org.immutables.value.Value;

/**
 * Comment that has links to its related entities, i.e. contains only their ids.
 */
@Value.Immutable
public interface CommentWithLinks extends CommentBase {

	/**
	 * The author id.
	 *
	 * @return The author id
	 */
	String getAuthorId();
}
