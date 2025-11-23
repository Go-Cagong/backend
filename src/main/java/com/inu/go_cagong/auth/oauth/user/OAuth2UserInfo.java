package com.inu.go_cagong.auth.oauth.user;

import java.util.Map;

public abstract class OAuth2UserInfo {

    protected Map<String, Object> attributes;
    protected String provider;

    public OAuth2UserInfo(Map<String, Object> attributes, String provider) {
        this.attributes = attributes;
        this.provider = provider;
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public String getProvider() {
        return provider;
    }

    public abstract String getId();

    public abstract String getName();

    public abstract String getEmail();

    public abstract String getImageUrl();
}
