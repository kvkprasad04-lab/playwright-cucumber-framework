package com.demo.steps;

import com.demo.factory.PlaywrightFactory;
import com.demo.pages.HomePage;
import com.microsoft.playwright.Locator;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.List;
import java.util.regex.Pattern;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static org.testng.Assert.assertTrue;

public class SearchSteps {

    private HomePage homePage;

    @Given("I am on the home page")
    public void iAmOnTheHomePage() {
        homePage = new HomePage(PlaywrightFactory.getPage()).open();
    }

    @When("I search for {string}")
    public void iSearchFor(String term) {
        homePage.searchFor(term);
    }

    @Then("the results should be for {string}")
    public void theResultsShouldBeFor(String term) {
        assertThat(homePage.searchCaption()).containsText(term);
    }

    @Then("every product shown should contain {string}")
    public void everyProductShownShouldContain(String term) {
        Locator products = homePage.productNames();
        Pattern matching = Pattern.compile(term, Pattern.CASE_INSENSITIVE);
        assertThat(products.first()).containsText(matching);
        // Wait until the grid has re-rendered: no product left that doesn't match the term
        assertThat(products.filter(new Locator.FilterOptions().setHasNotText(matching))).hasCount(0);
        List<String> names = products.allInnerTexts();
        assertTrue(!names.isEmpty(), "Expected at least one product for '" + term + "'");
        for (String name : names) {
            assertTrue(name.toLowerCase().contains(term.toLowerCase()),
                    "Product '" + name + "' does not match search term '" + term + "'");
        }
    }
}
