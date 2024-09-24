package com.wegotoo.infra.security.redirect.staregy;

import static com.wegotoo.infra.security.util.ServletUtils.addRefreshTokenCookieToResponse;
import static io.micrometer.common.util.StringUtils.isBlank;

import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class DefaultRedirectStrategy implements RedirectStrategy {

    @Override
    public boolean supports(String uri) {
        return isBlank(uri);
    }

    @Override
    public String buildRedirectUri(String uri, String accessToken, String refreshToken) {
        addRefreshTokenCookieToResponse(refreshToken);

        return UriComponentsBuilder.fromUriString(uri)
                .queryParam("token", accessToken)
                .build()
                .toUriString();
    }

}
