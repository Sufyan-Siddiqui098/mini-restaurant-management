package com.computerstudent.food_menu_order_management.config.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.InvalidKeyException;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class JwtUtil {

    private SecretKey getSigningKey() {
        String jwtSecret = "rN2kA3sF1bG7vYxR8eKmZqW4tU9xMdLqJ5c8HbXsWq1eCvZfF0TyRjLkWzQ==";
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    //    public String generateToken(UserDetails userDetails){
    public String generateToken(Authentication authentication) {
        try {
//            principal = your full UserDetails
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            // 30 minutes
            long jwtExpirationMs = 1000 * 60 * 30;
            return Jwts.builder()
                    .subject(userDetails.getUsername())
                    // getAuthorities() returns a Collection<? extends GrantedAuthority> -- can cause a problem
                    .claim("roles", userDetails.getAuthorities().stream()
                            .map(GrantedAuthority::getAuthority)
                            .collect(Collectors.toList()))
                    .issuedAt(new Date(System.currentTimeMillis()))
                    .expiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                    .signWith(getSigningKey())
                    .compact();
        } catch (InvalidKeyException e) {
            throw new RuntimeException("JWT token generation failed unexpectedly", e);
        }
    }

    // Claim -- payload data
    public Claims extractAllClaims(String token) {

        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // Expiration of token
    public Date extractExpiration(String token) {
        return extractAllClaims(token).getExpiration();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Boolean validateToken(String token) {
        return !isTokenExpired(token);
    }

    public String extractUsername(String token) {
        Claims claims = extractAllClaims(token);
        return claims.getSubject();
    }
}
