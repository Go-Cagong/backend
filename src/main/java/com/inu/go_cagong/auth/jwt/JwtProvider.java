package com.inu.go_cagong.auth.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.security.Key;
import java.util.Date;

@Component
public class JwtProvider {

    @Value("${jwt.secret}")
    private String secretKeyPlain;

    @Value("${jwt.access-token-expiration}")
    private long accessTokenExpiration;

    @Value("${jwt.refresh-token-expiration}")
    private long refreshTokenExpiration;

    private Key secretKey;

    @PostConstruct
    public void init() {
        // 문자열 secret → 암호화 Key로 변환
        this.secretKey = Keys.hmacShaKeyFor(secretKeyPlain.getBytes());
    }

    // ===========================
    //     JWT 발급
    // ===========================

    public String createAccessToken(Long userId, String email) {
        return createToken(userId, email, accessTokenExpiration);
    }

    public String createRefreshToken(Long userId, String email) {
        return createToken(userId, email, refreshTokenExpiration);
    }

    private String createToken(Long userId, String email, long expirationTime) {
        Date now = new Date();
        Date expire = new Date(now.getTime() + expirationTime);

        return Jwts.builder()
                .setSubject(String.valueOf(userId))
                .claim("email", email)
                .setIssuedAt(now)
                .setExpiration(expire)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    // ===========================
    //     JWT 검증
    // ===========================

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);
            return true;

        } catch (ExpiredJwtException e) {
            System.out.println("JWT 만료됨: " + e.getMessage());
        } catch (JwtException e) {
            System.out.println("JWT 검증 실패: " + e.getMessage());
        }
        return false;
    }

    // ===========================
    //     Claims 꺼내기
    // ===========================

    public Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public Long getUserId(String token) {
        return Long.valueOf(getClaims(token).getSubject());
    }

    public String getEmail(String token) {
        return getClaims(token).get("email", String.class);
    }
}
