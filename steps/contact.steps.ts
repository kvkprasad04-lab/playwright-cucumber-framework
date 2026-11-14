import { expect } from '@playwright/test';
import { createBdd } from 'playwright-bdd';
import { ContactPage } from '../pages/contact-page';

const { Given, When, Then } = createBdd();

Given('I am on the contact page', async ({ page }) => {
  await new ContactPage(page).open();
});

// The table has two columns (field name | value), read as a key/value map
When('I fill in the contact form with:', async ({ page }, dataTable) => {
  const data = dataTable.rowsHash();
  await new ContactPage(page).fillForm({
    firstName: data['first name'],
    lastName: data['last name'],
    email: data['email'],
    subject: data['subject'],
    message: data['message'],
  });
});

When('I submit the contact form', async ({ page }) => {
  await new ContactPage(page).submit();
});

Then('I should see the confirmation {string}', async ({ page }, text: string) => {
  await expect(new ContactPage(page).successMessage()).toContainText(text);
});
