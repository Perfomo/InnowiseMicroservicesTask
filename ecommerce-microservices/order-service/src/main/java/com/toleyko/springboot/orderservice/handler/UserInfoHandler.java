package com.toleyko.springboot.orderservice.handler;

import com.toleyko.springboot.orderservice.handler.exception.TokenDataExtractionException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

@Component
public class UserInfoHandler {
    //Не знаю, как по-другому id юзера достать :(
    public String getUserId() throws TokenDataExtractionException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof JwtAuthenticationToken jwtAuthenticationToken) {
            return jwtAuthenticationToken.getTokenAttributes().get("sub").toString();
        }
        throw new TokenDataExtractionException("Can't get user id from token");
    }
}