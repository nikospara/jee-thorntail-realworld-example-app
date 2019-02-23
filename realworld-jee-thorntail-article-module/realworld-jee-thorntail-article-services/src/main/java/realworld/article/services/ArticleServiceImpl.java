package realworld.article.services;

import static realworld.article.model.ArticleUpdateData.PropName.BODY;
import static realworld.article.model.ArticleUpdateData.PropName.DESCRIPTION;
import static realworld.article.model.ArticleUpdateData.PropName.TITLE;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import realworld.EntityDoesNotExistException;
import realworld.article.model.ArticleResult;
import realworld.article.model.ArticleUpdateData;
import realworld.article.model.ArticleWithLinks;
import realworld.authentication.AuthenticationContext;
import realworld.article.model.ArticleData;
import realworld.article.model.ArticleCreationData;
import realworld.authentication.User;
import realworld.services.DateTimeService;
import realworld.user.model.ProfileData;
import realworld.user.services.UserService;

/**
 * Implementation of the {@link ArticleService}.
 */
@ApplicationScoped
@Transactional(dontRollbackOn = EntityDoesNotExistException.class)
class ArticleServiceImpl implements ArticleService {
	
	private ArticleDao articleDao;

	private AuthenticationContext authenticationContext;
	
	private DateTimeService dateTimeService;

	private UserService userService;

	private static final ArticleSearchCriteria DEFAULT_CRITERIA = ArticleSearchCriteria.builder().withLimit(20).withOffset(0).build();
	
	/**
	 * Default constructor for the frameworks.
	 */
	ArticleServiceImpl() {
		// NOOP
	}

	@Inject
	public ArticleServiceImpl(ArticleDao articleDao, AuthenticationContext authenticationContext, DateTimeService dateTimeService, UserService userService) {
		this.articleDao = articleDao;
		this.authenticationContext = authenticationContext;
		this.dateTimeService = dateTimeService;
		this.userService = userService;
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
		var searchResult = articleDao.find(authenticationContext.getUserPrincipal().getUniqueId(), criteria, DEFAULT_CRITERIA);
		Map<String, ProfileData> profiles = searchResult.getArticles().stream()
				.map(ArticleWithLinks::getAuthorId)
				.collect(Collectors.toSet()).stream()
				.collect(Collectors.toMap(Function.identity(), userService::findProfileById));
		var result = new ArticleResult<ArticleData>();
		result.setArticlesCount(searchResult.getArticlesCount());
		result.setArticles(searchResult.getArticles().stream()
				.map(a -> ArticleData.make(a, profiles.get(a.getAuthorId()), articleDao.findTags(a.getId())))
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
	public ArticleData findArticleBySlug(String slug) throws EntityDoesNotExistException {
		String userId = Optional.ofNullable(authenticationContext.getUserPrincipal()).map(User::getUniqueId).orElse("");
		ArticleWithLinks article = articleDao.findArticleBySlug(userId, slug);
		Set<String> tags = articleDao.findTags(article.getId());
		return ArticleData.make(article, userService.findProfileById(article.getAuthorId()), tags);
	}

	@Override
	public ArticleData create(ArticleCreationData creationData) {
		String authorId = authenticationContext.getUserPrincipal().getUniqueId();
		ArticleWithLinks article = articleDao.create(creationData, makeSlug(creationData.getTitle()), new Date(dateTimeService.currentTimeMillis()), authorId, creationData.getTags());
		return ArticleData.make(article, userService.findProfileById(authorId), creationData.getTags());
	}

	@Override
	public ArticleData update(String slug, ArticleUpdateData updateData) throws EntityDoesNotExistException {
		ArticleData article = findArticleBySlug(slug);
		String newTitle = updateData.isExplicitlySet(TITLE) ? updateData.getTitle() : article.getTitle();
		String newSlug = updateData.isExplicitlySet(TITLE) ? makeSlug(updateData.getTitle()) : article.getSlug();
		String newDescription = updateData.isExplicitlySet(DESCRIPTION) ? updateData.getDescription() : article.getDescription();
		String newBody = updateData.isExplicitlySet(BODY) ? updateData.getBody() : article.getBody();
		Date updatedAt = new Date(dateTimeService.currentTimeMillis());
		articleDao.update(article.getId(), newTitle, newSlug, newDescription, newBody, updateData.getTags(), updatedAt);
		return ArticleData.make(article.getId(), newSlug, newTitle, newDescription, newBody, article.getCreatedAt(), updatedAt, article.isFavorited(), article.getFavoritesCount(), article.getAuthor(), article.getTags());
	}

	@Override
	public String makeSlug(String title) {
		return title.toLowerCase().replace(' ', '-').replaceAll("[^a-z0-9-]", "");
	}

	@Override
	public ArticleData favorite(String slug) throws EntityDoesNotExistException {
		String userId = userService.getCurrentUser().getId();
		ArticleWithLinks article = articleDao.findArticleBySlug(userId, slug);
		Set<String> tags = articleDao.findTags(article.getId());
		if( !article.isFavorited() ) {
			articleDao.addFavorite(userId, article.getId());
			return ArticleData.justFavorited(article, userService.findProfileById(article.getAuthorId()), tags);
		}
		return ArticleData.make(article, userService.findProfileById(article.getAuthorId()), tags);
	}

	@Override
	public ArticleData unfavorite(String slug) throws EntityDoesNotExistException {
		String userId = userService.getCurrentUser().getId();
		ArticleWithLinks article = articleDao.findArticleBySlug(userId, slug);
		Set<String> tags = articleDao.findTags(article.getId());
		if( article.isFavorited() ) {
			articleDao.removeFavorite(userId, article.getId());
			return ArticleData.justUnfavorited(article, userService.findProfileById(article.getAuthorId()), tags);
		}
		return ArticleData.make(article, userService.findProfileById(article.getAuthorId()), tags);
	}
}
