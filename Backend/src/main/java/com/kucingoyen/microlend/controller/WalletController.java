package com.kucingoyen.microlend.controller;

import com.kucingoyen.microlend.dto.*;
import com.kucingoyen.microlend.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for wallet operations.
 * Provides endpoints for deposit, transfer, and balance queries.
 */
@RestController
@RequestMapping("/api/wallet")
@RequiredArgsConstructor
public class WalletController {

    private final WalletService walletService;

    /**
     * Deposit (mint) tokens to the authenticated user's wallet.
     * 
     * @param user    Authenticated user details
     * @param request Deposit request with amount and currency
     * @return DepositResponse with contract ID and status
     */
    @PostMapping("/deposit")
    public ResponseEntity<DepositResponse> deposit(
            @AuthenticationPrincipal UserDetails user,
            @RequestBody DepositRequest request) {

        DepositResponse response = walletService.deposit(
                user.getUsername(), // email
                request.getAmount(),
                request.getCurrency());

        return ResponseEntity.ok(response);
    }

    /**
     * Transfer tokens from authenticated user to another user.
     * 
     * @param user    Authenticated user details
     * @param request Transfer request with recipient, amount, and currency
     * @return TransferResponse with status
     */
    @PostMapping("/transfer")
    public ResponseEntity<TransferResponse> transfer(
            @AuthenticationPrincipal UserDetails user,
            @RequestBody TransferRequest request) {

        TransferResponse response = walletService.transfer(
                user.getUsername(), // sender email
                request);

        return ResponseEntity.ok(response);
    }

    /**
     * Get token balance for the authenticated user.
     * 
     * @param user Authenticated user details
     * @return BalanceResponse with balances by currency
     */
    @GetMapping("/balance")
    public ResponseEntity<BalanceResponse> getBalance(
            @AuthenticationPrincipal UserDetails user) {

        BalanceResponse response = walletService.getBalance(user.getUsername());

        return ResponseEntity.ok(response);
    }

    /**
     * Merge multiple holdings of the same asset into one.
     *
     * @param user    Authenticated user details
     * @param request Merge request with holding contract IDs
     * @return MergeHoldingsResponse with merged holding details
     */
    @PostMapping("/merge")
    public ResponseEntity<MergeHoldingsResponse> mergeHoldings(
            @AuthenticationPrincipal UserDetails user,
            @RequestBody MergeHoldingsRequest request) {

        MergeHoldingsResponse response = walletService.mergeHoldings(
                user.getUsername(),
                request);

        return ResponseEntity.ok(response);
    }
}
