package com.demo.steps;

import com.demo.factory.PlaywrightFactory;
import com.demo.pages.ContactPage;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.Map;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class ContactSteps {

    private ContactPage contactPage;

    @Given("I am on the contact page")
    public void iAmOnTheContactPage() {
        contactPage = new ContactPage(PlaywrightFactory.getPage()).open();
    }

    @When("I fill in the contact form with:")
    public void iFillInTheContactFormWith(Map<String, String> data) {
        contactPage.fillForm(
                data.get("first name"),
                data.get("last name"),
                data.get("email"),
                data.get("subject"),
                data.get("message"));
    }

    @When("I submit the contact form")
    public void iSubmitTheContactForm() {
        contactPage.submit();
    }

    @Then("I should see the confirmation {string}")
    public void iShouldSeeTheConfirmation(String text) {
        assertThat(contactPage.successMessage()).containsText(text);
    }
}
