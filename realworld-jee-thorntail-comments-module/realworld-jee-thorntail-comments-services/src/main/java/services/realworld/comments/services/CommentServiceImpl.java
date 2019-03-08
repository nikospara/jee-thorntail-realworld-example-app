package services.realworld.comments.services;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import realworld.EntityDoesNotExistException;
import realworld.authentication.AuthenticationContext;
import realworld.comments.model.CommentCreationData;
import realworld.comments.model.CommentData;
import realworld.comments.model.CommentWithLinks;
import realworld.comments.model.ImmutableCommentData;
import realworld.services.DateTimeService;
import realworld.user.model.ProfileData;
import realworld.user.services.UserService;

/**
 * Implementation of the {@link CommentService}.
 */
@ApplicationScoped
@Transactional(dontRollbackOn = EntityDoesNotExistException.class)
class CommentServiceImpl implements CommentService {

	private CommentDao commentDao;

	private AuthenticationContext authenticationContext;

	private UserService userService;

	private DateTimeService dateTimeService;

	/**
	 * Default constructor for frameworks.
	 */
	CommentServiceImpl() {
		// NOOP
	}

	/**
	 * Injection constructor.
	 *
	 * @param commentDao            The DAO
	 * @param authenticationContext The authentication context
	 * @param userService           The user service
	 * @param dateTimeService       The date/time service
	 */
	@Inject
	public CommentServiceImpl(CommentDao commentDao, AuthenticationContext authenticationContext, UserService userService, DateTimeService dateTimeService) {
		this.commentDao = commentDao;
		this.authenticationContext = authenticationContext;
		this.userService = userService;
		this.dateTimeService = dateTimeService;
	}

	@Override
	public CommentData create(CommentCreationData creationData) {
		String userId = authenticationContext.getUserPrincipal().getUniqueId();
		ProfileData author = userService.findProfileById(userId);
		CommentWithLinks comment = commentDao.create(creationData, userId, dateTimeService.getNow());
		return ImmutableCommentData.builder().from(comment).author(author).build();
	}

	@Override
	public void delete(String id) {
		commentDao.delete(id);
	}

	@Override
	public List<CommentData> findCommentsWithIds(List<String> ids) {
		List<CommentWithLinks> comments = commentDao.findCommentsWithIds(ids);
		Map<String, ProfileData> profiles = comments.stream()
				.map(CommentWithLinks::getAuthorId)
				.distinct()
				.collect(Collectors.toMap(Function.identity(), userService::findProfileById));
		return comments.stream()
				.map(c -> ImmutableCommentData.builder().from(c).author(profiles.get(c.getAuthorId())).build())
				.collect(Collectors.toList());
	}
}
