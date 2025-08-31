package com.grocerystore.api.steps;

import java.util.Map;

import com.grocerystore.api.utils.CustomLoggingFilter;
import static io.restassured.RestAssured.given;

public class TokenHelper {
    public String getToken(Map<String, String> payload) {
        return given()
                .filter(new CustomLoggingFilter(Hooks.getScenario()))
                .contentType("application/json")
                .body(payload)
                .when()
                .post("/api-clients")
                .then()
                .statusCode(200)
                .extract()
                .response().jsonPath().getString("accessToken");
    }
}
