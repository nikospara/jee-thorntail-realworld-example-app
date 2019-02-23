package realworld.jaxrs.sys.exceptionmap;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import realworld.authorization.NotAuthenticatedException;

/**
 * Map the {@link NotAuthenticatedException} to JAX-RS
 * as a HTTP 401 "UNAUTHORIZED" response.
 */
@Provider
public class NotAuthenticatedExceptionMapper implements ExceptionMapper<NotAuthenticatedException> {
	@Override
	public Response toResponse(NotAuthenticatedException exception) {
		Response.ResponseBuilder builder = Response.status(Response.Status.UNAUTHORIZED);
		return builder.build();
	}
}
