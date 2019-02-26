package realworld.comments.jaxrs;

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
import realworld.comments.model.CommentData;

/**
 * Article comments endpoints.
 */
@Path("/articles/{slug}/comments")
@Api(tags = ArticleCommentsResource.TAG)
public interface ArticleCommentsResource {

	String TAG = "ArticleCommentsResource";

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value="Get comments from an article.", tags=TAG)
	CommentsList get(
			@ApiParam(value = "The slug of the article.", required = true)
			@PathParam("slug")
			String slug
	);

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value="Adds a comment to an article.", tags=TAG)
	CommentData add(
			@ApiParam(value = "The slug of the article.", required = true)
			@PathParam("slug")
			String slug,
			@ApiParam(value = "Creation data.", required = true)
			CreationParam param
	);

	@DELETE
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value="Delete comment.", tags=TAG)
	void delete(
			@ApiParam(value = "The slug of the article.", required = true)
			@PathParam("slug")
			String slug,
			@ApiParam(value = "The comment id.", required = true)
			@PathParam("id")
			String id
	);
}
