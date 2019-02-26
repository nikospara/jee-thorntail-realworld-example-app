package realworld.article.persistence;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

/**
 * Entity representing a like of a user for an article.
 */
@Entity
@Table(name = "RWL_ARTICLE_FAV")
@IdClass(ArticleFavoritePK.class)
public class ArticleFavorite {

	@Id
	@Column(name = "article_id")
	private String articleId;

	@Id
	@Column(name = "user_id")
	private String userId;

	public String getArticleId() {
		return articleId;
	}

	public void setArticleId(String articleId) {
		this.articleId = articleId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
}
