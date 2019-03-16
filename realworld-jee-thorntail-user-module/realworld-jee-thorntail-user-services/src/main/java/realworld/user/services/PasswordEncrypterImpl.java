package realworld.user.services;

import javax.enterprise.context.ApplicationScoped;

/**
 * Implementation of the {@link PasswordEncrypter}.
 * <p>
 * For the purposes of this, there isn't really any encryption!
 */
@ApplicationScoped
class PasswordEncrypterImpl implements PasswordEncrypter {
	@Override
	public String apply(String cleartextPassword) {
		return "ENC:" + cleartextPassword;
	}
}
