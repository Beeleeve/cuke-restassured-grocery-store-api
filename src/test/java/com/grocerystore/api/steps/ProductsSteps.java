package com.grocerystore.api.steps;

import io.cucumber.java.en.*;
import io.cucumber.java.Scenario;
import java.util.List;
import java.util.Map;

import com.grocerystore.api.service.ProductService;
import com.grocerystore.api.service.StatusService;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ProductsSteps {

    private Scenario scenario;
    private ProductService productService;
    private StatusService statusService;
    private ScenarioContext scenarioContext;

    public ProductsSteps(ScenarioContext scenarioContext) {
        this.scenarioContext = scenarioContext;
        this.scenario = Hooks.getScenario();
        this.productService = new ProductService(scenario);
        this.statusService = new StatusService(scenario);
    }

    // ----------------- Generic Given -----------------
    @Given("I want to browse products in grocery store")
    public void i_want_to_browse_products_in_grocery_store() {
        statusService.getApiStatus().then()
                .statusCode(200)
                .body("status", equalTo("UP"));
        scenario.log("Browsing products in grocery store");
    }

    @When("I filter products by category {string}")
    public void filterProductsByCategory(String category) {
        scenario.log("Filtering products by category: " + category);
        List<Map<String, Object>> products = productService.getProductsByCategory(category)
                .jsonPath().getList("$");

        scenarioContext.setContext(TestState.FILTERED_PRODUCTS, products);
        scenarioContext.setContext(TestState.SELECTED_CATEGORY, category);
    }

    @Then("I get products matching the category")
    public void verifyFilteredProducts() {
        List<Map<String, Object>> products = (List<Map<String, Object>>) scenarioContext
                .getContext(TestState.FILTERED_PRODUCTS);
        String category = scenarioContext.getContext(TestState.SELECTED_CATEGORY).toString();

        if (products.isEmpty()) {
            throw new AssertionError("No products returned for category: " + category);
        }

        for (Map<String, Object> product : products) {
            assertEquals(category, product.get("category"));
            assertNotNull(product.get("id"));
            assertNotNull(product.get("name"));
            assertNotNull(product.get("inStock"));
        }

        scenario.log("Successfully validated products for category: " + category);
    }

    // ----------------- Product ID Filtering -----------------
    @When("I filter products by product ID {string}")
    public void i_filter_products_by_product_id(String productID) {
        scenarioContext.setContext(TestState.PRODUCT_ID, productID);
        scenario.log("Filtering products by product ID: " + productID);

    }

    @Then("I get the product details for that product ID")
    public void i_get_product_details_for_product_id() {
        String productID = scenarioContext.getContext(TestState.PRODUCT_ID).toString();
        productService.getProductById(productID).then()
                .statusCode(200)
                .body("id", equalTo(Integer.parseInt(productID)))
                .body("name", notNullValue())
                .body("category", notNullValue())
                .body("inStock", notNullValue());

        scenario.log("Successfully fetched product details for ID: " + productID);
    }
}
