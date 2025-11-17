package com.example.asset.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.Principal;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Value("${app.jwt.secret}")
    private String secret;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();

        // ✔ SKIP auth endpoints (login/register)
        if (path.startsWith("/api/auth")) {
            filterChain.doFilter(request, response);
            return;
        }

        // ✔ Normal JWT processing for protected endpoints
        String auth = request.getHeader("Authorization");

        if (auth != null && auth.startsWith("Bearer ")) {
            String token = auth.substring(7);

            try {
                Claims body = Jwts.parserBuilder()
                        .setSigningKey(secret.getBytes())
                        .build()
                        .parseClaimsJws(token)
                        .getBody();

                String sub = body.getSubject();

                if (sub != null) {
                    Principal p = () -> sub;
                    request = new HttpServletRequestWrapperWithPrincipal(request, p);
                }

            } catch (Exception ignored) {
                // invalid token → let Spring Security handle it
            }
        }

        filterChain.doFilter(request, response);
    }
}
