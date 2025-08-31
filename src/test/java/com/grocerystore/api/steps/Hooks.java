package com.grocerystore.api.steps;

import com.grocerystore.api.utils.ConfigReader;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.restassured.RestAssured;

public class Hooks {

    private static final ThreadLocal<Scenario> scenarioThreadLocal = new ThreadLocal<>();

    @Before
    public void setUp(Scenario scenario) {
        // Store scenario in ThreadLocal
        setScenario(scenario);

        // Log scenario start
        getScenario().log("Starting scenario: " + scenario.getName());

        // Initialize RestAssured base URI
        RestAssured.baseURI = getBaseUri();
    }

    // Set the current scenario
    public static void setScenario(Scenario scenario) {
        scenarioThreadLocal.set(scenario);
    }

    // Get the current scenario
    public static Scenario getScenario() {
        return scenarioThreadLocal.get();
    }

    // Remove scenario after scenario finishes
    public static void clear() {
        scenarioThreadLocal.remove();
    }

    @After
    public void tearDown() {
        Scenario scenario = getScenario();
        if (scenario != null) {
            scenario.log("Scenario finished: " + scenario.getName());
        }
        clear();
    }

    private String getBaseUri() {
        String baseUri = System.getenv("BASE_URI");

        if (baseUri == null || baseUri.isEmpty()) {
            try {
                baseUri = ConfigReader.getBaseUrl();
            } catch (Exception e) {
                getScenario().log("Error reading base URL from config: " + e.getMessage());
            }
        }

        if (baseUri == null || baseUri.isEmpty()) {
            baseUri = "https://simple-grocery-store-api.click";
        }

        getScenario().log("Using Base URI: " + baseUri);
        return baseUri;
    }
}
