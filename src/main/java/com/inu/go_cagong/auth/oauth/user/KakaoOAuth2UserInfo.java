package com.inu.go_cagong.auth.oauth.user;

import java.util.Map;

public class KakaoOAuth2UserInfo extends OAuth2UserInfo {

    public KakaoOAuth2UserInfo(Map<String, Object> attributes) {
        super(attributes, "kakao");
    }

    @Override
    public String getId() {
        return String.valueOf(attributes.get("id"));
    }

    @Override
    public String getName() {
        Map<String, Object> profile = (Map<String, Object>) attributes.get("profile");
        if (profile == null) return null;

        return (String) profile.get("nickname");
    }

    @Override
    public String getEmail() {
        return (String) attributes.get("email");
    }

    @Override
    public String getImageUrl() {
        Map<String, Object> profile = (Map<String, Object>) attributes.get("profile");
        if (profile == null) return null;

        return (String) profile.get("profile_image_url");
    }
}
