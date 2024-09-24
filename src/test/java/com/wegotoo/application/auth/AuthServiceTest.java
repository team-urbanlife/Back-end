package com.wegotoo.application.auth;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

import com.wegotoo.application.ServiceTestSupport;
import com.wegotoo.application.auth.response.TokenResponse;
import com.wegotoo.domain.user.User;
import com.wegotoo.domain.user.repository.UserRepository;
import com.wegotoo.exception.BusinessException;
import com.wegotoo.infra.security.jwt.provider.JwtTokenProvider;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

public class AuthServiceTest extends ServiceTestSupport {

    @MockBean
    JwtTokenProvider tokenProvider;

    @Autowired
    UserRepository userRepository;

    @Autowired
    AuthService authService;

    @AfterEach
    void tearDown() {
        userRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("사용자가 로그아웃을 진행한다.")
    public void logout() {
        // given
        User user = userRepository.save(createUser("userA"));

        // when
        authService.logout(user.getId());

        // then
        User result = userRepository.findById(user.getId()).get();
        assertThat(result.getRefreshToken()).isNull();
    }

    @Test
    @DisplayName("존재하지 않는 사용자가 로그아웃을 요청한다.")
    public void logoutWithNotExistUser() {
        // given
        Long invalidUserId = 1L;

        // when // then
        assertThatThrownBy(() -> authService.logout(invalidUserId))
                .isInstanceOf(BusinessException.class)
                .hasMessage("사용자를 찾을 수 없습니다.");
    }

    @Test
    @DisplayName("토큰을 검증하고 재발급 받는다.")
    public void validateAndReissueToken() {
        // given
        User user = userRepository.save(createUser("userA"));

        given(tokenProvider.createAccessToken(anyLong(), anyString(), any()))
                .willReturn("access_token_value");
        given(tokenProvider.createRefreshToken())
                .willReturn("new_refresh_token_value");

        // when
        TokenResponse result = authService.validateAndReissueToken(user.getRefreshToken());

        // then
        User findUser = userRepository.findById(user.getId()).get();
        assertThat(findUser.getRefreshToken()).isEqualTo("new_refresh_token_value");
        assertThat(result.getAccessToken()).isEqualTo("access_token_value");
        assertThat(result.getRefreshToken()).isEqualTo("new_refresh_token_value");
    }

    @Test
    @DisplayName("존재하지 않는 사용자가 토큰을 검증하고 재발급 받는다.")
    public void validateAndReissueTokenWithNotExistUser() {
        // when // then
        assertThatThrownBy(() -> authService.validateAndReissueToken("token"))
                .isInstanceOf(BusinessException.class)
                .hasMessage("사용자를 찾을 수 없습니다.");
    }

    private User createUser(String username) {
        return User.builder()
                .name(username)
                .email(username + "@test.com")
                .profileImage("https://profile_image.com/" + username)
                .latitude(1.1)
                .refreshToken("refresh_token_value")
                .build();
    }

}
