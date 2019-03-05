package realworld.comments.persistence;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import realworld.EntityDoesNotExistException;
import realworld.comments.model.CommentCreationData;
import realworld.comments.model.CommentWithLinks;
import services.realworld.comments.services.CommentDao;

/**
 * JPA implementation of the {@link CommentDao}.
 */
@ApplicationScoped
public class CommentDaoImpl implements CommentDao {

	private EntityManager em;

	/**
	 * Default constructor for the frameworks.
	 */
	CommentDaoImpl() {
		// NOOP
	}

	/**
	 * Dependency injection constructor.
	 *
	 * @param em The entity manager
	 */
	@Inject
	public CommentDaoImpl(EntityManager em) {
		this.em = em;
	}

	@Override
	public CommentWithLinks add(CommentCreationData creationData, String articleId, String authorId, LocalDateTime createdAt) throws EntityDoesNotExistException {
		Comment comment = new Comment();
		comment.setId(UUID.randomUUID().toString());
		comment.setBody(creationData.getBody());
		comment.setCreatedAt(createdAt);
		comment.setUpdatedAt(createdAt);
		comment.setAuthorId(authorId);
		em.persist(comment);
		ArticleComment articleComment = new ArticleComment();
		articleComment.setArticleId(articleId);
		articleComment.setCommentId(comment.getId());
		em.persist(articleComment);
		return CommentWithLinks.make(comment.getId(), comment.getBody(), comment.getCreatedAt(), comment.getUpdatedAt(), authorId);
	}

	@Override
	public List<CommentWithLinks> findArticleComments(String articleId) throws EntityDoesNotExistException {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Comment> query = cb.createQuery(Comment.class);
		Root<Comment> comment = query.from(Comment.class);
		Subquery<String> subquery = query.subquery(String.class);
		Root<ArticleComment> articleComment = subquery.from(ArticleComment.class);
		subquery.select(articleComment.get(ArticleComment_.commentId)).where(cb.equal(articleComment.get(ArticleComment_.articleId), articleId));
		query.where(comment.get(Comment_.id).in(subquery));
		return em.createQuery(query).getResultStream()
				.map(c -> CommentWithLinks.make(c.getId(), c.getBody(), c.getCreatedAt(), c.getUpdatedAt(), c.getAuthorId()))
				.collect(Collectors.toList());
	}

	@Override
	public void delete(String id) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaDelete<ArticleComment> delArticleComment = cb.createCriteriaDelete(ArticleComment.class);
		Root<ArticleComment> articleCommentRoot = delArticleComment.from(ArticleComment.class);
		delArticleComment.where(cb.equal(articleCommentRoot.get(ArticleComment_.commentId), id));
		em.createQuery(delArticleComment).executeUpdate();
		CriteriaDelete<Comment> delComment = cb.createCriteriaDelete(Comment.class);
		Root<Comment> commentRoot = delComment.from(Comment.class);
		delComment.where(cb.equal(commentRoot.get(Comment_.id), id));
		em.createQuery(delComment).executeUpdate();
	}

	@Override
	public boolean checkAuthor(String id, String userId) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<String> query = cb.createQuery(String.class);
		Root<Comment> article = query.from(Comment.class);
		query.select(article.get(Comment_.authorId));
		query.where(cb.equal(article.get(Comment_.id), id));
		return em.createQuery(query).getResultStream().findFirst().map(userId::equals).orElse(false);
	}
}
