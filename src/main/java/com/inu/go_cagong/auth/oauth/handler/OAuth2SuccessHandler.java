package com.inu.go_cagong.auth.oauth.handler;

import com.inu.go_cagong.auth.jwt.CookieUtil;
import com.inu.go_cagong.auth.jwt.JwtProvider;
import com.inu.go_cagong.auth.oauth.user.CustomOAuth2User;
import com.inu.go_cagong.auth.entity.User;
import com.inu.go_cagong.auth.repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

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

        // JWT 발급
        String accessToken = jwtProvider.createAccessToken(user.getId(), user.getEmail());
        String refreshToken = jwtProvider.createRefreshToken(user.getId(), user.getEmail());

        // 쿠키 저장 (60초 = 테스트, 나중에 바꿔도 됨)
        //CookieUtil.addCookie(response, "access_token", accessToken, 60 * 30);
        //CookieUtil.addCookie(response, "refresh_token", refreshToken, 60 * 60 * 24 * 7);

        // 로그인 후 리다이렉트 경로
        //response.sendRedirect("/");

        // 주소를 안전하게 만듭니다 (cagong://login-success?access_token=...)
        String targetUrl = UriComponentsBuilder.fromUriString("cagong://login-success") // 앱을 깨우는 주문
                .queryParam("access_token", accessToken) // 토큰을 짐으로 실음
                .queryParam("refresh_token", refreshToken)
                .build().encode(StandardCharsets.UTF_8).toUriString();

// 만들어진 주소로 쏘세요! -> 앱이 켜집니다.
        response.sendRedirect(targetUrl);
    }
}
