package realworld.jaxrs.impl.user;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;

import realworld.user.jaxrs.UpdateParam;
import realworld.user.jaxrs.UserResource;
import realworld.user.jaxrs.UserWithToken;
import realworld.jaxrs.sys.authentication.TokenHelper;
import realworld.user.model.UserData;
import realworld.user.services.UserService;

/**
 * Implementation of the {@link UserResource}.
 */
@RequestScoped
public class UserResourceImpl implements UserResource {

	@Inject
	private UserService userService;

	@Inject
	private TokenHelper tokenHelper;

	@Context
	private HttpHeaders headers;

	@Override
	public UserWithToken get() {
		UserData userData = userService.getCurrentUser();
		return new UserWithToken(userData.getUsername(), userData.getEmail(), userData.getBio(), userData.getImage(), tokenHelper.extractRawToken(headers));
	}

	@Override
	public UserWithToken update(UpdateParam updateParam) {
		UserData userData = userService.update(updateParam);
		return new UserWithToken(userData.getUsername(), userData.getEmail(), userData.getBio(), userData.getImage(), tokenHelper.extractRawToken(headers));
	}
}
