package com.demo.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class HomePage extends BasePage {

    private final Locator searchBox;
    private final Locator searchButton;

    public HomePage(Page page) {
        super(page);
        this.searchBox = page.getByTestId("search-query");
        this.searchButton = page.getByTestId("search-submit");
    }

    public HomePage open() {
        open("/");
        return this;
    }

    public HomePage searchFor(String term) {
        searchBox.fill(term);
        // Wait for the search API call, otherwise the old product grid may still be on screen
        page.waitForResponse(
                response -> response.url().contains("/products/search") && response.ok(),
                searchButton::click);
        return this;
    }

    public Locator searchCaption() {
        return page.getByTestId("search-caption");
    }

    public Locator productNames() {
        return page.getByTestId("product-name");
    }
}
