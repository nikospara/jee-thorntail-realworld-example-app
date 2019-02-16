package realworld.article.persistence;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

import realworld.article.services.TagDao;

/**
 * Implementation of the {@link TagDao}.
 */
@ApplicationScoped
public class TagDaoImpl implements TagDao {

	private EntityManager em;

	/**
	 * Default constructor for frameworks.
	 */
	TagDaoImpl() {
		// NOOP
	}

	/**
	 * Injection constructor.
	 *
	 * @param em The entity manager
	 */
	@Inject
	public TagDaoImpl(EntityManager em) {
		this.em = em;
	}

	@Override
	public List<String> findAll() {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<String> query = cb.createQuery(String.class);
		Root<Tag> tag = query.from(Tag.class);
		query.select(tag.get(Tag_.name));
		return em.createQuery(query).getResultList();
	}
}
