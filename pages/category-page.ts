import type { Locator, Page } from '@playwright/test';

/** Turns a displayed price such as "$100.79" into 100.79. Returns NaN if there is no number. */
export function parsePrice(text: string): number {
  const digits = text.replace(/[^0-9.]/g, '');
  return digits === '' ? NaN : Number(digits);
}

export class CategoryPage {
  constructor(private readonly page: Page) {}

  /** Opens the Categories menu and follows the link with the given test id, e.g. "nav-power-tools". */
  async openFromMenu(menuLinkTestId: string): Promise<void> {
    await this.page.getByTestId('nav-categories').click();
    await this.page.getByTestId(menuLinkTestId).click();
  }

  productNames(): Locator {
    return this.page.getByTestId('product-name');
  }

  productPrices(): Locator {
    return this.page.getByTestId('product-price');
  }
}
