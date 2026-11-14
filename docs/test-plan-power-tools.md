# Test Plan: Power Tools Category Search

**Application under test:** https://practicesoftwaretesting.com
**Feature area:** Product search by category, then product detail verification
**Assumptions:** The Categories control is a dropdown (confirm on the live page, since the site may use checkboxes). "Power Tools" is a top-level category or has a visible option. Prices are in USD.

## Preconditions
- Browser is open, with no logged-in session required for browsing.
- Network access to the demo site.
- Test data: the Power Tools category, and a product priced above $100 (determined at run time from the results, so the test does not hardcode a product).

## Test Steps and Expected Outcomes

| # | Step | Expected Outcome |
|---|------|------------------|
| 1 | Open the home URL | Home page loads. The title and header are visible, and the Categories control is present. |
| 2 | Select the Categories dropdown | The dropdown opens and lists the available categories, including "Power Tools". |
| 3 | Click "Power Tools" | The URL or page updates to reflect the category filter. Product cards appear. The "Power Tools" option is shown as selected. |
| 4 | Inspect the results | At least one product card is shown. Every card belongs to the Power Tools category. No "no results" message appears. Result count is non-zero. |
| 5 | Select any power tool priced above $100 | Only products with a price over $100 are chosen. The selected product's detail page opens. |
| 6 | Identify product details to verify | See the verification list below. |

## Product Details to Verify (Detail Page)

| Attribute | Verification |
|-----------|--------------|
| Product name | Non-empty, and matches the name shown in the results list |
| Price | Displayed, numeric, and greater than 100. Matches the price shown in the list. |
| Currency | Shown as $ (USD) |
| Category | Matches "Power Tools" |
| Description | Non-empty text |
| Image | Visible, with a valid `src` and a non-zero rendered size |
| Brand | Displayed and non-empty |
| Rating | Displayed as a value from 0 to 5, if the site shows one |
| Add to cart button | Visible and enabled |
| Stock or availability | Shown, if the site displays it |

## Expected Outcomes for the Search Workflow
1. Selecting Power Tools filters the list. No products from other categories appear.
2. Every product in the filtered list shows the Power Tools category.
3. A product above $100 exists in the Power Tools results. If none exists, the test fails with a clear message, not a silent pass.
4. The product detail page matches the list card: same name and same price.
5. The detail page loads without console errors or broken images.
6. The user can return to the results list, and the category filter is kept.

## Negative and Edge Checks (Optional)
- Selecting a category with no products shows an empty-state message.
- A price filter boundary test: a product priced exactly $100 is excluded, and one at $100.01 is included, if the site offers a price filter.

## Test Data and Automation Notes
- Price comparison should parse the numeric value and strip the currency symbol, so the test does not depend on formatting.
- Pick the product by a rule (first item with price > 100), not by a fixed name, so the test stays stable as the catalog changes.
- Map this to the existing layout: a `features/` file for the scenario, a step file, and a page object with `getByTestId` locators where the site provides `data-test` attributes.
