import { defineConfig, devices } from '@playwright/test';
import { defineBddConfig } from 'playwright-bdd';
import { config } from './config/config';

// Generates test files from the .feature files and the step definitions in steps/
const testDir = defineBddConfig({
  features: 'features/**/*.feature',
  steps: 'steps/**/*.ts',
});

// Browser selection: chrome and msedge are branded Chromium installs on this machine
const browserProjects = {
  chromium: { ...devices['Desktop Chrome'], browserName: 'chromium' as const },
  chrome: { ...devices['Desktop Chrome'], browserName: 'chromium' as const, channel: 'chrome' },
  msedge: { ...devices['Desktop Chrome'], browserName: 'chromium' as const, channel: 'msedge' },
  firefox: { ...devices['Desktop Firefox'], browserName: 'firefox' as const },
  webkit: { ...devices['Desktop Safari'], browserName: 'webkit' as const },
};

type BrowserName = keyof typeof browserProjects;
const selected = config.browser as BrowserName;
if (!(selected in browserProjects)) {
  throw new Error(`Unsupported browser '${config.browser}'. Use chromium, firefox, webkit, chrome or msedge.`);
}

export default defineConfig({
  testDir,
  outputDir: 'test-results',
  fullyParallel: true,
  workers: config.workers,
  forbidOnly: !!process.env.CI,
  retries: 0,

  reporter: [
    ['list'],
    ['html', { outputFolder: 'playwright-report', open: 'never' }],
    ['json', { outputFile: 'reports/results.json' }],
  ],

  timeout: config.timeout * 3,
  expect: { timeout: config.timeout },

  use: {
    baseURL: config.baseUrl,
    headless: config.headless,
    launchOptions: { slowMo: config.slowMo },
    viewport: config.viewport,
    navigationTimeout: config.navigationTimeout,
    actionTimeout: config.timeout,
    // Demo site tags elements with data-test="...", so getByTestId() uses that attribute
    testIdAttribute: 'data-test',
    // Failure artifacts: trace, screenshot and video (same as the old Java hooks)
    trace: 'retain-on-failure',
    screenshot: 'only-on-failure',
    video: config.recordVideo ? 'on' : 'retain-on-failure',
  },

  projects: [
    {
      name: selected,
      use: browserProjects[selected],
    },
  ],
});
