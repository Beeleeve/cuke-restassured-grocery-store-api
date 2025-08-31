package com.grocerystore.api.service;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.List;
import java.util.Map;

import io.cucumber.java.Scenario;
import com.grocerystore.api.core.Endpoint;
import com.grocerystore.api.core.SpecFactory;

public class CartService {

    private Scenario scenario;

    public CartService(Scenario scenario) {
        this.scenario = scenario;
    }

    /**
     * Create a new empty cart
     * 
     * @return cartId
     */
    public String createNewCart() {
        return SpecFactory.createBaseSpec(scenario)
                .when()
                .post(Endpoint.CREATE_CART.getPath())
                .then()
                .statusCode(201)
                .extract()
                .response().jsonPath().getString("cartId");
    }

    public Response addItemToCart(String cartId, Map<String, Integer> payload) {
        String endPoint = Endpoint.ADD_ITEM_TO_CART.format(cartId);
        return SpecFactory.createBaseSpec(scenario)
                .contentType(ContentType.JSON)
                .body(payload)
                .when()
                .post(endPoint)
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
            addItemToCart(cartId, item);
        }
    }

    /**
     * Optional: Get all items from the cart
     * Can be used before checkout for verification
     */
    public List<Map<String, Integer>> getCartItems(String cartId) {
        return SpecFactory.createBaseSpec(scenario)
                .when()
                .get(Endpoint.GET_CART_ITEMS.format(cartId))
                .then()
                .statusCode(200)
                .extract()
                .response().jsonPath().getList("$");

    }
}
