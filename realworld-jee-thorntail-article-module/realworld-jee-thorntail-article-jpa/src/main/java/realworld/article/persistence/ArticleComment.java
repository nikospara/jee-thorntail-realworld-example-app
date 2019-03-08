package realworld.article.persistence;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.time.LocalDateTime;

/**
 * Entity connecting a comment to an article.
 */
@Entity
@Table(name = "RWL_ARTICLE_COMMENT")
@IdClass(ArticleCommentPK.class)
public class ArticleComment {

	@Id
	@Column(name = "article_id")
	private String articleId;

	@Id
	@Column(name = "comment_id")
	private String commentId;

	@Column(name = "created_at")
	private LocalDateTime createdAt;

	public String getArticleId() {
		return articleId;
	}

	public void setArticleId(String articleId) {
		this.articleId = articleId;
	}

	public String getCommentId() {
		return commentId;
	}

	public void setCommentId(String commentId) {
		this.commentId = commentId;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
}
