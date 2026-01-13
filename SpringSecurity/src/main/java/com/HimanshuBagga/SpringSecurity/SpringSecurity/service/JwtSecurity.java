package com.HimanshuBagga.SpringSecurity.SpringSecurity.service;

import com.HimanshuBagga.SpringSecurity.SpringSecurity.entities.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Set;

@Service
public class JwtSecurity {

    @Value("${jwt.secretKey}")
    private String jwtSecretKey;

    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(jwtSecretKey.getBytes(StandardCharsets.UTF_8));
    }

    public String generateAccessToken(User user){
        return Jwts.builder()
                .subject(String.valueOf(user.getId())) // who is the token for // 12->"12"
                .claim("email", user.getUsername()) // extra info about user
                .claim("roles", user.getRole().toString())
                .issuedAt(new Date()) // Marks when token was created
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 10)) // jwt is valid until
                .signWith(getSecretKey()) // takes my secret key and uses hmacShaKey and produce the signature
                .compact(); //header.payload.signature
    }
    public String generateRefreshToken(User user){
        return Jwts.builder()
                .subject(String.valueOf(user.getId())) // who is the token for // 12->"12"
                .issuedAt(new Date()) // Marks when token was created
                .expiration(new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 30 * 6)) // jwt is valid until
                .signWith(getSecretKey()) // takes my secret key and uses hmacShaKey and produce the signature
                .compact(); //header.payload.signature
    }
    public Long getUserIdFromToken(String token) {//The method extracts the user ID from a JWT.
        Claims claims = Jwts.parser() // “I’m ready to read a token.”
                .verifyWith(getSecretKey()) //Only tokens signed with your secret are valid.
                .build()//Finalizes the parser configuration. Now it’s ready to read a token.
                .parseSignedClaims(token) // header/payload/signature
                .getPayload();

        return Long.valueOf(claims.getSubject()); //"12" -> 12
    }
}
