package realworld.jaxrs.sys.authentication;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.StringReader;
import java.util.Base64;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.nimbusds.jwt.SignedJWT;
import org.jboss.weld.junit5.auto.EnableAutoWeld;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import realworld.services.DateTimeService;
import realworld.user.model.ImmutableUserData;
import realworld.user.model.UserData;

/**
 * Tests for the {@link JwtServiceImpl}.
 */
@EnableAutoWeld
@ExtendWith(MockitoExtension.class)
public class JwtServiceImplTest {

	private static final String USER_ID = "userid";
	private static final String USERNAME = "username";
	private static final String EMAIL = "userid@here.com";
	private static final Pattern TOKEN_RE = Pattern.compile("^([a-z0-9_-]+)\\.([a-z0-9_-]+)\\.[a-z0-9_-]+$", Pattern.CASE_INSENSITIVE);
	private static final String USER_ID2 = "userid2";
	private static final String USERNAME2 = "username2";

	@Produces @Mock
	private DateTimeService dateTimeService;

	@Produces @Mock
	private TokenAuthenticationConfig tokenAuthenticationConfig;

	@Inject
	private JwtServiceImpl sut;

	@BeforeEach
	void init() throws Exception {
		String jwtSecret = Base64.getMimeEncoder().encodeToString("A long String to generate enough bytes for the secret during the test".getBytes(UTF_8));
		when(tokenAuthenticationConfig.getJwtSecret()).thenReturn(jwtSecret);
		when(tokenAuthenticationConfig.getJwtSessionTime()).thenReturn(60 * 10L);
		when(dateTimeService.currentTimeMillis()).thenReturn(10000000L);
	}

	@Test
	void testToTokenAndVerify() throws Exception {
		UserData user = ImmutableUserData.builder().id(USER_ID).username(USERNAME).email(EMAIL).build();
		String token = sut.toToken(user);
		assertNotNull(token);
		Matcher m = TOKEN_RE.matcher(token);
		assertTrue(m.matches());
		MatchResult mr = m.toMatchResult();
		JsonObject tokenHeader = readBase64AsJson(mr.group(1));
		assertEquals("HS256", tokenHeader.getString("alg"));
		JsonObject tokenBody = readBase64AsJson(mr.group(2));
		assertEquals(USERNAME, tokenBody.getString("sub"));
		assertEquals(USER_ID, tokenBody.getString("uuid"));
		assertTrue(sut.verify(SignedJWT.parse(token)));
	}

	@Test
	void testUpdateUser() throws Exception {
		UserData user = ImmutableUserData.builder().id(USER_ID).username(USERNAME).email(EMAIL).build();
		String token = sut.toToken(user);
		Matcher m1 = TOKEN_RE.matcher(token);
		assertTrue(m1.matches());
		MatchResult mr1 = m1.toMatchResult();
		JsonObject tokenBody1 = readBase64AsJson(mr1.group(2));
		UserData modifiedUser = ImmutableUserData.builder().id(USER_ID2).username(USERNAME2).email(EMAIL).build();
		String token2 = sut.updateUser(modifiedUser, token);
		Matcher m2 = TOKEN_RE.matcher(token2);
		assertTrue(m2.matches());
		MatchResult mr2 = m2.toMatchResult();
		JsonObject tokenBody2 = readBase64AsJson(mr2.group(2));
		assertEquals(USERNAME2, tokenBody2.getString("sub"));
		assertEquals(USER_ID2, tokenBody2.getString("uuid"));
		assertEquals(tokenBody1.getInt("exp"), tokenBody2.getInt("exp"));
		assertTrue(sut.verify(SignedJWT.parse(token2)));
	}

	private JsonObject readBase64AsJson(String string) {
		String decodedString = new String(Base64.getUrlDecoder().decode(string), UTF_8);
		JsonReader jsonReader = Json.createReader(new StringReader(decodedString));
		return jsonReader.readObject();
	}
}
