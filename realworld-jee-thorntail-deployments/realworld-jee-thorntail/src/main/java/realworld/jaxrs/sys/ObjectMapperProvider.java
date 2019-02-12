package realworld.jaxrs.sys;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import realworld.jaxrs.impl.user.ProfileMixin;
import realworld.user.model.ProfileData;

/**
 * Provide the Jackson {@code ObjectMapper} to JAX-RS,
 * customized for the needs of the application.
 *
 * @see com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider
 * @see com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider
 * @see com.fasterxml.jackson.jaxrs.json.JsonMapperConfigurator
 */
@Provider
@ApplicationScoped
public class ObjectMapperProvider implements ContextResolver<ObjectMapper> {

	private ObjectMapper objectMapper;

	@PostConstruct
	void init() {
		objectMapper = new ObjectMapper();
		objectMapper.setAnnotationIntrospector(new JacksonAnnotationIntrospector());
		objectMapper.enable(SerializationFeature.WRAP_ROOT_VALUE);
		objectMapper.enable(DeserializationFeature.UNWRAP_ROOT_VALUE);
		objectMapper.addMixIn(ProfileData.class, ProfileMixin.class);
	}

	@Override
	public ObjectMapper getContext(Class<?> type) {
		return objectMapper;
	}
}
