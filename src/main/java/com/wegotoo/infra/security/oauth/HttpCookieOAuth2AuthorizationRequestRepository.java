package com.wegotoo.infra.security.oauth;

import static com.wegotoo.infra.security.util.CookieUtils.addCookie;
import static com.wegotoo.infra.security.util.CookieUtils.deleteCookie;
import static com.wegotoo.infra.security.util.CookieUtils.deserialize;
import static com.wegotoo.infra.security.util.CookieUtils.getCookie;
import static com.wegotoo.infra.security.util.CookieUtils.serialize;
import static io.micrometer.common.util.StringUtils.isBlank;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.stereotype.Component;

@Component
public class HttpCookieOAuth2AuthorizationRequestRepository implements
        AuthorizationRequestRepository<OAuth2AuthorizationRequest> {

    public static final String OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME = "oauth2_auth_request";
    public static final String REDIRECT_URI_PARAM_COOKIE_NAME = "redirect_uri";
    private static final int COOKIE_EXPIRE_SECONDS = 180;

    @Override
    public OAuth2AuthorizationRequest loadAuthorizationRequest(HttpServletRequest request) {
        return getCookie(request, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME)
                .map(cookie -> deserialize(cookie, OAuth2AuthorizationRequest.class))
                .orElse(null);
    }

    @Override
    public void saveAuthorizationRequest(OAuth2AuthorizationRequest authorizationRequest, HttpServletRequest request,
                                         HttpServletResponse response) {
        saveOAuth2AuthorizationRequestCookie(authorizationRequest, response);
        saveRedirectUriCookie(request, response);
    }

    @Override
    public OAuth2AuthorizationRequest removeAuthorizationRequest(HttpServletRequest request,
                                                                 HttpServletResponse response) {
        return this.loadAuthorizationRequest(request);
    }

    public void removeAuthorizationRequestCookies(HttpServletRequest request, HttpServletResponse response) {
        deleteCookie(request, response, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME);
        deleteCookie(request, response, REDIRECT_URI_PARAM_COOKIE_NAME);
    }

    private void saveOAuth2AuthorizationRequestCookie(OAuth2AuthorizationRequest authorizationRequest,
                                                      HttpServletResponse response) {
        addCookie(response, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME, serialize(authorizationRequest),
                COOKIE_EXPIRE_SECONDS);
    }

    private void saveRedirectUriCookie(HttpServletRequest request, HttpServletResponse response) {
        String redirectUri = request.getParameter(REDIRECT_URI_PARAM_COOKIE_NAME);

        if (isBlank(redirectUri)) {
            redirectUri = request.getHeader("Referer");
        }

        addCookie(response, REDIRECT_URI_PARAM_COOKIE_NAME, redirectUri, COOKIE_EXPIRE_SECONDS);
    }

}
