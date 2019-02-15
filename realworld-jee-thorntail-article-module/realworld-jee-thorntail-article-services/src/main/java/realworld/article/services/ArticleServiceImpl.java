package realworld.article.services;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.security.Principal;
import java.util.Date;
import java.util.Optional;

import realworld.EntityDoesNotExistException;
import realworld.article.model.ArticleWithLinks;
import realworld.authentication.AuthenticationContext;
import realworld.article.model.ArticleData;
import realworld.article.model.ArticleCreationData;
import realworld.services.DateTimeService;
import realworld.user.model.UserData;
import realworld.user.services.UserService;

/**
 * Implementation of the {@link ArticleService}.
 */
@ApplicationScoped
@Transactional
class ArticleServiceImpl implements ArticleService {
	
	private ArticleDao articleDao;

	private AuthenticationContext authenticationContext;
	
	private DateTimeService dateTimeService;

	private UserService userService;
	
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
	public ArticleData findArticleBySlug(String slug) throws EntityDoesNotExistException {
		String userId = Optional.ofNullable(authenticationContext.getUserPrincipal()).map(Principal::getName).map(userService::findByUserName).map(UserData::getId).orElse("");
		ArticleWithLinks article = articleDao.findArticleBySlug(userId, slug);
		return ArticleData.make(article, userService.findProfileById(article.getAuthorId()));
	}

	@Override
	public ArticleData create(ArticleCreationData creationData) {
		String authorId = userService.getCurrentUser().getId();
		ArticleWithLinks article = articleDao.create(creationData, makeSlug(creationData.getTitle()), new Date(dateTimeService.currentTimeMillis()), authorId);
		return ArticleData.make(article, userService.findProfileById(authorId));
	}

	@Override
	public String makeSlug(String title) {
		return title.toLowerCase().replace(' ', '-');
	}

	@Override
	public ArticleData favorite(String slug) throws EntityDoesNotExistException {
		String userId = userService.getCurrentUser().getId();
		ArticleWithLinks article = articleDao.findArticleBySlug(userId, slug);
		if( !article.isFavorited() ) {
			articleDao.addFavorite(userId, article.getId());
			return ArticleData.justFavorited(article, userService.findProfileById(article.getAuthorId()));
		}
		return ArticleData.make(article, userService.findProfileById(article.getAuthorId()));
	}

	@Override
	public ArticleData unfavorite(String slug) throws EntityDoesNotExistException {
		String userId = userService.getCurrentUser().getId();
		ArticleWithLinks article = articleDao.findArticleBySlug(userId, slug);
		if( article.isFavorited() ) {
			articleDao.removeFavorite(userId, article.getId());
			return ArticleData.justUnfavorited(article, userService.findProfileById(article.getAuthorId()));
		}
		return ArticleData.make(article, userService.findProfileById(article.getAuthorId()));
	}
}
