# cuke-restassured-grocery-store-api
Cucumber BDD RestAssured Ecomm Grocery Store API Tests
Note: These are example tests, not production code tests
* Also token is stored in text file for convenience, but in prod it would be kept in a secret

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
mvn clean test -Dtest="com.learn.restassured.runners.RunCucumberTest"
```
### Test reports

Cucumber reports are available under `target` folder > `cucumber-reports` > `index.html` file
