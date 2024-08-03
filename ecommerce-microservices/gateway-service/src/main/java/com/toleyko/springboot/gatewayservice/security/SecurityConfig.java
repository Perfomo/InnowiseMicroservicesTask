package com.toleyko.springboot.gatewayservice.security;

import jakarta.ws.rs.HttpMethod;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import java.util.stream.Stream;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain SecurityWebFilterChain(HttpSecurity http) throws Exception {
        http.oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))
                .sessionManagement(httpSecuritySessionManagementConfigurer -> httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(c -> c.ignoringRequestMatchers("/users/api/users", HttpMethod.POST))
                .authorizeHttpRequests(c -> c
                        .requestMatchers("/users/api/users", HttpMethod.POST).permitAll()
                    .requestMatchers("/error").permitAll()
                    .requestMatchers("/homepage").permitAll()
                    .requestMatchers("/api/logout").authenticated());
        http.authorizeHttpRequests(c -> c
                .requestMatchers("/users/**").authenticated()
                .requestMatchers("/products/api/products", HttpMethod.GET).permitAll()
                .requestMatchers("/products/**").hasRole("MANAGER")
                .requestMatchers("/actuator/**").permitAll()
                .requestMatchers("actuator/**").permitAll()
                .requestMatchers("/inventory/**").hasRole("MANAGER")
                .requestMatchers("/orders/api/orders", HttpMethod.POST).authenticated()
                .requestMatchers("/orders/api/*/orders", HttpMethod.GET).authenticated()
                .requestMatchers("/orders/**").hasRole("MANAGER"));
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
}
