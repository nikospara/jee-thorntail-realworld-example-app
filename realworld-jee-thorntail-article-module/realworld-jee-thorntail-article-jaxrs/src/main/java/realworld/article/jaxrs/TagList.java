package realworld.article.jaxrs;


import java.util.ArrayList;
import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * A list of tags for serialization.
 */
@JsonTypeName("tags")
@JsonTypeInfo(include = JsonTypeInfo.As.WRAPPER_OBJECT ,use = JsonTypeInfo.Id.NAME)
public class TagList extends ArrayList<String> {

	private static final long serialVersionUID = 1L;

	/**
	 * Default constructor.
	 */
	public TagList() {
		super();
	}

	/**
	 * Copy the given collection.
	 *
	 * @param c The collection to copy
	 */
	public TagList(Collection<? extends String> c) {
		super(c);
	}
}
