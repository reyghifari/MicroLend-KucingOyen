package com.kucingoyen.microlend.util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {
    private final JwtUtil jwtUtil;

    public OAuth2SuccessHandler(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws IOException {

        OAuth2User oauth2User =
                (OAuth2User) authentication.getPrincipal();

        String email = oauth2User.getAttribute("email");

        String token = jwtUtil.generateToken(email);

        response.setContentType("application/json");
        response.getWriter()
                .write("{\"token\":\"" + token + "\"}");
    }
}
