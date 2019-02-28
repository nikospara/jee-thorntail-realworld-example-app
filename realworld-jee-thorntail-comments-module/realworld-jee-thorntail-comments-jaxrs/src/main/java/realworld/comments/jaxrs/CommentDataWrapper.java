package realworld.comments.jaxrs;

import java.io.Serializable;

import realworld.comments.model.CommentData;

/**
 * Wrap an {@link CommentData} instance.
 * <p>
 * This class is necessary because of the unfortunate choice of the Realworld API
 * to wrap every object in a root, except for the {@code realworld.article.model.ArticleResult},
 * which is serialized as is.
 * Using {@code @JsonRootName} and {@code objectMapper.enable(SerializationFeature.WRAP_ROOT_VALUE)}
 * does not work, because it cannot make the exception for the {@code ArticleResult}.
 * Thus we are forced to use {@code @JsonTypeName}+{@code @JsonTypeInfo}.
 * If we annotate the {@code ProfileData} mixin however,
 * classes like the {@code CommentsList} that embed it are serialized as nested objects,
 * which violates the Realworld API.
 * <p>
 * We choose to serialize the {@code CommentData} as is and manually wrap it in this class,
 * when returned directly.
 */
public class CommentDataWrapper implements Serializable {

	private static final long serialVersionUID = 1L;

	private CommentData comment;

	/**
	 * Wrap a comment.
	 *
	 * @param comment The comment
	 */
	public CommentDataWrapper(CommentData comment) {
		this.comment = comment;
	}

	/**
	 * Get the wrapped comment.
	 *
	 * @return The wrapped comment
	 */
	public CommentData getComment() {
		return comment;
	}
}
