package com.demo.pages;

import com.microsoft.playwright.Page;

/** Common plumbing for all page objects. Each page object wraps the current thread's Page. */
public abstract class BasePage {

    protected final Page page;

    protected BasePage(Page page) {
        this.page = page;
    }

    /** Navigates relative to baseUrl from config.properties. */
    protected void open(String path) {
        page.navigate(path);
    }

    public String title() {
        return page.title();
    }

    public String url() {
        return page.url();
    }
}
