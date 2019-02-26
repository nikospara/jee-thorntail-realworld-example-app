package realworld.article.jaxrs;

import java.io.Serializable;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonRootName;
import realworld.article.model.ArticleCreationData;

/**
 * Article creation data implementation.
 */
@JsonRootName("article")
public class CreationParam implements ArticleCreationData, Serializable {

	private static final long serialVersionUID = 1L;
	
	private String title;
	private String description;
	private String body;
	private Set<String> tags;
	
	@Override
	public String getTitle() {
		return title;
	}

	/**
	 * Set the title.
	 *
	 * @param title The title
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public String getDescription() {
		return description;
	}

	/**
	 * Set the description.
	 *
	 * @param description The description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String getBody() {
		return body;
	}

	/**
	 * Set the body of the article.
	 *
	 * @param body The body of the article
	 */
	public void setBody(String body) {
		this.body = body;
	}

	@Override
	public Set<String> getTags() {
		return tags;
	}

	/**
	 * Set the tags.
	 *
	 * @param tags The tags
	 */
	public void setTags(Set<String> tags) {
		this.tags = tags;
	}
}
