package realworld.jaxrs.sys.authentication;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.wildfly.common.Assert.assertTrue;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MultivaluedMap;

import java.time.Instant;
import java.time.ZoneId;

import org.jboss.weld.junit5.auto.EnableAutoWeld;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import realworld.services.DateTimeService;

/**
 * Tests for the {@link TokenHelper}.
 */
@EnableAutoWeld
@ExtendWith(MockitoExtension.class)
public class TokenHelperTest {

	private static final String TOKEN_OTHER_ALG = "eyJhbGciOiJPVEhFUiJ9.eyJzdWIiOiJwYXRyaWNrIiwiZXhwIjoxNTYxMzc1MjI1LCJ1dWlkIjoiZjY2YjNmNzgtMTIxNS00NzE0LThjMWQtMTk4Y2M2YzRkZTk4In0.79zqo9XKbOEypAiXEC_MbaOYTbDIPIbngAeoM6ChxzA";
	private static final String TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJwYXRyaWNrIiwiZXhwIjoxNTYxMzc1MjI1LCJ1dWlkIjoiZjY2YjNmNzgtMTIxNS00NzE0LThjMWQtMTk4Y2M2YzRkZTk4In0.79zqo9XKbOEypAiXEC_MbaOYTbDIPIbngAeoM6ChxzA";
	private static final long TOKEN_EXPIRATION_TIME = 1561375225000L;

	@Produces @Mock
	private JwtService jwtService;

	@Produces @Mock
	private DateTimeService dateTimeService;

	@Produces @Mock
	private TokenAuthenticationConfig tokenAuthenticationConfig;

	@Inject
	private TokenHelper sut;

	@Test
	void testExtractRawTokenFromRequestContext() {
		@SuppressWarnings("unchecked")
		MultivaluedMap<String, String> headersMap = mock(MultivaluedMap.class);
		when(headersMap.getFirst(TokenHelper.AUTHORIZATION_HEADER)).thenReturn("token the_token", null);
		ContainerRequestContext reqctx = mock(ContainerRequestContext.class);
		when(reqctx.getHeaders()).thenReturn(headersMap);
		assertEquals("the_token", sut.extractRawToken(reqctx));
		// the header is null in the 2nd call
		assertNull(sut.extractRawToken(reqctx));
	}

	@Test
	void testExtractRawTokenFromHeaders() {
		@SuppressWarnings("unchecked")
		MultivaluedMap<String, String> headersMap = mock(MultivaluedMap.class);
		when(headersMap.getFirst(TokenHelper.AUTHORIZATION_HEADER)).thenReturn("token the_token", null);
		HttpHeaders headers = mock(HttpHeaders.class);
		when(headers.getRequestHeaders()).thenReturn(headersMap);
		assertEquals("the_token", sut.extractRawToken(headers));
		// the header is null in the 2nd call
		assertNull(sut.extractRawToken(headers));
	}

	@Test
	void testOnlyAcceptsAlgorithmHS256() {
		try {
			sut.processToken(TOKEN_OTHER_ALG);
			fail("only RS256 should be supported");
		}
		catch(NotAuthorizedException expected) {
			assertTrue(expected.getMessage().startsWith("unsupported algorithm: "));
		}
	}

	@Test
	void testTokensThatFailToVerifyAreRejected() throws Exception {
		when(jwtService.verify(any())).thenReturn(false);
		try {
			sut.processToken(TOKEN);
			fail("should reject tokens that fail to verify");
		}
		catch(NotAuthorizedException expected) {
			assertTrue(expected.getMessage().startsWith("failed to verify JWT: "));
		}
	}

	@Test
	void testExpiredTokensAreRejected() throws Exception {
		when(jwtService.verify(any())).thenReturn(true);
		when(dateTimeService.getNow()).thenReturn(Instant.ofEpochMilli(TOKEN_EXPIRATION_TIME + 1).atZone(ZoneId.systemDefault()).toLocalDateTime());
		try {
			sut.processToken(TOKEN);
			fail("should reject expired tokens");
		}
		catch(NotAuthorizedException expected) {
			assertTrue(expected.getMessage().startsWith("JWT expired at"));
		}
	}

	@Test
	void testCorrectTokenProducesUser() throws Exception {
		when(jwtService.verify(any())).thenReturn(true);
		when(dateTimeService.getNow()).thenReturn(Instant.ofEpochMilli(TOKEN_EXPIRATION_TIME).atZone(ZoneId.systemDefault()).toLocalDateTime());
		when(tokenAuthenticationConfig.getUsernameFieldInJwt()).thenReturn("sub");
		UserImpl user = sut.processToken(TOKEN);
		assertNotNull(user);
		assertEquals("patrick", user.getName());
		assertEquals("f66b3f78-1215-4714-8c1d-198cc6c4de98", user.getUniqueId());
	}
}
