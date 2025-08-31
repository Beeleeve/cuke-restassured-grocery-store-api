package com.grocerystore.api.utils;

import io.cucumber.java.Scenario;
import io.restassured.filter.Filter;
import io.restassured.filter.FilterContext;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;

public class CustomLoggingFilter implements Filter {

        private final Scenario scenario;

        public CustomLoggingFilter(Scenario scenario) {
                this.scenario = scenario;
        }

        @Override
        public Response filter(FilterableRequestSpecification requestSpec,
                        FilterableResponseSpecification responseSpec,
                        FilterContext ctx) {

                // Log request details
                String requestLog = "REQUEST:\n" +
                                requestSpec.getMethod() + " " + requestSpec.getURI() + "\n" +
                                "Headers: " + requestSpec.getHeaders() + "\n" +
                                "Query Params: " + requestSpec.getQueryParams() + "\n" +
                                "Body: " + (requestSpec.getBody() != null ? requestSpec.getBody() : "N/A");
                scenario.log(requestLog);

                // Proceed with the request
                Response response = ctx.next(requestSpec, responseSpec);

                // Log response details
                String responseLog = "RESPONSE:\n" +
                                "Status Code: " + response.getStatusCode() + "\n" +
                                "Body: " + response.asPrettyString();
                scenario.log(responseLog);

                // Attach response JSON to the report
                // scenario.attach(response.asPrettyString().getBytes(),
                // "application/json",
                // "API Response");

                return response;
        }
}
