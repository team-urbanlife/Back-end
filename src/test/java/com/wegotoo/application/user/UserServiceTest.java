package com.wegotoo.application.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.wegotoo.application.ServiceTestSupport;
import com.wegotoo.application.user.response.UserResponse;
import com.wegotoo.domain.user.User;
import com.wegotoo.domain.user.repository.UserRepository;
import com.wegotoo.exception.BusinessException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class UserServiceTest extends ServiceTestSupport {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;

    @AfterEach
    void tearDown() {
        userRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("사용자 정보 조회")
    public void findUser() {
        // given
        User user = userRepository.save(createUser());

        // when
        UserResponse result = userService.findUser(user.getId());

        // then
        assertThat(result.getUserId()).isEqualTo(user.getId());
        assertThat(result.getUsername()).isEqualTo(user.getName());
        assertThat(result.getUserProfileImage()).isEqualTo(user.getProfileImage());
    }

    @Test
    @DisplayName("존재하지 않는 사용자 정보 조회")
    public void findNotFoundUser() {
        // given
        Long invalidUserId = 1L;

        // when // then
        assertThatThrownBy(() -> userService.findUser(invalidUserId))
                .isInstanceOf(BusinessException.class)
                .hasMessage("사용자를 찾을 수 없습니다.");
    }

    private User createUser() {
        return User.builder()
                .name("user")
                .email("user@gmail.com")
                .profileImage("http://user-profile.com")
                .build();
    }

}
