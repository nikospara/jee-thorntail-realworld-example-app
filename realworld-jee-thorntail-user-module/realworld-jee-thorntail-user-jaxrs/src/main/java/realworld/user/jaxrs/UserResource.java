package realworld.user.jaxrs;

import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * User operations.
 */
@Path("/user")
@Api(tags= UserResource.TAG)
public interface UserResource {

	String TAG = "UserResource";

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value="Returns the current user.", tags=TAG)
	UserWithToken get();

	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value="Updates the current user.", tags=TAG)
	UserWithToken update(
			@ApiParam(value = "Information to update.", required = true)
			UpdateParam updateParam
	);
}
