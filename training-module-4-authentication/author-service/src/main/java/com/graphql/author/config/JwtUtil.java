package com.graphql.author.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import java.nio.charset.StandardCharsets;
import java.security.Key;

@Component
public class JwtUtil {
    private static final String SECRET = "my-super-secret-key-that-should-be-long-123456789";

    private Key getKey() {
        // ✅ Create an HMAC key directly from bytes (no Base64 decoding)
        return Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
    }

    public Claims decode(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            System.out.println("Invalid JWT: " + e.getMessage());
            return null;
        }
    }
}
