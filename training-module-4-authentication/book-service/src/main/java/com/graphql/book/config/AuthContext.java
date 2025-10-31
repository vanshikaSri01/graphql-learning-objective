package com.graphql.book.config;

import org.springframework.stereotype.Component;
import java.util.Map;

@Component
public class AuthContext {
    private static final ThreadLocal<Map<String, Object>> context = new ThreadLocal<>();

    public void setCurrentUser(Map<String, Object> user) {
        context.set(user);
    }

    public Map<String, Object> getCurrentUser() {
        return context.get();
    }

    public void clear() {
        context.remove();
    }
}
