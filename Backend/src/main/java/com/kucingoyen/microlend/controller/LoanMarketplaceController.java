package com.kucingoyen.microlend.controller;

import com.kucingoyen.microlend.dto.lending.marketplace.*;
import com.kucingoyen.microlend.service.LoanMarketplaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for loan marketplace operations.
 */
@RestController
@RequestMapping("/api/lending")
@RequiredArgsConstructor
public class LoanMarketplaceController {

    private final LoanMarketplaceService loanMarketplaceService;

    @PostMapping("/request")
    public ResponseEntity<CreateLoanRequestResponse> createLoanRequest(
            @AuthenticationPrincipal UserDetails user,
            @RequestBody CreateLoanRequestRequest request) {

        CreateLoanRequestResponse response = loanMarketplaceService.createLoanRequest(
                user.getUsername(), request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/marketplace")
    public ResponseEntity<MarketplaceResponse> getMarketplace(
            @AuthenticationPrincipal UserDetails user) {

        MarketplaceResponse response = loanMarketplaceService.getMarketplace(user.getUsername());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/my-requests")
    public ResponseEntity<MarketplaceResponse> getMyRequests(
            @AuthenticationPrincipal UserDetails user) {

        MarketplaceResponse response = loanMarketplaceService.getMyRequests(user.getUsername());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/request/fill")
    public ResponseEntity<FillLoanResponse> fillLoanRequest(
            @AuthenticationPrincipal UserDetails user,
            @RequestBody FillLoanRequest request) {

        FillLoanResponse response = loanMarketplaceService.fillLoanRequest(
                user.getUsername(), request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/request/{contractId}")
    public ResponseEntity<Void> cancelLoanRequest(
            @AuthenticationPrincipal UserDetails user,
            @PathVariable String contractId) {

        loanMarketplaceService.cancelLoanRequest(user.getUsername(), contractId);
        return ResponseEntity.ok().build();
    }
}
