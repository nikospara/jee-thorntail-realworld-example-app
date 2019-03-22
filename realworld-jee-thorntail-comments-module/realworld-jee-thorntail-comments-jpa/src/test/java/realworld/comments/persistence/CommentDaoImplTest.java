package realworld.comments.persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import org.junit.jupiter.api.extension.ExtendWith;
import realworld.comments.model.CommentCreationData;
import realworld.comments.model.CommentWithLinks;
import realworld.test.jpa.JpaDaoExtension;
import realworld.test.liquibase.LiquibaseExtension;

/**
 * Tests for the {@link CommentDaoImpl}.
 */
@ExtendWith({LiquibaseExtension.class, JpaDaoExtension.class})
@EnabledIfSystemProperty(named = "database-test.active", matches = "true")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CommentDaoImplTest {

	private static final String AUTHOR_ID = UUID.randomUUID().toString();
	private static final String COMMENT_BODY = "Comment body.";

	private EntityManager em;
	private CommentDaoImpl sut;

	@BeforeEach
	void init(EntityManager em) {
		this.em = em;
		sut = new CommentDaoImpl(em);
	}

	@Test
	@Order(1)
	void testCreate() {
		CommentCreationData creationData = new CommentCreationData() {
			@Override public String getBody() {
				return COMMENT_BODY;
			}
		};

		em.getTransaction().begin();
		LocalDateTime createdAt = LocalDateTime.of(2019, Month.MARCH, 16, 12, 11, 13);
		CommentWithLinks comment = sut.create(creationData, AUTHOR_ID, createdAt);
		em.getTransaction().commit();
		em.clear();

		assertNotNull(comment);
		assertNotNull(comment.getId());
		assertEquals(AUTHOR_ID, comment.getAuthorId());
		assertEquals(COMMENT_BODY, comment.getBody());
		assertEquals(createdAt, comment.getCreatedAt());
		assertEquals(createdAt, comment.getUpdatedAt());

		Comment c = em.find(Comment.class, comment.getId());
		assertEquals(AUTHOR_ID, c.getAuthorId());
		assertEquals(COMMENT_BODY, c.getBody());
		assertEquals(createdAt, c.getCreatedAt());
		assertEquals(createdAt, c.getUpdatedAt());
	}

	@Test
	@Order(2)
	void testFindCommentsWithIds() {
		List<String> commentIds = em.createQuery("SELECT c FROM Comment c", Comment.class).getResultStream().map(Comment::getId).collect(Collectors.toList());
		assertEquals(1, commentIds.size());
		commentIds.add("DOES NOT EXIST");
		em.clear();
		List<CommentWithLinks> result = sut.findCommentsWithIds(commentIds);
		assertEquals(1, result.size());
		assertEquals(COMMENT_BODY, result.get(0).getBody());
	}

	@Test
	@Order(3)
	void testCheckAuthor() {
		List<String> commentIds = em.createQuery("SELECT c FROM Comment c", Comment.class).getResultStream().map(Comment::getId).collect(Collectors.toList());
		assertEquals(1, commentIds.size());
		String commentId = commentIds.get(0);
		assertTrue(sut.checkAuthor(commentId, AUTHOR_ID));
		assertFalse(sut.checkAuthor("DOES NOT EXIST", AUTHOR_ID));
		assertFalse(sut.checkAuthor(commentId, "DOES NOT EXIST"));
	}

	@Test
	@Order(3)
	void testDelete() {
		List<String> commentIds = em.createQuery("SELECT c FROM Comment c", Comment.class).getResultStream().map(Comment::getId).collect(Collectors.toList());
		assertEquals(1, commentIds.size());
		String commentId = commentIds.get(0);

		em.getTransaction().begin();
		sut.delete(commentId);
		em.getTransaction().commit();
		em.clear();

		assertEquals(0L, em.createQuery("SELECT count(c) FROM Comment c", Long.class).getSingleResult());
	}
}
