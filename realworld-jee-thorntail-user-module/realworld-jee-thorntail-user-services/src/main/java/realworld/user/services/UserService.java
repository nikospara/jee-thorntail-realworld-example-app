package realworld.user.services;

import javax.validation.Valid;

import java.util.List;
import java.util.Map;

import realworld.EntityDoesNotExistException;
import realworld.authorization.NotAuthenticatedException;
import realworld.user.model.ProfileData;
import realworld.user.model.UserLoginData;
import realworld.user.model.UserData;
import realworld.user.model.UserRegistrationData;
import realworld.user.model.UserUpdateData;

/**
 * User services.
 */
public interface UserService {

	/**
	 * Register a user.
	 *
	 * @param registrationData User registration data
	 * @return The full user profile - never {@code null}
	 */
	UserData register(@Valid UserRegistrationData registrationData);

	/**
	 * Check that the user exists and has the correct password.
	 *
	 * @param loginData User email and password
	 * @return The user profile, if the user exists and the password is correct, throws otherwise
	 * @throws NotAuthenticatedException If authentication fails
	 */
	UserData login(@Valid UserLoginData loginData) throws NotAuthenticatedException;

	/**
	 * Get the profile data of the current user.
	 *
	 * @return The current user profile
	 */
	UserData getCurrentUser();

	/**
	 * Update the current user.
	 *
	 * @param userUpdateData User update data
	 * @return The updated user profile data
	 */
	UserData update(@Valid UserUpdateData userUpdateData);

	/**
	 * Find by user name.
	 *
	 * @param username The user name
	 * @return The user
	 * @throws EntityDoesNotExistException If not found
	 */
	UserData findByUserName(String username) throws EntityDoesNotExistException;

	/**
	 * Find the profile of the user with the given user name.
	 *
	 * @param username The user name
	 * @return The profile
	 */
	ProfileData findProfile(String username);

	/**
	 * Find the profile of the user with the given id.
	 *
	 * @param userid The user id
	 * @return The profile
	 */
	ProfileData findProfileById(String userid);

	/**
	 * As the current user, follow the given user.
	 *
	 * @param username The user to follow
	 * @return The followed user profile
	 */
	ProfileData follow(String username);

	/**
	 * As the current user, stop following the given user.
	 *
	 * @param username The user to stop following
	 * @return The unfollowed user profile
	 */
	ProfileData unfollow(String username);

	/**
	 * Get the ids of the users followed by the given follower user.
	 *
	 * @param followerName The follower user name
	 * @return The followed user ids
	 */
	List<String> findFollowedUserIds(String followerName);

	/**
	 * Map each of the given user names to their ids, if a user with the given name exists.
	 *
	 * @param usernames The list of user names
	 * @return Map user name to id for those names that exist
	 */
	Map<String,String> mapUserNamesToIds(List<String> usernames);
}
