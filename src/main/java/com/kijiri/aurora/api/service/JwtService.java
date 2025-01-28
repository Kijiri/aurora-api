package com.kijiri.aurora.api.service;

import com.kijiri.aurora.api.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtService {

    private final UserDetailsServiceImpl userDetailsServiceImpl;
    @Value("${jwt.expiration}")
    private long jwtExpiration;
    @Value("${jwt.refresh.expiration}")
    private long refreshExpiration;
    @Value("${jwt.secret}")
    private String secretKey;

    public String generateToken(UserDetails userDetails) {
        log.info("Generating JWT token for user: {}", userDetails.getUsername());
        return generateToken(new HashMap<>(), userDetails);
    }

    public String generateRefreshToken(UserDetails userDetails) {
        log.info("Generating refresh token for user: {}", userDetails.getUsername());
        return buildToken(new HashMap<>(), userDetails, refreshExpiration);
    }

    public String generateToken(Map<String, Object> claims, UserDetails userDetails) {
        log.info("Generating JWT token with claims for user: {}", userDetails.getUsername());
        return buildToken(claims, userDetails, jwtExpiration);
    }

    private String buildToken(Map<String, Object> claims, UserDetails userDetails, long jwtExpiration) {
        var authorities = userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
        claims.put("userId", ((User) userDetails).getId());

        String token = Jwts
                .builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .claim("authorities", authorities)
                .signWith(getSignInKey())
                .compact();

        log.info("JWT token generated for user: {}", userDetails.getUsername());
        return token;
    }

    public String createNewAccessToken(String refreshToken) {
        log.info("Attempting to refresh access token using refresh token: {}", refreshToken);
        
        final String username = extractUsername(refreshToken);
        if (username != null && isTokenActive(refreshToken)) {
            UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(username);
            String newAccessToken = generateToken(userDetails);
            log.info("New access token generated for user: {}", username);
            return newAccessToken;
        }
        log.warn("Failed to refresh token. Invalid or expired refresh token: {}", refreshToken);
        return null;
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        log.info("Validating token for user: {}", userDetails.getUsername());
        final String username = extractUsername(token);
        
        boolean isValid = username.equals(userDetails.getUsername()) && isTokenActive(token);
        if (isValid) {
            log.info("Token is valid for user: {}", userDetails.getUsername());
        } else {
            log.warn("Token validation failed for user: {}", userDetails.getUsername());
        }
        return isValid;
    }

    private boolean isTokenActive(String token) {
        return extractExpiration(token).after(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Long extractUserId(String token) {
        return extractClaim(token, claims -> claims.get("userId", Long.class));
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        log.info("Extracting claim from token");
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        log.info("Extracting all claims from token");
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
