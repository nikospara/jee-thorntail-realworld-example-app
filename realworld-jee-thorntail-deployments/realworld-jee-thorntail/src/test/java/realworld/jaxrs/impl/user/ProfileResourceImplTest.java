package realworld.jaxrs.impl.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;
import static realworld.jaxrs.JaxRsApp.APPLICATION_PATH;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
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
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import realworld.jaxrs.sys.ObjectMapperProvider;
import realworld.jaxrs.test.CustomMockDispatcherFactory;
import realworld.user.jaxrs.ProfileResource;
import realworld.user.model.ImmutableProfileData;
import realworld.user.services.UserService;

/**
 * Tests for the {@link ProfileResourceImpl}.
 */
@EnableAutoWeld
@AddBeanClasses(ObjectMapperProvider.class)
@AddExtensions(ResteasyCdiExtension.class)
@ExtendWith(MockitoExtension.class)
public class ProfileResourceImplTest {

	private static final String USERNAME = "theUser";

	@Produces @Mock
	private UserService userService;

	@Inject
	private ProfileResourceImpl sut;

	private Dispatcher dispatcher;

	private MockHttpResponse response;

	@BeforeEach
	void init() {
		dispatcher = CustomMockDispatcherFactory.createDispatcher();
		SingletonResource resourceFactory = new SingletonResource(sut, new DefaultResourceClass(ProfileResource.class, null));
		dispatcher.getRegistry().addResourceFactory(resourceFactory, APPLICATION_PATH);
		response = new MockHttpResponse();
	}

	@Test
	void testGet() throws Exception {
		MockHttpRequest request = MockHttpRequest.get(APPLICATION_PATH + "/profiles/" + USERNAME);
		when(userService.findProfile(USERNAME)).thenReturn(ImmutableProfileData.builder().username(USERNAME).isFollowing(false).build());

		dispatcher.invoke(request, response);

		assertProfileNode()
				.assertFollowing(false);
	}

	@Test
	void testFollow() throws Exception {
		MockHttpRequest request = MockHttpRequest.post(APPLICATION_PATH + "/profiles/" + USERNAME + "/follow");
		when(userService.follow(USERNAME)).thenReturn(ImmutableProfileData.builder().username(USERNAME).isFollowing(true).build());

		dispatcher.invoke(request, response);

		assertProfileNode()
				.assertFollowing(true);
	}

	@Test
	void testUnfollow() throws Exception {
		MockHttpRequest request = MockHttpRequest.delete(APPLICATION_PATH + "/profiles/" + USERNAME + "/follow");
		when(userService.unfollow(USERNAME)).thenReturn(ImmutableProfileData.builder().username(USERNAME).isFollowing(false).build());

		dispatcher.invoke(request, response);

		assertProfileNode()
				.assertFollowing(false);
	}

	private ProfileDataWrapperAssertions assertProfileNode() throws UnsupportedEncodingException {
		assertEquals(200, response.getStatus());
		JsonReader jsonReader = Json.createReader(new StringReader(response.getContentAsString()));
		JsonObject jobj = jsonReader.readObject();
		assertEquals(1, jobj.size());
		JsonObject profile = jobj.getJsonObject("profile");
		assertNotNull(profile);
		assertEquals(4, profile.size());
		return new ProfileDataWrapperAssertions(profile);
	}


	private static class ProfileDataWrapperAssertions {
		private JsonObject profile;

		ProfileDataWrapperAssertions(JsonObject profile) {
			this.profile = profile;
		}

		ProfileDataWrapperAssertions assertFollowing(boolean following) {
			assertEquals(following, profile.getBoolean("following"));
			return this;
		}
	}
}
