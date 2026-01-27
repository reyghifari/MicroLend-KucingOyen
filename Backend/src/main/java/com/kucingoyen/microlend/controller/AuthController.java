package com.kucingoyen.microlend.controller;

import com.kucingoyen.microlend.dto.GoogleAuthRequest;
import com.kucingoyen.microlend.dto.LoginResponse;
import com.kucingoyen.microlend.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*") // Allow requests from any frontend for now
public class AuthController {

    private final AuthService authService;

    @PostMapping("/google/verify")
    public ResponseEntity<LoginResponse> verifyGoogleToken(@RequestBody GoogleAuthRequest request) {
        String token = request.getToken();
        if (token == null || token.isBlank()) {
            return ResponseEntity.badRequest().build();
        }

        LoginResponse response = authService.verifyAndLogin(token);
        return ResponseEntity.ok(response);
    }
}
