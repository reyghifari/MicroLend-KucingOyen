package com.kucingoyen.microlend.service;

import com.kucingoyen.microlend.constant.LendingConstants;
import com.kucingoyen.microlend.model.SystemConfig;
import com.kucingoyen.microlend.repository.SystemConfigRepository;
import com.kucingoyen.microlend.service.DamlLedgerService.ContractResult;
import com.kucingoyen.microlend.service.DamlLedgerService.ExerciseResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Map;

/**
 * Scheduled service to update the TimeOracle contract.
 * Runs every minute to keep the ledger time current.
 */
@Service
@RequiredArgsConstructor
public class TimeOracleScheduler {

    private static final Logger log = LoggerFactory.getLogger(TimeOracleScheduler.class);

    private final DamlLedgerService damlService;
    private final SystemConfigRepository configRepository;
    private final AdminSetupService adminSetupService;

    /**
     * Update TimeOracle contract every minute.
     * Queries for active TimeOracle contract and updates it with current time.
     */
    @Scheduled(fixedRate = 60000) // Every 1 minute
    public void updateTimeOracle() {
        try {
            String operatorPartyId = adminSetupService.getAdminPartyId();
            if (operatorPartyId == null) {
                log.warn("Admin party not initialized. Skipping TimeOracle update.");
                return;
            }

            // Query for active TimeOracle contract instead of using cached ID
            // This handles contract rotation when UpdateTime creates new contracts
            List<ContractResult> contracts = damlService.queryContracts(
                    LendingConstants.TEMPLATE_TIME_ORACLE,
                    Map.of("operator", operatorPartyId),
                    operatorPartyId);

            if (contracts.isEmpty()) {
                log.warn("No active TimeOracle contract found. System may need re-initialization.");
                return;
            }

            // Use the first (should be only) TimeOracle contract
            String timeOracleCid = contracts.get(0).contractId();
            String currentTime = Instant.now().toString();

            log.debug("Updating TimeOracle {} with time {}", timeOracleCid, currentTime);

            // Exercise UpdateTime choice
            ExerciseResponse response = damlService.exerciseChoice(
                    LendingConstants.TEMPLATE_TIME_ORACLE,
                    timeOracleCid,
                    "UpdateTime",
                    Map.of("newTime", currentTime),
                    operatorPartyId);

            // UpdateTime returns new contract ID - update database
            if (response != null && response.result() != null) {
                Object result = response.result().exerciseResult();
                if (result instanceof String) {
                    String newTimeOracleCid = (String) result;

                    // Save the new contract ID to database
                    configRepository.save(new SystemConfig(
                            LendingConstants.CONFIG_TIME_ORACLE_CID,
                            newTimeOracleCid));

                    log.debug("TimeOracle updated successfully. New contract ID: {}", newTimeOracleCid);
                }
            }

        } catch (Exception e) {
            log.error("Failed to update TimeOracle: {}", e.getMessage());
            log.debug("TimeOracle error details:", e);
            // Don't throw - let the scheduler continue
        }
    }
}
