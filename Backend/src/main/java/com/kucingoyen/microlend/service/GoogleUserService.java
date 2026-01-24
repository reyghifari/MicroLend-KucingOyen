package com.kucingoyen.microlend.service;

import com.kucingoyen.microlend.model.Users;
import com.kucingoyen.microlend.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class GoogleUserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private static final Logger log = LoggerFactory.getLogger(GoogleUserService.class);

    private final UserRepository userRepository;
    private final DamlLedgerService damlLedgerService;

    public GoogleUserService(UserRepository userRepository, DamlLedgerService damlLedgerService) {
        this.userRepository = userRepository;
        this.damlLedgerService = damlLedgerService;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oauth2User = new DefaultOAuth2UserService().loadUser(userRequest);

        String googleSub = oauth2User.getAttribute("sub");
        String email = oauth2User.getAttribute("email");
        String name = oauth2User.getAttribute("name");

        Users user = userRepository.findByGoogleSub(googleSub)
                .orElseGet(() -> {
                    log.info("Creating new user for Google ID: {}, email: {}", googleSub, email);

                    Users newUser = new Users();
                    newUser.setGoogleSub(googleSub);
                    newUser.setEmail(email);
                    newUser.setFullName(name);
                    newUser.setUserLevel("1");
                    // damlPartyId will be set below on first login
                    return userRepository.save(newUser);
                });

        // Allocate DAML party on first login if user doesn't have one yet
        if (user.getDamlPartyId() == null || user.getDamlPartyId().isBlank()) {
            log.info("Allocating DAML party for user on first login: {}", email);
            String identifierHint = damlLedgerService.sanitizeIdentifierHint(name);
            String damlPartyId = damlLedgerService.allocateParty(name, identifierHint);
            user.setDamlPartyId(damlPartyId);
            userRepository.save(user);
        }

        return oauth2User;
    }
}
