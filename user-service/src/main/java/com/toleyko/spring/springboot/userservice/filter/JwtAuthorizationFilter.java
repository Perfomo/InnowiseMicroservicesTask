//package com.toleyko.spring.springboot.userservice.filter;
//
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jwts;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
//import java.io.IOException;
//
//public class JwtAuthorizationFilter extends BasicAuthenticationFilter {
//
//    public JwtAuthorizationFilter(AuthenticationManager authenticationManager) {
//        super(authenticationManager);
//    }
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
//                                    FilterChain chain) throws IOException, ServletException {
//        String header = request.getHeader("Authorization");
//
//        if (header == null || !header.startsWith("Bearer ")) {
//            chain.doFilter(request, response);
//            return;
//        }
//
//        Authentication authentication = getAuthentication(request);
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//        chain.doFilter(request, response);
//    }
//
//    private Authentication getAuthentication(HttpServletRequest request) {
//        String token = request.getHeader("Authorization");
//        if (token != null) {
//            // Parse and validate the JWT token
//            Claims claims = Jwts.parser().verifyWith("")
//
//            // Extract necessary information from the token
//            String username = claims.getSubject();
//            if (username != null) {
//                return new UsernamePasswordAuthenticationToken(username, null, null);
//            }
//        }
//        return null;
//    }
//}