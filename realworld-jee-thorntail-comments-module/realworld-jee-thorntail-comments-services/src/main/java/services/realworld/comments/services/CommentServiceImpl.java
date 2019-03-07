package services.realworld.comments.services;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import realworld.EntityDoesNotExistException;
import realworld.article.services.ArticleService;
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

	private ArticleService articleService;

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
	 * @param articleService        The article service
	 * @param authenticationContext The authentication context
	 * @param userService           The user service
	 * @param dateTimeService       The date/time service
	 */
	@Inject
	public CommentServiceImpl(CommentDao commentDao, ArticleService articleService, AuthenticationContext authenticationContext, UserService userService, DateTimeService dateTimeService) {
		this.commentDao = commentDao;
		this.articleService = articleService;
		this.authenticationContext = authenticationContext;
		this.userService = userService;
		this.dateTimeService = dateTimeService;
	}

	@Override
	public CommentData add(String articleSlug, CommentCreationData creationData) throws EntityDoesNotExistException {
		String articleId = articleService.findArticleIdBySlug(articleSlug);
		String userId = authenticationContext.getUserPrincipal().getUniqueId();
		ProfileData author = userService.findProfileById(userId);
		CommentWithLinks comment = commentDao.add(creationData, articleId, userId, dateTimeService.getNow());
		return ImmutableCommentData.builder().from(comment).author(author).build();
	}

	@Override
	public List<CommentData> findArticleComments(String articleSlug) throws EntityDoesNotExistException {
		String articleId = articleService.findArticleIdBySlug(articleSlug);
		List<CommentWithLinks> comments = commentDao.findArticleComments(articleId);
		Map<String, ProfileData> profiles = comments.stream()
				.map(CommentWithLinks::getAuthorId)
				.distinct()
				.collect(Collectors.toMap(Function.identity(), userService::findProfileById));
		return comments.stream()
				.map(c -> ImmutableCommentData.builder().from(c).author(profiles.get(c.getAuthorId())).build())
				.collect(Collectors.toList());
	}

	@Override
	public void delete(String id) {
		commentDao.delete(id);
	}
}
