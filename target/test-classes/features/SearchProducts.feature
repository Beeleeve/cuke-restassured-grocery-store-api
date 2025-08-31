Feature: Grocery Store Products API - Search/Filter Products
  # ----------------- Filter by Category Scenarios -----------------

  Scenario Outline: Get products by category with optional filters
    Given I want to browse products in grocery store
    When I filter products by category "<category>" with params
      | results | <results> |
      | inStock | <inStock> |
    Then I get products matching the category

    Examples:
      | category      | results | inStock |
      | coffee        |       3 | true    |
      | candy         |       5 | true    |
      | bread-bakery  |       2 | false   |
      | dairy         |       4 | true    |
      | meat-seafood  |       3 | true    |
      | fresh-produce |       5 | false   |
      
  # ----------------- Filter by Product ID Scenarios -----------------

  Scenario Outline: Get product by Product ID
    Given I want to browse products in grocery store
    When I filter products by product ID "<productID>"
    Then I get the product details for that product ID

    Examples:
      | productID |
      |      5774 |
      |      7395 |
      |      5851 |
      |      5478 |
      |      3674 |
      |      4875 |
