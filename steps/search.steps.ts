import { expect } from '@playwright/test';
import { createBdd } from 'playwright-bdd';
import { HomePage } from '../pages/home-page';

const { Given, When, Then } = createBdd();

function escapeRegExp(text: string): string {
  return text.replace(/[.*+?^${}()|[\]\\]/g, '\\$&');
}

Given('I am on the home page', async ({ page }) => {
  await new HomePage(page).open();
});

When('I search for {string}', async ({ page }, term: string) => {
  await new HomePage(page).searchFor(term);
});

Then('the results should be for {string}', async ({ page }, term: string) => {
  await expect(new HomePage(page).searchCaption()).toContainText(term);
});

// The demo site matches search terms against more than the product name (e.g. "pliers"
// returns "Bolt Cutters"), so only the top result is required to contain the term.
Then('the first product shown should contain {string}', async ({ page }, term: string) => {
  const products = new HomePage(page).productNames();
  const matching = new RegExp(escapeRegExp(term), 'i');

  await expect(products.first()).toContainText(matching);
});
