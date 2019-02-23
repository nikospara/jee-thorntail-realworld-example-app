package realworld.article.services;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * Tests for the {@link ArticleServiceImpl}.
 */
class ArticleServiceImplTest {

	private ArticleServiceImpl sut;

	@Test
	final void testMakeSlug() {
		sut = new ArticleServiceImpl();
		assertEquals("aa1-bbb-cccc", sut.makeSlug("Aa1 bbb. CCcc$%"));
	}
}
