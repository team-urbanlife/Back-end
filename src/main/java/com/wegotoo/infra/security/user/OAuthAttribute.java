package com.wegotoo.infra.security.user;

import com.wegotoo.domain.user.Role;
import com.wegotoo.domain.user.User;
import java.util.Map;
import lombok.Builder;
import lombok.Getter;

@Getter
public class OAuthAttribute {

    private String name;
    private String email;
    private String profileImage;
    private String nameAttributeKey;
    private Map<String, Object> attributes;

    @Builder
    private OAuthAttribute(String name, String email, String profileImage, String nameAttributeKey,
                           Map<String, Object> attributes) {
        this.name = name;
        this.email = email;
        this.profileImage = profileImage;
        this.nameAttributeKey = nameAttributeKey;
        this.attributes = attributes;
    }

    public static OAuthAttribute of(String registrationId, String usernameAttributeName,
                                    Map<String, Object> attributes) {
        return ofKakao(usernameAttributeName, attributes);
    }

    public static OAuthAttribute ofKakao(String usernameAttributeName, Map<String, Object> attributes) {
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> kakaoProfile = (Map<String, Object>) kakaoAccount.get("profile");

        return OAuthAttribute.builder()
                .name((String) kakaoProfile.get("nickname"))
                .email((String) kakaoAccount.get("email"))
                .profileImage((String) kakaoProfile.get("profile_image_url"))
                .nameAttributeKey(usernameAttributeName)
                .attributes(attributes)
                .build();
    }

    public User toEntity() {
        return User.builder()
                .name(name)
                .email(email)
                .profileImage(profileImage)
                .role(Role.USER)
                .build();
    }

}
