package com.kucingoyen.microlend.service;

import com.kucingoyen.microlend.dto.*;
import com.kucingoyen.microlend.exception.InsufficientBalanceException;
import com.kucingoyen.microlend.exception.NotFoundException;
import com.kucingoyen.microlend.model.Users;
import com.kucingoyen.microlend.repository.UserRepository;
import com.kucingoyen.microlend.service.DamlLedgerService.ContractResult;
import com.kucingoyen.microlend.service.DamlLedgerService.ExerciseResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Service for wallet operations: deposit, transfer, and balance queries.
 */
@Service
@RequiredArgsConstructor
public class WalletService {

    private static final Logger log = LoggerFactory.getLogger(WalletService.class);

    private final DamlLedgerService damlService;
    private final UserRepository userRepository;
    private final AdminSetupService adminSetupService;

    /**
     * Deposit (mint) tokens to user's wallet.
     * Automatically merges all holdings of the same asset after deposit.
     * 
     * @param email    User's email
     * @param amount   Amount to deposit
     * @param currency Currency/asset symbol (CC, USDx, etc.)
     * @return DepositResponse with contract ID and amount
     */
    public DepositResponse deposit(String email, BigDecimal amount, String currency) {
        log.info("Processing deposit for user: {}, amount: {}, currency: {}", email, amount, currency);

        // 1. Get user's DAML Party ID
        Users user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found: " + email));

        String userPartyId = user.getDamlPartyId();
        if (userPartyId == null || userPartyId.isBlank()) {
            throw new IllegalStateException("User does not have a DAML party ID");
        }

        String adminPartyId = adminSetupService.getAdminPartyId();
        if (adminPartyId == null) {
            throw new IllegalStateException("Admin party not initialized. Please restart the application.");
        }

        // 2. Exercise Mint choice using contract key instead of contract ID
        // Key format: (issuer party, asset symbol)
        Map<String, Object> factoryKey = Map.of(
                "_1", adminPartyId,
                "_2", currency // "CC", "USDx", etc.
        );

        ExerciseResponse response = damlService.exerciseChoiceByKey(
                "MicroLend.Finance.Asset:AssetFactory",
                factoryKey,
                "Mint",
                Map.of(
                        "recipient", userPartyId,
                        "mintAmount", amount.toString()),
                adminPartyId); // Act as Admin

        // 3. Extract holding contract ID from response
        // Mint choice returns tuple: (newFactoryId, holdingId)
        // We only need the holdingId (second element)
        String newHoldingCid = null;
        if (response != null && response.result() != null) {
            Object exerciseResult = response.result().exerciseResult();
            newHoldingCid = extractSecondContractId(exerciseResult);

            if (newHoldingCid == null) {
                throw new IllegalStateException("Failed to extract holding contract ID from mint response");
            }

            log.info("Deposit successful. New holding contract ID: {}", newHoldingCid);
        } else {
            throw new IllegalStateException("Failed to mint tokens: empty response from DAML");
        }

        // 4. Auto-merge: Get all holdings for this asset and merge them
        try {
            log.info("Auto-merging {} holdings for user {}...", currency, email);

            // Query all holdings for this user and asset
            List<ContractResult> allHoldings = damlService.queryContracts(
                    "MicroLend.Finance.Holding:Holding",
                    Map.of("owner", userPartyId),
                    userPartyId);

            // Filter holdings for this specific asset
            List<String> holdingCidsToMerge = new ArrayList<>();
            for (ContractResult result : allHoldings) {
                HoldingContract holding = convertToHoldingContract(result);
                if (holding.getAssetId().getSymbol().equals(currency)) {
                    holdingCidsToMerge.add(holding.getContractId());
                }
            }

            // If we have multiple holdings for this asset, merge them
            if (holdingCidsToMerge.size() > 1) {
                log.info("Found {} holdings for {}, merging...", holdingCidsToMerge.size(), currency);

                String baseHoldingCid = holdingCidsToMerge.get(0);
                BigDecimal totalAmount = amount;

                // Merge each subsequent holding into the base
                for (int i = 1; i < holdingCidsToMerge.size(); i++) {
                    String holdingToMergeCid = holdingCidsToMerge.get(i);

                    ExerciseResponse mergeResponse = damlService.exerciseChoice(
                            "MicroLend.Finance.Holding:Holding",
                            baseHoldingCid,
                            "Merge",
                            Map.of("otherHoldingCid", holdingToMergeCid),
                            userPartyId);

                    // Update base holding CID for next iteration
                    String mergedCid = extractContractId(mergeResponse);
                    if (mergedCid != null) {
                        baseHoldingCid = mergedCid;
                    }
                }

                // Query final merged holding to get total amount
                List<ContractResult> finalResults = damlService.queryContracts(
                        "MicroLend.Finance.Holding:Holding",
                        Map.of("owner", userPartyId),
                        userPartyId);

                for (ContractResult result : finalResults) {
                    if (result.contractId().equals(baseHoldingCid)) {
                        HoldingContract holding = convertToHoldingContract(result);
                        totalAmount = holding.getAmount();
                        break;
                    }
                }

                log.info("Auto-merge completed. Final holding: {}, Total: {}", baseHoldingCid, totalAmount);
                return new DepositResponse(baseHoldingCid, totalAmount, currency, "SUCCESS - Holdings auto-merged");
            }

            // No merge needed (only one holding)
            return new DepositResponse(newHoldingCid, amount, currency, "SUCCESS");

        } catch (Exception e) {
            log.warn("Auto-merge failed, but deposit succeeded: {}", e.getMessage());
            // Return the new holding even if merge fails
            return new DepositResponse(newHoldingCid, amount, currency, "SUCCESS - Auto-merge failed");
        }
    }

    /**
     * Transfer tokens from one user to another.
     * 
     * @param senderEmail Sender's email
     * @param request     Transfer request with recipient, amount, currency
     * @return TransferResponse with status
     */
    public TransferResponse transfer(String senderEmail, TransferRequest request) {
        log.info("Processing transfer from {} to party {}, amount: {}",
                senderEmail, request.getRecipientPartyId(), request.getAmount());

        // 1. Get sender party ID
        Users sender = userRepository.findByEmail(senderEmail)
                .orElseThrow(() -> new NotFoundException("Sender not found: " + senderEmail));

        String senderPartyId = sender.getDamlPartyId();
        String receiverPartyId = request.getRecipientPartyId();

        if (senderPartyId == null || senderPartyId.isBlank()) {
            throw new IllegalStateException("Sender does not have a DAML party ID");
        }

        if (receiverPartyId == null || receiverPartyId.isBlank()) {
            throw new IllegalArgumentException("Recipient party ID is required");
        }

        // 2. Query sender's holdings
        List<ContractResult> contractResults = damlService.queryContracts(
                "MicroLend.Finance.Holding:Holding",
                Map.of("owner", senderPartyId),
                senderPartyId); // Read as Sender

        if (contractResults.isEmpty()) {
            throw new InsufficientBalanceException("No balance found for user");
        }

        // 3. Convert to HoldingContract objects
        List<HoldingContract> holdings = contractResults.stream()
                .map(this::convertToHoldingContract)
                .filter(h -> h.getAssetId().getSymbol().equals(request.getCurrency()))
                .toList();

        if (holdings.isEmpty()) {
            throw new InsufficientBalanceException("No balance found for currency: " + request.getCurrency());
        }

        // 4. Find holding with sufficient balance
        HoldingContract holding = findHoldingWithBalance(holdings, request.getAmount());

        if (holding == null) {
            throw new InsufficientBalanceException("Insufficient balance. Required: " + request.getAmount());
        }

        // 5. Split if needed, then Transfer
        String transferContractId;

        if (holding.getAmount().compareTo(request.getAmount()) > 0) {
            // Split first
            log.debug("Splitting holding {} with amount {}", holding.getContractId(), request.getAmount());

            ExerciseResponse splitResponse = damlService.exerciseChoice(
                    "MicroLend.Finance.Holding:Holding",
                    holding.getContractId(),
                    "Split",
                    Map.of("splitAmount", request.getAmount().toString()),
                    senderPartyId); // Act as Sender

            // Extract the split holding ID
            String splitHoldingId = extractFirstContractId(splitResponse);

            if (splitHoldingId == null) {
                throw new IllegalStateException("Failed to split holding");
            }

            transferContractId = splitHoldingId;
        } else {
            // Transfer entire holding
            transferContractId = holding.getContractId();
        }

        // Transfer to recipient
        log.debug("Transferring contract {} to {}", transferContractId, receiverPartyId);

        damlService.exerciseChoice(
                "MicroLend.Finance.Holding:Holding",
                transferContractId,
                "Transfer",
                Map.of("newOwner", receiverPartyId),
                senderPartyId); // Act as Sender

        log.info("Transfer completed successfully");

        return new TransferResponse("SUCCESS", request.getAmount(), transferContractId);
    }

    /**
     * Get user's token balance.
     * 
     * @param email User's email
     * @return BalanceResponse with balances by currency and individual holdings
     */
    public BalanceResponse getBalance(String email) {
        log.info("Querying balance for user: {}", email);

        Users user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found: " + email));

        String userPartyId = user.getDamlPartyId();
        if (userPartyId == null) {
            throw new IllegalStateException("User does not have a DAML party ID");
        }

        // Query all holdings owned by user
        List<ContractResult> contractResults = damlService.queryContracts(
                "MicroLend.Finance.Holding:Holding",
                Map.of("owner", userPartyId),
                userPartyId); // Read as User

        // Sum up balances by currency AND collect individual holdings
        Map<String, BigDecimal> balances = new HashMap<>();
        Map<String, List<HoldingDto>> holdingsBySymbol = new HashMap<>();

        for (ContractResult result : contractResults) {
            HoldingContract holding = convertToHoldingContract(result);
            String symbol = holding.getAssetId().getSymbol();

            // Add to aggregate balance
            balances.merge(symbol, holding.getAmount(), BigDecimal::add);

            // Add to holdings list
            HoldingDto holdingDto = new HoldingDto(
                    holding.getContractId(),
                    holding.getAmount(),
                    symbol,
                    holding.getAssetId().getDescription());

            holdingsBySymbol.computeIfAbsent(symbol, k -> new ArrayList<>()).add(holdingDto);
        }

        log.info("Balance query completed. Found {} currencies, {} total holdings",
                balances.size(), contractResults.size());

        return new BalanceResponse(balances, holdingsBySymbol);
    }

    /**
     * Merge multiple holdings of the same asset into a single holding.
     * This reduces the number of contracts and simplifies collateral management.
     *
     * @param email   User's email
     * @param request Merge request with holding contract IDs
     * @return MergeHoldingsResponse with merged holding details
     */
    public MergeHoldingsResponse mergeHoldings(String email, MergeHoldingsRequest request) {
        log.info("Merging {} holdings for user: {}", request.getHoldingContractIds().size(), email);

        if (request.getHoldingContractIds().size() < 2) {
            throw new IllegalArgumentException("At least 2 holdings are required to merge");
        }

        Users user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found: " + email));

        String userPartyId = user.getDamlPartyId();
        if (userPartyId == null) {
            throw new IllegalStateException("User does not have a DAML party ID");
        }

        try {
            // Start with the first holding as the base
            String baseHoldingCid = request.getHoldingContractIds().get(0);
            BigDecimal totalAmount = BigDecimal.ZERO;

            // Merge each subsequent holding into the base
            for (int i = 1; i < request.getHoldingContractIds().size(); i++) {
                String holdingToMergeCid = request.getHoldingContractIds().get(i);

                log.debug("Merging holding {} into base holding {}", holdingToMergeCid, baseHoldingCid);

                // Exercise Merge choice on base holding
                ExerciseResponse response = damlService.exerciseChoice(
                        "MicroLend.Finance.Holding:Holding",
                        baseHoldingCid,
                        "Merge",
                        Map.of("otherCid", holdingToMergeCid),
                        userPartyId);

                // Merge returns the new merged holding contract ID
                String mergedCid = extractContractId(response);
                if (mergedCid == null) {
                    throw new IllegalStateException("Failed to extract merged contract ID");
                }

                // Update base for next iteration
                baseHoldingCid = mergedCid;
            }

            // Query the final merged holding to get the total amount
            List<ContractResult> results = damlService.queryContracts(
                    "MicroLend.Finance.Holding:Holding",
                    Map.of("owner", userPartyId),
                    userPartyId);

            // Find the merged holding
            for (ContractResult result : results) {
                if (result.contractId().equals(baseHoldingCid)) {
                    HoldingContract holding = convertToHoldingContract(result);
                    totalAmount = holding.getAmount();
                    break;
                }
            }

            log.info("Holdings merged successfully. Final contract ID: {}, Total: {}",
                    baseHoldingCid, totalAmount);

            return new MergeHoldingsResponse(
                    baseHoldingCid,
                    totalAmount,
                    request.getAssetSymbol(),
                    request.getHoldingContractIds().size(),
                    String.format("Successfully merged %d holdings into one",
                            request.getHoldingContractIds().size()));

        } catch (Exception e) {
            log.error("Failed to merge holdings: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to merge holdings: " + e.getMessage(), e);
        }
    }

    /**
     * Convert ContractResult to HoldingContract DTO.
     */
    private HoldingContract convertToHoldingContract(ContractResult result) {
        Map<String, Object> payload = result.payload();

        HoldingContract holding = new HoldingContract();
        holding.setContractId(result.contractId());
        holding.setOwner((String) payload.get("owner"));
        holding.setCustodian((String) payload.get("custodian"));

        // Parse amount
        Object amountObj = payload.get("amount");
        BigDecimal amount = new BigDecimal(amountObj.toString());
        holding.setAmount(amount);

        // Parse assetId
        @SuppressWarnings("unchecked")
        Map<String, Object> assetIdMap = (Map<String, Object>) payload.get("assetId");
        HoldingContract.AssetId assetId = new HoldingContract.AssetId(
                (String) assetIdMap.get("issuer"),
                (String) assetIdMap.get("symbol"),
                (String) assetIdMap.get("description"));
        holding.setAssetId(assetId);

        return holding;
    }

    /**
     * Find a holding with sufficient balance.
     */
    private HoldingContract findHoldingWithBalance(List<HoldingContract> holdings, BigDecimal requiredAmount) {
        // First, try to find exact match
        for (HoldingContract holding : holdings) {
            if (holding.getAmount().compareTo(requiredAmount) == 0) {
                return holding;
            }
        }

        // Otherwise, find the smallest holding that is >= requiredAmount
        HoldingContract bestMatch = null;
        for (HoldingContract holding : holdings) {
            if (holding.getAmount().compareTo(requiredAmount) >= 0) {
                if (bestMatch == null || holding.getAmount().compareTo(bestMatch.getAmount()) < 0) {
                    bestMatch = holding;
                }
            }
        }

        return bestMatch;
    }

    /**
     * Extract contract ID from ExerciseResponse.
     * Used for Merge choice which returns a single contract ID.
     */
    private String extractContractId(ExerciseResponse response) {
        if (response != null && response.result() != null) {
            Object result = response.result().exerciseResult();
            if (result instanceof String) {
                return (String) result;
            }
        }
        return null;
    }

    /**
     * Extract first contract ID from exercise response.
     * Handles both simple contract ID returns and tuple returns like (ContractId,
     * ContractId).
     * DAML tuples are serialized by JSON API as either:
     * - Map with keys "_1", "_2" for tuple elements
     * - List with 2 elements
     */
    private String extractFirstContractId(ExerciseResponse response) {
        if (response == null || response.result() == null) {
            log.warn("Exercise response or result is null");
            return null;
        }

        Object exerciseResult = response.result().exerciseResult();

        if (exerciseResult == null) {
            log.warn("Exercise result is null");
            return null;
        }

        log.debug("Extracting contract ID from exercise result type: {}", exerciseResult.getClass().getName());

        // Case 1: Direct string (simple return)
        if (exerciseResult instanceof String) {
            String contractId = (String) exerciseResult;
            log.debug("Extracted contract ID (direct): {}", contractId);
            return contractId;
        }

        // Case 2: Map representation of tuple (DAML tuples -> {"_1": "...", "_2":
        // "..."})
        else if (exerciseResult instanceof Map) {
            @SuppressWarnings("unchecked")
            Map<String, Object> resultMap = (Map<String, Object>) exerciseResult;

            // Try to get first element of tuple
            if (resultMap.containsKey("_1")) {
                Object firstElement = resultMap.get("_1");
                if (firstElement instanceof String) {
                    String contractId = (String) firstElement;
                    log.debug("Extracted contract ID from tuple._1: {}", contractId);
                    return contractId;
                }
            }

            log.warn("Map result does not contain '_1' key or it's not a String. Keys: {}", resultMap.keySet());
        }

        // Case 3: List representation of tuple
        else if (exerciseResult instanceof List) {
            @SuppressWarnings("unchecked")
            List<Object> resultList = (List<Object>) exerciseResult;

            if (!resultList.isEmpty()) {
                Object firstElement = resultList.get(0);

                if (firstElement instanceof String) {
                    String contractId = (String) firstElement;
                    log.debug("Extracted contract ID from list[0]: {}", contractId);
                    return contractId;
                } else {
                    log.warn("First element of list is not a String, type: {}",
                            firstElement.getClass().getName());
                }
            } else {
                log.warn("Result list is empty");
            }
        }

        else {
            log.warn("Unexpected exercise result type: {}. Value: {}",
                    exerciseResult.getClass().getName(), exerciseResult);
        }

        return null;
    }

    /**
     * Extract second contract ID from exercise response (for tuple returns).
     * Used when Mint returns (newFactoryId, holdingId) and we need the holdingId.
     */
    private String extractSecondContractId(Object exerciseResult) {
        if (exerciseResult == null) {
            log.warn("Exercise result is null");
            return null;
        }

        log.debug("Extracting second contract ID from exercise result type: {}", exerciseResult.getClass().getName());

        // Case 1: Map representation of tuple (DAML tuples -> {"_1": "...", "_2":
        // "..."})
        if (exerciseResult instanceof Map) {
            @SuppressWarnings("unchecked")
            Map<String, Object> resultMap = (Map<String, Object>) exerciseResult;

            if (resultMap.containsKey("_2")) {
                Object secondElement = resultMap.get("_2");
                if (secondElement instanceof String) {
                    String contractId = (String) secondElement;
                    log.debug("Extracted second contract ID from tuple._2: {}", contractId);
                    return contractId;
                }
            }

            log.warn("Map result does not contain '_2' key or it's not a String. Keys: {}", resultMap.keySet());
        }

        // Case 2: List representation of tuple
        else if (exerciseResult instanceof List) {
            @SuppressWarnings("unchecked")
            List<Object> resultList = (List<Object>) exerciseResult;

            if (resultList.size() >= 2) {
                Object secondElement = resultList.get(1);

                if (secondElement instanceof String) {
                    String contractId = (String) secondElement;
                    log.debug("Extracted second contract ID from list[1]: {}", contractId);
                    return contractId;
                } else {
                    log.warn("Second element of list is not a String, type: {}",
                            secondElement.getClass().getName());
                }
            } else {
                log.warn("Result list has less than 2 elements");
            }
        }

        else {
            log.warn("Unexpected exercise result type for tuple: {}. Value: {}",
                    exerciseResult.getClass().getName(), exerciseResult);
        }

        return null;
    }
}
