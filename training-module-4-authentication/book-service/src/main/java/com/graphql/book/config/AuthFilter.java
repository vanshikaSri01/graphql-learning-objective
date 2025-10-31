package com.graphql.book.config;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;

@Component
public class AuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final AuthContext authContext;

    public AuthFilter(JwtUtil jwtUtil, AuthContext authContext) {
        this.jwtUtil = jwtUtil;
        this.authContext = authContext;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            Claims claims = jwtUtil.decode(token);
            if (claims != null) {
                authContext.setCurrentUser(Map.of(
                        "username", claims.get("username"),
                        "role", claims.get("role")
                ));
            }
        }

        try {
            filterChain.doFilter(request, response);
        } finally {
            authContext.setCurrentUser(null); // Cleanup
        }
    }
}
