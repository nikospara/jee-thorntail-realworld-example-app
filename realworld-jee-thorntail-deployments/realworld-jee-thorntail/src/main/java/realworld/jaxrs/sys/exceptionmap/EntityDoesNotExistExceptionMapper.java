package realworld.jaxrs.sys.exceptionmap;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import realworld.EntityDoesNotExistException;

/**
 * Map the {@link EntityDoesNotExistException} to JAX-RS
 * as a HTTP 404 "NOT FOUND" response.
 */
@Provider
public class EntityDoesNotExistExceptionMapper implements ExceptionMapper<EntityDoesNotExistException> {
	@Override
	public Response toResponse(EntityDoesNotExistException exception) {
		Response.ResponseBuilder builder = Response.status(Response.Status.NOT_FOUND);
		return builder.build();
	}
}
