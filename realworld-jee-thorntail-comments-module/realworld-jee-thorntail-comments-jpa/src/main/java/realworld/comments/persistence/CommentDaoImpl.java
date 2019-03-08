package realworld.comments.persistence;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import realworld.EntityDoesNotExistException;
import realworld.comments.model.CommentCreationData;
import realworld.comments.model.CommentWithLinks;
import realworld.comments.model.ImmutableCommentWithLinks;
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
	public CommentWithLinks create(CommentCreationData creationData, String authorId, LocalDateTime createdAt) throws EntityDoesNotExistException {
		Comment comment = new Comment();
		comment.setId(UUID.randomUUID().toString());
		comment.setBody(creationData.getBody());
		comment.setCreatedAt(createdAt);
		comment.setUpdatedAt(createdAt);
		comment.setAuthorId(authorId);
		em.persist(comment);
		return fromComment(comment);
	}

	@Override
	public List<CommentWithLinks> findCommentsWithIds(List<String> ids) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Comment> query = cb.createQuery(Comment.class);
		Root<Comment> comment = query.from(Comment.class);
		query.where(comment.get(Comment_.id).in(ids));
		return em.createQuery(query).getResultStream()
				.map(this::fromComment)
				.collect(Collectors.toList());
	}

	private CommentWithLinks fromComment(Comment c) {
		return ImmutableCommentWithLinks.builder()
				.id(c.getId())
				.body(c.getBody())
				.createdAt(c.getCreatedAt())
				.updatedAt(c.getUpdatedAt())
				.authorId(c.getAuthorId())
				.build();
	}

	@Override
	public void delete(String id) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
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
