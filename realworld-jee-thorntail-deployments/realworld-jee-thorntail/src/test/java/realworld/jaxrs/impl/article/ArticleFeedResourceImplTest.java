package realworld.jaxrs.impl.article;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static realworld.jaxrs.JaxRsApp.APPLICATION_PATH;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonValue;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.jboss.resteasy.cdi.ResteasyCdiExtension;
import org.jboss.resteasy.core.Dispatcher;
import org.jboss.resteasy.mock.MockHttpRequest;
import org.jboss.resteasy.mock.MockHttpResponse;
import org.jboss.resteasy.plugins.server.resourcefactory.SingletonResource;
import org.jboss.resteasy.spi.metadata.DefaultResourceClass;
import org.jboss.weld.junit5.auto.AddBeanClasses;
import org.jboss.weld.junit5.auto.AddExtensions;
import org.jboss.weld.junit5.auto.EnableAutoWeld;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import realworld.article.jaxrs.ArticleFeedResource;
import realworld.article.model.ArticleData;
import realworld.article.model.ArticleResult;
import realworld.article.model.ImmutableArticleData;
import realworld.article.services.ArticleSearchCriteria;
import realworld.article.services.ArticleService;
import realworld.jaxrs.sys.ObjectMapperProvider;
import realworld.jaxrs.test.CustomMockDispatcherFactory;
import realworld.user.model.ImmutableProfileData;
import realworld.user.model.ProfileData;

/**
 * Tests for the {@link ArticleFeedResourceImpl}.
 */
@EnableAutoWeld
@AddBeanClasses(ObjectMapperProvider.class)
@AddExtensions(ResteasyCdiExtension.class)
@ExtendWith(MockitoExtension.class)
public class ArticleFeedResourceImplTest {

	private static final String ARTICLE_ID = "ID";
	private static final String SLUG = "slug";
	private static final String DESCRIPTION = "description";
	private static final String BODY = "The body of the article.";
	private static final String TITLE = "The Title";
	private static final LocalDateTime CREATED_AT = LocalDateTime.of(2019, Month.MARCH, 11, 16, 45, 55);
	private static final Integer LIMIT = 11;
	private static final Integer OFFSET = 3;

	@Produces @Mock
	private ArticleService articleService;

	@Inject
	private ArticleFeedResourceImpl sut;

	private Dispatcher dispatcher;

	private MockHttpResponse response;

	@BeforeEach
	void init() {
		dispatcher = CustomMockDispatcherFactory.createDispatcher();
		SingletonResource resourceFactory = new SingletonResource(sut, new DefaultResourceClass(ArticleFeedResource.class, null));
		dispatcher.getRegistry().addResourceFactory(resourceFactory, APPLICATION_PATH);
		response = new MockHttpResponse();
	}

	@Test
	void testFeed() throws Exception {
		MockHttpRequest request = MockHttpRequest.get(APPLICATION_PATH + "/articles/feed?limit=" + LIMIT + "&offset=" + OFFSET);
		ProfileData author = ImmutableProfileData.builder().username("").isFollowing(false).build();
		List<ArticleData> articles = Collections.singletonList(ImmutableArticleData.builder().id(ARTICLE_ID).slug(SLUG).description(DESCRIPTION).body(BODY).title(TITLE).createdAt(CREATED_AT).updatedAt(CREATED_AT).isFavorited(false).favoritesCount(0).author(author).build());
		ArticleResult<ArticleData> mockResult = new ArticleResult<>(articles, Integer.valueOf(articles.size()).longValue());
		when(articleService.feed(any(ArticleSearchCriteria.class))).thenReturn(mockResult);

		dispatcher.invoke(request, response);

		ArgumentCaptor<ArticleSearchCriteria> criteriaCaptor = ArgumentCaptor.forClass(ArticleSearchCriteria.class);
		verify(articleService).feed(criteriaCaptor.capture());
		ArticleSearchCriteria capturedCriteria = criteriaCaptor.getValue();
		assertNotNull(capturedCriteria);
		assertEquals(LIMIT, capturedCriteria.getLimit());
		assertEquals(OFFSET, capturedCriteria.getOffset());

		assertArticleResult()
				.next()
				.assertId(ARTICLE_ID)
				.assertCreatedAt("2019-03-11T16:45:55.000Z")
				.assertNoMore();
	}

	private ArticleResultAssertions assertArticleResult() throws UnsupportedEncodingException {
		assertEquals(200, response.getStatus());
		JsonReader jsonReader = Json.createReader(new StringReader(response.getContentAsString()));
		JsonObject jobj = jsonReader.readObject();
		assertEquals(2, jobj.size());
		assertEquals(1, jobj.getInt("articlesCount"));
		JsonArray articles = jobj.getJsonArray("articles");
		assertNotNull(articles);
		assertEquals(1, articles.size());
		return new ArticleResultAssertions(articles);
	}

	private static class ArticleResultAssertions {
		private JsonArray articles;
		private JsonObject currentArticle;
		private Iterator<JsonValue> articlesIterator;

		ArticleResultAssertions(JsonArray articles) {
			this.articles = articles;
		}

		ArticleResultAssertions next() {
			if( articlesIterator == null ) {
				articlesIterator = articles.iterator();
			}
			currentArticle = (JsonObject) articlesIterator.next();
			return this;
		}

		ArticleResultAssertions assertNoMore() {
			assertFalse(articlesIterator.hasNext());
			return this;
		}

		ArticleResultAssertions assertId(String id) {
			assertEquals(id, currentArticle.getString("id"));
			return this;
		}

		ArticleResultAssertions assertCreatedAt(String createdAt) {
			assertEquals(createdAt, currentArticle.getString("createdAt"));
			return this;
		}
	}
}
