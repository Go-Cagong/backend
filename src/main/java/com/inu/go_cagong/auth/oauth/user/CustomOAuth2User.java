package com.inu.go_cagong.auth.oauth.user;

import com.inu.go_cagong.auth.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

public class CustomOAuth2User implements OAuth2User, UserDetails {

    private final User user;   // DB User 엔티티
    private final Map<String, Object> attributes;  // provider raw attributes
    private final OAuth2UserInfo userInfo; // 파싱한 정보

    public CustomOAuth2User(User user,
                            Map<String, Object> attributes,
                            OAuth2UserInfo userInfo) {
        this.user = user;
        this.attributes = attributes;
        this.userInfo = userInfo;
    }

    public User getUser() {
        return user;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getName() {
        return user.getName();
    }

    public String getEmail() {
        return user.getEmail();
    }

    public String getProvider() {
        return user.getProvider();
    }

    @Override
    public String getPassword() {
        return null; // 소셜 로그인은 패스워드 없음
    }

    @Override
    public String getUsername() {
        return user.getEmail(); // Spring Security 내부 아이덴티티로 이메일 사용
    }

    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }
}
