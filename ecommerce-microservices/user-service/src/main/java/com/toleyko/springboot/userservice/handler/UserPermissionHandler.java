package com.toleyko.springboot.userservice.handler;


import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class UserPermissionHandler {
    public boolean isPermitted(String userName) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userName.equals(authentication.getName()) || authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_MANAGER"));
    }
}
