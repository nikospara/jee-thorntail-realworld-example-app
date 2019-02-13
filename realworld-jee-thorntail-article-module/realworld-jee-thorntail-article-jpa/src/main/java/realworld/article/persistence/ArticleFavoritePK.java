package realworld.article.persistence;

import java.io.Serializable;
import java.util.Objects;

/**
 * JPA PK class for the {@link ArticleFavorite}.
 */
public class ArticleFavoritePK implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String articleId;

	private String userId;

	/**
	 * Default constructor.
	 */
	public ArticleFavoritePK() {
		// NOOP
	}

	/**
	 * Full constructor.
	 * 
	 * @param articleId
	 * @param userId
	 */
	public ArticleFavoritePK(String articleId, String userId) {
		this.articleId = articleId;
		this.userId = userId;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		ArticleFavoritePK that = (ArticleFavoritePK) o;
		return articleId.equals(that.articleId) && userId.equals(that.userId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(articleId, userId);
	}

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
