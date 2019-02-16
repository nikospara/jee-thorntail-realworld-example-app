package realworld.article.services;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;

/**
 * Implementation of {@link TagService}.
 */
@ApplicationScoped
public class TagServiceImpl implements TagService {

	private TagDao tagDao;

	/**
	 * Default constructor for frameworks.
	 */
	TagServiceImpl() {
		// NOOP
	}

	/**
	 * Injection constructor.
	 *
	 * @param tagDao The Tag DAO
	 */
	@Inject
	public TagServiceImpl(TagDao tagDao) {
		this.tagDao = tagDao;
	}

	@Override
	public List<String> getAll() {
		return tagDao.findAll();
	}
}
