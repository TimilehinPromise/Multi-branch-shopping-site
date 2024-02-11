package com.valuemart.shop.config.security.oauth2;


import com.valuemart.shop.config.security.CustomUserDetails;
import com.valuemart.shop.domain.service.abstracts.UserService;
import com.valuemart.shop.persistence.entity.Role;
import com.valuemart.shop.persistence.entity.User;
import com.valuemart.shop.persistence.repository.RoleRepository;
import com.valuemart.shop.persistence.repository.UserRepository;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserService userService;

    private final RoleRepository roleRepository;
    private final List<OAuth2UserInfoExtractor> oAuth2UserInfoExtractors;

    public CustomOAuth2UserService(UserService userService, RoleRepository roleRepository, List<OAuth2UserInfoExtractor> oAuth2UserInfoExtractors) {
        this.userService = userService;
        this.roleRepository = roleRepository;
        this.oAuth2UserInfoExtractors = oAuth2UserInfoExtractors;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        Optional<OAuth2UserInfoExtractor> extractorOptional = oAuth2UserInfoExtractors.stream()
                .filter(extractor -> extractor.accepts(userRequest))
                .findFirst();

        if (!extractorOptional.isPresent()) {
            throw new InternalAuthenticationServiceException("The OAuth2 provider is not supported yet");
        }

        User user = extractorOptional.get().extractUserInfo(oAuth2User);
        user = upsertUser(user);

        CustomUserDetails customUserDetails = new CustomUserDetails(user, oAuth2User.getAttributes());

        return (OAuth2User) customUserDetails;
    }



    private User upsertUser(User user) {
        // Find the user by email
        Optional<User> userOptional = userService.getUserByEmail(user.getEmail());
        Role role = roleRepository.findFirstByName("CUSTOMER"); // Ensure this role exists in your system
        if (userOptional.isEmpty()) {
            user.setRole(role);
            user.setEnabled(true); // Optionally auto-enable users registered via OAuth2
        } else {
            User existingUser = userOptional.get();
            existingUser.setEmail(user.getEmail());
            // No need to update username since it's not used
            user = existingUser;
        }
        return userService.saveUser(user);
    }

}
