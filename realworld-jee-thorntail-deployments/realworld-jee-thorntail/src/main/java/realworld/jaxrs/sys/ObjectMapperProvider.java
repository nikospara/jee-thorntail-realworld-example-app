package realworld.jaxrs.sys;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import realworld.article.model.ArticleData;
import realworld.comments.model.CommentData;
import realworld.jaxrs.impl.article.ArticleMixin;
import realworld.jaxrs.impl.comments.CommentMixin;
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
		objectMapper.enable(DeserializationFeature.UNWRAP_ROOT_VALUE);
		objectMapper.addMixIn(ProfileData.class, ProfileMixin.class);
		objectMapper.addMixIn(ArticleData.class, ArticleMixin.class);
		objectMapper.addMixIn(CommentData.class, CommentMixin.class);
	}

	@Override
	public ObjectMapper getContext(Class<?> type) {
		return objectMapper;
	}
}
