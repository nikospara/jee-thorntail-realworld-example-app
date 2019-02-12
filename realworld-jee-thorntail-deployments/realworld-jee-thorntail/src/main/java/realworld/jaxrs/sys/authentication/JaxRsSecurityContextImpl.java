package realworld.jaxrs.sys.authentication;

import javax.enterprise.inject.Vetoed;
import javax.ws.rs.core.SecurityContext;
import java.security.Principal;

/**
 * Simple implementation of {@code javax.ws.rs.core.SecurityContext}.
 */
@Vetoed
public class JaxRsSecurityContextImpl implements SecurityContext {

	private Principal principal;
	private boolean secure;
	
	/**
	 * Default constructor for serialization.
	 */
	JaxRsSecurityContextImpl() {
		// NO OP
	}

	/**
	 * Full constructor.
	 * 
	 * @param principal The user
	 * @param secure    If the transport is secure
	 */
	public JaxRsSecurityContextImpl(Principal principal, boolean secure) {
		this.principal = principal;
		this.secure = secure;
	}

	@Override
	public Principal getUserPrincipal() {
		return principal;
	}

	@Override
	public boolean isUserInRole(String role) {
		return false;
	}

	@Override
	public boolean isSecure() {
		return secure;
	}

	@Override
	public String getAuthenticationScheme() {
		return null;
	}
}
