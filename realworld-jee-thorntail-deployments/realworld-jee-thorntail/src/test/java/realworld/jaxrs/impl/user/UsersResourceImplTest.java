package realworld.jaxrs.impl.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static realworld.jaxrs.JaxRsApp.APPLICATION_PATH;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonValue;
import javax.ws.rs.core.MediaType;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;

import org.jboss.resteasy.cdi.ResteasyCdiExtension;
import org.jboss.resteasy.core.Dispatcher;
import org.jboss.resteasy.mock.MockHttpRequest;
import org.jboss.resteasy.mock.MockHttpResponse;
import org.jboss.resteasy.plugins.server.resourcefactory.SingletonResource;
import org.jboss.resteasy.spi.metadata.DefaultResourceClass;
import org.jboss.weld.junit5.auto.AddBeanClasses;
import org.jboss.weld.junit5.auto.AddExtensions;
import org.jboss.weld.junit5.auto.EnableAutoWeld;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import realworld.jaxrs.sys.ObjectMapperProvider;
import realworld.jaxrs.sys.authentication.JwtService;
import realworld.jaxrs.test.CustomMockDispatcherFactory;
import realworld.user.jaxrs.UsersResource;
import realworld.user.model.ImmutableUserData;
import realworld.user.model.UserData;
import realworld.user.model.UserLoginData;
import realworld.user.model.UserRegistrationData;
import realworld.user.services.UserService;

/**
 * Tests for the {@link UsersResourceImpl}.
 */
@EnableAutoWeld
@AddBeanClasses(ObjectMapperProvider.class)
@AddExtensions(ResteasyCdiExtension.class)
@ExtendWith(MockitoExtension.class)
public class UsersResourceImplTest {

	private static final String USER_ID = "userid";
	private static final String USERNAME = "username";
	private static final String PASSWORD = "P@ssword";
	private static final String EMAIL = "userid@here.com";
	private static final String IMAGE = "http://pictures.com/image1";
	private static final String BIO = "Bio";

	@Produces @Mock
	private UserService userService;

	@Produces @Mock
	private JwtService jwtService;

	@Inject
	private UsersResourceImpl sut;

	private Dispatcher dispatcher;

	private MockHttpResponse response;

	@BeforeEach
	void init() {
		dispatcher = CustomMockDispatcherFactory.createDispatcher();
		SingletonResource resourceFactory = new SingletonResource(sut, new DefaultResourceClass(UsersResource.class, null));
		dispatcher.getRegistry().addResourceFactory(resourceFactory, APPLICATION_PATH);
		response = new MockHttpResponse();
		when(jwtService.toToken(any())).thenAnswer(a -> "T" + ((UserData) a.getArgument(0)).getId());
	}

	@Test
	void testRegister() throws Exception {
		MockHttpRequest request = MockHttpRequest.post(APPLICATION_PATH + "/users");
		request.contentType(MediaType.APPLICATION_JSON);
		request.accept(MediaType.APPLICATION_JSON);
		request.content(("{\"user\":{\"email\":\"" + EMAIL + "\", \"password\":\"" + PASSWORD + "\", \"username\":\"" + USERNAME + "\"}}").getBytes());

		when(userService.register(any())).thenAnswer(a -> {
			UserRegistrationData r = a.getArgument(0);
			return makeUser(USER_ID, r.getUsername(), r.getEmail(), null, null);
		});

		dispatcher.invoke(request, response);

		ArgumentCaptor<UserRegistrationData> captor = ArgumentCaptor.forClass(UserRegistrationData.class);
		verify(userService).register(captor.capture());
		assertEquals(PASSWORD, captor.getValue().getPassword());

		assertUserWithToken()
				.assertUsername(USERNAME)
				.assertEmail(EMAIL)
				.assertBio(null)
				.assertImage(null)
				.assertToken("T" + USER_ID);
	}

	@Test
	void testLogin() throws Exception {
		MockHttpRequest request = MockHttpRequest.post(APPLICATION_PATH + "/users/login");
		request.contentType(MediaType.APPLICATION_JSON);
		request.accept(MediaType.APPLICATION_JSON);
		request.content(("{\"user\":{\"email\":\"" + EMAIL + "\", \"password\":\"" + PASSWORD + "\"}}").getBytes());

		when(userService.login(any())).thenAnswer(a -> {
			UserLoginData r = a.getArgument(0);
			return makeUser(USER_ID, USERNAME, r.getEmail(), IMAGE, BIO);
		});

		dispatcher.invoke(request, response);

		ArgumentCaptor<UserLoginData> captor = ArgumentCaptor.forClass(UserLoginData.class);
		verify(userService).login(captor.capture());
		assertEquals(PASSWORD, captor.getValue().getPassword());

		assertUserWithToken()
				.assertUsername(USERNAME)
				.assertEmail(EMAIL)
				.assertBio(BIO)
				.assertImage(IMAGE)
				.assertToken("T" + USER_ID);
	}

	private UserData makeUser(String id, String username, String email, String image, String bio) {
		return ImmutableUserData.builder()
				.id(id)
				.username(username)
				.email(email)
				.image(image)
				.bio(bio)
				.build();
	}

	private UserWithTokenAssertions assertUserWithToken() throws UnsupportedEncodingException {
		assertEquals(200, response.getStatus());
		JsonReader jsonReader = Json.createReader(new StringReader(response.getContentAsString()));
		JsonObject jobj = jsonReader.readObject();
		assertEquals(1, jobj.size());
		JsonObject userWithToken = jobj.getJsonObject("user");
		assertNotNull(userWithToken);
		assertEquals(5, userWithToken.size());
		return new UserWithTokenAssertions(userWithToken);
	}

	private static class UserWithTokenAssertions {
		private JsonObject userWithToken;

		UserWithTokenAssertions(JsonObject userWithToken) {
			this.userWithToken = userWithToken;
		}

		UserWithTokenAssertions assertUsername(String username) {
			assertEquals(username, userWithToken.getString("username"));
			return this;
		}

		UserWithTokenAssertions assertEmail(String email) {
			assertEquals(email, userWithToken.getString("email"));
			return this;
		}

		UserWithTokenAssertions assertBio(String bio) {
			JsonValue bioVal = userWithToken.get("bio");
			assertEquals(bio, bioVal.getValueType() == JsonValue.ValueType.NULL ? null : userWithToken.getString("bio"));
			return this;
		}

		UserWithTokenAssertions assertImage(String image) {
			JsonValue imageVal = userWithToken.get("image");
			assertEquals(image, imageVal.getValueType() == JsonValue.ValueType.NULL ? null : userWithToken.getString("image"));
			return this;
		}

		UserWithTokenAssertions assertToken(String token) {
			assertEquals(token, userWithToken.getString("token"));
			return this;
		}
	}
}
