package com.grocerystore.api.service;

import com.grocerystore.api.core.Endpoint;
import com.grocerystore.api.core.SpecFactory;

import io.cucumber.java.Scenario;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class StatusService {

    private Scenario scenario;

    public StatusService(Scenario scenario) {
        this.scenario = scenario;
    }

    public Response getApiStatus() {
        return SpecFactory.createBaseSpec(scenario)
                .when()
                .get(Endpoint.GET_API_STATUS.getPath())
                .then()
                .statusCode(200)
                .extract()
                .response();
    }

}