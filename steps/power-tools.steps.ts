import { expect } from '@playwright/test';
import { createBdd } from 'playwright-bdd';
import { CategoryPage, parsePrice } from '../pages/category-page';
import { ProductPage } from '../pages/product-page';

const { When, Then } = createBdd();

// The product chosen in the current scenario. Each scenario sets it before the detail check reads it.
let selected: { name: string; price: number } | undefined;

When('I open the Power Tools category from the categories menu', async ({ page }) => {
  selected = undefined;
  await new CategoryPage(page).openFromMenu('nav-power-tools');
});

Then('the category results should be shown for {string}', async ({ page }, slug: string) => {
  const category = new CategoryPage(page);

  await expect(page).toHaveURL(new RegExp(`/category/${slug}$`));
  await expect(category.productNames().first()).toBeVisible();

  const names = await category.productNames().allInnerTexts();
  const prices = await category.productPrices().allInnerTexts();

  expect(names.length, 'Expected at least one product in the category').toBeGreaterThan(0);
  expect(prices.length, 'Every product should show a price').toBe(names.length);
  for (const [i, price] of prices.entries()) {
    expect(parsePrice(price), `Price '${price}' for '${names[i]}' is not a number`).not.toBeNaN();
  }
});

When('I select the first power tool priced above {int}', async ({ page }, minPrice: number) => {
  const category = new CategoryPage(page);
  const names = await category.productNames().allInnerTexts();
  const prices = await category.productPrices().allInnerTexts();

  const index = prices.findIndex((p) => parsePrice(p) > minPrice);
  expect(index, `No power tool priced above ${minPrice} in the results`).toBeGreaterThanOrEqual(0);

  selected = { name: names[index], price: parsePrice(prices[index]) };
  await category.productNames().nth(index).click();
  await expect(page).toHaveURL(/\/product\//);
});

Then('the product details should match the selected product', async ({ page }) => {
  expect(selected, 'No product was selected earlier in this scenario').toBeDefined();
  const expected = selected!;
  const product = new ProductPage(page);

  await expect(product.name()).toHaveText(expected.name);

  const detailPrice = parsePrice(await product.unitPrice().innerText());
  expect(detailPrice, 'Price on the detail page should match the list price').toBe(expected.price);
  expect(detailPrice, 'Selected product should be priced above 100').toBeGreaterThan(100);

  await expect(product.description()).not.toBeEmpty();
  await expect(product.addToCartButton()).toBeEnabled();
  expect(await product.specRows().count(), 'Expected at least one specification row').toBeGreaterThan(0);
});
