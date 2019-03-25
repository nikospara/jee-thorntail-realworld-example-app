package realworld.article.services;

import static realworld.article.model.ArticleUpdateData.PropName.BODY;
import static realworld.article.model.ArticleUpdateData.PropName.DESCRIPTION;
import static realworld.article.model.ArticleUpdateData.PropName.TITLE;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import realworld.EntityDoesNotExistException;
import realworld.article.model.ArticleCreationData;
import realworld.article.model.ArticleData;
import realworld.article.model.ArticleResult;
import realworld.article.model.ArticleUpdateData;
import realworld.article.model.ArticleWithLinks;
import realworld.article.model.ImmutableArticleData;
import realworld.authentication.AuthenticationContext;
import realworld.authentication.User;
import realworld.comments.model.CommentCreationData;
import realworld.comments.model.CommentData;
import realworld.services.DateTimeService;
import realworld.user.model.ProfileData;
import realworld.user.services.UserService;
import realworld.comments.services.CommentService;

/**
 * Implementation of the {@link ArticleService}.
 */
@ApplicationScoped
@Transactional(dontRollbackOn = EntityDoesNotExistException.class)
class ArticleServiceImpl implements ArticleService {

	private static final ArticleSearchCriteria DEFAULT_CRITERIA = ArticleSearchCriteria.builder().withLimit(20).withOffset(0).build();

	private ArticleDao articleDao;

	private AuthenticationContext authenticationContext;
	
	private DateTimeService dateTimeService;

	private UserService userService;

	private CommentService commentService;

	/**
	 * Default constructor for frameworks.
	 */
	ArticleServiceImpl() {
		// NOOP
	}

	@Inject
	public ArticleServiceImpl(ArticleDao articleDao, AuthenticationContext authenticationContext, DateTimeService dateTimeService, UserService userService, CommentService commentService) {
		this.articleDao = articleDao;
		this.authenticationContext = authenticationContext;
		this.dateTimeService = dateTimeService;
		this.userService = userService;
		this.commentService = commentService;
	}

	@Override
	public ArticleResult<ArticleData> find(ArticleSearchCriteria criteria) {
		if( criteria.getAuthors() != null && !criteria.getAuthors().isEmpty() ) {
			var authorIds = new ArrayList<>(userService.mapUserNamesToIds(criteria.getAuthors()).values());
			if (authorIds.isEmpty()) {
				return new ArticleResult<>(Collections.emptyList(), 0L);
			}
			criteria = criteria.withAuthors(authorIds);
		}

		return findWithAuthorIds(criteria);
	}

	private ArticleResult<ArticleData> findWithAuthorIds(ArticleSearchCriteria criteria) {
		if( criteria.getFavoritedBy() != null ) {
			try {
				criteria = criteria.withFavoritedBy(userService.findByUserName(criteria.getFavoritedBy()).getId());
			}
			catch( EntityDoesNotExistException e) {
				return new ArticleResult<>(Collections.emptyList(), 0L);
			}
		}
		var searchResult = articleDao.find(Optional.ofNullable(authenticationContext.getUserPrincipal()).map(User::getUniqueId).orElse(null), criteria, DEFAULT_CRITERIA);
		Map<String, ProfileData> profiles = searchResult.getArticles().stream()
				.map(ArticleWithLinks::getAuthorId)
				.distinct()
				.collect(Collectors.toMap(Function.identity(), userService::findProfileById));
		var result = new ArticleResult<ArticleData>();
		result.setArticlesCount(searchResult.getArticlesCount());
		result.setArticles(searchResult.getArticles().stream()
				.map(a -> ImmutableArticleData.builder().from(a).author(profiles.get(a.getAuthorId())).tags(articleDao.findTags(a.getId())).build())
				.collect(Collectors.toList())
		);
		return result;
	}

	@Override
	public ArticleResult<ArticleData> feed(ArticleSearchCriteria criteria) {
		List<String> followedUserIds = userService.findFollowedUserIds(authenticationContext.getUserPrincipal().getName());
		criteria = criteria.withAuthors(followedUserIds);
		return findWithAuthorIds(criteria);
	}

	@Override
	public ArticleData findArticleBySlug(String slug) {
		String userId = Optional.ofNullable(authenticationContext.getUserPrincipal()).map(User::getUniqueId).orElse("");
		ArticleWithLinks article = articleDao.findArticleBySlug(userId, slug);
		Set<String> tags = articleDao.findTags(article.getId());
		return ImmutableArticleData.builder().from(article).author(userService.findProfileById(article.getAuthorId())).tags(tags).build();
	}

	@Override
	public String findArticleIdBySlug(String slug) {
		return articleDao.findArticleIdBySlug(slug);
	}

	@Override
	public ArticleData create(ArticleCreationData creationData) {
		String authorId = authenticationContext.getUserPrincipal().getUniqueId();
		ArticleWithLinks article = articleDao.create(creationData, makeSlug(creationData.getTitle()), dateTimeService.getNow(), authorId, creationData.getTags());
		return ImmutableArticleData.builder().from(article).author(userService.findProfileById(authorId)).tags(creationData.getTags()).build();
	}

	@Override
	public ArticleData update(String slug, ArticleUpdateData updateData) {
		ArticleData a = findArticleBySlug(slug);
		String newTitle = updateData.isExplicitlySet(TITLE) ? updateData.getTitle() : a.getTitle();
		String newSlug = updateData.isExplicitlySet(TITLE) ? makeSlug(updateData.getTitle()) : a.getSlug();
		String newDescription = updateData.isExplicitlySet(DESCRIPTION) ? updateData.getDescription() : a.getDescription();
		String newBody = updateData.isExplicitlySet(BODY) ? updateData.getBody() : a.getBody();
		LocalDateTime updatedAt = dateTimeService.getNow();
		articleDao.update(a.getId(), newTitle, newSlug, newDescription, newBody, updateData.getTags(), updatedAt);
		return ImmutableArticleData.builder().from(a).slug(newSlug).title(newTitle).description(newDescription).body(newBody).updatedAt(updatedAt).build();
	}

	@Override
	public void delete(String slug) {
		articleDao.delete(slug);
	}

	@Override
	public String makeSlug(String title) {
		return title.toLowerCase().replace(' ', '-').replaceAll("[^a-z0-9-]", "");
	}

	@Override
	public ArticleData favorite(String slug) {
		String userId = userService.getCurrentUser().getId();
		ArticleWithLinks article = articleDao.findArticleBySlug(userId, slug);
		Set<String> tags = articleDao.findTags(article.getId());
		if( !article.isFavorited() ) {
			articleDao.addFavorite(userId, article.getId());
			return ImmutableArticleData.builder().from(article).isFavorited(true).favoritesCount(article.getFavoritesCount()+1).author(userService.findProfileById(article.getAuthorId())).tags(tags).build();
		}
		return ImmutableArticleData.builder().from(article).author(userService.findProfileById(article.getAuthorId())).tags(tags).build();
	}

	@Override
	public ArticleData unfavorite(String slug) {
		String userId = userService.getCurrentUser().getId();
		ArticleWithLinks article = articleDao.findArticleBySlug(userId, slug);
		Set<String> tags = articleDao.findTags(article.getId());
		if( article.isFavorited() ) {
			articleDao.removeFavorite(userId, article.getId());
			return ImmutableArticleData.builder().from(article).isFavorited(false).favoritesCount(article.getFavoritesCount()-1).author(userService.findProfileById(article.getAuthorId())).tags(tags).build();
		}
		return ImmutableArticleData.builder().from(article).author(userService.findProfileById(article.getAuthorId())).tags(tags).build();
	}

	@Override
	public CommentData comment(String slug, CommentCreationData creationData) {
		String articleId = articleDao.findArticleIdBySlug(slug);
		CommentData comment = commentService.create(creationData);
		articleDao.comment(articleId, comment.getId(), comment.getCreatedAt());
		return comment;
	}

	@Override
	public List<CommentData> findArticleComments(String slug) {
		String articleId = findArticleIdBySlug(slug);
		List<String> commentIds = articleDao.findCommentIds(articleId);
		return commentService.findCommentsWithIds(commentIds);
	}

	@Override
	public void deleteArticleComment(String slug, String commentId) {
		commentService.delete(commentId);
		articleDao.deleteComment(slug, commentId);
	}
}
