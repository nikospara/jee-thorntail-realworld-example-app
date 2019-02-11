package realworld.user.services;

import static javax.interceptor.Interceptor.Priority.APPLICATION;

import javax.annotation.Priority;
import javax.decorator.Decorator;
import javax.decorator.Delegate;
import javax.inject.Inject;

import realworld.authorization.Authorization;
import realworld.user.model.ProfileData;
import realworld.user.model.UserData;
import realworld.user.model.UserUpdateData;

/**
 * Security for the {@link UserService} implementation.
 */
@Decorator
@Priority(APPLICATION)
abstract class UserServiceAuthorizer implements UserService {

	private UserService delegate;

	private Authorization authorization;

	/**
	 * Injection constructor.
	 *
	 * @param delegate      The delegate of this decorator
	 * @param authorization The authorization utilities
	 */
	@Inject
	public UserServiceAuthorizer(@Delegate UserService delegate, Authorization authorization) {
		this.delegate = delegate;
		this.authorization = authorization;
	}

	@Override
	public UserData getCurrentUser() {
		authorization.requireLogin();
		return delegate.getCurrentUser();
	}

	@Override
	public UserData update(UserUpdateData userUpdateData) {
		authorization.requireLogin();
		return delegate.update(userUpdateData);
	}

	@Override
	public ProfileData follow(String username) {
		authorization.requireLogin();
		return delegate.follow(username);
	}

	@Override
	public ProfileData unfollow(String username) {
		authorization.requireLogin();
		return delegate.unfollow(username);
	}
}
