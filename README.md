# AUTO_API_SCREENPLAY

REST API automation project using the Screenplay pattern with Serenity BDD and Serenity Rest (RestAssured integration). The project validates a locally running API through a business-oriented scenario that covers the full product lifecycle using 2 POST and 2 GET operations in a single Cucumber scenario.

---

## 1. Project Description

This project automates a REST API running at `http://localhost:3001/` using the **Screenplay pattern** with **Serenity BDD** and **RestAssured**. Each HTTP operation is encapsulated in its own Task class following the Single Responsibility Principle. Actors receive the `CallAnApi` ability and interact with the API through declarative, business-oriented Gherkin steps.

**Stack:**
- Language: Java 11+
- Framework: Serenity BDD + Serenity Rest (RestAssured integration)
- Dependency management: Gradle
- Test runner: Cucumber (JUnit 4)
- Pattern: Screenplay with REST — no Selenium, no WebDriver

---

## 2. Prerequisites and Configuration

### Prerequisites
- Java 11 or higher
- Gradle (or use the included `./gradlew` wrapper)
- The target API must be running locally at `http://localhost:3001/`

### Base URI Configuration

The base URL is configured in `serenity.conf` at the project root:

```
restapi {
  baseURI = "http://localhost:3001/"
}
```

To point the tests at a different environment, update the `restapi.baseURI` value in `serenity.conf` before running.

---

## 3. Run Command

```bash
./gradlew clean test
```

---

## 4. Report Command

```bash
./gradlew reports
```

The HTML report will be generated in `target/site/serenity/index.html`.

---

## 5. Flow Description — 2 POST + 2 GET Strategy

The scenario executes four HTTP operations in sequence, simulating a realistic API consumer workflow:

| # | Step | HTTP Verb | Endpoint | Business Purpose |
|---|------|-----------|----------|-----------------|
| 1 | `Given a product has been registered in the catalog` | POST | `/products` | Creates the first product and confirms it was accepted (HTTP 201) |
| 2 | `When the catalog is queried to confirm the product was added` | GET | `/products` | Retrieves all products and verifies the first one appears in the list |
| 3 | `And a second product is registered with different specifications` | POST | `/products` | Creates a second, distinct product and captures its generated ID |
| 4 | `Then the second product can be retrieved and its details validated by identifier` | GET | `/products/{id}` | Retrieves the second product by its specific ID and validates its fields |

**Business Justification:**
- **POST 1** proves the creation endpoint is functional and returns the correct HTTP status.
- **GET all** proves that created resources are persisted and retrievable from the full list.
- **POST 2** proves the API handles multiple distinct resources correctly.
- **GET by ID** proves the retrieval-by-identifier endpoint returns the exact resource with correct data, which is the most common consumer use-case.

---

## 6. Project Structure

```
src/
  test/
    java/
      runner/
        CucumberTestRunner.java
      screenplay/
        actors/
          ApiActorFactory.java
        tasks/
          CreateResourceTask.java         ← POST 1: create first product
          GetAllResourcesTask.java        ← GET 1: list all products
          CreateAnotherResourceTask.java  ← POST 2: create second product
          GetResourceByIdTask.java        ← GET 2: retrieve product by ID
        questions/
          ResponseStatusQuestion.java
          ResponseBodyQuestion.java
        models/
          ProductRequest.java
          ProductResponse.java
      steps/
        CrudSteps.java
    resources/
      features/
        api_flow.feature
serenity.conf
build.gradle
```
