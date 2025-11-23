package com.inu.go_cagong.auth.controller;


import com.inu.go_cagong.auth.jwt.CookieUtil;
import com.inu.go_cagong.auth.jwt.JwtProvider;
import com.inu.go_cagong.auth.entity.User;
import com.inu.go_cagong.auth.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;

    // ===============================
    //   로그인된 유저 정보 조회
    // ===============================
    @GetMapping("/me")
    public Object me(Authentication authentication) {
        if (authentication == null) {
            return "UNAUTHORIZED";
        }

        String email = authentication.getName();
        return userRepository.findByEmail(email);
    }

    // ===============================
    //   리프레시 토큰 → Access 토큰 재발급
    // ===============================
    @PostMapping("/refresh")
    public Object refresh(HttpServletRequest request, HttpServletResponse response) {

        String refreshToken = getCookieValue(request, "refresh_token");
        if (refreshToken == null || !jwtProvider.validateToken(refreshToken))
            return "INVALID_REFRESH_TOKEN";

        Long userId = jwtProvider.getUserId(refreshToken);
        String email = jwtProvider.getEmail(refreshToken);

        // 새 Access Token 생성
        String newAccess = jwtProvider.createAccessToken(userId, email);

        CookieUtil.addCookie(response, "access_token", newAccess, 60 * 30);

        return newAccess;
    }

    // ===============================
    //   로그아웃 (쿠키 삭제)
    // ===============================
    @PostMapping("/logout")
    public Object logout(HttpServletResponse response) {
        CookieUtil.deleteCookie(response, "access_token");
        CookieUtil.deleteCookie(response, "refresh_token");
        return "LOGOUT_SUCCESS";
    }

    // 쿠키 읽기
    private String getCookieValue(HttpServletRequest request, String name) {
        if (request.getCookies() == null) return null;
        for (Cookie cookie : request.getCookies()) {
            if (name.equals(cookie.getName())) return cookie.getValue();
        }
        return null;
    }
}
