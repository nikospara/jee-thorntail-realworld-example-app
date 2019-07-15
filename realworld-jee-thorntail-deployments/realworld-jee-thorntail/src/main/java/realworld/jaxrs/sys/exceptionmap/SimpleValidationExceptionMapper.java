package realworld.jaxrs.sys.exceptionmap;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.stream.Collectors;

import realworld.services.SimpleValidationException;

/**
 * Map a {@link SimpleValidationException} the same way a {@code ValidationException} is mapped by {@link ValidationExceptionMapper}.
 */
@Provider
public class SimpleValidationExceptionMapper implements ExceptionMapper<SimpleValidationException> {
	@Override
	public Response toResponse(SimpleValidationException exception) {
		String response = exception.getViolations().stream()
				.map(v -> "\"" + v.getFieldName() + "\": [\"" + v.getMessage().replaceAll("\"", "\\\\\"") + "\"]")
				.collect(Collectors.joining(", ", "{\"errors\": {", "}}"));
		return Response.status(422).type(MediaType.APPLICATION_JSON).entity(response).build();
	}
}
