package com.valuemart.shop.config.security.oauth2;


import com.valuemart.shop.config.security.CustomUserDetails;
import com.valuemart.shop.persistence.entity.User;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;

public interface OAuth2UserInfoExtractor {

    User extractUserInfo(OAuth2User oAuth2User);

    boolean accepts(OAuth2UserRequest userRequest);
}
