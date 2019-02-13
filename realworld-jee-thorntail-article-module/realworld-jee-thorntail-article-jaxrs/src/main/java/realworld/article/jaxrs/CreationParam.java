package realworld.article.jaxrs;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonRootName;
import realworld.article.model.ArticleCreationData;

/**
 * Article creation data.
 * 
 * @see <a href="https://github.com/gothinkster/realworld/tree/master/api#create-article" target="_top">Specification</a>
 */
@JsonRootName("article")
public class CreationParam implements ArticleCreationData, Serializable {

	private static final long serialVersionUID = 1L;
	
	private String title;
	private String description;
	private String body;
	
	@Override
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}
}
