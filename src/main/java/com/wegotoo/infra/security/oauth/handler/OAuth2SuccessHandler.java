package com.wegotoo.infra.security.oauth.handler;

import com.wegotoo.domain.user.User;
import com.wegotoo.domain.user.repository.UserRepository;
import com.wegotoo.exception.BusinessException;
import com.wegotoo.exception.ErrorCode;
import com.wegotoo.infra.security.jwt.provider.JwtTokenProvider;
import com.wegotoo.infra.security.oauth.HttpCookieOAuth2AuthorizationRequestRepository;
import com.wegotoo.infra.security.user.CustomOAuth2User;
import com.wegotoo.infra.security.redirect.RedirectResponseBuilder;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final UserRepository userRepository;
    private final JwtTokenProvider tokenProvider;
    private final RedirectResponseBuilder redirectResponseBuilder;
    private final HttpCookieOAuth2AuthorizationRequestRepository authorizationRequestRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();

        String accessToken = tokenProvider.createAccessToken(oAuth2User.getId(), oAuth2User.getEmail(),
                oAuth2User.getRole());
        String refreshToken = tokenProvider.createRefreshToken();

        User user = userRepository.findById(oAuth2User.getId())
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        userRepository.save(user.updateRefreshToken(refreshToken));

        String targetUri = redirectResponseBuilder.buildRedirectUri(request, accessToken, refreshToken);

        clearAuthenticationAttribute(request, response);
        getRedirectStrategy().sendRedirect(request, response, targetUri);
    }

    private void clearAuthenticationAttribute(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        authorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
    }

}
