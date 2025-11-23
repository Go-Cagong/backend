package com.inu.go_cagong.auth.oauth.user;

import java.util.Map;

public class OAuth2UserInfoFactory {

    public static OAuth2UserInfo getOAuth2UserInfo(String provider, Map<String, Object> attributes) {

        if (provider.equals("kakao")) {
            return new KakaoOAuth2UserInfo(attributes);
        }

        if (provider.equals("google")) {
            return new GoogleOAuth2UserInfo(attributes);
        }

        if (provider.equals("naver")) {
            return new NaverOAuth2UserInfo(attributes);
        }

        throw new IllegalArgumentException("지원하지 않는 provider: " + provider);
    }
}
