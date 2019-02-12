package realworld.user.jaxrs;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * User-login and registration operations.
 */
@Path("/users")
@Api(tags= UsersResource.TAG)
public interface UsersResource {

	String TAG = "UsersResource";

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value="Register user and return user information and token.", tags=TAG)
	UserWithToken register(
			@ApiParam(value = "Information required to register.", required = true)
			RegisterParam registerParam
	);

	@POST
	@Path("/login")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value="Login and return user information and token.", tags=TAG)
	UserWithToken login(
			@ApiParam(value = "Information required to log in.", required = true)
			LoginParam loginParam
	);
}
