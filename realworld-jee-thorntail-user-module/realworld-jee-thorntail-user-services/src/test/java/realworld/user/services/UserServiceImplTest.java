package realworld.user.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import org.jboss.weld.junit5.auto.EnableAutoWeld;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import realworld.authentication.AuthenticationContext;
import realworld.services.SimpleValidationException;
import realworld.user.model.UserRegistrationData;

/**
 * Tests for the {@link UserServiceImpl}.
 */
@EnableAutoWeld
@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

	private static final String USERNAME1 = "username_one";

	@Produces @Mock
	private UserDao userDao;

	@Produces @Mock
	private PasswordEncrypter encrypter;

	@Produces @Mock
	private AuthenticationContext authenticationContext;

	@Inject
	private UserServiceImpl sut;

	@Test
	void testRegisterWithExistingUsername() {
		UserRegistrationData registrationData = mock(UserRegistrationData.class);
		when(registrationData.getUsername()).thenReturn(USERNAME1);
		when(userDao.usernameExists(USERNAME1)).thenReturn(true);

		try {
			sut.register(registrationData);
			fail("duplicate username should have been reported!");
		}
		catch( SimpleValidationException e ) {
			assertNotNull(e.getViolations());
			assertEquals(1, e.getViolations().size());
			assertEquals("username", e.getViolations().get(0).getFieldName());
			assertEquals("duplicate user name", e.getViolations().get(0).getMessage());
		}
	}
}
