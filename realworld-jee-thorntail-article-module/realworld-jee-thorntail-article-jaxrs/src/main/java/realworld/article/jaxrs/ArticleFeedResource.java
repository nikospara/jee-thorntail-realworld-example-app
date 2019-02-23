package realworld.article.jaxrs;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import realworld.article.model.ArticleData;
import realworld.article.model.ArticleResult;

/**
 * Article feed resource.
 */
@Path("/articles/feed")
@Api(tags = ArticleResource.TAG)
public interface ArticleFeedResource {
	@GET
	@ApiOperation(value="Searches for articles.", tags=ArticleResource.TAG)
	ArticleResult<ArticleData> feed(
			@ApiParam(value = "Limit returned results.")
			@QueryParam("limit")
			Integer limit,
			@ApiParam(value = "Offset/skip number of articles.")
			@QueryParam("offset")
			Integer offset
	);
}
