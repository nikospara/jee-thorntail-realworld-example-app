package realworld.jaxrs.impl.user;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import realworld.user.jaxrs.ProfileDataWrapper;
import realworld.user.jaxrs.ProfileResource;
import realworld.user.services.UserService;

/**
 * Implementation of the {@link ProfileResource}.
 */
@ApplicationScoped
public class ProfileResourceImpl implements ProfileResource {

	@Inject
	private UserService userService;

	@Override
	public ProfileDataWrapper get(String username) {
		return new ProfileDataWrapper(userService.findProfile(username));
	}

	@Override
	public ProfileDataWrapper follow(String username) {
		return new ProfileDataWrapper(userService.follow(username));
	}

	@Override
	public ProfileDataWrapper unfollow(String username) {
		return new ProfileDataWrapper(userService.unfollow(username));
	}
}
