package realworld.jaxrs.sys.exceptionmap;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import realworld.authorization.NotAuthenticatedException;
import realworld.authorization.NotAuthorizedException;

/**
 * Map the {@link NotAuthenticatedException} to JAX-RS
 * as a HTTP 403 "FORBIDDEN" response.
 */
@Provider
public class NotAuthorizedExceptionMapper implements ExceptionMapper<NotAuthorizedException> {
	@Override
	public Response toResponse(NotAuthorizedException e) {
		Response.ResponseBuilder builder = Response.status(Response.Status.FORBIDDEN);
		return builder.build();
	}
}
