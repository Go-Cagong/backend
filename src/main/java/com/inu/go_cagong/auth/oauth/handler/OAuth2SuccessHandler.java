package com.inu.go_cagong.auth.oauth.handler;

import com.inu.go_cagong.auth.jwt.JwtProvider;
import com.inu.go_cagong.auth.oauth.user.CustomOAuth2User;
import com.inu.go_cagong.auth.entity.User;
import com.inu.go_cagong.auth.repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws IOException, ServletException {

        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();

        String email = oAuth2User.getEmail();
        User user = userRepository.findByEmail(email).orElseThrow();

        // 1. 토큰 발급
        String accessToken = jwtProvider.createAccessToken(user.getId(), user.getEmail());
        String refreshToken = jwtProvider.createRefreshToken(user.getId(), user.getEmail());

        // 2. 요청 출처 확인 (HTML에서 심어준 'login_from' 쿠키 확인)
        String loginFrom = "app"; // 기본값은 앱으로 설정
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("login_from".equals(cookie.getName()) && "web".equals(cookie.getValue())) {
                    loginFrom = "web";
                    log.info("Login Request Detected from: WEB");

                    // 사용한 확인용 쿠키는 삭제해줍니다.
                    Cookie removeCookie = new Cookie("login_from", null);
                    removeCookie.setMaxAge(0);
                    removeCookie.setPath("/");
                    response.addCookie(removeCookie);
                    break;
                }
            }
        }

        // 3. 분기 처리
        if ("web".equals(loginFrom)) {
            handleWebLogin(response, accessToken, refreshToken);
        } else {
            handleAppLogin(request, response, accessToken, refreshToken);
        }
    }

    // [웹 로그인 처리] 쿠키 직접 생성 (Secure, Path 설정 필수)
    private void handleWebLogin(HttpServletResponse response, String accessToken, String refreshToken) throws IOException {
        log.info("Web Login Handling: Setting Secure Cookies");

        // 1. Access Token 쿠키
        Cookie accessCookie = new Cookie("access_token", accessToken);
        accessCookie.setPath("/");             // 모든 경로에서 접근 가능
        accessCookie.setHttpOnly(true);        // 자바스크립트 접근 차단 (보안)
        accessCookie.setSecure(true);          // HTTPS 전송 전용 (필수!)
        accessCookie.setMaxAge(60 * 30);       // 30분
        response.addCookie(accessCookie);

        // 2. Refresh Token 쿠키
        Cookie refreshCookie = new Cookie("refresh_token", refreshToken);
        refreshCookie.setPath("/");
        refreshCookie.setHttpOnly(true);
        refreshCookie.setSecure(true);         // HTTPS 전송 전용 (필수!)
        refreshCookie.setMaxAge(60 * 60 * 24 * 7); // 7일
        response.addCookie(refreshCookie);

        // 웹 테스트 페이지로 리다이렉트
        response.sendRedirect("https://go-cagong.ddns.net/cafe-api-test.html");
    }

    // [앱 로그인 처리] 딥링크로 이동
    private void handleAppLogin(HttpServletRequest request, HttpServletResponse response, String accessToken, String refreshToken) throws IOException {
        log.info("App Login Handling: Redirecting to cagong://");

        String targetUrl = UriComponentsBuilder.fromUriString("cagong://login-success")
                .queryParam("access_token", accessToken)
                .queryParam("refresh_token", refreshToken)
                .build().encode(StandardCharsets.UTF_8).toUriString();

        response.sendRedirect(targetUrl);
    }
}