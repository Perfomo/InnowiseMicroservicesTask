package com.toleyko.springboot.userservice.config;

import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KeycloakConfig {

    private final static String serverUrl = "http://172.17.0.1:8080";
    public final static String realm = "microServsRealm";
    private final static String clientId = "admin-cli";
    private final static String clientSecret = "zllYBgoqRezWpkNlXMYgx7iRZG9at1er";

    @Bean
    public Keycloak KeycloakInst() {
        return KeycloakBuilder.builder()
                .serverUrl(serverUrl)
                .realm(realm)
                .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .build();
    }
}