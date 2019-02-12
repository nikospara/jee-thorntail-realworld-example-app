package realworld.jaxrs.impl.user;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import realworld.user.jaxrs.ProfileResource;
import realworld.user.model.ProfileData;
import realworld.user.services.UserService;

/**
 * Implementation of the {@link ProfileResource}.
 */
@ApplicationScoped
public class ProfileResourceImpl implements ProfileResource {

	@Inject
	private UserService userService;

	@Override
	public ProfileData get(String username) {
		return userService.findProfile(username);
	}

	@Override
	public ProfileData follow(String username) {
		return userService.follow(username);
	}

	@Override
	public ProfileData unfollow(String username) {
		return userService.unfollow(username);
	}
}
