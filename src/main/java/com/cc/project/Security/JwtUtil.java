package com.cc.project.Security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration-ms:10800000}") // default 3h
    private long expirationMs;

    private Key key;

    @jakarta.annotation.PostConstruct
    public void init() {
        // Use the secret bytes to build an HMAC-SHA key
        // Your jwt.secret must be long enough (>= 32 bytes for HS256)
        key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    public String generateToken(String username, String role, Long userId) {
        Date now = new Date();
        Date exp = new Date(now.getTime() + expirationMs);

        return Jwts.builder()
                .setSubject(username)
                .claim("role", role)
                .claim("userId", userId)
                .setIssuedAt(now)
                .setExpiration(exp)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public Jws<Claims> parseToken(String token) throws JwtException {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);
    }

    public Claims extractClaims(String token) {
        return parseToken(token).getBody();
    }

    public boolean isTokenExpired(String token) {
        try {
            Date exp = extractClaims(token).getExpiration();
            return exp.before(new Date());
        } catch (JwtException e) {
            return true;
        }
    }
}
