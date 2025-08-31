package com.grocerystore.api.steps;


import io.cucumber.java.en.Given;
import io.cucumber.java.en.And;
import java.util.*;

import org.junit.Assert;

public class CartSteps {

	private final ScenarioContext scenarioContext;
	private final CartHelper cartHelper;

	public CartSteps(ScenarioContext scenarioContext) {
		this.scenarioContext = scenarioContext;
		this.cartHelper = new CartHelper(scenarioContext);
	}

	@Given("I have created a new empty cart")
	public void createEmptyCart() {
		String cartId = cartHelper.createNewCart();
		scenarioContext.setContext(TestState.CART_ID, cartId);
	}

	@And("I add the following items to the cart")
	public void addItemsToCart(List<Map<String, Integer>> itemsTable) {
		String cartId = (String) scenarioContext.getContext(TestState.CART_ID);

		List<Map<String, Integer>> items = new ArrayList<>();
		for (Map<String, Integer> row : itemsTable) {
			Map<String, Integer> map = new HashMap<>();
			map.put("productId", row.get("productId"));
			map.put("quantity", row.get("quantity"));
			items.add(map);
		}

		scenarioContext.setContext(TestState.EXPECTED_ORDER_ITEMS, items);
		cartHelper.addItemsToCart(cartId, items);
	}

	@And("the cart contains all items with correct quantities")
	public void verifyCartItems() {
		String cartId = (String) scenarioContext.getContext(TestState.CART_ID);
		List<Map<String, Integer>> expectedItems = (List<Map<String, Integer>>) scenarioContext
				.getContext(TestState.EXPECTED_ORDER_ITEMS);
		List<Map<String, Integer>> actualItems = cartHelper.getCartItems(cartId);

		Assert.assertEquals("Number of items in cart does not match", expectedItems.size(), actualItems.size());
		for (Map<String, Integer> expectedItem : expectedItems) {
			Assert.assertTrue("Expected item not found in cart: " + expectedItem,
					containsMatchingItem(actualItems, expectedItem));
		}

		Hooks.getScenario().log("Cart items added successfully.");
	}

	@And("I have a cart ready to checkout")
	public void cartReadyToCheckout() {
		Hooks.getScenario().log("Cart is ready for checkout.");
	}

	public static boolean containsMatchingItem(List<Map<String, Integer>> actualItems,
			Map<String, Integer> expectedItem) {
		return actualItems.stream()
				.anyMatch(actualItem -> expectedItem.get("productId").equals(actualItem.get("productId")) &&
						expectedItem.get("quantity").equals(actualItem.get("quantity")));
	}
}
