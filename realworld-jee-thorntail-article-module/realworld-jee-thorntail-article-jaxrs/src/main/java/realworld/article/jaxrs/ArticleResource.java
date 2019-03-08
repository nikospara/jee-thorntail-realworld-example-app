package realworld.article.jaxrs;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import realworld.article.model.ArticleData;
import realworld.article.model.ArticleResult;
import realworld.comments.jaxrs.CommentCreationParam;
import realworld.comments.jaxrs.CommentDataWrapper;
import realworld.comments.jaxrs.CommentsList;

/**
 * Article operations.
 */
@Path("/articles")
@Api(tags = ArticleResource.TAG)
public interface ArticleResource {

	String TAG = "ArticleResource";

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value="Searches for articles.", tags=TAG)
	ArticleResult<ArticleData> find(
			@ApiParam(value = "Filter by tag.")
			@QueryParam("tag")
			String tag,
			@ApiParam(value = "Filter by author.")
			@QueryParam("author")
			String author,
			@ApiParam(value = "Favorited by user.")
			@QueryParam("favorited")
			String favoritedBy,
			@ApiParam(value = "Limit returned results.")
			@QueryParam("limit")
			Integer limit,
			@ApiParam(value = "Offset/skip number of articles.")
			@QueryParam("offset")
			Integer offset
	);

	@GET
	@Path("/{slug}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value="Returns an article.", tags=TAG)
	ArticleDataWrapper get(
			@ApiParam(value = "The slug of the article.", required = true)
			@PathParam("slug")
			String slug
	);
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value="Creates an article.", tags=TAG)
	ArticleDataWrapper create(
			@ApiParam(value = "Creation data.", required = true)
			CreationParam param
	);

	@PUT
	@Path("/{slug}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value="Updates an article.", tags=TAG)
	ArticleDataWrapper update(
			@ApiParam(value = "The slug of the article.", required = true)
			@PathParam("slug")
			String slug,
			@ApiParam(value = "Information to update.", required = true)
			UpdateParam updateParam
	);

	@DELETE
	@Path("/{slug}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value="Deletes an article.", tags=TAG)
	void delete(
			@ApiParam(value = "The slug of the article.", required = true)
			@PathParam("slug")
			String slug
	);

	@POST
	@Path("/{slug}/favorite")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value="Favorite article.", tags=TAG)
	ArticleDataWrapper favorite(
			@ApiParam(value = "The slug of the article to favorite.", required = true)
			@PathParam("slug")
			String slug
	);

	@DELETE
	@Path("/{slug}/favorite")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value="Favorite article.", tags=TAG)
	ArticleDataWrapper unfavorite(
			@ApiParam(value = "The slug of the article to favorite.", required = true)
			@PathParam("slug")
			String slug
	);

	@GET
	@Path("/{slug}/comments")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value="Get comments from an article.", tags=TAG)
	CommentsList getComments(
			@ApiParam(value = "The slug of the article.", required = true)
			@PathParam("slug")
			String slug
	);

	@POST
	@Path("/{slug}/comments")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value="Adds a comment to an article.", tags=TAG)
	CommentDataWrapper comment(
			@ApiParam(value = "The slug of the article.", required = true)
			@PathParam("slug")
			String slug,
			@ApiParam(value = "Creation data.", required = true)
			CommentCreationParam param
	);

	@DELETE
	@Path("/{slug}/comments/{commentId}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value="Delete comment.", tags=TAG)
	void delete(
			@ApiParam(value = "The slug of the article.", required = true)
			@PathParam("slug")
			String slug,
			@ApiParam(value = "The comment id.", required = true)
			@PathParam("commentId")
			String commentId
	);
}
