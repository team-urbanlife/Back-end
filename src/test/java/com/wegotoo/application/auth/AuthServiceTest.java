package com.wegotoo.application.auth;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

import com.wegotoo.application.ServiceTestSupport;
import com.wegotoo.application.auth.response.TokenResponse;
import com.wegotoo.domain.user.RefreshToken;
import com.wegotoo.domain.user.User;
import com.wegotoo.domain.user.repository.RefreshTokenRepository;
import com.wegotoo.domain.user.repository.UserRepository;
import com.wegotoo.exception.BusinessException;
import com.wegotoo.infra.security.jwt.provider.JwtTokenProvider;
import java.util.Optional;
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
    RefreshTokenRepository refreshTokenRepository;

    @Autowired
    AuthService authService;

    @AfterEach
    void tearDown() {
        refreshTokenRepository.deleteAll();
        userRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("사용자가 로그아웃을 진행한다.")
    public void logout() {
        // given
        User user = userRepository.save(createUser("userA"));
        refreshTokenRepository.save(createRefreshToken(user.getId(), "XXX.XXX.XXX"));

        // when
        authService.logout(user.getId());

        // then
        Optional<RefreshToken> result = refreshTokenRepository.findById(user.getId());
        assertThat(result).isEmpty();
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
    @DisplayName("존재하지 않는 리프레시 토큰으로 로그아웃을 요청한다.")
    public void logoutWithNotFoundRefreshToken() {
        // given
        User user = userRepository.save(createUser("userA"));

        // when // then
        assertThatThrownBy(() -> authService.logout(user.getId()))
                .isInstanceOf(BusinessException.class)
                .hasMessage("리프레시 토큰을 찾을 수 없습니다.");
    }

    @Test
    @DisplayName("토큰을 검증하고 재발급 받는다.")
    public void reissueToken() {
        // given
        User user = userRepository.save(createUser("userA"));
        RefreshToken refreshToken = refreshTokenRepository.save(
                createRefreshToken(user.getId(), "refresh_token_value"));

        given(tokenProvider.extractSubId(anyString()))
                .willReturn(Optional.of(user.getId()));
        given(tokenProvider.createAccessToken(anyLong(), anyString(), any()))
                .willReturn("access_token_value");
        given(tokenProvider.createRefreshToken(anyLong()))
                .willReturn("new_refresh_token_value");

        // when
        TokenResponse result = authService.reissueToken(refreshToken.getToken());

        // then
        RefreshToken findToken = refreshTokenRepository.findById(user.getId()).get();
        assertThat(findToken.getToken()).isEqualTo("new_refresh_token_value");
        assertThat(result.getAccessToken()).isEqualTo("access_token_value");
        assertThat(result.getRefreshToken()).isEqualTo("new_refresh_token_value");
    }

    @Test
    @DisplayName("유효하지 않는 토큰으로 토큰을 재발급 받는다.")
    public void reissueTokenWithInvalidRefreshToken() {
        // given
        String invalidRefreshToken = "XXX.XXX.XXX";

        // when // then
        assertThatThrownBy(() -> authService.reissueToken(invalidRefreshToken))
                .isInstanceOf(BusinessException.class)
                .hasMessage("유효하지 않은 리프레시 토큰입니다.");
    }

    @Test
    @DisplayName("존재하지 않는 리프레시 토큰으로 재발급 받는다.")
    public void reissueTokenWithNotFoundToken() {
        // given
        String refreshToken = "XXX.XXX.XXX";

        given(tokenProvider.extractSubId(anyString()))
                .willReturn(Optional.of(1L));

        // when // then
        assertThatThrownBy(() -> authService.reissueToken(refreshToken))
                .isInstanceOf(BusinessException.class)
                .hasMessage("리프레시 토큰을 찾을 수 없습니다.");
    }

    @Test
    @DisplayName("존재하지 않는 사용자가 토큰을 재발급 받는다.")
    public void reissueTokenWithNotExistUser() {
        // given
        RefreshToken refreshToken = refreshTokenRepository.save(createRefreshToken(1L, "XXX.XXX.XXX"));

        given(tokenProvider.extractSubId(refreshToken.getToken()))
                .willReturn(Optional.of(1L));

        // when // then
        assertThatThrownBy(() -> authService.reissueToken(refreshToken.getToken()))
                .isInstanceOf(BusinessException.class)
                .hasMessage("사용자를 찾을 수 없습니다.");
    }

    private User createUser(String username) {
        return User.builder()
                .name(username)
                .email(username + "@test.com")
                .profileImage("https://profile_image.com/" + username)
                .latitude(1.1)
                .build();
    }

    private RefreshToken createRefreshToken(Long userId, String token) {
        return RefreshToken.create(userId, token);
    }

}
