package com.grocerystore.api.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.UUID;

import com.grocerystore.api.core.Endpoint;
import com.grocerystore.api.core.SpecFactory;

import io.cucumber.java.Scenario;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class TokenManager {

    private static final String TOKEN_FILE = "token.txt";
    private final Scenario scenario;

    public TokenManager(Scenario scenario) {
        this.scenario = scenario;
    }

    public String getToken(String clientName, String clientEmail) {

        String envToken = System.getenv("API_TOKEN");
        if (envToken != null && !envToken.isEmpty()) {
            scenario.log("Using token from environment variable");
            return envToken.trim();
        }
        String cachedToken = readTokenFromFile();
        if (cachedToken != null && !cachedToken.isEmpty()) {
            scenario.log("Using cached token from file");
            return cachedToken.trim();
        }

        Response response = requestToken(clientName, clientEmail);
        if (response.statusCode() == 201) {
            writeTokenToFile(response.jsonPath().getString("accessToken"));
            return response.jsonPath().getString("accessToken");
        }

        if (response.statusCode() == 409) {
            scenario.log("Client already registered, attempting to retrieve existing token");
            clientEmail = "client_" + UUID.randomUUID().toString().substring(0, 8) + "@example.com";
            response = requestToken(clientName, clientEmail);

        }
        scenario.log("New token obtained: " + response.jsonPath().getString("accessToken"));
        writeTokenToFile(response.jsonPath().getString("accessToken"));
        return response.jsonPath().getString("accessToken");
    }

    public String refreshToken(String clientName, String clientEmail) {
        String newToken = requestToken(clientName, clientEmail).jsonPath().getString("accessToken");
        writeTokenToFile(newToken);
        scenario.log("Token refreshed: " + newToken);
        return newToken;
    }

    private String readTokenFromFile() {
        try {
            Path path = Path.of(TOKEN_FILE);
            if (Files.exists(path)) {
                return Files.readString(path).trim();
            }
        } catch (IOException e) {
            scenario.log("Failed to read token file: " + e.getMessage());
        }
        return null;
    }

    private void writeTokenToFile(String token) {
        try {
            Files.writeString(Path.of(TOKEN_FILE), token);
        } catch (IOException e) {
            scenario.log("Failed to write token file: " + e.getMessage());
        }
    }

    private Response requestToken(String clientName, String clientEmail) {
        Map<String, String> payload = Map.of(
                "clientName", clientName,
                "clientEmail", clientEmail);

        return SpecFactory.createBaseSpec(scenario)
                .contentType("application/json")
                .body(payload)
                .when()
                .post(Endpoint.REGISTER_API_CLIENT.getPath());

    }
}