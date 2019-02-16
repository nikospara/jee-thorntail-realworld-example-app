package realworld.article.jaxrs;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * Tag operations.
 */
@Path("/tags")
@Api(tags = TagResource.TAG)
public interface TagResource {

	String TAG = "TagResource";

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value="Returns an article.", tags=TAG)
	TagList get();
}
