package realworld.jaxrs.impl.user;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import realworld.user.jaxrs.LoginParam;
import realworld.user.jaxrs.RegisterParam;
import realworld.user.jaxrs.UserWithToken;
import realworld.user.jaxrs.UsersResource;
import realworld.jaxrs.sys.authentication.JwtService;
import realworld.user.model.UserData;
import realworld.user.services.UserService;

/**
 * Implementation of the {@link UsersResource}.
 */
@ApplicationScoped
public class UsersResourceImpl implements UsersResource {

	@Inject
	private UserService userService;

	@Inject
	private JwtService jwtService;

	@Override
	public UserWithToken register(RegisterParam registerParam) {
		UserData userData = userService.register(registerParam);
		return new UserWithToken(userData.getUsername(), userData.getEmail(), userData.getBio(), userData.getImage(), jwtService.toToken(userData));
	}

	@Override
	public UserWithToken login(LoginParam loginParam) {
		UserData userData = userService.login(loginParam);
		return new UserWithToken(userData.getUsername(), userData.getEmail(), userData.getBio(), userData.getImage(), jwtService.toToken(userData));
	}
}
