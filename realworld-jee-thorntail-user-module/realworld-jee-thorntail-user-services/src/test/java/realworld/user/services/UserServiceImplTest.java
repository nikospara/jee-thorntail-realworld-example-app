package realworld.user.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import java.util.Optional;

import org.jboss.weld.junit5.auto.EnableAutoWeld;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import realworld.EntityDoesNotExistException;
import realworld.authentication.AuthenticationContext;
import realworld.authentication.User;
import realworld.services.SimpleValidationException;
import realworld.user.model.ImmutableUserData;
import realworld.user.model.ProfileData;
import realworld.user.model.UserData;
import realworld.user.model.UserRegistrationData;
import realworld.user.model.UserUpdateData;
import realworld.user.model.UserUpdateData.PropName;

/**
 * Tests for the {@link UserServiceImpl}.
 */
@EnableAutoWeld
@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

	private static final String USERID1 = "USERID1";
	private static final String USERID2 = "USERID2";
	private static final String USERNAME1 = "USERNAME1";
	private static final String USERNAME2 = "USERNAME2";
	private static final String EMAIL1 = "email.one@here.com";
	private static final String EMAIL2 = "email.two@here.com";
	private static final String PASSWORD1 = "PASSWORD1";
	private static final String PASSWORD2 = "PASSWORD2";
	private static final String BIO2 = "BIO2";
	private static final String IMAGE2 = "IMAGE2";
	private static final String ENCRYPTED_PASSWORD = "ENCRYPTED_PASSWORD";

	@Produces @Mock
	private UserDao userDao;

	@Produces @Mock(lenient = true)
	private PasswordEncrypter encrypter;

	@Produces @Mock
	private AuthenticationContext authenticationContext;

	@Inject
	private UserServiceImpl sut;

	@BeforeEach
	void init() {
		when(encrypter.apply(anyString())).thenAnswer(a -> "ENC:" + a.getArgument(0));
	}

	@Test
	void testRegisterWithExistingUsername() {
		UserRegistrationData registrationData = mock(UserRegistrationData.class);
		when(registrationData.getUsername()).thenReturn(USERNAME1);
		when(userDao.usernameExists(USERNAME1)).thenReturn(true);

		assertDuplicateUsername(() -> sut.register(registrationData));
	}

	@Test
	void testRegisterWithExistingEmail() {
		UserRegistrationData registrationData = mock(UserRegistrationData.class);
		when(registrationData.getEmail()).thenReturn(EMAIL1);
		when(userDao.emailExists(EMAIL1)).thenReturn(true);

		assertDuplicateEmail(() -> sut.register(registrationData));
	}

	@Test
	void testRegister() {
		UserRegistrationData registrationData = mock(UserRegistrationData.class);
		when(registrationData.getUsername()).thenReturn(USERNAME1);
		when(registrationData.getEmail()).thenReturn(EMAIL1);
		when(registrationData.getPassword()).thenReturn(PASSWORD1);
		when(userDao.add(any(UserData.class), anyString())).then(x -> x.getArgument(0));
		when(encrypter.apply(PASSWORD1)).thenReturn(ENCRYPTED_PASSWORD);

		UserData result = sut.register(registrationData);

		verify(userDao).add(any(UserData.class), eq(ENCRYPTED_PASSWORD));
		assertEquals(USERNAME1, result.getUsername());
		assertEquals(EMAIL1, result.getEmail());
	}

	@Test
	void testUpdateWithExistingUsername() {
		UserData cu = mockCurrentUser();
		when(userDao.findByUserName(USERNAME1)).thenReturn(Optional.of(cu));

		UserUpdateData userUpdateData = mock(UserUpdateData.class);
		when(userUpdateData.isExplicitlySet(PropName.USERNAME)).thenReturn(true);
		when(userUpdateData.getUsername()).thenReturn(USERNAME2);
		when(userDao.usernameExists(USERNAME2)).thenReturn(true);

		assertDuplicateUsername(() -> sut.update(userUpdateData));
	}

	@Test
	void testUpdateWithSameUsername() {
		UserData cu = mockCurrentUser();
		when(userDao.findByUserName(USERNAME1)).thenReturn(Optional.of(cu));

		UserUpdateData userUpdateData = mock(UserUpdateData.class);
		when(userUpdateData.isExplicitlySet(PropName.USERNAME)).thenReturn(true);
		when(userUpdateData.getUsername()).thenReturn(USERNAME1);

		sut.update(userUpdateData);

		verify(userDao).update(any());
	}

	@Test
	void testUpdateWithExistingEmail() {
		UserData cu = mockCurrentUser();
		when(userDao.findByUserName(USERNAME1)).thenReturn(Optional.of(cu));

		UserUpdateData userUpdateData = mock(UserUpdateData.class);
		when(userUpdateData.isExplicitlySet(PropName.USERNAME)).thenReturn(false);
		when(userUpdateData.isExplicitlySet(PropName.EMAIL)).thenReturn(true);
		when(userUpdateData.getEmail()).thenReturn(EMAIL2);
		when(userDao.emailExists(EMAIL2)).thenReturn(true);

		assertDuplicateEmail(() -> sut.update(userUpdateData));
	}

	@Test
	void testUpdateWithSameEmail() {
		UserData cu = mockCurrentUser();
		when(userDao.findByUserName(USERNAME1)).thenReturn(Optional.of(cu));

		UserUpdateData userUpdateData = mock(UserUpdateData.class);
		when(userUpdateData.isExplicitlySet(PropName.USERNAME)).thenReturn(false);
		when(userUpdateData.isExplicitlySet(PropName.EMAIL)).thenReturn(true);
		when(userUpdateData.getEmail()).thenReturn(EMAIL1);

		sut.update(userUpdateData);

		verify(userDao).update(any());
	}

	@Test
	void testUpdatePassword() {
		UserData cu = mockCurrentUser();
		when(userDao.findByUserName(USERNAME1)).thenReturn(Optional.of(cu));

		UserUpdateData userUpdateData = mock(UserUpdateData.class);
		when(userUpdateData.isExplicitlySet(any(PropName.class))).thenReturn(false);
		when(userUpdateData.isExplicitlySet(PropName.PASSWORD)).thenReturn(true);
		when(userUpdateData.getPassword()).thenReturn(PASSWORD2);

		sut.update(userUpdateData);

		verify(userDao).changePassword(USERID1, "ENC:" + PASSWORD2);
	}

	@Test
	void testUpdate() {
		UserData cu = mockCurrentUser();
		when(userDao.findByUserName(USERNAME1)).thenReturn(Optional.of(cu));

		UserUpdateData userUpdateData = mock(UserUpdateData.class);
		when(userUpdateData.isExplicitlySet(any(PropName.class))).thenReturn(true);
		when(userUpdateData.getUsername()).thenReturn(USERNAME2);
		when(userUpdateData.getEmail()).thenReturn(EMAIL2);
		when(userUpdateData.getBio()).thenReturn(BIO2);
		when(userUpdateData.getImage()).thenReturn(IMAGE2);
		when(userUpdateData.getPassword()).thenReturn(PASSWORD2);

		UserData result = sut.update(userUpdateData);

		assertEquals(USERID1, result.getId());
		assertEquals(USERNAME2, result.getUsername());
		assertEquals(EMAIL2, result.getEmail());
		assertEquals(BIO2, result.getBio());
		assertEquals(IMAGE2, result.getImage());
	}

	@Test
	void testFollowNonexistingUser() {
		when(userDao.findByUserName(USERNAME1)).thenReturn(Optional.empty());
		try {
			sut.follow(USERNAME1);
			fail("following non-existing user should throw");
		}
		catch( EntityDoesNotExistException e ) {
			// Normal
		}
	}

	@Test
	void testFollow() {
		mockCurrentUser();
		UserData u = ImmutableUserData.builder().id(USERID2).username(USERNAME2).email(EMAIL2).bio(BIO2).image(IMAGE2).build();
		when(userDao.findByUserName(USERNAME2)).thenReturn(Optional.of(u));

		ProfileData result = sut.follow(USERNAME2);

		verify(userDao).follow(USERID1, USERID2);
		assertEquals(USERNAME2, result.getUsername());
		assertEquals(BIO2, result.getBio());
		assertEquals(IMAGE2, result.getImage());
		assertTrue(result.isFollowing());
	}

	@Test
	void testUnfollowNonexistingUser() {
		when(userDao.findByUserName(USERNAME1)).thenReturn(Optional.empty());
		try {
			sut.unfollow(USERNAME1);
			fail("following non-existing user should throw");
		}
		catch( EntityDoesNotExistException e ) {
			// Normal
		}
	}

	private void assertDuplicateUsername(Runnable f) {
		try {
			f.run();
			fail("duplicate username should have been reported!");
		}
		catch( SimpleValidationException e ) {
			assertNotNull(e.getViolations());
			assertEquals(1, e.getViolations().size());
			assertEquals("username", e.getViolations().get(0).getFieldName());
			assertEquals("duplicate user name", e.getViolations().get(0).getMessage());
		}
	}

	private void assertDuplicateEmail(Runnable f) {
		try {
			f.run();
			fail("duplicate email should have been reported!");
		}
		catch( SimpleValidationException e ) {
			assertNotNull(e.getViolations());
			assertEquals(1, e.getViolations().size());
			assertEquals("email", e.getViolations().get(0).getFieldName());
			assertEquals("duplicate email", e.getViolations().get(0).getMessage());
		}
	}

	private UserData mockCurrentUser() {
		User user = mock(User.class, Mockito.withSettings().lenient());
		when(user.getName()).thenReturn(USERNAME1);
		when(user.getUniqueId()).thenReturn(USERID1);
		when(authenticationContext.getUserPrincipal()).thenReturn(user);

		return ImmutableUserData.builder().id(USERID1).username(USERNAME1).email(EMAIL1).bio("").image("").build();
	}
}
