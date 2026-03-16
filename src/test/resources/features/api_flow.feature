Feature: Product catalog management

  As an API consumer
  I want to manage products in the catalog
  So that the inventory is accurate and accessible

  Scenario: Complete product lifecycle with creation and retrieval
    Given a product has been registered in the catalog
    When the catalog is queried to confirm the product was added
    And a second product is registered with different specifications
    Then the second product can be retrieved and its details validated by identifier
