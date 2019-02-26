package realworld.comments.model;

/**
 * Comment creation data.
 *
 * @see <a href="https://github.com/gothinkster/realworld/tree/master/api#add-comments-to-an-article" target="_top">Specification</a>
 */
public interface CommentCreationData {

	/**
	 * The comment content.
	 *
	 * @return The comment content
	 */
	String getBody();
}
