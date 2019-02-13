package realworld.article.persistence;

import static javax.persistence.TemporalType.TIMESTAMP;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import java.util.Date;

@Entity
@Table(name = "RWL_ARTICLE")
public class Article {

	@Id
	@Column(name = "id")
	private String id;

	@Column(name = "slug")
	private String slug;

	@Column(name = "title")
	private String title;

	@Column(name = "description")
	private String description;

	@Column(name = "body")
	@Lob
	private String body;

	@Column(name = "createdAt")
	@Temporal(TIMESTAMP)
	private Date createdAt;

	@Column(name = "updatedAt")
	@Temporal(TIMESTAMP)
	private Date updatedAt;

	@Column(name = "user_id")
	private String author;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSlug() {
		return slug;
	}

	public void setSlug(String slug) {
		this.slug = slug;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}
}
