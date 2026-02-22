package com.kucingoyen.microlend.controller;

import com.kucingoyen.microlend.dto.lending.profile.CreateProfileResponse;
import com.kucingoyen.microlend.dto.lending.profile.UserProfileResponse;
import com.kucingoyen.microlend.service.LendingProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for user lending profile operations.
 */
@RestController
@RequestMapping("/api/lending/profile")
@RequiredArgsConstructor
public class LendingProfileController {

    private final LendingProfileService lendingProfileService;

    /**
     * Create a lending profile for the authenticated user.
     * This should be called the first time a user accesses lending features.
     *
     * @param user Authenticated user details
     * @return Response with created profile
     */
    @PostMapping
    public ResponseEntity<CreateProfileResponse> createProfile(
            @AuthenticationPrincipal UserDetails user) {

        CreateProfileResponse response = lendingProfileService.createUserProfile(user.getUsername());
        return ResponseEntity.ok(response);
    }

    /**
     * Get the lending profile of the authenticated user.
     *
     * @param user Authenticated user details
     * @return User's lending profile
     */
    @GetMapping
    public ResponseEntity<UserProfileResponse> getProfile(
            @AuthenticationPrincipal UserDetails user) {

        UserProfileResponse response = lendingProfileService.getUserProfile(user.getUsername());
        return ResponseEntity.ok(response);
    }
}
