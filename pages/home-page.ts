import type { Locator, Page } from '@playwright/test';

export class HomePage {
  constructor(private readonly page: Page) {}

  async open(): Promise<void> {
    await this.page.goto('/');
  }

  async searchFor(term: string): Promise<void> {
    await this.page.getByTestId('search-query').fill(term);
    // Wait for the search API call, otherwise the old product grid may still be on screen
    await Promise.all([
      this.page.waitForResponse((r) => r.url().includes('/products/search') && r.ok()),
      this.page.getByTestId('search-submit').click(),
    ]);
  }

  searchCaption(): Locator {
    return this.page.getByTestId('search-caption');
  }

  productNames(): Locator {
    return this.page.getByTestId('product-name');
  }
}
