package realworld.user.services;

import static realworld.user.model.UserUpdateData.PropName.*;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import realworld.EntityDoesNotExistException;
import realworld.authentication.AuthenticationContext;
import realworld.authorization.NotAuthenticatedException;
import realworld.services.SimpleConstraintViolation;
import realworld.services.SimpleValidationException;
import realworld.user.model.ProfileData;
import realworld.user.model.UserData;
import realworld.user.model.UserLoginData;
import realworld.user.model.UserRegistrationData;
import realworld.user.model.UserUpdateData;

/**
 * Implementation of the {@link UserService}.
 */
@ApplicationScoped
@Transactional
class UserServiceImpl implements UserService {

	private UserDao userDao;

	private PasswordEncrypter encrypter;

	private AuthenticationContext authenticationContext;

	/**
	 * Default constructors for the frameworks.
	 */
	UserServiceImpl() {
		// NOOP
	}

	/**
	 * Full constructor for dependency injection.
	 *
	 * @param userDao The user DAO
	 * @param encrypter The password encrypter
	 * @param authenticationContext The authentication context
	 */
	@Inject
	public UserServiceImpl(UserDao userDao, PasswordEncrypter encrypter, AuthenticationContext authenticationContext) {
		this.userDao = userDao;
		this.encrypter = encrypter;
		this.authenticationContext = authenticationContext;
	}

	@Override
	public UserData register(@Valid UserRegistrationData registrationData) {
		List<SimpleConstraintViolation> errors = new ArrayList<>();

		if( userDao.usernameExists(registrationData.getUsername()) ) {
			errors.add(new SimpleConstraintViolation("username", "duplicate user name"));
		}
		if( userDao.emailExists(registrationData.getEmail()) ) {
			errors.add(new SimpleConstraintViolation("email", "duplicate email"));
		}

		if( !errors.isEmpty() ) {
			throw new SimpleValidationException(errors);
		}

		return userDao.add(UserData.make(registrationData.getUsername(), registrationData.getEmail(), null, null), encrypter.apply(registrationData.getPassword()));
	}

	@Override
	public UserData login(UserLoginData loginData) throws NotAuthenticatedException {
		return userDao.findByEmailAndPassword(loginData.getEmail(), loginData.getPassword())
				.orElseThrow(NotAuthenticatedException::new);
	}

	@Override
	public UserData getCurrentUser() {
		return userDao.findByUserName(authenticationContext.getUserPrincipal().getName()).get();
	}

	@Override
	public UserData update(@Valid UserUpdateData userUpdateData) {
		UserData u = userDao.findByUserName(authenticationContext.getUserPrincipal().getName()).get();

		List<SimpleConstraintViolation> errors = new ArrayList<>();

		if( userUpdateData.isExplicitlySet(USERNAME) && !userUpdateData.getUsername().equals(u.getUsername()) && userDao.usernameExists(userUpdateData.getUsername()) ) {
			errors.add(new SimpleConstraintViolation("username", "duplicate user name"));
		}
		if( userUpdateData.isExplicitlySet(EMAIL) && !userUpdateData.getEmail().equals(u.getEmail()) && userDao.emailExists(userUpdateData.getEmail()) ) {
			errors.add(new SimpleConstraintViolation("email", "duplicate email"));
		}

		if( !errors.isEmpty() ) {
			throw new SimpleValidationException(errors);
		}

		UserData newUserData = UserData.make(
				u.getId(),
				userUpdateData.isExplicitlySet(USERNAME) ? userUpdateData.getUsername() : u.getUsername(),
				userUpdateData.isExplicitlySet(EMAIL) ? userUpdateData.getEmail() : u.getEmail(),
				userUpdateData.isExplicitlySet(BIO) ? userUpdateData.getBio() : u.getBio(),
				userUpdateData.isExplicitlySet(IMAGE) ? userUpdateData.getImage() : u.getImage()
		);
		userDao.update(newUserData);
		if( userUpdateData.isExplicitlySet(PASSWORD) ) {
			userDao.changePassword(u.getId(), userUpdateData.getPassword());
		}

		return newUserData;
	}

	@Override
	public ProfileData findProfile(String username) {
		UserData userData = userDao.findByUserName(username).orElseThrow(EntityDoesNotExistException::new);
		boolean follows = Optional.ofNullable(authenticationContext.getUserPrincipal()).map(principal -> userDao.follows(principal.getUniqueId(), userData.getId())).orElse(false);
		return ProfileData.make(userData.getUsername(), userData.getBio(), userData.getImage(), follows);
	}

	@Override
	public ProfileData follow(String username) {
		UserData userData = userDao.findByUserName(username).orElseThrow(EntityDoesNotExistException::new);
		userDao.follow(authenticationContext.getUserPrincipal().getUniqueId(), userData.getId());
		return ProfileData.make(userData.getUsername(), userData.getBio(), userData.getImage(), true);
	}

	@Override
	public ProfileData unfollow(String username) {
		UserData userData = userDao.findByUserName(username).orElseThrow(EntityDoesNotExistException::new);
		userDao.unfollow(authenticationContext.getUserPrincipal().getUniqueId(), userData.getId());
		return ProfileData.make(userData.getUsername(), userData.getBio(), userData.getImage(), false);
	}
}
