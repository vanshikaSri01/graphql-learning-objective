package com.graphql.author.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Map;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    private final AuthContext authContext;
    private final JwtUtil jwtUtil;

    public AuthInterceptor(AuthContext authContext, JwtUtil jwtUtil) {
        this.authContext = authContext;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object handler) {
        String authHeader = req.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            try {
                var claims = jwtUtil.decode(token);
                authContext.setCurrentUser(Map.of(
                        "role", claims.get("role"),
                        "username", claims.get("username")
                ));
                return true;
            } catch (Exception e) {
                System.out.println("❌ Invalid token: " + e.getMessage());
            }
        }

        // If no valid token, let it proceed as guest instead of blocking GraphQL introspection
        authContext.setCurrentUser(Map.of("role", "GUEST"));
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest req, HttpServletResponse res, Object handler, Exception ex) {
      authContext.clear(); // ✅ proper cleanup using ThreadLocal.remove()
    }

}
