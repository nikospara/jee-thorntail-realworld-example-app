package realworld.comments.persistence;

import java.io.Serializable;
import java.util.Objects;

/**
 * JPA PK class for the {@link ArticleComment}.
 */
public class ArticleCommentPK implements Serializable {

	private static final long serialVersionUID = 1L;

	private String articleId;

	private String commentId;

	/**
	 * Default constructor.
	 */
	public ArticleCommentPK() {
		// NOOP
	}

	/**
	 * Full constructor.
	 *
	 * @param articleId
	 * @param userId
	 */
	public ArticleCommentPK(String articleId, String userId) {
		this.articleId = articleId;
		this.commentId = userId;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		ArticleCommentPK that = (ArticleCommentPK) o;
		return articleId.equals(that.articleId) && commentId.equals(that.commentId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(articleId, commentId);
	}

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
