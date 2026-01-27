package com.kucingoyen.microlend.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.kucingoyen.microlend.dto.LoginResponse;
import com.kucingoyen.microlend.model.Users;
import com.kucingoyen.microlend.repository.UserRepository;
import com.kucingoyen.microlend.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);

    private final GoogleTokenVerificationService googleTokenVerificationService;
    private final UserRepository userRepository;
    private final DamlLedgerService damlLedgerService;
    private final JwtUtil jwtUtil;

    @Transactional
    public LoginResponse verifyAndLogin(String idTokenString) {
        // 1. Verify Google Token
        GoogleIdToken.Payload payload = googleTokenVerificationService.verify(idTokenString);
        String email = payload.getEmail();
        String googleSub = payload.getSubject();
        String name = (String) payload.get("name");

        // 2. Find or Create User
        Users user = userRepository.findByEmail(email)
                .map(existingUser -> {
                    // Update googleSub if missing (linking accounts)
                    if (existingUser.getGoogleSub() == null) {
                        existingUser.setGoogleSub(googleSub);
                        return userRepository.save(existingUser);
                    }
                    return existingUser;
                })
                .orElseGet(() -> {
                    // Create new user
                    Users newUser = Users.builder()
                            .email(email)
                            .fullName(name != null ? name : email)
                            .googleSub(googleSub)
                            .userLevel("1")
                            .build();
                    return userRepository.save(newUser);
                });

        // 3. Allocate DAML Party if needed
        if (user.getDamlPartyId() == null) {
            try {
                String identifierHint = damlLedgerService.sanitizeIdentifierHint(user.getEmail());
                String partyId = damlLedgerService.allocateParty(user.getFullName(), identifierHint);

                user.setDamlPartyId(partyId);
                user = userRepository.save(user); // Persist party ID
            } catch (Exception e) {
                log.error("Failed to allocate DAML party for user {}", user.getEmail(), e);
                // We might want to continue without DAML party or throw exception depending on
                // requirements
                // For now, we log and proceed (login works, but ledger ops will fail) or throw?
                // The prompt requirement implies strict integration, so let's allow it to fail
                // or just log.
                // Assuming critical for app function:
                throw new RuntimeException("Failed to allocate DAML party", e);
            }
        }

        // 4. Generate JWT
        String token = jwtUtil.generateToken(user.getEmail());

        return LoginResponse.builder()
                .token(token)
                .email(user.getEmail())
                .fullName(user.getFullName())
                .damlPartyId(user.getDamlPartyId())
                .build();
    }
}
