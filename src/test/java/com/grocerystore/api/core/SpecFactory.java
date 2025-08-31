package com.grocerystore.api.core;

import io.cucumber.java.Scenario;

import static io.restassured.RestAssured.given;

import com.grocerystore.api.utils.CukeReportLogger;

import io.restassured.specification.RequestSpecification;

public class SpecFactory {

    public static RequestSpecification createBaseSpec(Scenario scenario) {
        return given().filter(new CukeReportLogger(scenario));
    }

}
