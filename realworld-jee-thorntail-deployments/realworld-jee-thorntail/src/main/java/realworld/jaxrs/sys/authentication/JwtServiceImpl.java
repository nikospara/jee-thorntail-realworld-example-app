package realworld.jaxrs.sys.authentication;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.text.ParseException;
import java.util.Base64;
import java.util.Date;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import realworld.services.DateTimeService;
import realworld.user.model.UserData;

/**
 * Default implementation of the {@link JwtService}.
 *
 * @see <a href="https://connect2id.com/products/nimbus-jose-jwt/examples/jwt-with-hmac">example</a>
 */
@ApplicationScoped
public class JwtServiceImpl implements JwtService {

	private DateTimeService dateTimeService;

	private TokenAuthenticationConfig tokenAuthenticationConfig;

	/** The signer, it is thread-safe as per specs. */
	private MACSigner signer;

	/** The verifier, it is thread-safe as per specs. */
	private MACVerifier verifier;

	/**
	 * Default constructor for frameworks.
	 */
	JwtServiceImpl() {
		// NOOP
	}

	/**
	 * Constructor for injection.
	 *
	 * @param dateTimeService           The date and time service
	 * @param tokenAuthenticationConfig The token authentication configuration
	 */
	@Inject
	public JwtServiceImpl(DateTimeService dateTimeService, TokenAuthenticationConfig tokenAuthenticationConfig) {
		this.dateTimeService = dateTimeService;
		this.tokenAuthenticationConfig = tokenAuthenticationConfig;
	}

	@PostConstruct
	void init() {
		byte[] sharedSecret = Base64.getMimeDecoder().decode(tokenAuthenticationConfig.getJwtSecret());
		try {
			signer = new MACSigner(sharedSecret);
			verifier = new MACVerifier(sharedSecret);
		}
		catch( JOSEException e ) {
			throw new TokenGenerationException(e);
		}
	}

	@Override
	public String toToken(UserData user) {
		SignedJWT signedJWT = makeSignedJWT(user.getUsername(), user.getId(), new Date(dateTimeService.currentTimeMillis() + tokenAuthenticationConfig.getJwtSessionTime() * 1000));
		return signedJWT.serialize();
	}

	@Override
	public String updateUser(UserData user, String currentToken) {
		try {
			SignedJWT oldJwt = SignedJWT.parse(currentToken);
			SignedJWT newJwt = makeSignedJWT(user.getUsername(), user.getId(), oldJwt.getJWTClaimsSet().getExpirationTime());
			return newJwt.serialize();
		}
		catch (ParseException e) {
			throw new TokenGenerationException(e);
		}
	}

	private SignedJWT makeSignedJWT(String subject, String uuid, Date expirationTime) {
		JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
				.subject(subject)
				.claim("uuid", uuid)
				.expirationTime(expirationTime)
				.build();
		SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), claimsSet);
		try {
			signedJWT.sign(signer);
		}
		catch (JOSEException e) {
			throw new TokenGenerationException(e);
		}
		return signedJWT;
	}

	@Override
	public boolean verify(SignedJWT jwt) throws JOSEException {
		return jwt.verify(verifier);
	}
}
