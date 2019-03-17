# ![RealWorld Example App using JEE 8 and Thorntail](logo.png)

> ### Java EE and Java 11 in Thorntail (with CDI, JAX-RS, JPA and others) codebase containing real world examples (CRUD, auth, advanced patterns, etc) that adheres to the [RealWorld](https://github.com/gothinkster/realworld) spec and API.

This codebase was created to demonstrate a fully fledged fullstack application built with **Java EE** including CRUD operations, authentication, routing, pagination, and more.

We've gone to great lengths to adhere to the **Java EE** community styleguides & best practices.

For more information on how to this works with other frontends/backends, head over to the [RealWorld](https://github.com/gothinkster/realworld) repo.

# How it works

This is a very traditional implementation of a web app in terms of design, but uses the
latest JEE (currently version 8) APIs over Java 11.
It is mostly "packaged by feature", instead of the more traditional "layered" design.

Hopefully it can be used a a starting point or reference for more modern designs.

See a detailed description in [README-Architecture](README-Architecture.md).

# Getting started

## Prerequisites

- Java 11
- Maven >= 3.3.9

## Building

> Generic instructions follow, but see below for a concrete example!

You need to make sure the following properties are available to Maven during the build:

	database.url=<the JDBC URL to your database>
	database.username=<username>
	database.password=<password>

You can set them in the command line of every `mvn` command, e.g. as `mvn -Ddatabase.url=xxxxx`.
You can place them in a profile of your `$HOME/.m2/settings.xml` and make sure this profile is active.
Enable the profile corresponding to the database you selected, e.g. for H2 use `-Ph2`.
For the time being, only H2 is supported.

Then execute:

	mvn clean package -P<selected database type>[,<other necessary profiles>]

Profiles used by the build:

- Database type, currently only `h2` - required
- `dbmigrate`: Run the db migrations. To run the migrations only, execute:

		mvn process-resources -Ph2,dbmigrate[,<other necessary profiles>]

### Executing the DB tests

This example comes with tests for the DAOs. These use a real database - actually they deploy the real schema to an
empty database and then run the tests. To activate, use the `test-<DBTYPE>` profile. At this moment only H2 is
implemented. It starts in in-memory mode, so it is always clean and discarded after the test. To run it execute:

	mvn clean package -Ptest-h2[,<other necessary profiles>]

### Example setup

Suppose you want an H2 DB placed under `/home/myuser/.h2/thorntail-realworld`.
First make sure the parent directory exists:

	mkdir -p /home/myuser/.h2

Add the following to your `$HOME/.m2/settings.xml`:

	<?xml version="1.0" encoding="UTF-8"?>
	<settings ...>
		...
		<profiles>
			...
			<profile>
				<id>thorntail-realworld-local-h2</id>
				<properties>
					<database.url>jdbc:h2:/home/myuser/.h2/thorntail-realworld</database.url>
					<database.username>sa</database.username>
					<database.password>sa</database.password>
				</properties>
			</profile>
		</profiles>
		...
	</settings>

These values will be used both from the application at runtime and from Liquibase at build time.

You need to activate this profile for every build - from IDEs too! Since the database is H2, you also need the `h2` profile,
so cd to the root directory of the project and run:

	mvn clean package -Ph2,thorntail-realworld-local-h2,test-h2

If you also want to run the DB migrations, which you *SHOULD* the first time, run:

	mvn clean package -Ph2,thorntail-realworld-local-h2,test-h2,dbmigrate

These commands run the DB tests as well on an in-memory H2.

## Running

Assuming you are at the project root folder and you have successfully built the project:

	cd realworld-jee-thorntail-deployments/realworld-jee-thorntail/target
	java -jar realworld-jee-thorntail-thorntail.jar

The API root is `http://localhost:8080/` (e.g. to [login](https://github.com/gothinkster/realworld/tree/master/api#authentication),
send the request to `http://localhost:8080/api/users/login`).

A Swagger JSON is available at `http://localhost:8080/api/swagger.json`.

## Running/Debugging from IDE

You will need to recreate some portions of the Maven configuration in your IDE.
In general:

- Class to run: `org.wildfly.swarm.Swarm`
- Use classpath of project `realworld-jee-thorntail` (the WAR project)
- Add VM arguments:
	- `-Ddatabase.thorntail.name=<driver name, e.g. h2>`
	- `-Ddatabase.url=<same value as in settings.xml>`
	- `-Ddatabase.username=<same value as in settings.xml>`
	- `-Ddatabase.password=<same value as in settings.xml>`
- Activate the appropriate DB profile (e.g. `h2`) in your IDE, so that the IDE includes the driver in the classpath
	- In IntelliJ IDEA open the Maven drawer, expand "Profiles" and make sure the appropriate is selected
	- In Eclipse right-click on the WAR project, Maven, "Select Maven Profiles..." and select the appropriate one; make sure the one selected is from the WAR project (see the "Source" column)

### Eclipse specific

Several settings are not understood automatically by Eclipse. Please execute the following manual steps - NOTE: Eclipse distribution is JEE, JBoss Tools installed:

1. Add the Immutables annotation processor to model projects (realworld-jee-thorntail-article-model, realworld-jee-thorntail-comments-model, realworld-jee-thorntail-user-model):
	- Right click project, select properties (or select project, press Alt+Enter)
	- Select "Java Compiler" -> "Annotation Processing"
	- Check the following:
		- "Enable project specific settings"
		- "Enable annotation processing"
		- "Enable processing in editor"
		- Generated source directory: `target/generated-sources/annotations/` (this is where Maven puts them by default)
	- Select "Factory Path" (it is a child of "Java Compiler" on the tree)
	- Add the following external jars, from your Maven repository:
		- `org/immutables/value/2.7.5/value-2.7.5.jar`
2. Add the hibernate-jpamodelgen annotation processor to the JPA projects (realworld-jee-thorntail-article-jpa, realworld-jee-thorntail-comments-jpa, realworld-jee-thorntail-user-jpa):
	- Follow the steps above
	- Add the following external jars, from your Maven repository:
		- `org/hibernate/hibernate-jpamodelgen/5.4.1.Final/hibernate-jpamodelgen-5.4.1.Final.jar`
		- `javax/persistence/persistence-api/1.0.2/persistence-api-1.0.2.jar`
3. "No persistence.xml file found in project" errors:
	- Project properties -> JPA -> Errors/Warnings:
		- Enable project-specific settings
	 	- Project -> "No persistence.xml file found in project": Ignore
4. "The persistence.xml file does not have supported content for this JPA platform." errors (this occurs because, for the time being, the JPA tools do not recognize JPA 2.2):
	- Project properties -> JPA -> Errors/Warnings:
		- Enable project-specific settings
	 	- Project -> "The persistence.xml file does not have supported content for this JPA platform.": Ignore
5. To run DAO tests from within the IDE, the following system properties must be defined in the test configuration of each test:
	- `database-test.active=true`
	- `database-test.hibernate.dialect=<see DB test profile in parent pom.xml>`
	- `database-test.url=<see DB test profile in parent pom.xml>`
	- `database-test.username=<see DB test profile in parent pom.xml>`
	- `database-test.password=<see DB test profile in parent pom.xml>`

## Code quality

This project integrates with SonarQube. First run a build with coverage by activating the `jacoco` profile:

	mvn clean package -Pjacoco,<selected database type>[,<other necessary profiles>]

The run `sonar:sonar`:

	mvn sonar:sonar

This assumes you have SonarQube running on `localhost:9000`.
