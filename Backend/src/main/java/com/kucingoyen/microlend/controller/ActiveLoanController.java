package com.kucingoyen.microlend.controller;

import com.kucingoyen.microlend.dto.lending.loan.*;
import com.kucingoyen.microlend.service.ActiveLoanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for active loan operations.
 */
@RestController
@RequestMapping("/api/lending/loan")
@RequiredArgsConstructor
public class ActiveLoanController {

    private final ActiveLoanService activeLoanService;

    @GetMapping("/my-loans")
    public ResponseEntity<MyLoansResponse> getMyLoans(
            @AuthenticationPrincipal UserDetails user) {

        MyLoansResponse response = activeLoanService.getMyLoans(user.getUsername());
        return ResponseEntity.ok(response);
    }

    /**
     * Get loans funded by current user (as lender).
     */
    @GetMapping("/funded")
    public ResponseEntity<List<ActiveLoanDto>> getFundedLoans(
            @AuthenticationPrincipal UserDetails user) {

        List<ActiveLoanDto> loans = activeLoanService.getFundedLoans(user.getUsername());
        return ResponseEntity.ok(loans);
    }

    @GetMapping("/requests")
    public ResponseEntity<List<ActiveLoanDto>> getFundedRequest(
            @AuthenticationPrincipal UserDetails user) {

        List<ActiveLoanDto> loans = activeLoanService.getFundedRequest(user.getUsername());
        return ResponseEntity.ok(loans);
    }

    @PostMapping("/{contractId}/repay")
    public ResponseEntity<RepayLoanResponse> repayLoan(
            @AuthenticationPrincipal UserDetails user,
            @PathVariable String contractId,
            @RequestBody RepayLoanRequest request) {

        RepayLoanResponse response = activeLoanService.repayLoan(
                user.getUsername(), contractId, request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{contractId}/liquidate")
    public ResponseEntity<LiquidateResponse> liquidateLoan(
            @AuthenticationPrincipal UserDetails user,
            @PathVariable String contractId) {

        LiquidateResponse response = activeLoanService.liquidateLoan(
                user.getUsername(), contractId);
        return ResponseEntity.ok(response);
    }
}
