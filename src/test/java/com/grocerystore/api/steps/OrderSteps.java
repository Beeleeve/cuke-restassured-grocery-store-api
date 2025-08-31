package com.grocerystore.api.steps;

import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.restassured.response.Response;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

public class OrderSteps {

	private final ScenarioContext scenarioContext;
	private String clientName = "MyClient";
	private String clientEmail = "client1234@example.com";
	private OrderHelper orderHelper;
	private TokenHelper tokenHelper;

	public OrderSteps(ScenarioContext scenarioContext) {
		this.scenarioContext = scenarioContext;
		this.orderHelper = new OrderHelper(scenarioContext);
		this.tokenHelper = new TokenHelper();
	}

	@When("I place an order for the cart")
	public void placeOrder() throws Exception {

		String cartId = (String) scenarioContext.getContext(TestState.CART_ID);
		String token = (String) scenarioContext.getContext(TestState.BEARER_TOKEN);
		if (token == null || token.isEmpty()) {
			token = getBearerToken();
		}
		Map<String, Object> payload = Map.of(
				"cartId", cartId,
				"customerName", clientName);

		Response response = orderHelper.placeOrderRequest(token, payload);
		if (response.statusCode() == 401) {
			// Token might be expired, request a new one and retry once
			token = requestNewToken();
			scenarioContext.setContext(TestState.BEARER_TOKEN, token);
			response = orderHelper.placeOrderRequest(token, payload);
		}
		String orderId = response.jsonPath().getString("orderId");
		scenarioContext.setContext(TestState.ORDER_ID, orderId);
		Hooks.getScenario().log("Order placed successfully. Order ID: " + orderId);
	}

	@Then("the order is successfully placed with an order ID")
	public void verifyOrderCreated() {
		String orderId = (String) scenarioContext.getContext(TestState.ORDER_ID);
		if (orderId == null || orderId.isEmpty()) {
			throw new AssertionError("Order ID is missing after placing the order");
		}
	}

	@Then("the order contains correct products and quantities")
	public void verifyOrderItems(List<Map<String, String>> expectedTable) throws Exception {
		String orderId = (String) scenarioContext.getContext(TestState.ORDER_ID);
		String token = (String) scenarioContext.getContext(TestState.BEARER_TOKEN);

		List<Map<String, Object>> actualItems = orderHelper.getOrderItemsRequest(orderId, token);

		for (Map<String, String> expected : expectedTable) {
			String expectedProductId = expected.get("productId");
			int expectedQty = Integer.parseInt(expected.get("quantity"));

			boolean matchFound = actualItems.stream()
					.anyMatch(item -> item.get("productId").toString().equals(expectedProductId)
							&& Integer.parseInt(item.get("quantity").toString()) == expectedQty);

			if (!matchFound) {
				throw new AssertionError("Expected item not found or quantity mismatch: " + expected);
			}
		}
	}

	private String getBearerToken() throws Exception {
		// Check if token exists in scenarioContext
		if (scenarioContext.isContains(TestState.BEARER_TOKEN)) {
			return (String) scenarioContext.getContext(TestState.BEARER_TOKEN);
		}

		// Check if token exists in local file
		File tokenFile = new File("token.txt");
		if (tokenFile.exists()) {
			try {
				String tokenFromFile = new String(Files.readAllBytes(Paths.get("token.txt"))).trim();
				if (!tokenFromFile.isEmpty()) {
					scenarioContext.setContext(TestState.BEARER_TOKEN, tokenFromFile);
					return tokenFromFile;
				}
			} catch (IOException e) {
				Hooks.getScenario().log("Failed to read token from file: " + e.getMessage());
			}
		}

		// Request new token
		String newToken = requestNewToken();

		// Save new token to file for next time
		try {
			Files.write(Paths.get("token.txt"), newToken.getBytes());
		} catch (IOException e) {
			Hooks.getScenario().log("Failed to write token to file: " + e.getMessage());
		}

		scenarioContext.setContext(TestState.BEARER_TOKEN, newToken);
		return newToken;
	}

	private String requestNewToken() {
		// Implement the logic to request a new token from the authentication service
		Map<String, String> payload = Map.of(
				"clientName", clientName,
				"clientEmail", clientEmail);

		String token = tokenHelper.getToken(payload);
		Hooks.getScenario().log("New token obtained: " + token);
		return token;
	}

}
