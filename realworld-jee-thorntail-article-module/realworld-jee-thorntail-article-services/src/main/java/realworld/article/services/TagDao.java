package realworld.article.services;

import java.util.List;

/**
 * Tag entity DAO.
 */
public interface TagDao {
	/**
	 * Get all tags.
	 *
	 * @return A list of all tags
	 */
	List<String> findAll();
}
