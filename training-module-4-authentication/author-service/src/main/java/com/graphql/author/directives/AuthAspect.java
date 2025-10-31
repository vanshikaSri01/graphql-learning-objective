package com.graphql.author.directives;

import com.graphql.author.config.AuthContext;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import java.util.Map;

@Aspect
@Component
public class AuthAspect {
    private final AuthContext authContext;

    public AuthAspect(AuthContext authContext) {
        this.authContext = authContext;
    }

    @Around("@annotation(auth)")
    public Object authorize(ProceedingJoinPoint pjp, Auth auth) throws Throwable {
        Map<String, Object> user = authContext.getCurrentUser();
        if (user == null)
            throw new RuntimeException("Unauthorized");

        String userRole = (String) user.get("role");
        if (!userRole.equalsIgnoreCase(auth.requires()))
            throw new RuntimeException("Forbidden - Requires " + auth.requires());

        return pjp.proceed();
    }
}
