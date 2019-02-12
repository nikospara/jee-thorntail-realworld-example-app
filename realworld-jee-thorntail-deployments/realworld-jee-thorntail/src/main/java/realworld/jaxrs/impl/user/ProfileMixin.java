package realworld.jaxrs.impl.user;

import com.fasterxml.jackson.annotation.JsonRootName;

/**
 * Jackson mixin to customize the serialization of the {@link realworld.user.model.ProfileData}.
 */
@JsonRootName("profile")
public class ProfileMixin {
}
