package realworld.authorization;

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
import realworld.authentication.User;

/**
 * Tests for the {@link AuthorizationImpl}.
 */
@EnableAutoWeld
@ExtendWith(MockitoExtension.class)
public class AuthorizationImplTest {

	@Produces @Mock
	private AuthenticationContext authenticationContext;

	@Inject
	private AuthorizationImpl sut;

	@Test
	void testRequireLoginWithoutUser() {
		when(authenticationContext.getUserPrincipal()).thenReturn(null);
		try {
			sut.requireLogin();
			fail("should have thrown");
		}
		catch( NotAuthenticatedException expected ) {
			// expected
		}
	}

	@Test
	void testRequireLogin() {
		when(authenticationContext.getUserPrincipal()).thenReturn(mock(User.class));
		sut.requireLogin();
	}
}
