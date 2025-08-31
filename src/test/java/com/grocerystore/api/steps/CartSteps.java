package com.grocerystore.api.steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.And;
import java.util.*;

import org.junit.Assert;

import com.grocerystore.api.service.CartService;

public class CartSteps {

	private final ScenarioContext scenarioContext;
	private final CartService cartService;
	private Scenario scenario;

	public CartSteps(ScenarioContext scenarioContext) {
		this.scenarioContext = scenarioContext;
		this.scenario = Hooks.getScenario();
		this.cartService = new CartService(scenario);
	}

	@Given("I have created a new empty cart")
	public void createEmptyCart() {
		String cartId = cartService.createNewCart();
		scenarioContext.setContext(TestState.CART_ID, cartId);
		scenario.log("Created new cart with ID: " + cartId);
	}

	@And("I add the following items to the cart")
	public void addItemsToCart(List<Map<String, Integer>> itemsTable) {
		String cartId = (String) scenarioContext.getContext(TestState.CART_ID);
		scenario.log("Adding items to cart ID: " + cartId);
		scenarioContext.setContext(TestState.EXPECTED_ORDER_ITEMS, itemsTable);
		cartService.addItemsToCart(cartId, itemsTable);

	}

	@And("the cart contains all items with correct quantities")
	public void verifyCartItems() {
		String cartId = (String) scenarioContext.getContext(TestState.CART_ID);
		List<Map<String, Integer>> expectedItems = (List<Map<String, Integer>>) scenarioContext
				.getContext(TestState.EXPECTED_ORDER_ITEMS);
		List<Map<String, Integer>> actualItems = cartService.getCartItems(cartId);

		Assert.assertEquals("Number of items in cart does not match", expectedItems.size(), actualItems.size());
		for (Map<String, Integer> expectedItem : expectedItems) {
			Assert.assertTrue("Expected item not found in cart: " + expectedItem,
					containsMatchingItem(actualItems, expectedItem));
		}

		scenario.log("Cart items added successfully.");
	}

	@And("I have a cart ready to checkout")
	public void cartReadyToCheckout() {
		scenario.log("Cart is ready for checkout.");
	}

	public static boolean containsMatchingItem(List<Map<String, Integer>> actualItems,
			Map<String, Integer> expectedItem) {
		return actualItems.stream()
				.anyMatch(actualItem -> expectedItem.get("productId").equals(actualItem.get("productId")) &&
						expectedItem.get("quantity").equals(actualItem.get("quantity")));
	}
}
