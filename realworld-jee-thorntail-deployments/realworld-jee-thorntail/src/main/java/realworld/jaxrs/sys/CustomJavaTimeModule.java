package realworld.jaxrs.sys;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

/**
 * Customize the way Jackson serializes the {@code java.time.LocalDateTime}.
 */
class CustomJavaTimeModule extends SimpleModule {
	/**
	 * Construct and customize behavior.
	 */
	public CustomJavaTimeModule() {
		addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'hh:mm:ss.SSS'Z'").withZone(ZoneId.of("UTC"))));
	}
}
