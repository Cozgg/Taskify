package com.ccq.utils;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import com.ccq.configs.EnvConfig;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
@PropertySource("classpath:configs.properties")
public class JwtUtil {

    @Value("${jwt.expiration-ms}")
    private long expirationMs;

    @Value("${jwt.refresh-expiration-ms}")
    private long refreshExpirationMs;

    private SecretKey getSigningKey() {
        String signingSecret = EnvConfig.get("JWT_SECRET");
        if (signingSecret == null || signingSecret.isBlank()) {
            throw new IllegalStateException("JWT_SECRET is missing");
        }
        return Keys.hmacShaKeyFor(signingSecret.getBytes(StandardCharsets.UTF_8));
    }

    // mặc định 24h
    public String generateToken(String username) {
        return buildToken(username, expirationMs);
    }

    // mặc định 7 ngày
    public String generateRefreshToken(String username) {
        return buildToken(username, refreshExpirationMs);
    }

    private String buildToken(String username, long ttlMs) {
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + ttlMs))
                .signWith(getSigningKey())
                .compact();
    }

    public String extractUsername(String token) {
        return parseClaims(token).getSubject();
    }

    public boolean validateToken(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
