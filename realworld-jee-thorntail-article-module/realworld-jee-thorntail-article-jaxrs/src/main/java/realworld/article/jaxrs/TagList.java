package realworld.article.jaxrs;


import java.util.ArrayList;
import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonRootName;

/**
 * A list of tags for serialization.
 */
@JsonRootName("tags")
public class TagList extends ArrayList<String> {

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
