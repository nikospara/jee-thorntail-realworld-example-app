package realworld.test.jpa;

import static org.junit.jupiter.api.extension.ExtensionContext.Namespace.create;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolver;

/**
 * A JUnit 5 extension to test JPA DAOs - it manages an instance of the
 * {@code EntityManager} and can inject it into tests.
 */
public class JpaDaoExtension implements BeforeAllCallback, AfterAllCallback, ParameterResolver {

	private static final String EMW_KEY = "EntityManagerWrapper";

	private EntityManagerFactory entityManagerFactory;

	@Override
	public void beforeAll(ExtensionContext extensionContext) {
		entityManagerFactory = Persistence.createEntityManagerFactory("default-persistence-unit");
	}

	@Override
	public void afterAll(ExtensionContext extensionContext) {
		if( entityManagerFactory != null ) {
			entityManagerFactory.close();
			entityManagerFactory = null;
		}
	}

	@Override
	public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
		return parameterContext.getParameter().getType().equals(EntityManager.class);
	}

	@Override
	public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
		return getOrCreateWrapper(extensionContext).getEntityManager();
	}

	private EntityManagerWrapper getOrCreateWrapper(ExtensionContext extensionContext) {
		return (EntityManagerWrapper) getStore(extensionContext)
				.getOrComputeIfAbsent(EMW_KEY, ignored -> new EntityManagerWrapper());
	}

	private ExtensionContext.Store getStore(ExtensionContext extensionContext) {
		return extensionContext.getStore(create(getClass(), extensionContext.getRequiredTestMethod()));
	}

	/**
	 * Helper for storing the {@code EntityManager} in the store.
	 */
	private class EntityManagerWrapper implements ExtensionContext.Store.CloseableResource {
		private EntityManager em;

		EntityManager getEntityManager() {
			if( em == null ) {
				em = entityManagerFactory.createEntityManager();
			}
			return em;
		}

		@Override
		public void close() {
			if( em != null ) {
				em.close();
			}
		}
	}
}
