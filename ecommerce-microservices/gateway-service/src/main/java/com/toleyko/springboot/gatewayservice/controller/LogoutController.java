package com.toleyko.springboot.gatewayservice.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

@RestController
//@CrossOrigin("*")
public class LogoutController {

    @Value("${GATEWAY_SERVICE_PORT}")
    private String port;

    @GetMapping("/homepage")
    private ResponseEntity<String> showHomepage() {
        return ResponseEntity.ok().body("Homepage");
    }

    @GetMapping("/api/logout")
    private RedirectView logout() {
        RedirectView redirectView = new RedirectView();
        redirectView.setUrl("http://172.17.0.1:" + port + "/logout");
        return redirectView;
    }
}
