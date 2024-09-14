package com.wegotoo.api.auth;

import com.wegotoo.api.ApiResponse;
import com.wegotoo.application.auth.AuthService;
import com.wegotoo.application.auth.response.TokenResponse;
import com.wegotoo.infra.resolver.refresh.RefreshToken;
import com.wegotoo.infra.security.util.ServletUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/reissue")
    public ApiResponse<Void> reissueTokens(@RefreshToken String refreshToken) {
        TokenResponse token = authService.validateAndReissueToken(refreshToken);

        ServletUtils.addAuthorizationHeaderToResponse(token.getAccessToken());
        ServletUtils.addRefreshTokenCookieToResponse(token.getRefreshToken());

        return ApiResponse.ok();
    }

}
