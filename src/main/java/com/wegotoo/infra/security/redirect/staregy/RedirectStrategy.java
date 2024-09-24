package com.wegotoo.infra.security.redirect.staregy;

public interface RedirectStrategy {

    boolean supports(String uri);
    String buildRedirectUri(String uri, String accessToken, String refreshToken);

}
