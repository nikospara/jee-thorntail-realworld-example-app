package realworld.user.services;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jboss.weld.junit5.auto.AddBeanClasses;
import org.jboss.weld.junit5.auto.AddEnabledDecorators;
import org.jboss.weld.junit5.auto.EnableAutoWeld;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import realworld.EntityDoesNotExistException;
import realworld.authorization.Authorization;
import realworld.authorization.NotAuthenticatedException;
import realworld.user.model.ProfileData;
import realworld.user.model.UserData;
import realworld.user.model.UserLoginData;
import realworld.user.model.UserRegistrationData;
import realworld.user.model.UserUpdateData;

/**
 * Tests for the {@link UserServiceAuthorizer}.
 */
@EnableAutoWeld
@AddBeanClasses(UserServiceAuthorizerTest.DummyUserService.class)
@AddEnabledDecorators(UserServiceAuthorizer.class)
@ExtendWith(MockitoExtension.class)
public class UserServiceAuthorizerTest {

	private static final UserData FROM_GET_CURENT_USER = mock(UserData.class, "FROM_GET_CURENT_USER");
	private static final UserData FROM_UPDATE = mock(UserData.class, "FROM_UPDATE");
	private static final UserData FROM_REGISTER = mock(UserData.class, "FROM_REGISTER");
	private static final UserData FROM_LOGIN = mock(UserData.class, "FROM_LOGIN");
	private static final ProfileData FROM_FOLLOW = mock(ProfileData.class, "FROM_FOLLOW");
	private static final ProfileData FROM_UNFOLLOW = mock(ProfileData.class, "FROM_UNFOLLOW");
	private static final ProfileData FROM_FIND_PROFILE = mock(ProfileData.class, "FROM_FIND_PROFILE");
	private static final UserUpdateData USER_UPDATE_DATA = mock(UserUpdateData.class);
	private static final UserRegistrationData USER_REG_DATA = mock(UserRegistrationData.class);
	private static final UserLoginData USER_LOGIN_DATA = mock(UserLoginData.class);
	private static final String USER_TO_FOLLOW = "USER_TO_FOLLOW";
	private static final String USER_TO_UNFOLLOW = "USER_TO_UNFOLLOW";
	private static final String USER_TO_FIND_PROFILE = "USER_TO_FIND_PROFILE";
	private static final String USER_TO_FIND_FOLLOWED = "USER_TO_FIND_FOLLOWED";
	private static final List<String> FROM_FIND_FOLLOWED_USER_IDS = new ArrayList<>();

	@Produces @Mock
	private Authorization authorization;

	@Inject
	private DummyUserService dummy;

	@Test
	void testRegister() {
		Object result = dummy.register(USER_REG_DATA);
		assertSame(FROM_REGISTER, result);
	}

	@Test
	void testLogin() {
		Object result = dummy.login(USER_LOGIN_DATA);
		assertSame(FROM_LOGIN, result);
	}

	@Test
	void testGetCurrentUserWithoutLogin() {
		doThrow(NotAuthenticatedException.class).when(authorization).requireLogin();
		expectNotAuthenticatedException(() -> dummy.getCurrentUser());
	}

	@Test
	void testGetCurrentUser() {
		doNothing().when(authorization).requireLogin();
		Object result = dummy.getCurrentUser();
		assertSame(FROM_GET_CURENT_USER, result);
	}

	@Test
	void testUpdateWithoutLogin() {
		doThrow(NotAuthenticatedException.class).when(authorization).requireLogin();
		expectNotAuthenticatedException(() -> dummy.update(USER_UPDATE_DATA));
	}

	@Test
	void testUpdate() {
		doNothing().when(authorization).requireLogin();
		Object result = dummy.update(USER_UPDATE_DATA);
		assertSame(FROM_UPDATE, result);
	}

	@Test
	void testFindProfile() {
		Object result = dummy.findProfile(USER_TO_FIND_PROFILE);
		assertSame(FROM_FIND_PROFILE, result);
	}

	@Test
	void testFollowWithoutLogin() {
		doThrow(NotAuthenticatedException.class).when(authorization).requireLogin();
		expectNotAuthenticatedException(() -> dummy.follow(USER_TO_FOLLOW));
	}

	@Test
	void testFollow() {
		doNothing().when(authorization).requireLogin();
		Object result = dummy.follow(USER_TO_FOLLOW);
		assertSame(FROM_FOLLOW, result);
	}

	@Test
	void testUnfollowWithoutLogin() {
		doThrow(NotAuthenticatedException.class).when(authorization).requireLogin();
		expectNotAuthenticatedException(() -> dummy.unfollow(USER_TO_UNFOLLOW));
	}

	@Test
	void testUnfollow() {
		doNothing().when(authorization).requireLogin();
		Object result = dummy.unfollow(USER_TO_UNFOLLOW);
		assertSame(FROM_UNFOLLOW, result);
	}

	private void expectNotAuthenticatedException(Runnable f) {
		try {
			f.run();
			fail("expected NotAuthenticatedException");
		}
		catch( NotAuthenticatedException expected ) {
			// expected
		}
	}

	@ApplicationScoped
	static class DummyUserService implements UserService {
		@Override
		public UserData register(@Valid UserRegistrationData registrationData) {
			if( registrationData != USER_REG_DATA ) {
				throw new IllegalArgumentException();
			}
			return FROM_REGISTER;
		}

		@Override
		public UserData login(@Valid UserLoginData loginData) throws NotAuthenticatedException {
			if( loginData != USER_LOGIN_DATA ) {
				throw new IllegalArgumentException();
			}
			return FROM_LOGIN;
		}

		@Override
		public UserData getCurrentUser() {
			return FROM_GET_CURENT_USER;
		}

		@Override
		public UserData update(@Valid UserUpdateData userUpdateData) {
			if( userUpdateData != USER_UPDATE_DATA ) {
				throw new IllegalArgumentException();
			}
			return FROM_UPDATE;
		}

		@Override
		public UserData findByUserName(String username) throws EntityDoesNotExistException {
			return null;
		}

		@Override
		public ProfileData findProfile(String username) {
			if( username != USER_TO_FIND_PROFILE ) {
				throw new IllegalArgumentException();
			}
			return FROM_FIND_PROFILE;
		}

		@Override
		public ProfileData findProfileById(String userid) {
			return null;
		}

		@Override
		public ProfileData follow(String username) {
			if( username != USER_TO_FOLLOW ) {
				throw new IllegalArgumentException();
			}
			return FROM_FOLLOW;
		}

		@Override
		public ProfileData unfollow(String username) {
			if( username != USER_TO_UNFOLLOW ) {
				throw new IllegalArgumentException();
			}
			return FROM_UNFOLLOW;
		}

		@Override
		public List<String> findFollowedUserIds(String followerName) {
			if( followerName != USER_TO_FIND_FOLLOWED ) {
				throw new IllegalArgumentException();
			}
			return FROM_FIND_FOLLOWED_USER_IDS;
		}

		@Override
		public Map<String, String> mapUserNamesToIds(List<String> usernames) {
			return null;
		}
	}
}
