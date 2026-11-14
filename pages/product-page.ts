import type { Locator, Page } from '@playwright/test';

export class ProductPage {
  constructor(private readonly page: Page) {}

  name(): Locator {
    return this.page.getByTestId('product-name');
  }

  /** Numeric price without a currency symbol, e.g. "58.48". */
  unitPrice(): Locator {
    return this.page.getByTestId('unit-price');
  }

  description(): Locator {
    return this.page.getByTestId('product-description');
  }

  addToCartButton(): Locator {
    return this.page.getByTestId('add-to-cart');
  }

  specRows(): Locator {
    return this.page.getByTestId('spec-row');
  }
}
