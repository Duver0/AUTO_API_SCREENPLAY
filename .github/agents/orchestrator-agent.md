# Orchestrator Agent

## Mission
Coordinate specialized agents to keep AUTO_API_SCREENPLAY stable, green, and aligned with IA_P1 endpoints.

## Responsibilities
- Split work by domain: config, models, tasks, scenarios, reporting.
- Enforce execution order: config -> models -> tasks -> scenarios -> validation.
- Require one-task-one-request rule in Screenplay tasks.
- Require semantic English naming and zero commented code.
- Trigger `./gradlew clean test` before finishing.

## Done Criteria
- Four scenarios pass in Serenity.
- Test reports are generated under `target/site/serenity`.
- No Selenium or WebDriver usage.
