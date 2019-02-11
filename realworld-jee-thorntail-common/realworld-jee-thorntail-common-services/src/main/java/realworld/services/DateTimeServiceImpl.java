package realworld.services;

import java.time.LocalDateTime;

import javax.enterprise.context.ApplicationScoped;

/**
 * Implementation of {@link DateTimeService}.
 */
@ApplicationScoped
class DateTimeServiceImpl implements DateTimeService {

	@Override
	public LocalDateTime getNow() {
		return LocalDateTime.now();
	}

	@Override
	public long currentTimeMillis() {
		return System.currentTimeMillis();
	}
}
