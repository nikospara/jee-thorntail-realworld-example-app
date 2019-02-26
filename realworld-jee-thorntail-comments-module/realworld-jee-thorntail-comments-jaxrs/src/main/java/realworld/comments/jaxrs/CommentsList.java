package realworld.comments.jaxrs;

import java.util.ArrayList;
import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonRootName;
import realworld.comments.model.CommentData;

@JsonRootName("comments")
public class CommentsList extends ArrayList<CommentData> {

	/**
	 * Default constructor.
	 */
	public CommentsList() {
		super();
	}

	/**
	 * Copy the given collection.
	 *
	 * @param c The collection to copy
	 */
	public CommentsList(Collection<? extends CommentData> c) {
		super(c);
	}
}
