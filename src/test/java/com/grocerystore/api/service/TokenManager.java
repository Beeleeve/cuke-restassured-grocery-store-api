package com.grocerystore.api.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

import com.grocerystore.api.core.Endpoint;
import com.grocerystore.api.core.SpecFactory;

import io.cucumber.java.Scenario;
import io.restassured.specification.RequestSpecification;

public class TokenManager {

    private static final String TOKEN_FILE = "token.txt";
    private final Scenario scenario;

    public TokenManager(Scenario scenario) {
        this.scenario = scenario;
    }

    public String getToken(String clientName, String clientEmail) {
        String cachedToken = readTokenFromFile();
        if (cachedToken != null && !cachedToken.isEmpty()) {
            scenario.log("Using cached token");
            return cachedToken;
        }

        String newToken = requestToken(clientName, clientEmail);
        scenario.log("New token obtained: " + newToken);
        writeTokenToFile(newToken);
        return newToken;
    }

    public String refreshToken(String clientName, String clientEmail) {
        String newToken = requestToken(clientName, clientEmail);
        writeTokenToFile(newToken);
        scenario.log("Token refreshed: " + newToken);
        return newToken;
    }

    private String readTokenFromFile() {
        try {
            File tokenFile = new File(TOKEN_FILE);
            if (tokenFile.exists()) {
                return new String(Files.readAllBytes(Paths.get(TOKEN_FILE))).trim();
            }
        } catch (IOException e) {
            scenario.log("Failed to read token file: " + e.getMessage());
        }
        return null;
    }

    private void writeTokenToFile(String token) {
        try {
            Files.write(Paths.get(TOKEN_FILE), token.getBytes());
        } catch (IOException e) {
            scenario.log("Failed to write token file: " + e.getMessage());
        }
    }

    private String requestToken(String clientName, String clientEmail) {
        Map<String, String> payload = Map.of(
                "clientName", clientName,
                "clientEmail", clientEmail);

        return SpecFactory.createBaseSpec(scenario)
                .body(payload)
                .when()
                .post(Endpoint.REGISTER_API_CLIENT.getPath())
                .then()
                .statusCode(200)
                .extract()
                .response().jsonPath().getString("accessToken");
    }
}