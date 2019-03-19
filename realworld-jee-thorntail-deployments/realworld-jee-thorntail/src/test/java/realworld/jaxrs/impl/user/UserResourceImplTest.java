package realworld.jaxrs.impl.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static realworld.jaxrs.JaxRsApp.APPLICATION_PATH;
import static realworld.jaxrs.impl.user.UserWithTokenAssertions.assertUserWithToken;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.ws.rs.core.MediaType;

import org.jboss.resteasy.cdi.ResteasyCdiExtension;
import org.jboss.resteasy.core.Dispatcher;
import org.jboss.resteasy.mock.MockHttpRequest;
import org.jboss.resteasy.mock.MockHttpResponse;
import org.jboss.resteasy.plugins.server.resourcefactory.SingletonResource;
import org.jboss.resteasy.spi.metadata.DefaultResourceClass;
import org.jboss.weld.junit5.auto.ActivateScopes;
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
import realworld.jaxrs.sys.authentication.TokenAuthenticationConfig;
import realworld.jaxrs.test.CustomMockDispatcherFactory;
import realworld.services.DateTimeService;
import realworld.user.jaxrs.UpdateParam;
import realworld.user.jaxrs.UserResource;
import realworld.user.model.ImmutableUserData;
import realworld.user.model.UserUpdateData;
import realworld.user.services.UserService;

/**
 * Tests for the {@link UserResourceImpl}.
 */
@EnableAutoWeld
@AddBeanClasses(ObjectMapperProvider.class)
@AddExtensions(ResteasyCdiExtension.class)
@ActivateScopes(RequestScoped.class)
@ExtendWith(MockitoExtension.class)
public class UserResourceImplTest {

	private static final String USER_ID = "userid";
	private static final String USERNAME = "username";
	private static final String EMAIL = "userid@here.com";
	private static final String IMAGE = "http://pictures.com/image1";
	private static final String BIO = "Bio";
	private static final String PASSWORD = "P@ssword";

	@Produces @Mock
	private UserService userService;

	@Produces @Mock
	private JwtService jwtService;

	@Produces @Mock
	private DateTimeService dateTimeService;

	@Produces @Mock
	private TokenAuthenticationConfig tokenAuthenticationConfig;

	@Inject
	private UserResourceImpl sut;

	private Dispatcher dispatcher;

	private MockHttpResponse response;

	@BeforeEach
	void init() {
		dispatcher = CustomMockDispatcherFactory.createDispatcher();
		SingletonResource resourceFactory = new SingletonResource(sut, new DefaultResourceClass(UserResource.class, null));
		dispatcher.getRegistry().addResourceFactory(resourceFactory, APPLICATION_PATH);
		response = new MockHttpResponse();
	}

	@Test
	void testGet() throws Exception {
		MockHttpRequest request = MockHttpRequest.get(APPLICATION_PATH + "/user");
		request.accept(MediaType.APPLICATION_JSON);
		request.getHttpHeaders().getRequestHeaders().add("Authorization", "Token THE_TOKEN");

		when(userService.getCurrentUser()).thenReturn(ImmutableUserData.builder().id(USER_ID).username(USERNAME).email(EMAIL).bio(BIO).image(IMAGE).build());

		dispatcher.invoke(request, response);

		assertUserWithToken(response)
				.assertUsername(USERNAME)
				.assertEmail(EMAIL)
				.assertBio(BIO)
				.assertImage(IMAGE)
				.assertToken("THE_TOKEN");
	}

	@Test
	void testUpdate() throws Exception {
		MockHttpRequest request = MockHttpRequest.put(APPLICATION_PATH + "/user");
		request.accept(MediaType.APPLICATION_JSON);
		request.getHttpHeaders().getRequestHeaders().add("Authorization", "Token THE_TOKEN");
		request.contentType(MediaType.APPLICATION_JSON);
		request.content(("{\"user\":{\"email\":\"" + EMAIL + "\", \"password\":\"" + PASSWORD + "\", \"username\":\"" + USERNAME + "\", \"bio\":\"" + BIO + "\", \"image\":\"" + IMAGE + "\", \"password\":\"" + PASSWORD + "\"}}").getBytes());

		when(userService.update(any())).thenAnswer(a -> {
			UpdateParam p = a.getArgument(0);
			return ImmutableUserData.builder().id(USER_ID).username(p.getUsername()).email(p.getEmail()).bio(p.getBio()).image(p.getImage()).build();
		});

		dispatcher.invoke(request, response);

		ArgumentCaptor<UserUpdateData> captor = ArgumentCaptor.forClass(UserUpdateData.class);
		verify(userService).update(captor.capture());
		assertEquals(PASSWORD, captor.getValue().getPassword());

		assertUserWithToken(response)
				.assertUsername(USERNAME)
				.assertEmail(EMAIL)
				.assertBio(BIO)
				.assertImage(IMAGE)
				.assertToken("THE_TOKEN");
	}
}
