package realworld.user.persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import javax.persistence.EntityManager;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import org.junit.jupiter.api.extension.ExtendWith;
import realworld.EntityDoesNotExistException;
import realworld.test.jpa.JpaDaoExtension;
import realworld.test.liquibase.LiquibaseExtension;
import realworld.user.model.ImmutableUserData;
import realworld.user.model.UserData;

/**
 * Tests for the {@link UserDaoImpl}.
 */
@ExtendWith({LiquibaseExtension.class, JpaDaoExtension.class})
@EnabledIfSystemProperty(named = "database-test.active", matches = "true")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserDaoImplTest {

	private static final String USERNAME = "username";
	private static final String EMAIL = "email.one@here.com";
	private static final String BIO = "Biography";
	private static final String IMAGE = "IMAGE";
	private static final String ENCRYPTED_PASSWD = "enc_passwd";
	private static final String UPDATED_USERNAME = "updated_username";
	private static final String UPDATED_EMAIL = "updated_email.one@here.com";
	private static final String UPDATED_BIO = "updated_Biography";
	private static final String UPDATED_IMAGE = "updated_IMAGE";
	private static final String UPDATED_ENCRYPTED_PASSWD = "updated_enc_passwd";
	private static final String USERNAME2 = "username2";
	private static final String EMAIL2 = "email2.one@here.com";
	private static final String BIO2 = "Biography2";
	private static final String IMAGE2 = "IMAGE2";
	private static final String ENCRYPTED_PASSWD2 = "enc_passwd2";

	private EntityManager em;
	private UserDaoImpl sut;

	@BeforeEach
	void init(EntityManager em) {
		this.em = em;
		sut = new UserDaoImpl(em);
	}

	@Test
	@Order(1)
	void testAdd() {
		em.getTransaction().begin();
		UserData result = sut.add(ImmutableUserData.builder().username(USERNAME).email(EMAIL).bio(BIO).image(IMAGE).build(), ENCRYPTED_PASSWD);
		em.getTransaction().commit();
		em.clear();

		assertNotNull(result.getId());

		User u = em.find(User.class, result.getId());
		assertNotNull(u);
		assertEquals(USERNAME, u.getUsername());
		assertEquals(EMAIL, u.getEmail());
		assertEquals(BIO, u.getBio());
		assertEquals(IMAGE, u.getImage());
		assertEquals(ENCRYPTED_PASSWD, u.getPassword());
	}

	@Test
	@Order(2)
	void testUsernameExists() {
		assertTrue(sut.usernameExists(USERNAME));
		assertFalse(sut.usernameExists("I do not exist"));
	}

	@Test
	@Order(3)
	void testEmailExists() {
		assertTrue(sut.emailExists(EMAIL));
		assertFalse(sut.emailExists("I do not exist"));
	}

	@Test
	@Order(4)
	void testFindByEmailAndPassword() {
		UserData result = sut.findByEmailAndPassword(EMAIL, ENCRYPTED_PASSWD).get();
		assertEquals(BIO, result.getBio());
		assertEquals(IMAGE, result.getImage());
		assertTrue(sut.findByEmailAndPassword("xxx", ENCRYPTED_PASSWD).isEmpty());
		assertTrue(sut.findByEmailAndPassword(EMAIL, "xxx").isEmpty());
	}

	@Test
	@Order(5)
	void testFindByUsername() {
		UserData result = sut.findByUserName(USERNAME).get();
		assertEquals(EMAIL, result.getEmail());
		assertEquals(BIO, result.getBio());
		assertEquals(IMAGE, result.getImage());
		assertTrue(sut.findByUserName("xxx").isEmpty());
	}
	
	@Test
	@Order(6)
	void testFollows() {
		String user1id = sut.findByUserName(USERNAME).get().getId();
		assertFalse(sut.follows(user1id, "xxx"));
		assertFalse(sut.follows("xxx", user1id));

		em.getTransaction().begin();
		String user2id = sut.add(ImmutableUserData.builder().username(USERNAME2).email(EMAIL2).bio(BIO2).image(IMAGE2).build(), ENCRYPTED_PASSWD2).getId();
		em.getTransaction().commit();
		em.clear();

		assertFalse(sut.follows(user1id, user2id));
		assertFalse(sut.follows(user2id, user1id));
	}

	@Test
	@Order(7)
	void testFollow() {
		String user1id = sut.findByUserName(USERNAME).get().getId();
		String user2id = sut.findByUserName(USERNAME2).get().getId();

		em.getTransaction().begin();
		sut.follow(user1id, user2id);
		em.getTransaction().commit();

		assertTrue(sut.follows(user1id, user2id));
		assertFalse(sut.follows(user2id, user1id));
	}

	@Test
	@Order(8)
	void testFindFollowedUserIds() {
		List<String> result = sut.findFollowedUserIds(USERNAME);
		String user2id = sut.findByUserName(USERNAME2).get().getId();
		assertEquals(1, result.size());
		assertEquals(user2id, result.get(0));
	}

	@Test
	@Order(9)
	void testUnfollow() {
		String user1id = sut.findByUserName(USERNAME).get().getId();
		String user2id = sut.findByUserName(USERNAME2).get().getId();

		em.getTransaction().begin();
		sut.unfollow(user1id, user2id);
		em.getTransaction().commit();

		assertFalse(sut.follows(user1id, user2id));
	}

	@Test
	@Order(10)
	void testMapUserNamesToIds() {
		String user1id = sut.findByUserName(USERNAME).get().getId();
		String user2id = sut.findByUserName(USERNAME2).get().getId();

		Map<String, String> result = sut.mapUserNamesToIds(Arrays.asList(USERNAME, USERNAME2, "DOES NOT EXIST"));

		assertEquals(2, result.size());
		assertEquals(user1id, result.get(USERNAME));
		assertEquals(user2id, result.get(USERNAME2));
	}

	@Test
	@Order(11)
	void testUpdate() {
		em.getTransaction().begin();
		UserData user = sut.findByUserName(USERNAME).get();
		user = ImmutableUserData.builder().from(user).username(UPDATED_USERNAME).email(UPDATED_EMAIL).bio(UPDATED_BIO).image(UPDATED_IMAGE).build();
		UserData result = sut.update(user);
		em.getTransaction().commit();
		em.clear();

		assertEquals(user.getId(), result.getId());
		assertEquals(user.getUsername(), result.getUsername());
		assertEquals(user.getEmail(), result.getEmail());
		assertEquals(user.getBio(), result.getBio());
		assertEquals(user.getImage(), result.getImage());

		User u = em.find(User.class, user.getId());
		assertEquals(user.getId(), u.getId());
		assertEquals(user.getUsername(), u.getUsername());
		assertEquals(user.getEmail(), u.getEmail());
		assertEquals(user.getBio(), u.getBio());
		assertEquals(user.getImage(), u.getImage());
	}

	@Test
	@Order(11)
	void testUpdateNonExistingUser() {
		em.getTransaction().begin();
		UserData user = ImmutableUserData.builder().id("DOES NOT EXIST").username(UPDATED_USERNAME).email(UPDATED_EMAIL).bio(UPDATED_BIO).image(UPDATED_IMAGE).build();
		try {
			sut.update(user);
			fail("should throw when trying to update non-existing user");
		}
		catch( EntityDoesNotExistException expected ) {
			// expected
		}
		em.getTransaction().commit();
	}

	@Test
	@Order(11)
	void testChangePassword() {
		em.getTransaction().begin();
		UserData user = sut.findByUserName(USERNAME).get();
		sut.changePassword(user.getId(), UPDATED_ENCRYPTED_PASSWD);
		em.getTransaction().commit();
		em.clear();

		User u = em.find(User.class, user.getId());
		assertEquals(UPDATED_ENCRYPTED_PASSWD, u.getPassword());
	}

	@Test
	@Order(11)
	void testChangePasswordNonExistingUser() {
		em.getTransaction().begin();
		try {
			sut.changePassword("DOES NOT EXIST", UPDATED_ENCRYPTED_PASSWD);
			fail("should throw when trying to update non-existing user");
		}
		catch( EntityDoesNotExistException expected ) {
			// expected
		}
		em.getTransaction().commit();
	}
}
