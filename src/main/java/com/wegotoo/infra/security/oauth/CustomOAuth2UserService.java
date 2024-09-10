package com.wegotoo.infra.security.oauth;

import com.wegotoo.domain.user.User;
import com.wegotoo.domain.user.repository.UserRepository;
import com.wegotoo.infra.security.user.CustomOAuth2User;
import com.wegotoo.infra.security.user.OAuthAttribute;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        final OAuth2User oAuth2User = super.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String usernameAttribute = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint()
                .getUserNameAttributeName();

        OAuthAttribute attribute = OAuthAttribute.of(registrationId, usernameAttribute, oAuth2User.getAttributes());

        User user = userRepository.findByEmail(attribute.getEmail())
                .map(userEntity -> userEntity.updateNameAndProfileImage(attribute.getName(),
                        attribute.getProfileImage()))
                .orElse(attribute.toEntity());

        userRepository.save(user);

        return CustomOAuth2User.of(usernameAttribute, user, oAuth2User.getAttributes());
    }

}
