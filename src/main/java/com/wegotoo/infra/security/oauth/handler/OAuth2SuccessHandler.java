package com.wegotoo.infra.security.oauth.handler;

import static com.wegotoo.infra.security.oauth.HttpCookieOAuth2AuthorizationRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME;

import com.wegotoo.domain.user.User;
import com.wegotoo.domain.user.repository.UserRepository;
import com.wegotoo.exception.BusinessException;
import com.wegotoo.exception.ErrorCode;
import com.wegotoo.infra.security.jwt.provider.JwtTokenProvider;
import com.wegotoo.infra.security.oauth.HttpCookieOAuth2AuthorizationRequestRepository;
import com.wegotoo.infra.security.user.CustomOAuth2User;
import com.wegotoo.infra.security.util.CookieUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final UserRepository userRepository;
    private final JwtTokenProvider tokenProvider;
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

        String targetUri = extractTargetUri(request, accessToken);

        clearAuthenticationAttribute(request, response);
        addRefreshTokenForCookie(response, refreshToken);
        getRedirectStrategy().sendRedirect(request, response, targetUri);
    }

    protected String extractTargetUri(HttpServletRequest request, String accessToken) {
        return createTargetUri(findRedirectUri(request), accessToken);
    }

    private String createTargetUri(Optional<String> redirectUri, String accessToken) {
        return UriComponentsBuilder.fromUriString(redirectUri.orElse(getDefaultTargetUrl()))
                .queryParam("token", accessToken)
                .build()
                .toUriString();
    }

    private Optional<String> findRedirectUri(HttpServletRequest request) {
        return CookieUtils.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)
                .map(Cookie::getValue);
    }

    private void clearAuthenticationAttribute(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        authorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
    }

    private void addRefreshTokenForCookie(HttpServletResponse response, String token) {
        CookieUtils.addToken(response, token);
    }

}
