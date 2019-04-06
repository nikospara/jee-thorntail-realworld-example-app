package realworld.test.liquibase;

import java.sql.Connection;
import java.sql.DriverManager;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A JUnit 5 extension that runs Liquibase before all tests.
 */
public class LiquibaseExtension implements BeforeAllCallback {

	private static final Logger LOG = LoggerFactory.getLogger(LiquibaseExtension.class);

	@Override
	public void beforeAll(ExtensionContext extensionContext) throws Exception {
		String dburl = System.getProperty("database-test.url");
		String dbuser = System.getProperty("database-test.username");
		String dbpass = System.getProperty("database-test.password");
		if( LOG.isDebugEnabled() ) {
			LOG.debug(
					"Running...\n" +
					"**************************************************************\n" +
					"* Liquibase Junit 5 Extension\n" +
					"* DB URL: {}\n" +
					"* Username: {}\n" +
					"* Password: {}\n" +
					"**************************************************************",
					dburl, dbuser, (dbpass != null ? "(set to secret value)" : "null")
			);
		}

		try( Connection conn = DriverManager.getConnection(dburl, dbuser, dbpass) ) {
			Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(conn));
			Liquibase liquibase = new Liquibase("db.changelog.xml", new ClassLoaderResourceAccessor(getClass().getClassLoader()), database);
			liquibase.update("");
		}
	}
}
