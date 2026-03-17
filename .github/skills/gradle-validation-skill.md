# Skill: Gradle Validation

## Purpose
Guarantee stable build and deterministic test feedback.

## Validation Flow
1. Run `./gradlew clean test --no-daemon`.
2. Review `build/test-results/test/TEST-*.xml`.
3. Confirm zero failures and zero errors.
4. Verify Serenity outputs under `target/site/serenity`.

## Guardrails
- Fail fast on endpoint mismatch.
- Keep dependency set minimal and aligned with Serenity REST stack.
