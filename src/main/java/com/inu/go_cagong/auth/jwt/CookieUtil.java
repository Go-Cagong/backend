package com.inu.go_cagong.auth.jwt;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

public class CookieUtil {

    // ===========================
    //   쿠키 생성
    // ===========================
    public static void addCookie(HttpServletResponse response, String name, String value, int maxAge) {
        Cookie cookie = new Cookie(name, value);

        cookie.setPath("/");           // 모든 경로에서 쿠키 접근 가능
        cookie.setHttpOnly(true);      // JS에서 접근 불가 (XSS 방지)
        cookie.setMaxAge(maxAge);      // 쿠키 유효시간 (초 단위)

        // HTTPS 환경이면 true 권장
        // cookie.setSecure(true);

        response.addCookie(cookie);
    }

    // ===========================
    //   쿠키 삭제 (로그아웃)
    // ===========================
    public static void deleteCookie(HttpServletResponse response, String name) {
        Cookie cookie = new Cookie(name, null);

        cookie.setPath("/");
        cookie.setMaxAge(0);      // 즉시 만료
        cookie.setHttpOnly(true);

        response.addCookie(cookie);
    }
}

