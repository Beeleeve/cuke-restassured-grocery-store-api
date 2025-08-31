package com.grocerystore.api.steps;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.*;
import io.cucumber.java.Scenario;
import io.restassured.response.Response;

import java.util.Map;

import com.grocerystore.api.utils.*;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class ProductsSteps {

    private Response response;
    private String selectedCategory;
    private String productId;
    private Scenario scenario;

    public ProductsSteps() {
        this.scenario = Hooks.getScenario();
    }

    // ----------------- Generic Given -----------------
    @Given("I want to browse products in grocery store")
    public void i_want_to_browse_products_in_grocery_store() {
        scenario.log("Browsing products in grocery store");
    }

    // ----------------- Category Filtering -----------------
    @When("I filter products by category {string}")
    public void i_filter_products_by_category(String category) {
        // Default limit = 5
        i_filter_products_by_category_with_params(category, null);
    }

    @When("I filter products by category {string} with params")
    public void i_filter_products_by_category_with_params(String category, DataTable table) {
        this.selectedCategory = category;
        scenario.log("Filtering products by category: " + category);

        var request = given().filter(new CustomLoggingFilter(scenario))
                .queryParam("category", category);

        // Convert DataTable to Map<String,String> if present
        if (table != null) {
            Map<String, String> queryParams = table.asMap(String.class, String.class);
            queryParams.forEach(request::queryParam);
        }

        response = request.when().get("/products");
    }

    @Then("I get products matching the category")
    public void i_get_products_matching_the_category() {
        response.then()
                .statusCode(200)
                .body("size()", greaterThan(0))
                .body("category", everyItem(equalTo(selectedCategory)))
                .body("id", everyItem(notNullValue()))
                .body("name", everyItem(notNullValue()))
                .body("inStock", everyItem(notNullValue()));

        scenario.log("Successfully fetched products for category: " + selectedCategory);
    }

    // ----------------- Product ID Filtering -----------------
    @When("I filter products by product ID {string}")
    public void i_filter_products_by_product_id(String productID) {
        this.productId = productID;
        scenario.log("Filtering products by product ID: " + productID);

        response = given()
                .filter(new CustomLoggingFilter(scenario))
                .pathParam("id", productID)
                .when()
                .get("/products/{id}");
    }

    @Then("I get the product details for that product ID")
    public void i_get_product_details_for_product_id() {
        response.then()
                .statusCode(200)
                .body("id", equalTo(Integer.parseInt(productId)))
                .body("name", notNullValue())
                .body("category", notNullValue())
                .body("inStock", notNullValue());

        scenario.log("Successfully fetched product details for ID: " + productId);
    }
}
