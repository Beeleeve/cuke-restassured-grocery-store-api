package com.grocerystore.api.service;

import java.util.List;
import java.util.Map;

import com.grocerystore.api.core.SpecFactory;
import io.cucumber.java.Scenario;
import io.restassured.response.Response;

public class OrderService {

    private Scenario scenario;

    public OrderService(Scenario scenario) {
        this.scenario = scenario;
    }

    public Response placeOrderRequest(String token, Map<String, Object> payload) {
        return SpecFactory.createBaseSpec(scenario)
                .header("Authorization", "Bearer " + token)
                .contentType("application/json")
                .body(payload)
                .when()
                .post("/orders")
                .then()
                .statusCode(201)
                .extract()
                .response();
    }

    public Response getOrderItemsRequest(String orderId, String token) {
        return SpecFactory.createBaseSpec(scenario)
                .header("Authorization", "Bearer " + token)
                .contentType("application/json")
                .when()
                .get("/orders/" + orderId)
                .then()
                .statusCode(200)
                .extract()
                .response();
    }
}
