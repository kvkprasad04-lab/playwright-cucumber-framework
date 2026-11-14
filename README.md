# Playwright + Cucumber (playwright-bdd) UI tests in TypeScript

Target site: https://practicesoftwaretesting.com (public "Toolshop" demo). Settings live in `config/config.ts`.

The Java version (Playwright + Cucumber + TestNG) is kept for reference in `legacy-java/`.

## Prerequisites
- Node.js 20+ (`node -v`)
- On the first run, install the browsers: `npx playwright install chromium`

## Setup
```bash
npm install
npx playwright install chromium     # add firefox / webkit if you use them
```

## Run
```bash
npm test                                   # all scenarios (generates tests, then runs them)
npm run test:headed                        # watch the browser
npm run test:firefox                       # or test:webkit
npm run test:login                         # only @login scenarios
npx playwright test --grep @search         # any tag, via npx
BROWSER=msedge npm test                    # chromium | firefox | webkit | chrome | msedge
RECORD_VIDEO=true npm test                 # keep a video of every scenario
WORKERS=1 npm test                         # one scenario at a time
BASE_URL=https://staging.example.com npm test
```

Any setting in `config/config.ts` can be overridden with an environment variable (`HEADLESS=false`, `SLOW_MO=300`, ...).

Under the hood, `npm test` runs `npx bddgen` (turns the `.feature` files into Playwright tests in `.features-gen/`) and then `npx playwright test`.

## Reports & debugging
| Output | Location |
|---|---|
| Playwright HTML report (open with `npm run report`) | `playwright-report/index.html` |
| JSON results (for CI) | `reports/results.json` |
| Trace of each failed scenario | `test-results/**/trace.zip` |
| Screenshot / video of failures | `test-results/` |

Open a trace with `npx playwright show-trace <file>.zip`, or drag it into https://trace.playwright.dev.

## Structure
```
features/                 Gherkin scenarios (login, search, contact)
steps/                    Step definitions (login, search, contact)
pages/                    Page objects (home, login, account, contact)
config/config.ts          Settings and env-variable overrides
playwright.config.ts      Browsers, parallelism, reporters, failure artifacts
legacy-java/              The original Java/TestNG version
```

## Adding a test
1. Add a page object in `pages/` (a class that takes a Playwright `Page`).
2. Write the scenario in a `.feature` file in `features/`.
3. Implement the steps in `steps/`, using `createBdd()` and the `{ page }` fixture. Run `npm test` to check.
