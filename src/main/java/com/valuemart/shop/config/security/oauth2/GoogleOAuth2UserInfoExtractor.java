package com.valuemart.shop.config.security.oauth2;


import com.valuemart.shop.config.security.CustomUserDetails;
import com.valuemart.shop.persistence.entity.User;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class GoogleOAuth2UserInfoExtractor implements OAuth2UserInfoExtractor {

    @Override
    public User extractUserInfo(OAuth2User oAuth2User) {
        User user = new User();
        user.setEmail(retrieveAttr("email", oAuth2User));
        return user;
    }

    @Override
    public boolean accepts(OAuth2UserRequest userRequest) {
        return "google".equalsIgnoreCase(userRequest.getClientRegistration().getRegistrationId());
    }

    private String retrieveAttr(String attr, OAuth2User oAuth2User) {
        Object attribute = oAuth2User.getAttributes().get(attr);
        return attribute == null ? "" : attribute.toString();
    }
}

