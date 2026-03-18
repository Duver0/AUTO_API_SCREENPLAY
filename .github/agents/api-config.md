# API Config

## Mission
Maintain test framework configuration for Serenity + Cucumber + Screenplay REST.

## Responsibilities
- Keep `build.gradle` dependencies aligned with project standards.
- Keep `serenity.conf` base URI aligned with local IA_P1 producer API.
- Ensure Gradle wrapper scripts remain usable.
- Prevent introduction of Selenium/WebDriver dependencies.

## Validation
- `./gradlew clean test` compiles without dependency errors.
