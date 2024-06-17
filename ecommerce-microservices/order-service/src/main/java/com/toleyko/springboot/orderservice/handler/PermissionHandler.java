package com.toleyko.springboot.orderservice.handler;

import com.toleyko.springboot.orderservice.handler.exception.TokenDataExtractionException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

@Component
public class PermissionHandler {
    public boolean isManager() throws TokenDataExtractionException {
        if (!SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
            throw new TokenDataExtractionException("Anonymous token");
        }
        return SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getAuthorities()
                .stream()
                .anyMatch(el -> el.toString().equals("ROLE_MANAGER"));
    }

    public String getUsername() throws TokenDataExtractionException {
        String username = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof JwtAuthenticationToken) {
            var jwtAuthenticationToken = (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
            return jwtAuthenticationToken.getTokenAttributes().get("preferred_username").toString();
        }
        throw new TokenDataExtractionException("Can't get username from token");
    }

    public String getUserId() throws TokenDataExtractionException {
        String id = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof JwtAuthenticationToken) {
            var jwtAuthenticationToken = (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
            return jwtAuthenticationToken.getTokenAttributes().get("sub").toString();
        }
        throw new TokenDataExtractionException("Can't get user id from token");
    }
}
