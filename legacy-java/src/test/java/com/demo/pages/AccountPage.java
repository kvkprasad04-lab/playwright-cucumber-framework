package com.demo.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class AccountPage extends BasePage {

    public AccountPage(Page page) {
        super(page);
    }

    public Locator pageTitle() {
        return page.getByTestId("page-title");
    }

    public Locator userMenu() {
        return page.getByTestId("nav-menu");
    }
}
