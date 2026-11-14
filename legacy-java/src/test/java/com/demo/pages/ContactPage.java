package com.demo.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.SelectOption;

public class ContactPage extends BasePage {

    public ContactPage(Page page) {
        super(page);
    }

    public ContactPage open() {
        open("/contact");
        return this;
    }

    public ContactPage fillForm(String firstName, String lastName, String email, String subject, String message) {
        page.getByTestId("first-name").fill(firstName);
        page.getByTestId("last-name").fill(lastName);
        page.getByTestId("email").fill(email);
        page.getByTestId("subject").selectOption(new SelectOption().setLabel(subject));
        page.getByTestId("message").fill(message);
        return this;
    }

    public ContactPage submit() {
        page.getByTestId("contact-submit").click();
        return this;
    }

    public Locator successMessage() {
        return page.locator(".alert-success");
    }
}
