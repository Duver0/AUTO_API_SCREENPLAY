@ciclo_vida_consultorio
Feature: Consultorio lifecycle through medical API commands

  As a medical operations user
  I want to manage a consultorio lifecycle with independent API flows
  So that doctor assignment, patient attention, availability, and release remain consistent

  @consultorio_post
  Scenario: POST links an authenticated doctor to an available consultorio
    Given a doctor account is authenticated and an available consultorio is identified
    When the doctor links to the selected consultorio
    Then the last operation should return status 202
    And the consultorio state should become "ConMedicoDisponible"
    And the consultorio should be linked to the authenticated doctor

  @consultorio_get
  Scenario: GET returns active attention state with visible assigned patient
    Given a doctor account is authenticated, linked, and has an active attention with a patient
    When the actor consults the selected consultorio state
    Then the last operation should return status 200
    And the consultorio state should be "EnAtencion"
    And the active patient should be visible in consultorio state

  @consultorio_put
  Scenario: PATCH updates consultorio availability to non available after active attention
    Given a doctor account is authenticated, linked, and has completed the active attention
    When the doctor updates consultorio availability to false
    Then the last operation should return status 202
    And the consultorio state should become "ConMedicoNoDisponible"

  @consultorio_delete
  Scenario: POST release leaves the consultorio without doctor
    Given a doctor account is authenticated, linked, and the consultorio is non available
    When the doctor releases the selected consultorio
    Then the last operation should return status 202
    And the consultorio state should become "SinMedico"
    And the consultorio should not have a linked doctor
