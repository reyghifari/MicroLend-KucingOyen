package com.kucingoyen.microlend.controller;

import com.kucingoyen.microlend.dto.lending.admin.LendingSetupRequest;
import com.kucingoyen.microlend.dto.lending.admin.LendingSetupResponse;
import com.kucingoyen.microlend.dto.lending.admin.UpdateConfigRequest;
import com.kucingoyen.microlend.dto.lending.admin.UpdateConfigResponse;
import com.kucingoyen.microlend.service.LendingAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for lending system admin operations.
 * Provides endpoints for initializing and configuring the lending system.
 */
@RestController
@RequestMapping("/api/admin/lending")
@RequiredArgsConstructor
public class LendingAdminController {

    private final LendingAdminService lendingAdminService;

    /**
     * Initialize the lending system by creating core contracts.
     * This should be called once during deployment.
     *
     * @param request Setup request with initial configuration
     * @return Response with created contract IDs
     */
    @PostMapping("/setup")
    public ResponseEntity<LendingSetupResponse> setupLendingSystem(
            @RequestBody LendingSetupRequest request) {

        LendingSetupResponse response = lendingAdminService.setupLendingSystem(request);
        return ResponseEntity.ok(response);
    }

    /**
     * Update lending system configuration.
     *
     * @param request Update request with new configuration values
     * @return Response with success status
     */
    @PutMapping("/config")
    public ResponseEntity<UpdateConfigResponse> updateConfiguration(
            @RequestBody UpdateConfigRequest request) {

        UpdateConfigResponse response = lendingAdminService.updateConfiguration(request);
        return ResponseEntity.ok(response);
    }
}
