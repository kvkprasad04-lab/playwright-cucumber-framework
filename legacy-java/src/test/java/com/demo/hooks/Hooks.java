package com.demo.hooks;

import com.demo.config.ConfigReader;
import com.demo.factory.PlaywrightFactory;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Tracing;
import com.microsoft.playwright.Video;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Hooks {

    private static final Path TRACE_DIR = Paths.get("target", "traces");

    @Before
    public void startBrowser() {
        PlaywrightFactory.start();
        // Record every scenario; the trace is only written to disk if it fails.
        PlaywrightFactory.getContext().tracing().start(new Tracing.StartOptions()
                .setScreenshots(true)
                .setSnapshots(true)
                .setSources(true));
    }

    @After
    public void stopBrowser(Scenario scenario) {
        // page.video() is never null in Playwright Java, so check the config flag instead
        Video video = ConfigReader.getBoolean("recordVideo") ? PlaywrightFactory.getPage().video() : null;
        try {
            if (scenario.isFailed()) {
                Page page = PlaywrightFactory.getPage();
                try {
                    byte[] shot = page.screenshot(new Page.ScreenshotOptions().setFullPage(true));
                    scenario.attach(shot, "image/png", "Screenshot on failure");
                } catch (RuntimeException e) {
                    scenario.log("Could not take screenshot: " + e.getMessage());
                }
                Path trace = TRACE_DIR.resolve(safeName(scenario) + ".zip");
                PlaywrightFactory.getContext().tracing().stop(new Tracing.StopOptions().setPath(trace));
                scenario.log("Trace saved: " + trace.toAbsolutePath()
                        + "\nOpen it with: mvn exec:java -e -D\"exec.mainClass=com.microsoft.playwright.CLI\" -D\"exec.classpathScope=test\" -D\"exec.args=show-trace " + trace + "\""
                        + "\nor drag it into https://trace.playwright.dev");
            } else {
                PlaywrightFactory.getContext().tracing().stop();
            }
        } finally {
            try {
                // The video file is only complete once the context is closed,
                // and must be saved before Playwright itself is shut down.
                if (video != null) {
                    PlaywrightFactory.getContext().close();
                    Path target = Paths.get("target", "videos", safeName(scenario) + ".webm");
                    video.saveAs(target);
                    video.delete();
                    scenario.log("Video saved: " + target.toAbsolutePath());
                }
            } finally {
                PlaywrightFactory.stop();
            }
        }
    }

    private static String safeName(Scenario scenario) {
        String name = scenario.getName().replaceAll("[^a-zA-Z0-9-_]+", "_");
        return name + "_line" + scenario.getLine() + "_" + System.currentTimeMillis();
    }
}
