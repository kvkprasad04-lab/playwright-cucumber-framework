package com.demo.steps;

import com.demo.config.ConfigReader;
import com.demo.factory.PlaywrightFactory;
import com.demo.pages.AccountPage;
import com.demo.pages.LoginPage;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class LoginSteps {

    // Cucumber creates a new instance of each step class per scenario, so these fields are never shared between threads.
    private LoginPage loginPage;
    private AccountPage accountPage;

    @Given("I am on the login page")
    public void iAmOnTheLoginPage() {
        loginPage = new LoginPage(PlaywrightFactory.getPage()).open();
    }

    @When("I log in with valid credentials")
    public void iLogInWithValidCredentials() {
        accountPage = loginPage.loginAs(ConfigReader.get("username"), ConfigReader.get("password"));
    }

    @Then("I should see the {string} page")
    public void iShouldSeeThePage(String title) {
        assertThat(accountPage.pageTitle()).hasText(title);
    }
}
