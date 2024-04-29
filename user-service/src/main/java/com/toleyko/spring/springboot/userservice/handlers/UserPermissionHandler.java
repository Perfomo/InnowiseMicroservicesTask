package com.toleyko.spring.springboot.userservice.handlers;

import com.toleyko.spring.springboot.userservice.services.UserService;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class UserPermissionHandler {
    private UserService userService;
    public boolean isPermitted(String userName) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserRepresentation user = userService.getUserByUsername(userName);
        return user.getUsername().equals(authentication.getName()) || authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_MANAGER"));
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
