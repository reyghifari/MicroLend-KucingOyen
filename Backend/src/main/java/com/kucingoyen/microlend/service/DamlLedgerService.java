package com.kucingoyen.microlend.service;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * Service for integrating with the DAML Ledger via JSON API.
 * Handles party allocation for new users.
 */
@Service
public class DamlLedgerService {

    private static final Logger log = LoggerFactory.getLogger(DamlLedgerService.class);

    private final WebClient webClient;
    private final String adminToken;

    public DamlLedgerService(
            @Value("${daml.json-api.url}") String damlJsonApiUrl,
            @Value("${daml.admin.token:}") String adminToken) {
        this.webClient = WebClient.builder()
                .baseUrl(damlJsonApiUrl)
                .build();
        // If no token provided, generate an insecure admin token for development
        this.adminToken = (adminToken == null || adminToken.isBlank())
                ? generateInsecureAdminToken()
                : adminToken;
        log.info("DamlLedgerService initialized with JSON API URL: {}", damlJsonApiUrl);
    }

    /**
     * Generates an insecure JWT token for DAML JSON API with
     * --allow-insecure-tokens.
     * This is for development/testing only and should not be used in production.
     * 
     * The token format is: header.payload.signature (all base64 encoded)
     * With --allow-insecure-tokens, the signature is not validated.
     */
    private String generateInsecureAdminToken() {
        // JWT Header
        String header = "{\"alg\":\"HS256\",\"typ\":\"JWT\"}";

        // JWT Payload with admin rights (using the standard DAML claims format)
        // actAs and readAs with wildcard allows all operations
        String payload = """
                {
                    "https://daml.com/ledger-api": {
                        "ledgerId": "sandbox",
                        "applicationId": "microlend-backend",
                        "actAs": ["public"],
                        "admin": true
                    },
                    "exp": 9999999999
                }
                """.replaceAll("\\s+", "");

        String encodedHeader = Base64.getUrlEncoder().withoutPadding()
                .encodeToString(header.getBytes(StandardCharsets.UTF_8));
        String encodedPayload = Base64.getUrlEncoder().withoutPadding()
                .encodeToString(payload.getBytes(StandardCharsets.UTF_8));

        // For insecure tokens, we use a dummy signature
        String signature = Base64.getUrlEncoder().withoutPadding()
                .encodeToString("signature".getBytes(StandardCharsets.UTF_8));

        String token = encodedHeader + "." + encodedPayload + "." + signature;
        log.debug("Generated insecure admin token for DAML JSON API");
        return token;
    }

    /**
     * Allocates a new party on the DAML ledger.
     *
     * @param displayName    The display name for the party (typically user's name
     *                       or email)
     * @param identifierHint A hint for the party identifier (e.g., sanitized email)
     * @return The allocated party ID from the DAML ledger
     */
    public String allocateParty(String displayName, String identifierHint) {
        try {
            AllocatePartyRequest request = new AllocatePartyRequest(identifierHint, displayName);

            log.debug("Allocating party with hint: {}, displayName: {}", identifierHint, displayName);

            AllocatePartyResponse response = webClient.post()
                    .uri("/v1/parties/allocate")
                    .header("Authorization", "Bearer " + adminToken)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(AllocatePartyResponse.class)
                    .block();

            if (response != null && response.result() != null) {
                String partyId = response.result().identifier();
                log.info("Successfully allocated DAML party: {} for user: {}", partyId, displayName);
                return partyId;
            }

            throw new DamlIntegrationException("Failed to allocate party: empty response from DAML ledger");

        } catch (WebClientResponseException e) {
            log.error("DAML JSON API error while allocating party for {}: {} - {}",
                    displayName, e.getStatusCode(), e.getResponseBodyAsString());
            throw new DamlIntegrationException("Failed to allocate party on DAML ledger: " + e.getMessage(), e);
        } catch (Exception e) {
            log.error("Unexpected error while allocating DAML party for {}: {}", displayName, e.getMessage(), e);
            throw new DamlIntegrationException("Failed to allocate party on DAML ledger: " + e.getMessage(), e);
        }
    }

    /**
     * Sanitizes an email address to create a valid DAML party identifier hint.
     * Removes special characters and replaces @ and . with underscores.
     */
    public String sanitizeIdentifierHint(String email) {
        if (email == null) {
            return "user";
        }
        return email.replaceAll("[^a-zA-Z0-9]", "_").toLowerCase();
    }

    // Request/Response DTOs for DAML JSON API

    record AllocatePartyRequest(
            @JsonProperty("identifierHint") String identifierHint,
            @JsonProperty("displayName") String displayName) {
    }

    record AllocatePartyResponse(
            @JsonProperty("result") PartyResult result,
            @JsonProperty("status") int status) {
    }

    record PartyResult(
            @JsonProperty("identifier") String identifier,
            @JsonProperty("displayName") String displayName,
            @JsonProperty("isLocal") boolean isLocal) {
    }

    /**
     * Custom exception for DAML integration failures.
     */
    public static class DamlIntegrationException extends RuntimeException {
        public DamlIntegrationException(String message) {
            super(message);
        }

        public DamlIntegrationException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
