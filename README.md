# DD Watch Backend

#### Prerequisites
- Java **12**
- Docker

#### Running the app
- Note: if you have Postgres or Sonar running locally, do yourself a service and stop them 😀
- First run `mvn docker:run` to start a **postgres** container and a **sonarqube** container
- You can start the Spring Boot app with `mvn spring-boot:run` or using a run configuration in IntelliJ
- After the app is up and running, some mock data is inserted, including project repositories which contain __sonar urls__ and __sonar component keys__.
By default these point to your local DD Watch Backend repository.
- To populate sonar with meaningful data, run `mvn sonar:sonar`. If you haven't configured your Maven plugin, read [this](https://docs.sonarqube.org/display/SCAN/Analyzing+with+SonarQube+Scanner+for+Maven).
- To access the Sonar UI, go to `localhost:9000`
- To stop and remove the containers, run `mvn docker:stop`

### Tests
- The app uses an in-memory H2 database for tests that require storing data
- Use the test profile either by setting it on the run configuration if running from IntelliJ or by exporting it in the
shell: `export SPRING_PROFILES_ACTIVE=test`
- Run with `mvn test`
