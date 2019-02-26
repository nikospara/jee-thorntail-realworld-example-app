package realworld.comments.jaxrs;

import java.util.ArrayList;
import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import realworld.comments.model.CommentData;

@JsonTypeName("comments")
@JsonTypeInfo(include = JsonTypeInfo.As.WRAPPER_OBJECT ,use = JsonTypeInfo.Id.NAME)
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
