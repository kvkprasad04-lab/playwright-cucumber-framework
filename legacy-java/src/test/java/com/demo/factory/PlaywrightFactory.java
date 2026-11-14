package com.demo.factory;

import com.demo.config.ConfigReader;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;

import java.nio.file.Paths;

/**
 * Thread-safe Playwright lifecycle.
 *
 * Playwright objects are not thread-safe, so every test thread gets its own
 * Playwright -> Browser -> BrowserContext -> Page chain, held in ThreadLocals.
 * Scenarios running in parallel therefore never share a browser or page.
 */
public final class PlaywrightFactory {

    private static final ThreadLocal<Playwright> PLAYWRIGHT = new ThreadLocal<>();
    private static final ThreadLocal<Browser> BROWSER = new ThreadLocal<>();
    private static final ThreadLocal<BrowserContext> CONTEXT = new ThreadLocal<>();
    private static final ThreadLocal<Page> PAGE = new ThreadLocal<>();

    private PlaywrightFactory() {
    }

    /** Starts a fresh, isolated browser session for the current thread. */
    public static Page start() {
        Playwright playwright = Playwright.create();
        // The demo site tags elements with data-test="...", so page.getByTestId() uses that attribute
        playwright.selectors().setTestIdAttribute("data-test");
        PLAYWRIGHT.set(playwright);

        Browser browser = launch(playwright, ConfigReader.get("browser").toLowerCase());
        BROWSER.set(browser);

        int width = ConfigReader.getInt("viewportWidth");
        int height = ConfigReader.getInt("viewportHeight");
        Browser.NewContextOptions contextOptions = new Browser.NewContextOptions()
                .setBaseURL(ConfigReader.baseUrl())
                .setViewportSize(width, height);
        if (ConfigReader.getBoolean("recordVideo")) {
            contextOptions.setRecordVideoDir(Paths.get("target", "videos", "tmp"))
                    .setRecordVideoSize(width, height);
        }
        BrowserContext context = browser.newContext(contextOptions);
        context.setDefaultTimeout(ConfigReader.getInt("timeout"));
        context.setDefaultNavigationTimeout(ConfigReader.getInt("navigationTimeout"));
        CONTEXT.set(context);

        Page page = context.newPage();
        PAGE.set(page);
        return page;
    }

    private static Browser launch(Playwright playwright, String name) {
        BrowserType.LaunchOptions options = new BrowserType.LaunchOptions()
                .setHeadless(ConfigReader.getBoolean("headless"))
                .setSlowMo(ConfigReader.getInt("slowMo"));
        switch (name) {
            case "chromium":
                return playwright.chromium().launch(options);
            case "chrome":
            case "msedge":
                // Branded browsers installed on the machine
                return playwright.chromium().launch(options.setChannel(name));
            case "firefox":
                return playwright.firefox().launch(options);
            case "webkit":
                return playwright.webkit().launch(options);
            default:
                throw new IllegalArgumentException("Unsupported browser '" + name
                        + "'. Use chromium, firefox, webkit, chrome or msedge.");
        }
    }

    public static Page getPage() {
        Page page = PAGE.get();
        if (page == null) {
            throw new IllegalStateException("No browser started on thread " + Thread.currentThread().getName());
        }
        return page;
    }

    public static BrowserContext getContext() {
        return CONTEXT.get();
    }

    /** Closes everything for the current thread and clears the ThreadLocals. */
    public static void stop() {
        try {
            if (CONTEXT.get() != null) CONTEXT.get().close();
            if (BROWSER.get() != null) BROWSER.get().close();
            if (PLAYWRIGHT.get() != null) PLAYWRIGHT.get().close();
        } finally {
            PAGE.remove();
            CONTEXT.remove();
            BROWSER.remove();
            PLAYWRIGHT.remove();
        }
    }
}
