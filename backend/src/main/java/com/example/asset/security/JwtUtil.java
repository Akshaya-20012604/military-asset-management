package com.example.asset.security;

import com.example.asset.entity.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import java.util.Date;
import java.security.Principal;
import java.util.Map;

@Component
public class JwtUtil {
    @Value("${app.jwt.secret}")
    private String secret;

    @Value("${app.jwt.expiration-ms}")
    private long expirationMs;

    public String generateToken(User user) {
        Date now = new Date();
        Date exp = new Date(now.getTime() + expirationMs);
        return Jwts.builder()
            .setSubject(String.valueOf(user.getId()))
            .claim("username", user.getUsername())
            .claim("roles", user.getRoles().stream().map(r->r.getName().replace("ROLE_","")))
            .setIssuedAt(now)
            .setExpiration(exp)
            .signWith(SignatureAlgorithm.HS256, secret.getBytes())
            .compact();
    }

    public Long extractUserIdFromPrincipal(Principal principal) {
        if (principal == null) return null;
        // principal name will be userId in our simple filter
        try { return Long.valueOf(principal.getName()); } catch(Exception e) { return null; }
    }
}
