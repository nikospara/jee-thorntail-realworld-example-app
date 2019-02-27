package realworld.article.jaxrs;

import java.io.Serializable;
import java.util.EnumSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import realworld.article.model.ArticleUpdateData;

/**
 * Information for updating an article.
 *
 * @see <a href="https://github.com/gothinkster/realworld/tree/master/api#update-article" target="_top">Specification</a>
 */
@JsonRootName("article")
public class UpdateParam implements Serializable, ArticleUpdateData {

	private static final long serialVersionUID = 1L;

	private String title;
	private String description;
	private String body;
	@JsonProperty("tagList")
	private Set<String> tags;
	private EnumSet<PropName> explicitlySetProps = EnumSet.noneOf(PropName.class);

	@Override
	public String getTitle() {
		return title;
	}

	/**
	 * Set the title of the article.
	 *
	 * @param title The article title
	 */
	public void setTitle(String title) {
		explicitlySetProps.add(PropName.TITLE);
		this.title = title;
	}

	@Override
	public String getDescription() {
		return description;
	}

	/**
	 * Set the description of the article.
	 *
	 * @param description The article description
	 */
	public void setDescription(String description) {
		explicitlySetProps.add(PropName.DESCRIPTION);
		this.description = description;
	}

	@Override
	public String getBody() {
		return body;
	}

	/**
	 * Set the body of the article.
	 *
	 * @param body The article body
	 */
	public void setBody(String body) {
		explicitlySetProps.add(PropName.BODY);
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
		explicitlySetProps.add(PropName.TAGS);
		this.tags = tags;
	}

	@Override
	public boolean isExplicitlySet(PropName prop) {
		return explicitlySetProps.contains(prop);
	}
}
