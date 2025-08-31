package com.grocerystore.api.runners;

import org.junit.runner.RunWith;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

@RunWith(Cucumber.class)
@CucumberOptions(features = "src/test/resources/features", glue = {
        "com.learn.restassured.steps" }, plugin = {
                "pretty", 
                "html:target/cucumber-reports/html-report.html",  // <-- HTML report
                "json:target/cucumber-reports/cucumber.json"     // optional JSON
        },
        monochrome = true)
public class RunCucumberTest {
}
