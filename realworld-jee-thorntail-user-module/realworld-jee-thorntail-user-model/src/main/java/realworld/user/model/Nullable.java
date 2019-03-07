package realworld.user.model;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * A nullable marker to appease Immutables.
 */
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
public @interface Nullable {
}
