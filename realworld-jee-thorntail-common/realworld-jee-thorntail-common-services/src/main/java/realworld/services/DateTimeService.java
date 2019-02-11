package realworld.services;

import java.time.LocalDateTime;

/**
 * Date and time utilities.
 */
public interface DateTimeService {

	/**
	 * Get the server's current local datetime.
	 */
	LocalDateTime getNow();
	
	/**
	 * Get the system current time in milliseconds.
	 * 
	 * @return The system current time in milliseconds
	 */
	long currentTimeMillis();
}
