package com.example.ecommerce_backend.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.persistence.Column;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {

//    private final SecretKey secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    @Value("${jwt.secret}")
    private String secretString;

    private SecretKey getSecretKey(){
        return Keys.hmacShaKeyFor(secretString.getBytes());
    }

    private final long EXPIRATION_MS = 1000 * 60 * 60 * 24;




    public String generateToken(String email){
        return Jwts.builder()
                .subject(email)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_MS))
                .signWith(getSecretKey())
                .compact();
    }


    public String extractEmail(String token){
        return Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }


}
