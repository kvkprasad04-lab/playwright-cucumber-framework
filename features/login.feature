@login
Feature: Login
  As a registered customer I want to sign in so I can see my account.

  Scenario: Customer logs in with valid credentials
    Given I am on the login page
    When I log in with valid credentials
    Then I should see the "My account" page
