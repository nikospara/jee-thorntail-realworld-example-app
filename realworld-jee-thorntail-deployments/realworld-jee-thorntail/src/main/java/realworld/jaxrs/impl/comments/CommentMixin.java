package realworld.jaxrs.impl.comments;

import com.fasterxml.jackson.annotation.JsonRootName;

/**
 * Customize the serialization of {@link realworld.comments.model.CommentData}.
 */
@JsonRootName("comment")
public class CommentMixin {
}
