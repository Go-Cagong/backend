package com.inu.go_cagong.config;

import com.inu.go_cagong.auth.jwt.JwtAuthenticationFilter;
import com.inu.go_cagong.auth.jwt.JwtProvider;
import com.inu.go_cagong.auth.oauth.handler.OAuth2SuccessHandler;
import com.inu.go_cagong.auth.oauth.service.CustomOAuth2UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtProvider jwtProvider;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        JwtAuthenticationFilter jwtFilter = new JwtAuthenticationFilter(jwtProvider);

        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> {}) // WebConfig에서 설정

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/",
                                "/oauth2/**",
                                "/api/auth/refresh",
                                "/api/auth/logout",
                                "/api/**",
                                "/admin/**",
                                "/cafe-api-test.html"
                        ).permitAll()
                        .anyRequest().authenticated()
                )

                // OAuth2 로그인 설정
                .oauth2Login(oauth -> oauth
                        .userInfoEndpoint(config -> config.userService(customOAuth2UserService))
                        .successHandler(oAuth2SuccessHandler)
                )

                // 로그아웃 설정 추가
                .logout(logout -> logout
                        .logoutUrl("/api/auth/logout") // 로그아웃을 처리할 URL
                        .invalidateHttpSession(true)   // 서버 세션 무효화
                        .deleteCookies("JSESSIONID", "access_token", "refresh_token") // [핵심] 이 쿠키들을 삭제하라고 브라우저에 명령
                        .logoutSuccessHandler((request, response, authentication) -> {
                            // 로그아웃 성공 시 JSON 응답 반환 (리다이렉트 X)
                            response.setStatus(HttpServletResponse.SC_OK);
                            response.setCharacterEncoding("UTF-8");
                            response.setContentType("application/json");
                            response.getWriter().write("{\"message\": \"로그아웃 되었습니다.\"}");
                        })
                )

                // JWT 인증 필터 추가 (UsernamePasswordAuthenticationFilter 전에 실행)
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
