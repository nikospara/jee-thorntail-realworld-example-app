package realworld.user.services;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import realworld.user.model.UserData;

/**
 * DAO interfaces for the User entity.
 */
public interface UserDao {

	/**
	 * Add a user to the store.
	 *
	 * @param user     The user data
	 * @param password The initial password
	 * @return A user object, could be the same as the input
	 */
	UserData add(UserData user, String password);

	/**
	 * Check if the given user name exists in the DB.
	 *
	 * @param username The user name to check
	 * @return Whether the user name exists
	 */
	boolean usernameExists(String username);

	/**
	 * Check if the given email exists in the DB. Comparison is case-insensitive.
	 *
	 * @param email The email to check
	 * @return Whether the email exists
	 */
	boolean emailExists(String email);

	/**
	 * Find a user by email (case-insensitive comparison) and password.
	 *
	 * @param email    The email
	 * @param password The encrypted password
	 * @return The user profile
	 */
	Optional<UserData> findByEmailAndPassword(String email, String password);

	/**
	 * Find user by user name.
	 *
	 * @param username The user name
	 * @return The user with the given user name
	 */
	Optional<UserData> findByUserName(String username);

	/**
	 * Find user by id.
	 *
	 * @param userid The user id
	 * @return The user with the given user name
	 */
	Optional<UserData> findById(String userid);

	/**
	 * Update the database for the given user.
	 *
	 * @param user The user to update; the id stays the same, all other properties are written back
	 * @return The updated user
	 */
	UserData update(UserData user);

	/**
	 * Set the password for the given user id.
	 *
	 * @param userId      The user id of the user to change the password
	 * @param newPassword The new password
	 */
	void changePassword(String userId, String newPassword);

	/**
	 * Check if the given user follows the given user name.
	 *
	 * @param followerId Id of the follower
	 * @param followedId Id of the followed user
	 */
	boolean follows(String followerId, String followedId);

	/**
	 * Add the follower to the followers of the followed.
	 *
	 * @param followerId Id of the follower
	 * @param followedId Id of the followed user
	 */
	void follow(String followerId, String followedId);

	/**
	 * Remove the follower from the followers of the followed.
	 *
	 * @param followerId Id of the follower
	 * @param followedId Id of the no longer followed user
	 */
	void unfollow(String followerId, String followedId);

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
