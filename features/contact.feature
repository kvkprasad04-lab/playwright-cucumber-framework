@contact
Feature: Contact form

  Scenario: Visitor submits the contact form
    Given I am on the contact page
    When I fill in the contact form with:
      | first name | Jane                                                                 |
      | last name  | Tester                                                               |
      | email      | jane.tester@example.com                                              |
      | subject    | Customer service                                                     |
      | message    | This is an automated test message, long enough to pass validation.  |
    And I submit the contact form
    Then I should see the confirmation "Thanks for your message"
