package com.toleyko.springboot.orderservice.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.util.Base64;

@Component
public class TokenHandler {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private JsonNode getTokenPayload(String authorizationHeader) throws JsonProcessingException {
        String accessToken = authorizationHeader.substring("Bearer ".length());
        String[] tokenParts = accessToken.split("\\.");
        String payload = new String(Base64.getDecoder().decode(tokenParts[1]));
        System.out.println(payload);
        return objectMapper.readTree(payload);
    }
    public String getUsername(String authorizationHeader) throws JsonProcessingException {
        return this.getTokenPayload(authorizationHeader).get("preferred_username").asText();
    }
    public boolean isManager(String authorizationHeader) throws JsonProcessingException {
        JsonNode rolesNode = this.getTokenPayload(authorizationHeader)
                .path("realm_access")
                .path("roles");
        System.out.println(rolesNode.asText());
        for (JsonNode roleNode : rolesNode) {
            if ("ROLE_MANAGER".equals(roleNode.asText())) {
                return true;
            }
        }
        return false;
    }
}
