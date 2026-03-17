# Skill: Screenplay REST Implementation

## Purpose
Standardize implementation style for Serenity Screenplay REST tests.

## Rules
- One task class per endpoint action.
- One task performs one HTTP request only.
- Keep reusable static factories for readability.
- Put assertions in Questions + step layer.
- Keep task names and method names semantic and in English.
