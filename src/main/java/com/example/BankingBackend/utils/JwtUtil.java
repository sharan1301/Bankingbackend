//package com.example.BankingBackend.utils;
//
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.JwtException;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.SignatureAlgorithm;
//import io.jsonwebtoken.security.Keys;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.stereotype.Component;
//
//import java.nio.charset.StandardCharsets;
//import java.security.Key;
//import java.util.Date;
//
//@Component
//public class JwtUtil {
//    private final String SECRET = "Our OFSS Training project is Banking@";
//    private final long EXPIRATION = 1000 * 60*60;
//    private final Key secretkey = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
//
//    public String generateTokenWithRole(String email, String role) {
//        return Jwts.builder()
//                .setSubject(email)
//                .claim("role", role)
//                .setIssuedAt(new Date(System.currentTimeMillis()))
//                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
//                .signWith(secretkey, SignatureAlgorithm.HS256)
//                .compact();
//
//    }
//
//    public String extractEmail(String token) {
//        return extractAllClaims(token).getSubject();
//    }
//
//    public String extractRole(String token) {
//        return extractAllClaims(token).get("role", String.class);
//    }
//
//    public boolean validateJwtToken(String token) {
//        try {
//            extractAllClaims(token);
//            return true;
//        } catch (JwtException exception) {
//            return false;
//        }
//    }
//
//    private Claims extractAllClaims(String token) {
//        return Jwts.parserBuilder()
//                .setSigningKey(secretkey)
//                .build()
//                .parseClaimsJws(token)
//                .getBody();
//
//
//    }
//}
