import type { Locator, Page } from '@playwright/test';

export class AccountPage {
  constructor(private readonly page: Page) {}

  pageTitle(): Locator {
    return this.page.getByTestId('page-title');
  }

  userMenu(): Locator {
    return this.page.getByTestId('nav-menu');
  }
}
