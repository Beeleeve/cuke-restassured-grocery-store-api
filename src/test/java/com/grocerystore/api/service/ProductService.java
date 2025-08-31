package com.grocerystore.api.service;

import com.grocerystore.api.core.Endpoint;
import com.grocerystore.api.core.SpecFactory;

import io.cucumber.java.Scenario;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class ProductService {

    private Scenario scenario;

    public ProductService(Scenario scenario) {
        this.scenario = scenario;
    }

    // Add product-related methods here as needed

    public Response getProductById(String productId) {
        return SpecFactory.createBaseSpec(scenario)
                .pathParam("productId", productId)
                .when()
                .get(Endpoint.GET_PRODUCT_BY_ID.getPath())
                .then()
                .statusCode(200)
                .extract()
                .response();
    }

    public Response getAllProducts() {
        return SpecFactory.createBaseSpec(scenario)
                .when()
                .get(Endpoint.GET_ALL_PRODUCTS.getPath())
                .then()
                .statusCode(200)
                .extract()
                .response();
    }

    public Response getProductsByCategory(String category) {
        var request = SpecFactory.createBaseSpec(scenario)
                .queryParam("category", category);
        return request.when()
                .get(Endpoint.GET_ALL_PRODUCTS.getPath())
                .then()
                .statusCode(200)
                .extract()
                .response();
    }
}
