package com.grocerystore.api.steps;

import java.util.List;
import java.util.Map;

import com.grocerystore.api.utils.CustomLoggingFilter;

import io.restassured.response.Response;
import static io.restassured.RestAssured.given;

public class OrderHelper {
    private final ScenarioContext scenarioContext;

    public OrderHelper(ScenarioContext scenarioContext) {
        this.scenarioContext = scenarioContext;
    }

    public Response placeOrderRequest(String token, Map<String, Object> payload) {
        return given()
                .filter(new CustomLoggingFilter(Hooks.getScenario()))
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

    public List<Map<String, Object>> getOrderItemsRequest(String orderId, String token) {
        return given()
                .filter(new CustomLoggingFilter(Hooks.getScenario()))
                .header("Authorization", "Bearer " + token)
                .contentType("application/json")
                .when()
                .get("/orders/" + orderId)
                .then()
                .statusCode(200)
                .extract()
                .response().jsonPath().getList("items");
    }
}
