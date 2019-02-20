package realworld.article.persistence;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import realworld.EntityDoesNotExistException;
import realworld.article.model.ArticleCreationData;
import realworld.article.model.ArticleResult;
import realworld.article.model.ArticleWithLinks;
import realworld.article.services.ArticleDao;
import realworld.article.services.ArticleSearchCriteria;

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
	public ArticleResult<ArticleWithLinks> find(String userId, ArticleSearchCriteria criteria, ArticleSearchCriteria defaultCriteria) {
		CriteriaBuilder cb = em.getCriteriaBuilder();

		CriteriaQuery<Object[]> query = cb.createQuery(Object[].class);
		Root<Article> articleRoot = applyCriteria(cb, query, criteria, defaultCriteria);
		query.multiselect(
				articleRoot,
				favoriteSubquery(cb, query, articleRoot, userId),
				favoritesCountSubquery(cb, query, articleRoot)
		);
		int maxResults = criteria.getLimit() != null ? criteria.getLimit() : defaultCriteria.getLimit();
		int firstResult = criteria.getOffset() != null ? criteria.getOffset() : defaultCriteria.getOffset();
		var results = em.createQuery(query)
				.setMaxResults(maxResults)
				.setFirstResult(firstResult)
				.getResultList().stream()
				.map(this::fromQueryResult)
				.collect(Collectors.toList());

		long count = results.size();
		if( count >= maxResults || firstResult != 0 ) {
			CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
			applyCriteria(cb, countQuery, criteria, defaultCriteria);
			count = em.createQuery(countQuery.select(cb.count(countQuery.getRoots().iterator().next()))).getSingleResult();
		}
		return new ArticleResult<>(results, count);
	}

	private ArticleWithLinks fromQueryResult(Object[] result) {
		Article a = (Article) result[0];
		return ArticleWithLinks.make(a.getId(), a.getSlug(), a.getTitle(), a.getDescription(), a.getBody(), a.getCreatedAt(), a.getUpdatedAt(), (Boolean) result[1], ((Long) result[2]).intValue(), a.getAuthor());
	}

	private Root<Article> applyCriteria(CriteriaBuilder cb, CriteriaQuery<?> query, ArticleSearchCriteria criteria, ArticleSearchCriteria defaultCriteria) {
		Root<Article> articleRoot = query.from(Article.class);
		var restrictions = new ArrayList<Predicate>();

		String tag = criteria.getTag() != null ? criteria.getTag() : defaultCriteria.getTag();
		if( tag != null ) {
			restrictions.add(cb.isMember(new Tag(tag), articleRoot.get(Article_.tags)));
		}

		List<String> authors = criteria.getAuthors() != null && !criteria.getAuthors().isEmpty() ? criteria.getAuthors() : defaultCriteria.getAuthors();
		if( authors != null && !authors.isEmpty() ) {
			restrictions.add(articleRoot.get(Article_.author).in(authors));
		}

		String favoritedBy = criteria.getFavoritedBy() != null ? criteria.getFavoritedBy() : defaultCriteria.getFavoritedBy();
		if( favoritedBy != null ) {
			Subquery<ArticleFavorite> favoriteSubquery = query.subquery(ArticleFavorite.class);
			Root<ArticleFavorite> favRoot = query.from(ArticleFavorite.class);
			favoriteSubquery.where(
					cb.equal(favRoot.get(ArticleFavorite_.articleId), articleRoot.get(Article_.id)),
					cb.equal(favRoot.get(ArticleFavorite_.userId), favoritedBy)
			);
			restrictions.add(cb.exists(favoriteSubquery));
		}

		query.where(restrictions.toArray(new Predicate[0]));

		return articleRoot;
	}

	@Override
	public ArticleWithLinks findArticleBySlug(String userId, String slug) throws EntityDoesNotExistException {
		try {
			Object[] res = em.createQuery(findArticleBySlugCriteriaQuery(userId, slug)).getSingleResult();
			return fromQueryResult(res);
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
				favoriteSubquery(cb, query, article, userId),
				favoritesCountSubquery(cb, query, article)
		);
		return query;
	}

	private Expression<Boolean> favoriteSubquery(CriteriaBuilder cb, CriteriaQuery<?> query, Root<Article> article, String userId) {
		Subquery<Long> subquery = query.subquery(Long.class);
		Root<ArticleFavorite> articleFavorite = subquery.from(ArticleFavorite.class);
		subquery.select(cb.count(articleFavorite));
		subquery.where(
				cb.equal(articleFavorite.get(ArticleFavorite_.articleId), article.get(Article_.id)),
				cb.equal(articleFavorite.get(ArticleFavorite_.userId), userId)
		);
		return cb.greaterThan(subquery, 0L);
	}

	private Expression<Long> favoritesCountSubquery(CriteriaBuilder cb, CriteriaQuery<?> query, Root<Article> article) {
		Subquery<Long> subquery = query.subquery(Long.class);
		Root<ArticleFavorite> articleFavorite = subquery.from(ArticleFavorite.class);
		subquery.select(cb.count(articleFavorite));
		subquery.where(cb.equal(articleFavorite.get(ArticleFavorite_.articleId), article.get(Article_.id)));
		return cb.sum(subquery, 0L);
	}

	@Override
	public Set<String> findTags(String articleId) throws EntityDoesNotExistException {
		Article article = Optional.ofNullable(em.find(Article.class, articleId)).orElseThrow(EntityDoesNotExistException::new);
		return article.getTags().stream().map(Tag::getName).collect(Collectors.toSet());
	}

	@Override
	public ArticleWithLinks create(ArticleCreationData creationData, String slug, Date creationDate, String authorId, Set<String> tags) {
		Article article = new Article();
		article.setId(UUID.randomUUID().toString());
		article.setTitle(creationData.getTitle());
		article.setSlug(slug);
		article.setDescription(creationData.getDescription());
		article.setBody(creationData.getBody());
		article.setCreatedAt(creationDate);
		article.setAuthor(authorId);
		if( tags != null ) {
			Set<Tag> dbtags = tags.stream().map(tag -> Optional.ofNullable(em.find(Tag.class, tag)).orElseGet(() -> new Tag(tag))).collect(Collectors.toSet());
			article.setTags(dbtags);
		}
		em.persist(article);
		return ArticleWithLinks.make(article.getId(), article.getSlug(), article.getTitle(), article.getDescription(), article.getBody(), article.getCreatedAt(), article.getUpdatedAt(), false, 0, authorId);
	}

	@Override
	public void addFavorite(String userId, String articleId) throws EntityDoesNotExistException {
		try {
			ArticleFavorite af = new ArticleFavorite();
			af.setArticleId(articleId);
			af.setUserId(userId);
			em.persist(af);
		}
		catch( NoResultException e ) {
			throw new EntityDoesNotExistException();
		}
	}

	@Override
	public void removeFavorite(String userId, String articleId) throws EntityDoesNotExistException {
		try {
			ArticleFavorite af = em.getReference(ArticleFavorite.class, new ArticleFavoritePK(articleId, userId));
			em.remove(af);
		}
		catch( NoResultException e ) {
			throw new EntityDoesNotExistException();
		}
	}
}
