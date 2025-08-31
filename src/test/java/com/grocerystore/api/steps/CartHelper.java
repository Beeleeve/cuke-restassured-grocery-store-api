package com.grocerystore.api.steps;

import io.restassured.http.ContentType;
import io.restassured.response.Response;

import java.util.List;
import java.util.Map;

import com.grocerystore.api.utils.CustomLoggingFilter;

import java.util.HashMap;

import static io.restassured.RestAssured.given;

public class CartHelper {

    private final ScenarioContext scenarioContext;

    public CartHelper(ScenarioContext scenarioContext) {
        this.scenarioContext = scenarioContext;
    }

    /**
     * Create a new empty cart
     * 
     * @return cartId
     */
    public String createNewCart() {
        Response response = given()
                .filter(new CustomLoggingFilter(Hooks.getScenario()))
                .contentType("application/json")
                .when()
                .post("/carts")
                .then()
                .statusCode(201)
                .extract()
                .response();

        String cartId = response.jsonPath().getString("cartId");
        Hooks.getScenario().log("New cart created with ID: " + cartId);
        return cartId;
    }

    /**
     * Add a single item to a cart
     */
    public void addItemToCart(String cartId, Integer productId, int quantity) {
        Map<String, Integer> payload = new HashMap<>();
        payload.put("productId", productId);
        payload.put("quantity", quantity);
        Hooks.getScenario().log("Adding item to cart: " + productId + " with quantity: " + quantity);
        given()
                .filter(new CustomLoggingFilter(Hooks.getScenario()))
                .contentType("application/json")
                .body(payload)
                .when()
                .post("/carts/" + cartId + "/items")
                .then()
                .statusCode(201)
                .extract()
                .response();

    }

    /**
     * Add multiple items to a cart
     */
    public void addItemsToCart(String cartId, List<Map<String, Integer>> items) {
        for (Map<String, Integer> item : items) {
            Integer productId = item.get("productId");
            Integer quantity = item.get("quantity");
            addItemToCart(cartId, productId, quantity);
        }
    }

    /**
     * Optional: Get all items from the cart
     * Can be used before checkout for verification
     */
    public List<Map<String, Integer>> getCartItems(String cartId) {
        return given()
                .filter(new CustomLoggingFilter(Hooks.getScenario()))
                .contentType(ContentType.JSON)
                .when()
                .get("/carts/" + cartId + "/items")
                .then()
                .statusCode(200)
                .extract()
                .response().jsonPath().getList("$");

    }
}
