package com.demo.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class LoginPage extends BasePage {

    private final Locator email;
    private final Locator password;
    private final Locator submit;
    private final Locator error;

    public LoginPage(Page page) {
        super(page);
        this.email = page.getByTestId("email");
        this.password = page.getByTestId("password");
        this.submit = page.getByTestId("login-submit");
        this.error = page.getByTestId("login-error");
    }

    public LoginPage open() {
        open("/auth/login");
        return this;
    }

    public AccountPage loginAs(String user, String pass) {
        email.fill(user);
        password.fill(pass);
        submit.click();
        return new AccountPage(page);
    }

    public Locator error() {
        return error;
    }
}
