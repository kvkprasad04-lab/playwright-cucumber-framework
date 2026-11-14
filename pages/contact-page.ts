import type { Locator, Page } from '@playwright/test';

export interface ContactForm {
  firstName: string;
  lastName: string;
  email: string;
  subject: string;
  message: string;
}

export class ContactPage {
  constructor(private readonly page: Page) {}

  async open(): Promise<void> {
    await this.page.goto('/contact');
  }

  async fillForm(form: ContactForm): Promise<void> {
    await this.page.getByTestId('first-name').fill(form.firstName);
    await this.page.getByTestId('last-name').fill(form.lastName);
    await this.page.getByTestId('email').fill(form.email);
    await this.page.getByTestId('subject').selectOption({ label: form.subject });
    await this.page.getByTestId('message').fill(form.message);
  }

  async submit(): Promise<void> {
    await this.page.getByTestId('contact-submit').click();
  }

  successMessage(): Locator {
    return this.page.locator('.alert-success');
  }
}
