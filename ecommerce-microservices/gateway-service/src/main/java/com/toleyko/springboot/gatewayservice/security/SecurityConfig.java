package com.toleyko.springboot.gatewayservice.security;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.HttpMethod;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import java.util.stream.Stream;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Value("${GATEWAY_SERVICE_PORT}")
    private String gatewayPort;
    @Bean
    public SecurityFilterChain spSecurityWebFilterChain(HttpSecurity http) throws Exception {
        http.oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))
                .csrf(c -> c.ignoringRequestMatchers("/users/api/users", HttpMethod.POST))
                .authorizeHttpRequests(c -> c
                        .requestMatchers("/users/api/users", HttpMethod.POST).permitAll()
                    .requestMatchers("/error").permitAll()
                    .requestMatchers("/homepage").permitAll()
                    .requestMatchers("/api/logout").authenticated());
        http.oauth2Login(Customizer.withDefaults());
        http.authorizeHttpRequests(c -> c
                .requestMatchers("/users/**").authenticated()
                .requestMatchers("/products/**").hasRole("MANAGER")
                .requestMatchers("/inventory/**").hasRole("MANAGER")
                .requestMatchers("/orders/api/orders", HttpMethod.POST).authenticated()
                .requestMatchers("/orders/api/*/orders", HttpMethod.GET).authenticated()
                .requestMatchers("/orders/**").hasRole("MANAGER"));

        http.logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessHandler((request, response, authentication) -> {
                    if (authentication != null && authentication.getPrincipal() instanceof OidcUser) {
                        OidcUser oidcUser = (OidcUser) authentication.getPrincipal();
                        OidcIdToken idToken = oidcUser.getIdToken();
                        String idTokenValue = idToken.getTokenValue();
                        String logoutUrl = "http://172.17.0.1:8080/realms/microServsRealm/protocol/openid-connect/logout";
                        String postLogoutRedirectUri = "http://172.17.0.1:" + gatewayPort +"/homepage";
                        response.setStatus(HttpServletResponse.SC_OK);
                        response.sendRedirect(logoutUrl +
                                "?post_logout_redirect_uri=" + postLogoutRedirectUri +
                                "&id_token_hint=" + idTokenValue);
                    }
                    else {
                        response.sendRedirect("http://172.17.0.1:" + gatewayPort + "/api/logout");
                    }
                })
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .deleteCookies("JSESSIONID"));
        return http.build();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        var converter = new JwtAuthenticationConverter();
        var jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        converter.setPrincipalClaimName("preferred_username");
        converter.setJwtGrantedAuthoritiesConverter(jwt -> {
            var authorities = jwtGrantedAuthoritiesConverter.convert(jwt);
            var roles = jwt.getClaimAsStringList("spring-sec-roles");

            return Stream.concat(authorities.stream(),
                            roles.stream()
                                    .filter(role -> role.startsWith("ROLE_"))
                                    .map(SimpleGrantedAuthority::new)
                                    .map(GrantedAuthority.class::cast))
                    .toList();
        });

        return converter;
    }

    @Bean
    public OAuth2UserService<OidcUserRequest, OidcUser> oAuth2UserService() {
        var oidcUserService = new OidcUserService();
        return userRequest -> {
            var oidcUser = oidcUserService.loadUser(userRequest);
            var roles = oidcUser.getClaimAsStringList("spring-sec-roles");
            var authorities = Stream.concat(oidcUser.getAuthorities().stream(),
                            roles.stream()
                                    .filter(role -> role.startsWith("ROLE_"))
                                    .map(SimpleGrantedAuthority::new)
                                    .map(GrantedAuthority.class::cast))
                    .toList();

            return new DefaultOidcUser(authorities, oidcUser.getIdToken(), oidcUser.getUserInfo());
        };
    }

}
