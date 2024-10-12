package com.wegotoo.application.user.response;

import com.wegotoo.domain.user.User;
import com.wegotoo.domain.user.repository.UserRepository;
import lombok.Builder;
import lombok.Getter;

@Getter
public class UserResponse {

    private Long userId;
    private String username;
    private String userProfileImage;

    @Builder
    private UserResponse(Long userId, String username, String userProfileImage) {
        this.userId = userId;
        this.username = username;
        this.userProfileImage = userProfileImage;
    }

    public static UserResponse of(User user) {
        return UserResponse.builder()
                .userId(user.getId())
                .username(user.getName())
                .userProfileImage(user.getProfileImage())
                .build();
    }

}
