import type { Locator, Page } from '@playwright/test';

export class LoginPage {
  constructor(private readonly page: Page) {}

  async open(): Promise<void> {
    await this.page.goto('/auth/login');
  }

  async loginAs(user: string, pass: string): Promise<void> {
    await this.page.getByTestId('email').fill(user);
    await this.page.getByTestId('password').fill(pass);
    await this.page.getByTestId('login-submit').click();
  }

  error(): Locator {
    return this.page.getByTestId('login-error');
  }
}
