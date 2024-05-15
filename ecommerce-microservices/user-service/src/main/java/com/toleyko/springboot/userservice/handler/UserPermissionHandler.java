package com.toleyko.springboot.userservice.handler;

import com.toleyko.springboot.userservice.service.UserKeycloakService;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class UserPermissionHandler {
    private UserKeycloakService userKeycloakService;
    public boolean isPermitted(String userName) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserRepresentation user = userKeycloakService.getUserByUsername(userName);
        return user.getUsername().equals(authentication.getName()) || authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_MANAGER"));
    }

    @Autowired
    public void setUserService(UserKeycloakService userKeycloakService) {
        this.userKeycloakService = userKeycloakService;
    }
}
