/**
 * Central settings, replacing the old config.properties.
 * Override any value with an environment variable, e.g.:
 *   BROWSER=firefox HEADLESS=false SLOW_MO=300 npx playwright test
 */

function env(name: string, fallback: string): string {
  const value = process.env[name];
  return value && value.trim() !== '' ? value.trim() : fallback;
}

function envBool(name: string, fallback: boolean): boolean {
  return env(name, String(fallback)).toLowerCase() === 'true';
}

export const config = {
  // Target site (public "Toolshop" demo)
  baseUrl: env('BASE_URL', 'https://practicesoftwaretesting.com').replace(/\/+$/, ''),

  // chromium | firefox | webkit | chrome | msedge
  browser: env('BROWSER', 'chromium').toLowerCase(),
  headless: envBool('HEADLESS', true),
  slowMo: Number(env('SLOW_MO', '0')),

  // Record a video of every scenario into test-results/ (RECORD_VIDEO=true)
  recordVideo: envBool('RECORD_VIDEO', false),

  // Parallel scenarios (one browser per worker)
  workers: Number(env('WORKERS', '3')),

  timeout: Number(env('TIMEOUT', '15000')),
  navigationTimeout: Number(env('NAVIGATION_TIMEOUT', '30000')),

  viewport: {
    width: Number(env('VIEWPORT_WIDTH', '1366')),
    height: Number(env('VIEWPORT_HEIGHT', '768')),
  },

  // Demo account (public)
  username: env('USERNAME', 'customer@practicesoftwaretesting.com'),
  password: env('PASSWORD', 'welcome01'),
};
