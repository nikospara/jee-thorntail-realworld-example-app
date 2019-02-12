package realworld.jaxrs.sys.authentication;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import java.text.ParseException;
import java.time.ZoneId;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import realworld.services.DateTimeService;

/**
 * Token processing helper.
 */
@ApplicationScoped
public class TokenHelper {

	private static final String AUTHORIZATION_HEADER = "Authorization";
	private static final Pattern AUTH_HEADER_RE = Pattern.compile("^Token (.*)$", Pattern.CASE_INSENSITIVE);
	
	private JwtService jwtService;
	
	private DateTimeService dateTimeService;
	
	private TokenAuthenticationConfig tokenAuthenticationConfig;
	
	/**
	 * Default constructor required by the infrastructure.
	 */
	TokenHelper() {
		// NO OP
	}
	
	/**
	 * Injection constructor.
	 * 
	 * @param jwtService                The JWT service
	 * @param dateTimeService           The date service
	 * @param tokenAuthenticationConfig The configuration
	 */
	@Inject
	public TokenHelper(JwtService jwtService, DateTimeService dateTimeService, TokenAuthenticationConfig tokenAuthenticationConfig) {
		this.jwtService = jwtService;
		this.dateTimeService = dateTimeService;
		this.tokenAuthenticationConfig = tokenAuthenticationConfig;
	}

	/**
	 * Try to extract the token from the {@code ContainerRequestContext}.
	 * 
	 * @param requestContext The context
	 * @return The token or {@code null}
	 */
	public String extractRawToken(ContainerRequestContext requestContext) {
		String authenticationHeaderValue = requestContext.getHeaders().getFirst(AUTHORIZATION_HEADER);
		return extractRawToken(authenticationHeaderValue);
	}

	/**
	 * Try to extract the token from the {@code HttpHeaders}.
	 *
	 * @param headers The headers
	 * @return The token or {@code null}
	 */
	public String extractRawToken(HttpHeaders headers) {
		String authenticationHeaderValue = headers.getRequestHeaders().getFirst(AUTHORIZATION_HEADER);
		return extractRawToken(authenticationHeaderValue);
	}

	private String extractRawToken(String authenticationHeaderValue) {
		String result = null;
		if( authenticationHeaderValue != null ) {
			Matcher m = AUTH_HEADER_RE.matcher(authenticationHeaderValue.trim());
			if( m.matches() ) {
				result = m.group(1);
			}
		}
		return result;
	}

	/**
	 * Process the token to create a {@link UserImpl}.
	 * 
	 * @param token The token, cannot be {@code null}
	 * @return The user
	 */
	public UserImpl processToken(String token) {
		try {
			SignedJWT jwt = parse(token);
			
			if( !"HS256".equals(jwt.getHeader().getAlgorithm().getName()) ) {
				throw new NotAuthorizedException("unsupported algorithm: " + jwt.getHeader().getAlgorithm().getName(), unauthorized());
			}
			
			if( !jwtService.verify(jwt) ) {
				throw new NotAuthorizedException("failed to verify JWT: " + token, unauthorized());
			}
			
			JWTClaimsSet jwtClaimsSet = extractJWTClaimsSet(jwt); 
			if( jwtClaimsSet.getExpirationTime().before(Date.from(dateTimeService.getNow().atZone(ZoneId.systemDefault()).toInstant())) ) {
				throw new NotAuthorizedException("JWT expired at " + jwtClaimsSet.getExpirationTime(), unauthorized());
			}
			
			return new UserImpl(jwtClaimsSet.getClaim(tokenAuthenticationConfig.getUsernameFieldInJwt()).toString(), jwtClaimsSet.getClaim("uuid").toString());
		}
		catch( JOSEException e ) {
			throw new NotAuthorizedException("failed to verify JWT", unauthorized(), e);
		}
	}
	
	private SignedJWT parse(String token) {
		try {
			return SignedJWT.parse(token);
		}
		catch( ParseException e ) {
			throw new NotAuthorizedException("error parsing token " + token, unauthorized(), e);
		}
	}
	
	private JWTClaimsSet extractJWTClaimsSet(SignedJWT jwt) {
		try {
			return jwt.getJWTClaimsSet();
		}
		catch( ParseException e ) {
			throw new NotAuthorizedException("failed to parse JWT", unauthorized(), e);
		}
	}
	
	private static Response unauthorized() {
		return Response.status(Response.Status.UNAUTHORIZED).build();
	}
}
