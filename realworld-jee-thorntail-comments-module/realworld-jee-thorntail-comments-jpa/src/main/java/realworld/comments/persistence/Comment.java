package realworld.comments.persistence;

import static javax.persistence.TemporalType.TIMESTAMP;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import java.util.Date;

/**
 * The comment entity.
 */
@Entity
@Table(name = "RWL_COMMENT")
public class Comment {

	@Id
	@Column(name = "id")
	private String id;

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
	private String authorId;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public String getAuthorId() {
		return authorId;
	}

	public void setAuthorId(String author) {
		this.authorId = author;
	}
}
