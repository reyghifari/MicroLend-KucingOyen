package com.kucingoyen.microlend.service;

import com.kucingoyen.microlend.constant.LendingConstants;
import com.kucingoyen.microlend.service.DamlLedgerService.ContractResult;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Service for querying TimeOracle contract.
 * Provides current ledger time for loan expiry validation.
 */
@Service
@RequiredArgsConstructor
public class TimeOracleService {

    private static final Logger log = LoggerFactory.getLogger(TimeOracleService.class);

    private final DamlLedgerService damlService;
    private final AdminSetupService adminSetupService;

    /**
     * Get current time from TimeOracle contract.
     * 
     * @return Current time as string (ISO-8601 format)
     * @throws IllegalStateException if TimeOracle not found or unavailable
     */
    public String getCurrentTime() {
        String operatorPartyId = adminSetupService.getAdminPartyId();
        if (operatorPartyId == null) {
            throw new IllegalStateException("Admin party not initialized");
        }

        try {
            // Query active TimeOracle contract
            List<ContractResult> contracts = damlService.queryContracts(
                    LendingConstants.TEMPLATE_TIME_ORACLE,
                    Map.of("operator", operatorPartyId),
                    operatorPartyId);

            if (contracts.isEmpty()) {
                throw new IllegalStateException(
                        "No active TimeOracle contract found. System may need re-initialization.");
            }

            // Get current time from oracle payload
            Map<String, Object> payload = contracts.get(0).payload();
            Object currentTime = payload.get("currentTime");

            if (currentTime == null) {
                throw new IllegalStateException("TimeOracle contract has null currentTime");
            }

            String timeString = currentTime.toString();
            log.debug("Retrieved current time from TimeOracle: {}", timeString);

            return timeString;

        } catch (Exception e) {
            log.error("Failed to get current time from TimeOracle: {}", e.getMessage(), e);
            throw new IllegalStateException("Failed to get current time from TimeOracle: " + e.getMessage(), e);
        }
    }

    /**
     * Get current time with fallback to system time if TimeOracle unavailable.
     * Use this for non-critical time queries where availability is more important
     * than oracle source.
     * 
     * @return Current time as string
     */
    public String getCurrentTimeWithFallback() {
        try {
            return getCurrentTime();
        } catch (Exception e) {
            log.warn("TimeOracle unavailable, falling back to system time: {}", e.getMessage());
            return java.time.Instant.now().toString();
        }
    }
}
