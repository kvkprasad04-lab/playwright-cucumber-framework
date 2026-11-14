# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Commands

```bash
npm install && npx playwright install chromium   # one-time setup
npm test                                         # bddgen + playwright test (all scenarios)
npm run test:login                               # bddgen + only @login scenarios
npx bddgen && npx playwright test --grep @search # any tag, or a scenario name via --grep
npx bddgen && npx playwright test --grep "Customer logs in"   # single scenario by name
npx bddgen && npx playwright test --project=chromium --headed  # or: HEADLESS=false npm test
npm run report                                   # open the Playwright HTML report
npx tsc --noEmit                                 # type-check (tsconfig covers config/, pages/, steps/)
```

- There is no lint script. Type-checking via `tsc` is the only static check.
- Browser is chosen with `BROWSER=chromium|firefox|webkit|chrome|msedge`. Each value maps to one Playwright project in `playwright.config.ts`, so only that browser runs.
- Settings come from `config/config.ts`, which reads env vars (`BASE_URL`, `HEADLESS`, `SLOW_MO`, `WORKERS`, `TIMEOUT`, `RECORD_VIDEO`, `USERNAME`, `PASSWORD`).

## Architecture

**BDD generation step.** `.feature` files are not run directly. `bddgen` (from `playwright-bdd`) compiles them into Playwright spec files under `.features-gen/`, which is gitignored and regenerated. `playwright.config.ts` calls `defineBddConfig` to point at `features/**/*.feature` and `steps/**/*.ts`, and sets `testDir` to the generated output. If you run `npx playwright test` without `bddgen` first, you run stale generated tests. Any change to a feature or step file needs `bddgen` before the run.

**Layers:**
- `features/` holds Gherkin scenarios, tagged `@login`, `@search`, `@contact`.
- `steps/` holds step definitions. Each file calls `createBdd()` and gets Playwright's `{ page }` fixture. Steps stay thin: they create a page object and call one method.
- `pages/` holds page objects. Each class takes a Playwright `Page` in its constructor and exposes `open()`, action methods, and `Locator`-returning getters for assertions. Assertions (`expect`) live in the steps, not in the page objects.
- `config/config.ts` is the single source of settings. `playwright.config.ts` maps them onto Playwright options.

**Selectors.** `playwright.config.ts` sets `testIdAttribute: 'data-test'`, so `getByTestId('x')` matches `data-test="x"` on the target site. Prefer this over CSS or text selectors when writing new page objects.

**Target site.** Tests run against the public demo at `https://practicesoftwaretesting.com`, with the demo customer account as the default credentials. They need network access, and they can break when the site changes its markup.

**Parallelism.** `fullyParallel: true` with `WORKERS` (default 3). Scenarios must not depend on each other's state.

**Failure artifacts.** Traces, screenshots, and videos are kept on failure in `test-results/`. `RECORD_VIDEO=true` records every scenario. The JSON reporter writes `reports/results.json`.

**Legacy code.** `legacy-java/` is the original Java/TestNG + Cucumber version, kept for reference. It is not part of the Node build and `tsconfig` excludes it. Don't change it unless asked.
