package realworld.article.services;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.security.Principal;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import realworld.EntityDoesNotExistException;
import realworld.article.model.ArticleResult;
import realworld.article.model.ArticleWithLinks;
import realworld.authentication.AuthenticationContext;
import realworld.article.model.ArticleData;
import realworld.article.model.ArticleCreationData;
import realworld.services.DateTimeService;
import realworld.user.model.ProfileData;
import realworld.user.model.UserData;
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
		if( criteria.getAuthor() != null ) {
			try {
				criteria = criteria.withAuthor(userService.findByUserName(criteria.getAuthor()).getId());
			}
			catch( EntityDoesNotExistException e) {
				return new ArticleResult<>(Collections.emptyList(), 0L);
			}
		}
		if( criteria.getFavoritedBy() != null ) {
			try {
				criteria = criteria.withFavoritedBy(userService.findByUserName(criteria.getFavoritedBy()).getId());
			}
			catch( EntityDoesNotExistException e) {
				return new ArticleResult<>(Collections.emptyList(), 0L);
			}
		}
		var searchResult = articleDao.find(criteria, DEFAULT_CRITERIA);
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
	public ArticleData findArticleBySlug(String slug) throws EntityDoesNotExistException {
		String userId = Optional.ofNullable(authenticationContext.getUserPrincipal()).map(Principal::getName).map(userService::findByUserName).map(UserData::getId).orElse("");
		ArticleWithLinks article = articleDao.findArticleBySlug(userId, slug);
		Set<String> tags = articleDao.findTags(article.getId());
		return ArticleData.make(article, userService.findProfileById(article.getAuthorId()), tags);
	}

	@Override
	public ArticleData create(ArticleCreationData creationData) {
		String authorId = userService.getCurrentUser().getId();
		ArticleWithLinks article = articleDao.create(creationData, makeSlug(creationData.getTitle()), new Date(dateTimeService.currentTimeMillis()), authorId, creationData.getTags());
		return ArticleData.make(article, userService.findProfileById(authorId), creationData.getTags());
	}

	@Override
	public String makeSlug(String title) {
		return title.toLowerCase().replace(' ', '-');
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
