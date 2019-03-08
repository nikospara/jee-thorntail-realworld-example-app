package realworld.article.persistence;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import realworld.EntityDoesNotExistException;
import realworld.article.model.ArticleCreationData;
import realworld.article.model.ArticleResult;
import realworld.article.model.ArticleWithLinks;
import realworld.article.model.ImmutableArticleWithLinks;
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
				.getResultStream()
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
		Article article = (Article) result[0];
		return fromArticle(article, (Boolean) result[1], ((Long) result[2]).intValue());
	}

	private ArticleWithLinks fromArticle(Article a, boolean isFavorited, int favoritesCount) {
		return ImmutableArticleWithLinks.builder()
				.id(a.getId())
				.slug(a.getSlug())
				.title(a.getTitle())
				.description(a.getDescription())
				.body(a.getBody())
				.createdAt(a.getCreatedAt())
				.updatedAt(a.getUpdatedAt())
				.isFavorited(isFavorited)
				.favoritesCount(favoritesCount)
				.authorId(a.getAuthor())
				.build();
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

	@Override
	public String findArticleIdBySlug(String slug) throws EntityDoesNotExistException {
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<String> query = cb.createQuery(String.class);
			Root<Article> article = query.from(Article.class);
			query.select(article.get(Article_.id)).where(cb.equal(article.get(Article_.slug), slug));
			return em.createQuery(query).getSingleResult();
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

	/**
	 * Query whether the given user has favorited an article.
	 */
	private Expression<Boolean> favoriteSubquery(CriteriaBuilder cb, CriteriaQuery<?> query, Root<Article> article, String userId) {
		if( userId == null ) {
			return cb.literal(false);
		}
		else {
			Subquery<Long> subquery = query.subquery(Long.class);
			Root<ArticleFavorite> articleFavorite = subquery.from(ArticleFavorite.class);
			subquery.select(cb.count(articleFavorite));
			subquery.where(
					cb.equal(articleFavorite.get(ArticleFavorite_.articleId), article.get(Article_.id)),
					cb.equal(articleFavorite.get(ArticleFavorite_.userId), userId)
			);
			return cb.greaterThan(subquery, 0L);
		}
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
	public ArticleWithLinks create(ArticleCreationData creationData, String slug, LocalDateTime creationDate, String authorId, Set<String> tags) {
		Article a = new Article();
		a.setId(UUID.randomUUID().toString());
		a.setTitle(creationData.getTitle());
		a.setSlug(slug);
		a.setDescription(creationData.getDescription());
		a.setBody(creationData.getBody());
		a.setCreatedAt(creationDate);
		a.setUpdatedAt(creationDate);
		a.setAuthor(authorId);
		handleTags(a, tags);
		em.persist(a);
		return fromArticle(a, false, 0);
	}

	@Override
	public void update(String id, String title, String slug, String description, String body, Set<String> tags, LocalDateTime updatedAt) {
		Article article = em.find(Article.class, id);
		article.setTitle(title);
		article.setTitle(title);
		article.setSlug(slug);
		article.setDescription(description);
		article.setBody(body);
		article.setUpdatedAt(updatedAt);
		handleTags(article, tags);
	}

	@Override
	public void delete(String slug) throws EntityDoesNotExistException {
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Article> query = cb.createQuery(Article.class);
			Root<Article> articleRoot = query.from(Article.class);
			query.where(cb.equal(articleRoot.get(Article_.slug), slug));
			Article article = em.createQuery(query).getSingleResult();
			em.remove(article);
			deleteAllFavorites(article.getId());
			deleteAllComments(article.getId());
		}
		catch( NoResultException e ) {
			throw new EntityDoesNotExistException();
		}
	}

	private void deleteAllFavorites(String articleId) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaDelete<ArticleFavorite> deleteQuery = cb.createCriteriaDelete(ArticleFavorite.class);
		Root<ArticleFavorite> articleFavoriteRoot = deleteQuery.from(ArticleFavorite.class);
		deleteQuery.where(cb.equal(articleFavoriteRoot.get(ArticleFavorite_.articleId), articleId));
		em.createQuery(deleteQuery).executeUpdate();
	}

	private void deleteAllComments(String articleId) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaDelete<ArticleComment> deleteQuery = cb.createCriteriaDelete(ArticleComment.class);
		Root<ArticleComment> articleCommentRoot = deleteQuery.from(ArticleComment.class);
		deleteQuery.where(cb.equal(articleCommentRoot.get(ArticleComment_.articleId), articleId));
		em.createQuery(deleteQuery).executeUpdate();
	}

	private void handleTags(Article article, Set<String> tags) {
		if( tags != null ) {
			Set<Tag> dbtags = tags.stream().map(tag -> Optional.ofNullable(em.find(Tag.class, tag)).orElseGet(() -> new Tag(tag))).collect(Collectors.toSet());
			article.setTags(dbtags);
		}
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

	@Override
	public boolean checkArticleAuthor(String slug, String userId) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<String> query = cb.createQuery(String.class);
		Root<Article> article = query.from(Article.class);
		query.select(article.get(Article_.author));
		query.where(cb.equal(article.get(Article_.slug), slug));
		return em.createQuery(query).getResultStream().findFirst().map(userId::equals).orElse(false);
	}

	@Override
	public void comment(String articleId, String commentId, LocalDateTime commentCreatedAt) throws EntityDoesNotExistException {
		ArticleComment articleComment = new ArticleComment();
		articleComment.setArticleId(articleId);
		articleComment.setCommentId(commentId);
		articleComment.setCreatedAt(commentCreatedAt);
		em.persist(articleComment);
	}

	@Override
	public List<String> findCommentIds(String articleId) throws EntityDoesNotExistException {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<String> query = cb.createQuery(String.class);
		Root<ArticleComment> articleComment = query.from(ArticleComment.class);
		query.select(articleComment.get(ArticleComment_.commentId)).where(cb.equal(articleComment.get(ArticleComment_.articleId), articleId));
		return em.createQuery(query).getResultList();
	}

	@Override
	public void deleteComment(String slug, String commentId) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaDelete<ArticleComment> deleteQuery = cb.createCriteriaDelete(ArticleComment.class);
		Root<ArticleComment> articleCommentRoot = deleteQuery.from(ArticleComment.class);
		Subquery<String> articleBySlug = deleteQuery.subquery(String.class);
		Root<Article> article = articleBySlug.from(Article.class);
		articleBySlug.select(article.get(Article_.id)).where(cb.equal(article.get(Article_.slug), slug));
		deleteQuery.where(cb.and(
				cb.equal(articleCommentRoot.get(ArticleComment_.commentId), commentId),
				cb.equal(articleCommentRoot.get(ArticleComment_.articleId), articleBySlug)
		));
		em.createQuery(deleteQuery).executeUpdate();
	}
}
