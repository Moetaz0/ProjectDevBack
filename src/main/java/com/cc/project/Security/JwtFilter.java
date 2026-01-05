package com.cc.project.Security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final AntPathMatcher matcher = new AntPathMatcher();

    // Paths that do NOT require JWT authentication:
    private final List<String> publicPaths = List.of(
            "/auth/**",
            "/api/public/**",
            "/api/doctors/**",
            "/privacy-policy",
            "/terms-of-service",
            "/");

    public JwtFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    private boolean isPublicPath(HttpServletRequest request) {
        String path = request.getRequestURI();
        for (String pattern : publicPaths) {
            if (matcher.match(pattern, path))
                return true;
        }
        // Allow OPTIONS for preflight CORS requests
        if (HttpMethod.OPTIONS.matches(request.getMethod()))
            return true;
        return false;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        if (isPublicPath(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Missing or invalid Authorization header");
            return;
        }

        String token = authHeader.substring(7);
        try {
            Claims claims = jwtUtil.extractClaims(token);
            String username = claims.getSubject();
            String role = claims.get("role", String.class);
            // Create an Authentication with role mapping (simple)
            var authority = Collections.singleton(new SimpleGrantedAuthority("ROLE_" + role));
            var auth = new UsernamePasswordAuthenticationToken(username, null, authority);
            SecurityContextHolder.getContext().setAuthentication(auth);

            // continue filter chain
            filterChain.doFilter(request, response);
        } catch (JwtException e) {
            // token invalid or expired
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid or expired token");
        }
    }
}
