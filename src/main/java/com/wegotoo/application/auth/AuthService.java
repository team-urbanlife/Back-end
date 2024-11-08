package com.wegotoo.application.auth;

import static com.wegotoo.exception.ErrorCode.*;

import com.wegotoo.application.auth.response.TokenResponse;
import com.wegotoo.domain.user.RefreshToken;
import com.wegotoo.domain.user.User;
import com.wegotoo.domain.user.repository.RefreshTokenRepository;
import com.wegotoo.domain.user.repository.UserRepository;
import com.wegotoo.exception.BusinessException;
import com.wegotoo.infra.security.jwt.provider.JwtTokenProvider;
import com.wegotoo.infra.security.jwt.validator.JwtTokenValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthService {

    private final JwtTokenProvider tokenProvider;
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public void logout(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(USER_NOT_FOUND));

        RefreshToken refreshToken = refreshTokenRepository.findById(user.getId())
                .orElseThrow(() -> new BusinessException(NOT_FOUND_REFRESH_TOKEN));

        refreshTokenRepository.delete(refreshToken);
    }

    @Transactional
    public TokenResponse reissueToken(String token) {
        Long subId = tokenProvider.extractSubId(token)
                .orElseThrow(() -> new BusinessException(INVALID_REFRESH_TOKEN));

        RefreshToken refreshToken = refreshTokenRepository.findById(subId)
                .orElseThrow(() -> new BusinessException(NOT_FOUND_REFRESH_TOKEN));

        User user = userRepository.findById(refreshToken.getId())
                .orElseThrow(() -> new BusinessException(USER_NOT_FOUND));

        String newAccessToken = tokenProvider.createAccessToken(user.getId(), user.getEmail(), user.getRole());
        String newRefreshToken = tokenProvider.createRefreshToken(user.getId());

        refreshToken.updateToken(newRefreshToken);
        refreshTokenRepository.save(refreshToken);

        return TokenResponse.of(newAccessToken, newRefreshToken);
    }

}
