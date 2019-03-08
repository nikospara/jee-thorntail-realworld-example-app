package realworld.comments.jaxrs;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonRootName;
import realworld.comments.model.CommentCreationData;

/**
 * Comment creation data implementation.
 */
@JsonRootName("comment")
public class CommentCreationParam implements CommentCreationData, Serializable {

	private static final long serialVersionUID = 1L;

	private String body;

	@Override
	public String getBody() {
		return body;
	}

	/**
	 * Set the body.
	 *
	 * @param body The body
	 */
	public void setBody(String body) {
		this.body = body;
	}
}
