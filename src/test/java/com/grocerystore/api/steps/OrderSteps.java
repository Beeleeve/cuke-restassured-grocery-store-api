package com.grocerystore.api.steps;

import io.cucumber.java.en.When;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.Then;
import io.restassured.response.Response;

import java.util.List;
import java.util.Map;

import com.grocerystore.api.service.OrderService;
import com.grocerystore.api.service.TokenManager;

public class OrderSteps {

	private final ScenarioContext scenarioContext;
	private String clientName = "MyClient";
	private String clientEmail = "client1234@example.com";
	private OrderService orderService;
	private TokenManager tokenHelper;
	private Scenario scenario;

	public OrderSteps(ScenarioContext scenarioContext) {
		this.scenarioContext = scenarioContext;
		this.scenario = Hooks.getScenario();
		this.orderService = new OrderService(scenario);
		this.tokenHelper = new TokenManager(scenario);
	}

	@When("I place an order for the cart")
	public void placeOrder() throws Exception {
		String cartId = (String) scenarioContext.getContext(TestState.CART_ID);
		String token = (String) scenarioContext.getContext(TestState.BEARER_TOKEN);
		if (token == null || token.isEmpty()) {
			token = tokenHelper.getToken(clientName, clientEmail);
		}
		Map<String, Object> payload = Map.of(
				"cartId", cartId,
				"customerName", clientName);

		Response response = orderService.placeOrderRequest(token, payload);
		if (response.statusCode() == 401) {
			// Token might be expired, request a new one and retry once
			token = tokenHelper.refreshToken(clientName, clientEmail);
			scenarioContext.setContext(TestState.BEARER_TOKEN, token);
			response = orderService.placeOrderRequest(token, payload);
		}
		String orderId = response.jsonPath().getString("orderId");
		scenarioContext.setContext(TestState.ORDER_ID, orderId);
		scenario.log("Order placed successfully. Order ID: " + orderId);
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
		if (token == null || token.isEmpty()) {
			token = tokenHelper.getToken(clientName, clientEmail);
		}

		Response response = orderService.getOrderItemsRequest(orderId, token);

		if (response.statusCode() == 401) {
			// Token might be expired, request a new one and retry once
			token = tokenHelper.refreshToken(clientName, clientEmail);
			scenarioContext.setContext(TestState.BEARER_TOKEN, token);
			response = orderService.getOrderItemsRequest(orderId, token);
		}
		List<Map<String, Object>> actualItems = response.jsonPath().getList("items");

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

}
