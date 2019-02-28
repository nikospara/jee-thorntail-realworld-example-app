package realworld.user.jaxrs;

import java.io.Serializable;

import realworld.user.model.ProfileData;

/**
 * Wrap an {@link ProfileData} instance.
 * <p>
 * This class is necessary because of the unfortunate choice of the Realworld API
 * to wrap every object in a root, except for the {@code realworld.article.model.ArticleResult},
 * which is serialized as is.
 * Using {@code @JsonRootName} and {@code objectMapper.enable(SerializationFeature.WRAP_ROOT_VALUE)}
 * does not work, because it cannot make the exception for the {@code ArticleResult}.
 * Thus we are forced to use {@code @JsonTypeName}+{@code @JsonTypeInfo}.
 * If we annotate the {@code ProfileData} mixin however,
 * classes like {@code ArticleData} that embed it are serialized as nested objects,
 * which violates the Realworld API.
 * <p>
 * We choose to serialize the {@code ProfileData} as is and manually wrap it in this class,
 * when returned directly.
 */
public class ProfileDataWrapper implements Serializable {

	private static final long serialVersionUID = 1L;

	private ProfileData profile;

	/**
	 * Wrap a profile.
	 *
	 * @param profile The profile
	 */
	public ProfileDataWrapper(ProfileData profile) {
		this.profile = profile;
	}

	/**
	 * Get the wrapped profile.
	 *
	 * @return The wrapped profile
	 */
	public ProfileData getProfile() {
		return profile;
	}
}
