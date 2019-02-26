package realworld.jaxrs.impl.user;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * Jackson mixin to customize the serialization of the {@link realworld.user.model.ProfileData}.
 */
@JsonTypeName("profile")
@JsonTypeInfo(include = JsonTypeInfo.As.WRAPPER_OBJECT ,use = JsonTypeInfo.Id.NAME)
public class ProfileMixin {
}
