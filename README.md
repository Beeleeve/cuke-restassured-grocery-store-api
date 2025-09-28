# cuke-restassured-grocery-store-api
Cucumber BDD RestAssured Ecomm Grocery Store API Tests
Note: These are example tests, not production code tests
* Also token is stored in text file for convenience, but in prod it would be kept in a secret

## API Under Test
This project tests the public API: [Grocery Store API](https://simple-grocery-store-api.click)

## Key Features & Practices
- **Advanced Cucumber features**: Data tables are used for parameterizing test steps and scenarios.
- **Cucumber best practices**: Gherkin syntax is written following recommended conventions for clarity and maintainability.
- **Cucumber shared context**: Shared context is implemented to pass state and data between step definitions.
- **RestAssured advanced features**: Reusable request and response specifications are used for efficient and consistent API testing.
- **RestAssured logging**: Request and response logging is enabled for better debugging and traceability.

### Grocery store API features
* Location: `src/test/resources/features`
* Feature for search or filter products in grocery store
* Feature to place orders with multiple cart items

### Config
* base url for a given env is set in config files under `src/test/resources/config`
 
### How to run the tests

* pom.xml changes for maven surefire plugin config

```xml
<plugin>
          <artifactId>maven-surefire-plugin</artifactId>
          <version>3.3.1</version>
          <configuration>
            <includes>
              <include>**/runners/*Test.java</include>
            </includes>
          </configuration>
        </plugin>
```
#### Maven command to run a Cucumber runner file in terminal
```bash
mvn clean test -Dtest="com.grocerystore.api.runners.RunCucumberTest"
```
### Test reports

Cucumber reports are available under `target` folder > `cucumber-reports` > `index.html` file

---

## CI/CD & Secrets Management

- **GitHub Actions**: Planned integration for automated test execution on every push and pull request, ensuring continuous validation of API tests.

## Future plans
- **Secrets Management**: Future plans include storing API access tokens and passwords securely using GitHub vault or AWS Secrets Manager, replacing local text file storage for enhanced security and compliance.
