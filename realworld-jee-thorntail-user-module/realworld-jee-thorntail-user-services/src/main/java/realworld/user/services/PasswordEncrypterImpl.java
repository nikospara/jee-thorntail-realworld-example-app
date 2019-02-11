package realworld.user.services;

import javax.enterprise.context.ApplicationScoped;

/**
 * Implementation of the {@link PasswordEncrypter}.
 */
@ApplicationScoped
class PasswordEncrypterImpl implements PasswordEncrypter {
	@Override
	public String apply(String cleartextPassword) {
		return cleartextPassword;
	}
}
