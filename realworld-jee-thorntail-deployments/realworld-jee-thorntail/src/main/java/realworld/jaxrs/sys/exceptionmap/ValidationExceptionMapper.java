package realworld.jaxrs.sys.exceptionmap;

import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import javax.validation.ValidationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Map the {@code ValidationException} as per specs (see JAX-RS 2.1 ch. 7.6) and
 * <a href="https://github.com/gothinkster/realworld/tree/master/api#errors-and-status-codes">application requirements</a>.
 */
@Provider
public class ValidationExceptionMapper implements ExceptionMapper<ValidationException> {

	private static final Logger LOG = LoggerFactory.getLogger(ValidationExceptionMapper.class);

	@Override
	public Response toResponse(ValidationException exception) {
		if( exception instanceof ConstraintViolationException ) {
			return constraintViolationToResponse((ConstraintViolationException) exception);
		}
		else {
			LOG.error("encountered non-ConstraintViolationException ValidationException, responding with HTTP 500", exception);
			return Response.serverError().build();
		}
	}

	private Response constraintViolationToResponse(ConstraintViolationException e) {
		String response = e.getConstraintViolations().stream()
				.map(v -> "\"" + lastNode(v.getPropertyPath()) + "\": [\"" + v.getMessage().replaceAll("\"", "\\\\\"") + "\"]")
				.collect(Collectors.joining(", ", "{\"errors\": {", "}}"));
		return Response.status(422).type(MediaType.APPLICATION_JSON).entity(response).build();
	}

	private String lastNode(Path path) {
		String lastNode = "";
		for( Path.Node n : path ) {
			lastNode = n.getName();
		}
		return lastNode;
	}
}
