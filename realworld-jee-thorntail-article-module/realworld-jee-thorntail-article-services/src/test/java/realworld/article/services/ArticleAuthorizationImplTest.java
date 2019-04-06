package realworld.article.services;

import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
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
import realworld.authorization.NotAuthenticatedException;
import realworld.authorization.NotAuthorizedException;

/**
 * Tests for the {@link ArticleAuthorizationImpl}.
 */
@EnableAutoWeld
@ExtendWith(MockitoExtension.class)
public class ArticleAuthorizationImplTest {

	private static final String SLUG = "slug";
	private static final String SOME_USER_ID = "id1";

	@Produces @Mock
	private AuthenticationContext authenticationContext;

	@Produces @Mock
	private ArticleDao articleDao;

	@Inject
	private ArticleAuthorizationImpl sut;

	@Test
	void testRequireCurrentUserToBeAuthorOfWithoutUser() {
		try {
			sut.requireCurrentUserToBeAuthorOf(SLUG);
			fail("requireCurrentUserToBeAuthorOf should fail if there is no current user");
		}
		catch(NotAuthenticatedException expected) {
			// expected
		}
	}

	@Test
	void testRequireCurrentUserToBeAuthorOfWithoutBeingAuthor() {
		User user = mock(User.class);
		when(user.getUniqueId()).thenReturn(SOME_USER_ID);
		when(authenticationContext.getUserPrincipal()).thenReturn(user);
		when(articleDao.checkArticleAuthor(SLUG, SOME_USER_ID)).thenReturn(false);
		try {
			sut.requireCurrentUserToBeAuthorOf(SLUG);
			fail("requireCurrentUserToBeAuthorOf should fail if the current user is not the author of the article");
		}
		catch(NotAuthorizedException expected) {
			// expected
		}
	}

	@Test
	void testRequireCurrentUserToBeAuthorOf() {
		User user = mock(User.class);
		when(user.getUniqueId()).thenReturn(SOME_USER_ID);
		when(authenticationContext.getUserPrincipal()).thenReturn(user);
		when(articleDao.checkArticleAuthor(SLUG, SOME_USER_ID)).thenReturn(true);
		sut.requireCurrentUserToBeAuthorOf(SLUG);
		verify(authenticationContext, times(2)).getUserPrincipal();
	}
}
