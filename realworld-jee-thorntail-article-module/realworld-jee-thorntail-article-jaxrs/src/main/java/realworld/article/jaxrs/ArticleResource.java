package realworld.article.jaxrs;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import realworld.article.model.ArticleData;

/**
 * Article operations.
 */
@Path("/articles")
@Api(tags = ArticleResource.TAG)
public interface ArticleResource {

	String TAG = "ArticleResource";

	@GET
	@Path("/{slug}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value="Returns an article.", tags=TAG)
	ArticleData get(
			@ApiParam(value = "The slug of the article.", required = true)
			@PathParam("slug")
			String slug
	);
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value="Creates an article.", tags=TAG)
	ArticleData create(
			@ApiParam(value = "Creation data.", required = true)
			CreationParam param
	);

	@POST
	@Path("/{slug}/favorite")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value="Favorite article.", tags=TAG)
	ArticleData favorite(
			@ApiParam(value = "The slug of the article to favorite.", required = true)
			@PathParam("slug")
			String slug
	);

	@DELETE
	@Path("/{slug}/favorite")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value="Favorite article.", tags=TAG)
	ArticleData unfavorite(
			@ApiParam(value = "The slug of the article to favorite.", required = true)
			@PathParam("slug")
			String slug
	);
}
