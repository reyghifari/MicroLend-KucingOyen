package com.kucingoyen.microlend.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * Redirect controller for convenience endpoints.
 */
@RestController
public class LoginRedirectController {

    /**
     * Redirect /login to Google OAuth2 authorization.
     * This provides a simpler endpoint for users to initiate login.
     */
    @GetMapping("/login")
    public void redirectToGoogleLogin(HttpServletResponse response) throws IOException {
        response.sendRedirect("/oauth2/authorization/google");
    }
}
