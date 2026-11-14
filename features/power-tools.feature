@power-tools
Feature: Power tools category
  As a visitor I want to browse the Power Tools category and inspect a product so I can judge its details.

  Scenario: Visitor browses Power Tools and checks a product priced above $100
    Given I am on the home page
    When I open the Power Tools category from the categories menu
    Then the category results should be shown for "power-tools"
    When I select the first power tool priced above 100
    Then the product details should match the selected product
