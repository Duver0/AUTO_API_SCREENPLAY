# AUTO_API_SCREENPLAY

REST API automation project using the Screenplay pattern with Serenity BDD and Serenity Rest (RestAssured integration). The project validates a locally running medical software API through a full CRUD lifecycle.

---

## 1. Project Description

This project automates a REST API running at `http://localhost:3000/` using the **Screenplay pattern** with **Serenity BDD** and **RestAssured**. Each HTTP operation is encapsulated in its own Task class following the Single Responsibility Principle. Actors receive the `CallAnApi` ability and interact with the API through declarative, business-oriented Gherkin steps.

**Stack:**
- Language: Java 17+
- Framework: Serenity BDD + Serenity Rest (RestAssured integration)
- Dependency management: Gradle
- Test runner: Cucumber (JUnit 4)
- Pattern: Screenplay with REST — no Selenium, no WebDriver

---

## 2. Prerequisites and Configuration

### Prerequisites
- Java 17 or higher
- Gradle (or use the included `./gradlew` wrapper)
- The target API must be running locally at `http://localhost:3000/`

### Base URI Configuration

The base URL is configured in `serenity.conf` at the project root:

```hocon
environments {
  default {
    base.url = "http://localhost:3000/"
  }
}
```

To point the tests at a different environment, update the `base.url` value in `serenity.conf` before running.

---

## 3. Run Command

```bash
./gradlew clean test aggregate
```

---

## 4. Report Command

```bash
./gradlew aggregate
```

The HTML report will be generated in `target/site/serenity/index.html`.

To open it from Linux after generation:

```bash
xdg-open target/site/serenity/index.html
```

---

## 5. Scenario Matrix — Medical Software Endpoints

The suite runs a single scenario covering the full CRUD lifecycle:

| # | Action | HTTP Verb | Endpoint | What it validates |
|---|----------|-----------|----------|-------------------|
| 1 | Register account | POST | `/auth/signUp` | Staff account creation |
| 2 | Sign in | POST | `/auth/signIn` | Authentication works |
| 3 | Create appointment | POST | `/turnos` | Appointment accepted |
| 4 | Consult queue | GET | `/turnos` | List monitoring |
| 5 | Update appointment | PUT | `/turnos/{id}` | Resource modification |
| 6 | Delete appointment | DELETE | `/turnos/{id}` | Resource removal |

---

## 6. Project Structure

```
src/
  test/
    java/
      com.autoapiscreenplay/
        runner/
          CucumberTestRunner.java
        screenplay/
          actors/
            ApiActorFactory.java
          tasks/
            CreateAccountTask.java
            SignInTask.java
            CreateAppointmentTask.java
            GetAppointmentQueueTask.java
            UpdateAppointmentTask.java
            DeleteAppointmentTask.java
          questions/
            ResponseStatusQuestion.java
            ResponseBodyQuestion.java
          models/
            SignUpRequest.java
            SignInRequest.java
            AppointmentRequest.java
        steps/
          MedicalWorkflowSteps.java
    resources/
      features/
        api_flow.feature
serenity.conf
build.gradle
```
