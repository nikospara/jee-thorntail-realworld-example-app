package realworld.jaxrs;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

/**
 * The JAX-RS application.
 */
@ApplicationPath(JaxRsApp.APPLICATION_PATH)
public class JaxRsApp extends Application {

	/**
	 * The JAX-RS application path.
	 */
	public static final String APPLICATION_PATH = "/api";
}
