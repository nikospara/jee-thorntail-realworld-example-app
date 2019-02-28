package realworld.jaxrs.sys;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import realworld.article.model.ArticleData;
import realworld.jaxrs.impl.article.ArticleMixin;

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
		objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
		objectMapper.registerModule(new JavaTimeModule());
		objectMapper.registerModule(new SimpleModule() {
			{
				addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'hh:mm:ss.SSS'Z'").withZone(ZoneId.of("UTC"))));
			}
		});
		SimpleDateFormat traditional8601 = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSS'Z'"); // NOTE: This is cloned, so it is thread safe
		traditional8601.setTimeZone(TimeZone.getTimeZone("UTC"));
		objectMapper.setDateFormat(traditional8601);
		objectMapper.addMixIn(ArticleData.class, ArticleMixin.class);
	}

	@Override
	public ObjectMapper getContext(Class<?> type) {
		return objectMapper;
	}
}
