package com.kucingoyen.microlend.service;

import com.kucingoyen.microlend.model.Users;
import com.kucingoyen.microlend.repository.UserRepository;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class GoogleUserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;

    public GoogleUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oauth2User =
                new DefaultOAuth2UserService().loadUser(userRequest);

        String googleId = oauth2User.getAttribute("sub");
        String email = oauth2User.getAttribute("email");
        String name = oauth2User.getAttribute("name");

        Users user = userRepository.findByGoogleId(googleId)
                .orElseGet(() -> {
                    Users newUser = new Users();
                    newUser.setGoogleId(googleId);
                    newUser.setEmail(email);
                    newUser.setFullName(name);
                    newUser.setDamlPartyId(UUID.randomUUID().toString());
                    newUser.setUserLevel("1");
                    return userRepository.save(newUser);
                });

        return oauth2User;
    }
}
