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
    private final String financePackageId;

    public DamlLedgerService(
            @Value("${daml.json-api.url}") String damlJsonApiUrl,
            @Value("${daml.admin.token:}") String adminToken,
            @Value("${daml.finance.package-id:}") String financePackageId) {
        this.webClient = WebClient.builder()
                .baseUrl(damlJsonApiUrl)
                .build();
        // If no token provided, generate an insecure admin token for development
        this.adminToken = (adminToken == null || adminToken.isBlank())
                ? generateInsecureAdminToken()
                : adminToken;
        this.financePackageId = financePackageId;
        log.info("DamlLedgerService initialized with JSON API URL: {}", damlJsonApiUrl);
        if (financePackageId != null && !financePackageId.isBlank()) {
            log.info("Using DAML Finance package ID: {}", financePackageId);
        } else {
            log.warn("DAML Finance package ID not configured. Template IDs must include package ID.");
        }
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

    /**
     * Build a full template ID with package ID.
     * If the template ID already contains a package ID (has two colons), returns it
     * as-is.
     * Otherwise, prepends the configured package ID.
     * 
     * @param templateId Template ID in format "Module:Entity" or
     *                   "PackageId:Module:Entity"
     * @return Full template ID in format "PackageId:Module:Entity"
     */
    public String buildTemplateId(String templateId) {
        if (templateId == null) {
            throw new IllegalArgumentException("Template ID cannot be null");
        }

        // Count colons to determine if package ID is already included
        long colonCount = templateId.chars().filter(ch -> ch == ':').count();

        if (colonCount >= 2) {
            // Already has package ID
            return templateId;
        }

        // Need to add package ID
        if (financePackageId == null || financePackageId.isBlank()) {
            throw new DamlIntegrationException(
                    "DAML Finance package ID not configured. Please set daml.finance.package-id property. " +
                            "Template ID '" + templateId + "' requires package ID in format 'packageId:module:entity'");
        }

        return financePackageId + ":" + templateId;
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
     * Exercise a choice on a contract.
     *
     * @param templateId The template ID of the contract
     * @param contractId The contract ID to exercise the choice on
     * @param choice     The choice name to exercise
     * @param argument   The argument for the choice
     * @return ExerciseResponse containing the result
     */
    public ExerciseResponse exerciseChoice(
            String templateId,
            String contractId,
            String choice,
            java.util.Map<String, Object> argument) {
        return exerciseChoice(templateId, contractId, choice, argument, null);
    }

    /**
     * Exercise a choice on a contract with a specific acting party.
     *
     * @param templateId    The template ID of the contract
     * @param contractId    The contract ID to exercise the choice on
     * @param choice        The choice name to exercise
     * @param argument      The argument for the choice
     * @param actingPartyId The party ID to act as (generates a token for this
     *                      party)
     * @return ExerciseResponse containing the result
     */
    public ExerciseResponse exerciseChoice(
            String templateId,
            String contractId,
            String choice,
            java.util.Map<String, Object> argument,
            String actingPartyId) {

        String fullTemplateId = buildTemplateId(templateId);
        ExerciseRequest request = new ExerciseRequest(fullTemplateId, contractId, choice, argument);
        String token = (actingPartyId != null && !actingPartyId.isBlank()) ? generateUserToken(actingPartyId)
                : adminToken;

        try {
            log.debug("Exercising choice {} on contract {} with template {} acting as {}",
                    choice, contractId, fullTemplateId, actingPartyId != null ? actingPartyId : "admin/public");

            return webClient.post()
                    .uri("/v1/exercise")
                    .header("Authorization", "Bearer " + token)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(ExerciseResponse.class)
                    .block();
        } catch (WebClientResponseException e) {
            log.error("DAML JSON API error while exercising choice: {} - {}",
                    e.getStatusCode(), e.getResponseBodyAsString());
            throw new DamlIntegrationException("Failed to exercise choice: " + e.getMessage(), e);
        }
    }

    /**
     * Exercise a choice on a contract using its key instead of contract ID.
     * This avoids the need to track contract IDs which change after consuming
     * choices.
     *
     * @param templateId The template ID of the contract
     * @param key        The contract key - can be String (for Party keys) or Map
     *                   (for composite keys)
     * @param choice     The choice name to exercise
     * @param argument   The argument for the choice
     * @return ExerciseResponse containing the result
     */
    public ExerciseResponse exerciseChoiceByKey(
            String templateId,
            Object key,
            String choice,
            java.util.Map<String, Object> argument) {
        return exerciseChoiceByKey(templateId, key, choice, argument, null);
    }

    /**
     * Exercise a choice on a contract using its key with a specific acting party.
     *
     * @param templateId    The template ID of the contract
     * @param key           The contract key - can be String (for Party keys) or Map
     *                      (for composite keys)
     * @param choice        The choice name to exercise
     * @param argument      The argument for the choice
     * @param actingPartyId The party ID to act as
     * @return ExerciseResponse containing the result
     */
    public ExerciseResponse exerciseChoiceByKey(
            String templateId,
            Object key,
            String choice,
            java.util.Map<String, Object> argument,
            String actingPartyId) {

        String fullTemplateId = buildTemplateId(templateId);
        ExerciseByKeyRequest request = new ExerciseByKeyRequest(fullTemplateId, key, choice, argument);
        String token = (actingPartyId != null && !actingPartyId.isBlank()) ? generateUserToken(actingPartyId)
                : adminToken;

        try {
            log.debug("Exercising choice {} by key on template {} acting as {}",
                    choice, fullTemplateId, actingPartyId != null ? actingPartyId : "admin/public");
            log.debug("Key: {}", key);

            return webClient.post()
                    .uri("/v1/exercise")
                    .header("Authorization", "Bearer " + token)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(ExerciseResponse.class)
                    .block();
        } catch (WebClientResponseException e) {
            log.error("DAML JSON API error while exercising choice by key: {} - {}",
                    e.getStatusCode(), e.getResponseBodyAsString());
            throw new DamlIntegrationException("Failed to exercise choice by key: " + e.getMessage(), e);
        }
    }

    /**
     * Query contracts by template and filter.
     *
     * @param templateId The template ID to query
     * @param query      The query filter
     * @return List of matching contracts
     */
    public java.util.List<ContractResult> queryContracts(String templateId, java.util.Map<String, Object> query) {
        return queryContracts(templateId, query, null);
    }

    /**
     * Query contracts by template and filter with specific reading party.
     *
     * @param templateId    The template ID to query
     * @param query         The query filter
     * @param actingPartyId The party ID to read as (generates a token for this
     *                      party)
     * @return List of matching contracts
     */
    public java.util.List<ContractResult> queryContracts(String templateId, java.util.Map<String, Object> query,
            String actingPartyId) {
        String fullTemplateId = buildTemplateId(templateId);
        QueryRequest request = new QueryRequest(java.util.List.of(fullTemplateId), query);
        String token = (actingPartyId != null && !actingPartyId.isBlank()) ? generateUserToken(actingPartyId)
                : adminToken;

        try {
            log.debug("Querying contracts for template: {} acting as {}",
                    fullTemplateId, actingPartyId != null ? actingPartyId : "admin/public");

            QueryResponse response = webClient.post()
                    .uri("/v1/query")
                    .header("Authorization", "Bearer " + token)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(QueryResponse.class)
                    .block();

            return response != null ? response.result() : java.util.List.of();
        } catch (WebClientResponseException e) {
            log.error("DAML JSON API error while querying contracts: {} - {}",
                    e.getStatusCode(), e.getResponseBodyAsString());
            throw new DamlIntegrationException("Failed to query contracts: " + e.getMessage(), e);
        }
    }

    /**
     * Create a contract on the ledger.
     *
     * @param templateId The template ID of the contract to create
     * @param payload    The payload for the contract
     * @return CreateResponse containing the new contract ID
     */
    public CreateResponse createContract(String templateId, java.util.Map<String, Object> payload) {
        return createContract(templateId, payload, null);
    }

    /**
     * Create a contract on the ledger with a specific acting party.
     *
     * @param templateId    The template ID of the contract to create
     * @param payload       The payload for the contract
     * @param actingPartyId The party ID to act as (generates a token for this
     *                      party)
     * @return CreateResponse containing the new contract ID
     */
    public CreateResponse createContract(String templateId, java.util.Map<String, Object> payload,
            String actingPartyId) {
        String fullTemplateId = buildTemplateId(templateId);
        CreateRequest request = new CreateRequest(fullTemplateId, payload);
        String token = (actingPartyId != null && !actingPartyId.isBlank()) ? generateUserToken(actingPartyId)
                : adminToken;

        try {
            log.debug("Creating contract for template: {} acting as {}",
                    fullTemplateId, actingPartyId != null ? actingPartyId : "admin/public");

            return webClient.post()
                    .uri("/v1/create")
                    .header("Authorization", "Bearer " + token)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(CreateResponse.class)
                    .block();
        } catch (WebClientResponseException e) {
            log.error("DAML JSON API error while creating contract: {} - {}",
                    e.getStatusCode(), e.getResponseBodyAsString());
            throw new DamlIntegrationException("Failed to create contract: " + e.getMessage(), e);
        }
    }

    /**
     * Generate a JWT token for multiple parties to interact with DAML.
     * Used for submitMulti commands that require multiple acting parties.
     * 
     * @param partyIds List of party IDs that will act together
     * @return JWT token string
     */
    public String generateMultiPartyToken(java.util.List<String> partyIds) {
        String header = "{\"alg\":\"HS256\",\"typ\":\"JWT\"}";

        long expirationTime = System.currentTimeMillis() / 1000 + 3600; // 1 hour from now

        // Build actAs array with all parties
        String partiesJson = partyIds.stream()
                .map(p -> "\"" + p + "\"")
                .collect(java.util.stream.Collectors.joining(","));

        String payload = String.format("""
                {
                    "https://daml.com/ledger-api": {
                        "ledgerId": "sandbox",
                        "applicationId": "microlend-android",
                        "actAs": [%s],
                        "readAs": [%s]
                    },
                    "exp": %d
                }
                """, partiesJson, partiesJson, expirationTime).replaceAll("\\s+", "");

        String encodedHeader = Base64.getUrlEncoder().withoutPadding()
                .encodeToString(header.getBytes(StandardCharsets.UTF_8));
        String encodedPayload = Base64.getUrlEncoder().withoutPadding()
                .encodeToString(payload.getBytes(StandardCharsets.UTF_8));
        String signature = Base64.getUrlEncoder().withoutPadding()
                .encodeToString("signature".getBytes(StandardCharsets.UTF_8));

        return encodedHeader + "." + encodedPayload + "." + signature;
    }

    /**
     * Generate a JWT token for a user to interact with DAML.
     * 
     * @param partyId The party ID of the user
     * @return JWT token string
     */
    public String generateUserToken(String partyId) {
        String header = "{\"alg\":\"HS256\",\"typ\":\"JWT\"}";

        long expirationTime = System.currentTimeMillis() / 1000 + 3600; // 1 hour from now
        String payload = String.format("""
                {
                    "https://daml.com/ledger-api": {
                        "ledgerId": "sandbox",
                        "applicationId": "microlend-android",
                        "actAs": ["%s"],
                        "readAs": ["%s"]
                    },
                    "exp": %d
                }
                """, partyId, partyId, expirationTime).replaceAll("\\s+", "");

        String encodedHeader = Base64.getUrlEncoder().withoutPadding()
                .encodeToString(header.getBytes(StandardCharsets.UTF_8));
        String encodedPayload = Base64.getUrlEncoder().withoutPadding()
                .encodeToString(payload.getBytes(StandardCharsets.UTF_8));
        String signature = Base64.getUrlEncoder().withoutPadding()
                .encodeToString("signature".getBytes(StandardCharsets.UTF_8));

        return encodedHeader + "." + encodedPayload + "." + signature;
    }

    /**
     * Exercise a choice on a contract with multiple acting parties (submitMulti).
     * Required when a choice needs multiple controllers.
     *
     * @param templateId     The template ID of the contract
     * @param contractId     The contract ID to exercise the choice on
     * @param choice         The choice name to exercise
     * @param argument       The argument for the choice
     * @param actingPartyIds List of party IDs to act as (submitMulti)
     * @return ExerciseResponse containing the result
     */
    public ExerciseResponse exerciseChoiceMulti(
            String templateId,
            String contractId,
            String choice,
            java.util.Map<String, Object> argument,
            java.util.List<String> actingPartyIds) {

        String fullTemplateId = buildTemplateId(templateId);
        ExerciseRequest request = new ExerciseRequest(fullTemplateId, contractId, choice, argument);
        String token = generateMultiPartyToken(actingPartyIds);

        try {
            log.debug("Exercising choice {} on contract {} with template {} acting as {} (submitMulti)",
                    choice, contractId, fullTemplateId, actingPartyIds);

            return webClient.post()
                    .uri("/v1/exercise")
                    .header("Authorization", "Bearer " + token)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(ExerciseResponse.class)
                    .block();
        } catch (WebClientResponseException e) {
            log.error("DAML JSON API error while exercising choice (submitMulti): {} - {}",
                    e.getStatusCode(), e.getResponseBodyAsString());
            throw new DamlIntegrationException("Failed to exercise choice (submitMulti): " + e.getMessage(), e);
        }
    }

    // DTOs for DAML JSON API operations

    record ExerciseRequest(
            @JsonProperty("templateId") String templateId,
            @JsonProperty("contractId") String contractId,
            @JsonProperty("choice") String choice,
            @JsonProperty("argument") java.util.Map<String, Object> argument) {
    }

    record ExerciseByKeyRequest(
            @JsonProperty("templateId") String templateId,
            @JsonProperty("key") Object key, // Can be String (Party) or Map (composite key)
            @JsonProperty("choice") String choice,
            @JsonProperty("argument") java.util.Map<String, Object> argument) {
    }

    record ExerciseResponse(
            @JsonProperty("status") int status,
            @JsonProperty("result") ExerciseResult result) {
    }

    record ExerciseResult(
            @JsonProperty("exerciseResult") Object exerciseResult,
            @JsonProperty("events") java.util.List<Object> events) {
    }

    record QueryRequest(
            @JsonProperty("templateIds") java.util.List<String> templateIds,
            @JsonProperty("query") java.util.Map<String, Object> query) {
    }

    record QueryResponse(
            @JsonProperty("status") int status,
            @JsonProperty("result") java.util.List<ContractResult> result) {
    }

    record ContractResult(
            @JsonProperty("contractId") String contractId,
            @JsonProperty("payload") java.util.Map<String, Object> payload) {
    }

    record CreateRequest(
            @JsonProperty("templateId") String templateId,
            @JsonProperty("payload") java.util.Map<String, Object> payload) {
    }

    record CreateResponse(
            @JsonProperty("status") int status,
            @JsonProperty("result") CreateResult result) {
    }

    record CreateResult(
            @JsonProperty("contractId") String contractId) {
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
