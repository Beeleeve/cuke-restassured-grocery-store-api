Feature: Place Order and Checkout
 # ----------------- Place Order and Checkout Scenarios -----------------

  Scenario: Place order and checkout cart
    Given I have created a new empty cart
    And I add the following items to the cart
      | productId | quantity |
      |      5478 |        1 |
      |      3674 |        2 |
      |      4875 |        2 |
    And the cart contains all items with correct quantities
    And I have a cart ready to checkout
    When I place an order for the cart
    Then the order is successfully placed with an order ID
    And the order contains correct products and quantities
      | productId | quantity |
      |      5478 |        1 |
      |      3674 |        2 |
      |      4875 |        2 |
