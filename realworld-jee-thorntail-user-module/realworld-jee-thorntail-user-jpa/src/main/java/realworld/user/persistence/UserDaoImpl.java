package realworld.user.persistence;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Root;

import java.util.Optional;
import java.util.UUID;
import java.util.function.BiFunction;

import realworld.EntityDoesNotExistException;
import realworld.user.model.UserData;
import realworld.user.services.UserDao;

/**
 * DAO for the {@link User} entity.
 */
@ApplicationScoped
class UserDaoImpl implements UserDao {

	private EntityManager em;

	/**
	 * Default constructors for the frameworks.
	 */
	UserDaoImpl() {
		// NOOP
	}

	/**
	 * Full constructor for dependency injection.
	 *
	 * @param em The JPA entity manager
	 */
	@Inject
	public UserDaoImpl(EntityManager em) {
		this.em = em;
	}

	@Override
	public UserData add(UserData user, String password) {
		User u = new User();
		u.setId(UUID.randomUUID().toString());
		u.setUsername(user.getUsername());
		u.setPassword(password);
		u.setEmail(user.getEmail());
		u.setBio(user.getBio());
		u.setImage(user.getImage());
		em.persist(u);
		return UserData.make(u.getId(), u.getUsername(), u.getEmail(), u.getBio(), u.getImage());
	}

	@Override
	public boolean usernameExists(String username) {
		return unique((cb, root) -> cb.equal(root.get(User_.username), username));
	}

	@Override
	public boolean emailExists(String email) {
		return unique((cb, root) -> cb.equal(cb.lower(root.get(User_.email)), email.toLowerCase()));
	}

	private boolean unique(BiFunction<CriteriaBuilder, Root<User>, Expression<Boolean>> callback) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<String> query = cb.createQuery(String.class);
		Root<User> root = query.from(User.class);
		query.select(root.get(User_.username));
		query.where(callback.apply(cb, root));
		return !em.createQuery(query).setMaxResults(1).getResultList().isEmpty();
	}

	@Override
	public Optional<UserData> findByEmailAndPassword(String email, String password) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<User> query = cb.createQuery(User.class);
		Root<User> root = query.from(User.class);
		query.where(cb.and(
				cb.equal(cb.lower(root.get(User_.email)), email.toLowerCase()),
				cb.equal(root.get(User_.password), password)
		));
		return em.createQuery(query).setMaxResults(1).getResultList().stream()
				.findFirst()
				.map(u -> UserData.make(u.getId(), u.getUsername(), u.getEmail(), u.getBio(), u.getImage()));
	}

	@Override
	public Optional<UserData> findByUserName(String username) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<User> query = cb.createQuery(User.class);
		Root<User> root = query.from(User.class);
		query.where(cb.equal(root.get(User_.username), username));
		return em.createQuery(query).setMaxResults(1).getResultList().stream()
				.findFirst()
				.map(u -> UserData.make(u.getId(), u.getUsername(), u.getEmail(), u.getBio(), u.getImage()));
	}

	@Override
	public UserData update(UserData user) {
		User u = em.find(User.class, user.getId());
		if( u == null ) {
			throw new EntityDoesNotExistException();
		}
		u.setUsername(user.getUsername());
		u.setEmail(user.getEmail());
		u.setBio(user.getBio());
		u.setImage(user.getImage());
		return user;
	}

	@Override
	public void changePassword(String userId, String newPassword) {
		User u = em.find(User.class, userId);
		if( u == null ) {
			throw new EntityDoesNotExistException();
		}
		u.setPassword(newPassword);
	}

	@Override
	public boolean follows(String followerId, String followedId) {
		FollowId followId = new FollowId(followerId, followedId);
		Follow follow = em.find(Follow.class, followId);
		return follow != null;
	}

	@Override
	public void follow(String followerId, String followedId) {
		FollowId followId = new FollowId(followerId, followedId);
		if( em.find(Follow.class, followId) == null ) {
			Follow follow = new Follow();
			User follower = em.getReference(User.class, followerId);
			User followed = em.getReference(User.class, followedId);
			follow.setFollower(follower);
			follow.setFollowed(followed);
			em.persist(follow);
		}
	}

	@Override
	public void unfollow(String followerId, String followedId) {
		FollowId followId = new FollowId(followerId, followedId);
		Follow follow = em.find(Follow.class, followId);
		if( follow != null ) {
			em.remove(follow);
		}
	}
}
