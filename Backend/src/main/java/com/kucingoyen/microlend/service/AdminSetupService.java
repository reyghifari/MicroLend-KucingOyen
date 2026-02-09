package com.kucingoyen.microlend.service;

import com.kucingoyen.microlend.model.SystemConfig;
import com.kucingoyen.microlend.repository.SystemConfigRepository;
import com.kucingoyen.microlend.service.DamlLedgerService.CreateResponse;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Service to initialize and manage the admin party for the MicroLend system.
 * The admin party acts as the bank/issuer with authority to mint tokens and
 * manage accounts.
 */
@Service
@RequiredArgsConstructor
public class AdminSetupService {

    private static final Logger log = LoggerFactory.getLogger(AdminSetupService.class);

    private final DamlLedgerService damlService;
    private final SystemConfigRepository configRepository;

    // Keys for storing configuration
    private static final String ADMIN_PARTY_ID_KEY = "ADMIN_PARTY_ID";

    // Cached value
    private String adminPartyId;

    /**
     * Initialize admin party on application startup.
     * This method runs once when the application starts.
     */
    @PostConstruct
    public void initializeAdminParty() {
        try {
            log.info("Initializing admin party for MicroLend system...");

            // Check if admin already exists in database
            Optional<SystemConfig> existingAdmin = configRepository.findByKey(ADMIN_PARTY_ID_KEY);

            if (existingAdmin.isPresent()) {
                // Admin already exists, load from database
                this.adminPartyId = existingAdmin.get().getValue();
                log.info("Loaded existing admin party: {}", adminPartyId);
            } else {
                // Allocate admin party if not exists
                this.adminPartyId = damlService.allocateParty("MicroLend Admin", "admin");

                // Save admin party ID to database
                configRepository.save(new SystemConfig(ADMIN_PARTY_ID_KEY, adminPartyId));
                log.info("Created new admin party: {}", adminPartyId);

                // Setup contracts on first run
                setupInitialContracts();
            }

            log.info("Admin party initialization completed successfully");

        } catch (Exception e) {
            log.error("Failed to initialize admin party: {}", e.getMessage(), e);
            // Don't throw exception - let the application start but log the error
        }
    }

    /**
     * Create AssetIssuerService and AssetFactory contracts on first run.
     * With exerciseByKey, we don't need to track contract IDs anymore.
     */
    private void setupInitialContracts() {
        try {
            log.info("Setting up initial contracts for admin party: {}", adminPartyId);

            // Create AssetIssuerService (with key = issuer party)
            CreateResponse issuerResponse = damlService.createContract(
                    "MicroLend.Finance.Asset:AssetIssuerService",
                    Map.of(
                            "issuer", adminPartyId,
                            "observers", List.of()),
                    adminPartyId);

            if (issuerResponse != null && issuerResponse.result() != null) {
                log.info("Created AssetIssuerService for admin");
            }

            // Create CC Token Factory using exerciseByKey on AssetIssuerService
            // For Party key type, DAML JSON API expects just the party string directly
            damlService.exerciseChoiceByKey(
                    "MicroLend.Finance.Asset:AssetIssuerService",
                    adminPartyId, // Key is just the party string for Party type
                    "CreateCCFactory",
                    java.util.Collections.singletonMap("maxSupply", null), // unlimited supply
                    adminPartyId);

            log.info("Created CC AssetFactory via exerciseByKey");

            // Create USDx Token Factory for USD Stablecoin deposits
            damlService.exerciseChoiceByKey(
                    "MicroLend.Finance.Asset:AssetIssuerService",
                    adminPartyId,
                    "CreateUSDxFactory",
                    java.util.Collections.singletonMap("maxSupply", null), // unlimited supply
                    adminPartyId);

            log.info("Created USDx AssetFactory via exerciseByKey");

        } catch (Exception e) {
            log.error("Failed to setup initial contracts: {}", e.getMessage(), e);
        }
    }

    // Getter for admin party ID

    public String getAdminPartyId() {
        return adminPartyId;
    }
}
