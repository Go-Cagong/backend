package com.inu.go_cagong.auth.oauth.service;

import com.inu.go_cagong.auth.oauth.user.CustomOAuth2User;
import com.inu.go_cagong.auth.oauth.user.OAuth2UserInfo;
import com.inu.go_cagong.auth.oauth.user.OAuth2UserInfoFactory;
import com.inu.go_cagong.auth.entity.User;
import com.inu.go_cagong.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);

        String registrationId = oAuth2UserRequest.getClientRegistration().getRegistrationId();
        Map<String, Object> attributes = oAuth2User.getAttributes();

        OAuth2UserInfo userInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(registrationId, attributes);

        if (userInfo.getEmail() == null) {
            throw new OAuth2AuthenticationException("Email not found from OAuth2 provider");
        }

        // DB 저장 or 조회
        User user = saveOrUpdateUser(userInfo, registrationId);

        return new CustomOAuth2User(user, attributes, userInfo);
    }

    private User saveOrUpdateUser(OAuth2UserInfo userInfo, String provider) {

        return userRepository.findByEmail(userInfo.getEmail())
                .map(existingUser -> existingUser) // 이미 있으면 그대로 사용
                .orElseGet(() -> { // 없으면 새로 저장
                    User newUser = User.builder()
                            .email(userInfo.getEmail())
                            .name(userInfo.getName())
                            .provider(provider)
                            .role("USER")
                            .build();
                    return userRepository.save(newUser);
                });
    }

}
