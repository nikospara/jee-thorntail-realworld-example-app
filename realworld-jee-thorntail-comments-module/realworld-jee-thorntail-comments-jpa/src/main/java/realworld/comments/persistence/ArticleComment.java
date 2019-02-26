package realworld.comments.persistence;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

/**
 * Entity connecting a comment to an article.
 */
@Entity
@Table(name = "RWL_COMMENT_ARTICLE")
@IdClass(ArticleCommentPK.class)
public class ArticleComment {

	@Id
	@Column(name = "article_id")
	private String articleId;

	@Id
	@Column(name = "comment_id")
	private String commentId;

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
}
