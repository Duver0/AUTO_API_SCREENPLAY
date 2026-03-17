# Skill: Serenity Report Audit

## Purpose
Validate report integrity and business traceability.

## Checklist
- Scenario names reflect business capabilities.
- Each scenario has expected HTTP status validation.
- No hidden Selenium/WebDriver artifacts.
- Failure output includes endpoint and status mismatch context.

## Artifacts
- Serenity JSON: `target/site/serenity/*.json`
- JUnit XML: `build/test-results/test/*.xml`
