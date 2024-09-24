package com.wegotoo.infra.security.redirect;

import static com.wegotoo.infra.security.oauth.HttpCookieOAuth2AuthorizationRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME;

import com.wegotoo.infra.security.redirect.staregy.DefaultRedirectStrategy;
import com.wegotoo.infra.security.redirect.staregy.RedirectStrategy;
import com.wegotoo.infra.security.util.CookieUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedirectResponseBuilder {

    private final DefaultRedirectStrategy defaultRedirectStrategy;
    private final List<RedirectStrategy> redirectUriStrategies;

    public String buildRedirectUri(HttpServletRequest request, String accessToken, String refreshToken) {
        String cookieRedirectUri = findRedirectUri(request);

        return redirectUriStrategies.stream()
                .filter(redirectStrategy -> redirectStrategy.supports(cookieRedirectUri))
                .findFirst()
                .orElse(defaultRedirectStrategy)
                .buildRedirectUri(cookieRedirectUri, accessToken, refreshToken);
    }

    private String findRedirectUri(HttpServletRequest request) {
        return CookieUtils.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)
                .map(Cookie::getValue)
                .orElse("");
    }

}
