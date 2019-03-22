package realworld.article.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.jboss.weld.junit5.auto.EnableAutoWeld;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import realworld.EntityDoesNotExistException;
import realworld.article.model.ArticleData;
import realworld.article.model.ArticleResult;
import realworld.article.model.ArticleWithLinks;
import realworld.article.model.ImmutableArticleWithLinks;
import realworld.authentication.AuthenticationContext;
import realworld.services.DateTimeService;
import realworld.user.model.ImmutableProfileData;
import realworld.user.model.ImmutableUserData;
import realworld.user.services.UserService;
import services.realworld.comments.services.CommentService;

/**
 * Tests for the {@link ArticleServiceImpl}.
 */
@EnableAutoWeld
@ExtendWith(MockitoExtension.class)
class ArticleServiceImplTest {

	private static final String TAG = "tag";
	private static final String USER = "user";
	private static final String USER_ID = "userid";
	private static final String NON_EXISTING_USER = "nonExistingUser";
	private static final String ARTICLE_ID = "articleId";
	private static final String AUTHOR_ID = "authorId";
	private static final String AUTHOR_USER = "author";
	private static final String AUTHOR_ID2 = "authorId2";
	private static final String AUTHOR_USER2 = "author2";

	@Produces @Mock
	private ArticleDao articleDao;

	@Produces @Mock
	private AuthenticationContext authenticationContext;

	@Produces @Mock
	private DateTimeService dateTimeService;

	@Produces @Mock
	private UserService userService;

	@Produces @Mock
	private CommentService commentService;

	@Inject
	private ArticleServiceImpl sut;

	@Test
	void testMakeSlug() {
		assertEquals("aa1-bbb-cccc", sut.makeSlug("Aa1 bbb. CCcc$%"));
	}

	@Test
	void testFindUnknownUser() {
		when(userService.findByUserName(NON_EXISTING_USER)).thenThrow(EntityDoesNotExistException.class);

		ArticleSearchCriteria criteria = ArticleSearchCriteria.builder()
				.withTag(TAG)
				.favoritedBy(NON_EXISTING_USER)
				.withLimit(10)
				.withOffset(20)
				.build();

		var result = sut.find(criteria);

		assertEquals(0, result.getArticlesCount());
	}

	@Test
	void testFindNoAuthors() {
		when(userService.findByUserName(USER)).thenReturn(ImmutableUserData.builder().id(USER_ID).username(USER).email("").build());
		when(userService.findProfileById(AUTHOR_ID)).thenReturn(ImmutableProfileData.builder().username(AUTHOR_USER).isFollowing(false).build());
		when(articleDao.find(any(), any(), any())).thenReturn(new ArticleResult<>(Collections.singletonList(makeArticleWithLinks()), 33L));
		when(articleDao.findTags(ARTICLE_ID)).thenReturn(Collections.singleton(TAG));

		ArticleSearchCriteria criteria = ArticleSearchCriteria.builder()
				.withTag(TAG)
				.favoritedBy(USER)
				.withLimit(10)
				.withOffset(20)
				.build();

		var result = sut.find(criteria);

		assertEquals(33, result.getArticlesCount());
		ArticleData a = result.getArticles().get(0);
		assertEquals(ARTICLE_ID, a.getId());
		assertEquals(AUTHOR_USER, a.getAuthor().getUsername());
		assertFalse(a.getAuthor().isFollowing());
		assertEquals(Collections.singleton(TAG), a.getTags());
	}

	@Test
	void testFindUnknownAuthors() {
		when(userService.mapUserNamesToIds(any())).thenReturn(Collections.emptyMap());
		var result = sut.find(makeCriteriaWithAuthors());
		assertEquals(0, result.getArticlesCount());
	}

	@Test
	void testFind() {
		Map<String, String> userNamesToIds = new HashMap<>();
		userNamesToIds.put(AUTHOR_USER, AUTHOR_ID);
		userNamesToIds.put(AUTHOR_USER2, AUTHOR_ID2);
		when(userService.findByUserName(USER)).thenReturn(ImmutableUserData.builder().id(USER_ID).username(USER).email("").build());
		when(userService.findProfileById(AUTHOR_ID)).thenReturn(ImmutableProfileData.builder().username(AUTHOR_USER).isFollowing(false).build());
		when(userService.mapUserNamesToIds(any())).thenReturn(userNamesToIds);
		when(articleDao.find(any(), any(), any())).thenReturn(new ArticleResult<>(Collections.singletonList(makeArticleWithLinks()), 33L));
		when(articleDao.findTags(ARTICLE_ID)).thenReturn(Collections.singleton(TAG));

		ArticleSearchCriteria criteria = ArticleSearchCriteria.builder()
				.withTag(TAG)
				.favoritedBy(USER)
				.withLimit(10)
				.withOffset(20)
				.withAuthors(Arrays.asList(AUTHOR_ID, AUTHOR_ID2))
				.build();

		var result = sut.find(criteria);

		assertEquals(33, result.getArticlesCount());
		ArticleData a = result.getArticles().get(0);
		assertEquals(ARTICLE_ID, a.getId());
		assertEquals(AUTHOR_USER, a.getAuthor().getUsername());
		assertFalse(a.getAuthor().isFollowing());
		assertEquals(Collections.singleton(TAG), a.getTags());
	}

	private ArticleWithLinks makeArticleWithLinks() {
		return ImmutableArticleWithLinks.builder()
				.id(ARTICLE_ID)
				.authorId(AUTHOR_ID)
				.favoritesCount(3)
				.isFavorited(false)
				.updatedAt(LocalDateTime.now())
				.createdAt(LocalDateTime.now())
				.slug("slug")
				.title("title")
				.description("Description")
				.body("Body")
				.build();
	}

	private ArticleSearchCriteria makeCriteriaWithAuthors() {
		return ArticleSearchCriteria.builder()
				.withTag(TAG)
				.favoritedBy(USER)
				.withLimit(10)
				.withOffset(20)
				.withAuthors(Arrays.asList(AUTHOR_ID, AUTHOR_ID2))
				.build();
	}
}
