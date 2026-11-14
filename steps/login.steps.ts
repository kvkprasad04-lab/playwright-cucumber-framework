import { expect } from '@playwright/test';
import { createBdd } from 'playwright-bdd';
import { config } from '../config/config';
import { AccountPage } from '../pages/account-page';
import { LoginPage } from '../pages/login-page';

const { Given, When, Then } = createBdd();

Given('I am on the login page', async ({ page }) => {
  await new LoginPage(page).open();
});

When('I log in with valid credentials', async ({ page }) => {
  await new LoginPage(page).loginAs(config.username, config.password);
});

Then('I should see the {string} page', async ({ page }, title: string) => {
  await expect(new AccountPage(page).pageTitle()).toHaveText(title);
});
