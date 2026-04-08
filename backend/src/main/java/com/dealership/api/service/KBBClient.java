package com.dealership.api.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

/**
 * Client for fetching KBB (Kelley Blue Book) valuations via VehicleDatabases.com free API.
 *
 * API Documentation: https://www.vehicledatabases.com/
 * No API key required for basic tier.
 */
@Component
public class KBBClient {

    private static final Logger logger = LoggerFactory.getLogger(KBBClient.class);
    private static final String API_BASE_URL = "https://www.vehicledatabases.com/api";
    private static final int TIMEOUT_SECONDS = 10;

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public KBBClient() {
        this.httpClient = HttpClient.newBuilder()
            .connectTimeout(java.time.Duration.ofSeconds(TIMEOUT_SECONDS))
            .build();
        this.objectMapper = new ObjectMapper();
    }

    /**
     * Fetch KBB value for a vehicle by VIN.
     *
     * @param vin Vehicle Identification Number (must be valid, 17 chars)
     * @return Optional containing BigDecimal KBB value, or empty if not found/error
     */
    public Optional<BigDecimal> fetchKBBValueByVin(String vin) {
        if (vin == null || vin.trim().isEmpty()) {
            logger.warn("KBBClient: VIN is null or empty");
            return Optional.empty();
        }

        // Validate VIN format (must be 17 characters)
        String trimmedVin = vin.trim().toUpperCase();
        if (trimmedVin.length() != 17) {
            logger.warn("KBBClient: Invalid VIN length. Expected 17, got {}: {}", trimmedVin.length(), trimmedVin);
            return Optional.empty();
        }

        try {
            // Note: Using a placeholder endpoint. VehicleDatabases.com free tier may require
            // actual VIN decoding. This example shows the pattern for calling such APIs.
            String encodedVin = URLEncoder.encode(trimmedVin, StandardCharsets.UTF_8);
            String url = API_BASE_URL + "/vin/decode?vin=" + encodedVin;

            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(java.time.Duration.ofSeconds(TIMEOUT_SECONDS))
                .header("User-Agent", "DealershipAPI/1.0")
                .GET()
                .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                return parseKBBValue(response.body(), trimmedVin);
            } else {
                logger.warn("KBBClient: API returned status {} for VIN {}", response.statusCode(), trimmedVin);
                return Optional.empty();
            }

        } catch (IOException | InterruptedException e) {
            logger.error("KBBClient: Error fetching KBB value for VIN {}: {}", vin, e.getMessage());
            Thread.currentThread().interrupt();
            return Optional.empty();
        }
    }

    /**
     * Parse KBB value from API response JSON.
     * This is a placeholder implementation; adjust based on actual API response structure.
     *
     * @param jsonResponse Raw JSON response from the API
     * @param vin The VIN being looked up (for logging)
     * @return Optional containing extracted KBB value
     */
    private Optional<BigDecimal> parseKBBValue(String jsonResponse, String vin) {
        try {
            JsonNode root = objectMapper.readTree(jsonResponse);

            // Example parsing: adjust path based on actual API response structure
            // This example assumes a "value" or "kbb_value" field
            JsonNode valueNode = root.path("value").isMissingNode()
                ? root.path("kbb_value")
                : root.path("value");

            if (!valueNode.isMissingNode() && valueNode.isNumber()) {
                BigDecimal value = new BigDecimal(valueNode.asDouble());
                logger.info("KBBClient: Successfully fetched KBB value ${} for VIN {}", value, vin);
                return Optional.of(value);
            } else {
                logger.warn("KBBClient: No value found in response for VIN {}", vin);
                return Optional.empty();
            }

        } catch (IOException e) {
            logger.error("KBBClient: Error parsing KBB response for VIN {}: {}", vin, e.getMessage());
            return Optional.empty();
        }
    }

    /**
     * Fallback method for testing: returns a mock KBB value.
     * Remove or modify in production.
     *
     * @param vin Vehicle ID
     * @return Mock BigDecimal for demonstration
     */
    public Optional<BigDecimal> fetchKBBValueMock(String vin) {
        if (vin != null && vin.length() == 17) {
            // Return a mock value between $10,000 and $50,000
            long hashCode = Math.abs(vin.hashCode());
            long mockValue = 10000 + (hashCode % 40000);
            return Optional.of(new BigDecimal(mockValue));
        }
        return Optional.empty();
    }
}
