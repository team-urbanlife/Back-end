package com.wegotoo.api.auth;

import static com.wegotoo.infra.security.util.ServletUtils.addAuthorizationHeaderToResponse;
import static com.wegotoo.infra.security.util.ServletUtils.addAuthorizationRefreshHeaderToResponse;
import static com.wegotoo.infra.security.util.ServletUtils.addRefreshTokenCookieToResponse;
import static com.wegotoo.infra.security.util.ServletUtils.deleteRefreshTokenCookieToResponse;

import com.wegotoo.api.ApiResponse;
import com.wegotoo.application.auth.AuthService;
import com.wegotoo.application.auth.response.TokenResponse;
import com.wegotoo.infra.resolver.auth.Auth;
import com.wegotoo.infra.resolver.refresh.AppRefreshToken;
import com.wegotoo.infra.resolver.refresh.RefreshToken;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/v1/logout")
    public ApiResponse<Void> logout(@Auth Long userId) {
        authService.logout(userId);

        deleteRefreshTokenCookieToResponse();

        return ApiResponse.ok();
    }

    @PostMapping("/v1/app/logout")
    public ApiResponse<Void> logoutForApp(@Auth Long userId) {
        authService.logout(userId);

        return ApiResponse.ok();
    }


    @PostMapping("/v1/reissue")
    public ApiResponse<Void> reissueTokens(@RefreshToken String refreshToken) {
        TokenResponse token = authService.validateAndReissueToken(refreshToken);

        addAuthorizationHeaderToResponse(token.getAccessToken());
        addRefreshTokenCookieToResponse(token.getRefreshToken());

        return ApiResponse.ok();
    }

    @PostMapping("/v1/app/reissue")
    public ApiResponse<Void> reissueTokensForApp(@AppRefreshToken String refreshToken) {
        TokenResponse token = authService.validateAndReissueToken(refreshToken);

        addAuthorizationHeaderToResponse(token.getAccessToken());
        addAuthorizationRefreshHeaderToResponse(token.getRefreshToken());

        return ApiResponse.ok();
    }

}
