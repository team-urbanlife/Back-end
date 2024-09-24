package com.wegotoo.application.auth;

import static com.wegotoo.exception.ErrorCode.*;

import com.wegotoo.application.auth.response.TokenResponse;
import com.wegotoo.domain.user.User;
import com.wegotoo.domain.user.repository.UserRepository;
import com.wegotoo.exception.BusinessException;
import com.wegotoo.exception.ErrorCode;
import com.wegotoo.infra.security.jwt.provider.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthService {

    private final JwtTokenProvider tokenProvider;
    private final UserRepository userRepository;

    @Transactional
    public void logout(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(USER_NOT_FOUND));

        user.deleteRefreshToken();
    }

    @Transactional
    public TokenResponse validateAndReissueToken(String token) {
        User user = userRepository.findByRefreshToken(token)
                .orElseThrow(() -> new BusinessException(USER_NOT_FOUND));

        String accessToken = tokenProvider.createAccessToken(user.getId(), user.getEmail(), user.getRole());
        String refreshToken = tokenProvider.createRefreshToken();

        user.updateRefreshToken(refreshToken);

        return TokenResponse.of(accessToken, refreshToken);
    }

}
