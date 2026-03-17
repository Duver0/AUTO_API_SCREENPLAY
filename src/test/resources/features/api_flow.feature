Feature: Medical appointments api workflow

  As an operations user
  I want to use account and appointment services
  So that medical shifts can be registered and consulted reliably

  @CRUD
  Scenario: Complete medical appointment lifecycle
    Given an api consumer is authenticated to call the services
    When the consumer creates a new internal account
    Then the operation should return status 201

    When the consumer signs in with valid credentials
    Then the operation should return status 201

    When the consumer creates a new appointment
    Then the operation should return status 202

    When the consumer consults the appointment queue
    Then the operation should return status 200

