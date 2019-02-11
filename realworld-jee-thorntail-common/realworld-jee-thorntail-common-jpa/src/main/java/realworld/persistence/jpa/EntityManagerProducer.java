package realworld.persistence.jpa;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * A CDI producer for the JPA {@code EntityManager}.
 */
@ApplicationScoped
class EntityManagerProducer {

	@PersistenceContext(name = "default-persistence-unit")
	private EntityManager em;

	/**
	 * Producer method for the {@code EntityManager} at request scope.
	 *
	 * @return The {@code EntityManager}
	 */
	@Produces
	@RequestScoped
	public EntityManager getEntityManager() {
		return em;
	}
}
