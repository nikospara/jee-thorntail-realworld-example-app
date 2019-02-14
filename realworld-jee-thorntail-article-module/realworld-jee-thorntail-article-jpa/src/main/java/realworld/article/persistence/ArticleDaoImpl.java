package realworld.article.persistence;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import java.util.Date;
import java.util.UUID;

import realworld.EntityDoesNotExistException;
import realworld.article.model.ArticleCreationData;
import realworld.article.model.ArticleWithLinks;
import realworld.article.services.ArticleDao;

/**
 * Implementation of the {@link ArticleDao}.
 */
@ApplicationScoped
class ArticleDaoImpl implements ArticleDao {

	private EntityManager em;

	/**
	 * Default constructor for the frameworks.
	 */
	ArticleDaoImpl() {
		// NOOP
	}

	/**
	 * Dependency injection constructor.
	 *
	 * @param em The entity manager
	 */
	@Inject
	public ArticleDaoImpl(EntityManager em) {
		this.em = em;
	}

	@Override
	public ArticleWithLinks findArticleBySlug(String userId, String slug) throws EntityDoesNotExistException {
		try {
			Object[] res = em.createQuery(findArticleBySlugCriteriaQuery(userId, slug)).getSingleResult();
			Article a = (Article) res[0];
			return ArticleWithLinks.make(slug, a.getTitle(), a.getDescription(), a.getBody(), a.getCreatedAt(), a.getUpdatedAt(), (Boolean) res[1], ((Long) res[2]).intValue(), a.getAuthor());
		}
		catch( NoResultException e ) {
			throw new EntityDoesNotExistException();
		}
	}

	private CriteriaQuery<Object[]> findArticleBySlugCriteriaQuery(String userId, String slug) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Object[]> query = cb.createQuery(Object[].class);
		Root<Article> article = query.from(Article.class);
		query.where(cb.equal(article.get(Article_.slug), slug));
		query.multiselect(
				article,
				cb.greaterThan(favoriteSubquery(cb, query, article, userId), 0L),
				favoritesCountSubquery(cb, query, article)
		);
		return query;
	}

	private Expression<Long> favoriteSubquery(CriteriaBuilder cb, CriteriaQuery<Object[]> query, Root<Article> article, String userId) {
		Subquery<Long> subquery = query.subquery(Long.class);
		Root<ArticleFavorite> articleFavorite = subquery.from(ArticleFavorite.class);
		subquery.select(cb.count(articleFavorite));
		subquery.where(
				cb.equal(articleFavorite.get(ArticleFavorite_.articleId), article.get(Article_.id)),
				cb.equal(articleFavorite.get(ArticleFavorite_.userId), userId)
		);
		return subquery;
	}

	private Expression<Long> favoritesCountSubquery(CriteriaBuilder cb, CriteriaQuery<Object[]> query, Root<Article> article) {
		Subquery<Long> subquery = query.subquery(Long.class);
		Root<ArticleFavorite> articleFavorite = subquery.from(ArticleFavorite.class);
		subquery.select(cb.count(articleFavorite));
		subquery.where(cb.equal(articleFavorite.get(ArticleFavorite_.articleId), article.get(Article_.id)));
		return cb.sum(subquery, 0L);
	}

	@Override
	public ArticleWithLinks create(ArticleCreationData creationData, String slug, Date creationDate, String authorId) {
		Article article = new Article();
		article.setId(UUID.randomUUID().toString());
		article.setTitle(creationData.getTitle());
		article.setSlug(slug);
		article.setDescription(creationData.getDescription());
		article.setBody(creationData.getBody());
		article.setCreatedAt(creationDate);
		article.setAuthor(authorId);
		em.persist(article);
		return ArticleWithLinks.make(article.getSlug(), article.getTitle(), article.getDescription(), article.getBody(), article.getCreatedAt(), article.getUpdatedAt(), false, 0, authorId);
	}

	@Override
	public ArticleWithLinks favorite(String userId, String slug) throws EntityDoesNotExistException {
		try {
			Object[] res = em.createQuery(findArticleBySlugCriteriaQuery(userId, slug)).getSingleResult();
			Article a = (Article) res[0];
			boolean favorited = (Boolean) res[1];
			int favoritesCount = ((Long) res[2]).intValue();
			if( !favorited ) {
				favoritesCount++;
				ArticleFavorite af = new ArticleFavorite();
				af.setArticleId(a.getId());
				af.setUserId(userId);
				em.persist(af);
			}
			return ArticleWithLinks.make(slug, a.getTitle(), a.getDescription(), a.getBody(), a.getCreatedAt(), a.getUpdatedAt(), true, favoritesCount, a.getAuthor());
		}
		catch( NoResultException e ) {
			throw new EntityDoesNotExistException();
		}
	}

	@Override
	public ArticleWithLinks unfavorite(String userId, String slug) throws EntityDoesNotExistException {
		try {
			Object[] res = em.createQuery(findArticleBySlugCriteriaQuery(userId, slug)).getSingleResult();
			Article a = (Article) res[0];
			boolean favorited = (Boolean) res[1];
			int favoritesCount = ((Long) res[2]).intValue();
			if( favorited ) {
				favoritesCount--;
				ArticleFavoritePK pk = new ArticleFavoritePK(a.getId(), userId);
				ArticleFavorite af = em.find(ArticleFavorite.class, pk);
				em.remove(af);
			}
			return ArticleWithLinks.make(slug, a.getTitle(), a.getDescription(), a.getBody(), a.getCreatedAt(), a.getUpdatedAt(), false, favoritesCount, a.getAuthor());
		}
		catch( NoResultException e ) {
			throw new EntityDoesNotExistException();
		}
	}
}
