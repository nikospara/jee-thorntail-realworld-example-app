package realworld.jaxrs.sys.authentication;

import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import java.net.URL;

/**
 * Implementation of {@link TokenAuthenticationConfig} using JEE resources.
 */
@ApplicationScoped
public class TokenAuthenticationConfigImpl implements TokenAuthenticationConfig {
	
	public static final String JWT_SECRET_KEY = "java:/jwt.secret";
	public static final String JWT_SESSION_TIME_KEY = "java:/jwt.sessionTime";
	public static final String USERNAME_FIELD_IN_JWT_KEY = "java:/jwt.map.userName";

	@Resource(name=JWT_SECRET_KEY)
	private String jwtSecret;
	
	@Resource(name=JWT_SESSION_TIME_KEY)
	private Long jwtSessionTime;

	@Resource(name=USERNAME_FIELD_IN_JWT_KEY)
	private String usernameFieldInJwt;

	@Override
	public String getJwtSecret() {
		return jwtSecret;
	}

	@Override
	public Long getJwtSessionTime() {
		return jwtSessionTime;
	}

	@Override
	public URL getJwkUrl() {
		return null;
	}

	@Override
	public String getUsernameFieldInJwt() {
		return usernameFieldInJwt;
	}
}
