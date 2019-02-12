package realworld.user.jaxrs;

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
import realworld.user.model.ProfileData;

/**
 * Profile operations.
 */
@Path("/profiles/{username}")
@Api(tags= ProfileResource.TAG)
public interface ProfileResource {

	String TAG = "ProfileResource";

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value="Returns the profile of the given user.", tags=TAG)
	ProfileData get(
			@ApiParam(value = "The user name to apply this operation to.", required = true)
			@PathParam("username")
			String username
	);

	@POST
	@Path("/follow")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value="Follow the given user.", tags=TAG)
	ProfileData follow(
			@ApiParam(value = "The user name to apply this operation to.", required = true)
			@PathParam("username")
			String username
	);

	@DELETE
	@Path("/follow")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value="Unfollow the given user.", tags=TAG)
	ProfileData unfollow(
			@ApiParam(value = "The user name to apply this operation to.", required = true)
			@PathParam("username")
			String username
	);
}
