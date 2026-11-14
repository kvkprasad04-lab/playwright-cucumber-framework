@search
Feature: Product search

  Scenario: Search returns matching products
    Given I am on the home page
    When I search for "pliers"
    Then the results should be for "pliers"
    And the first product shown should contain "pliers"
