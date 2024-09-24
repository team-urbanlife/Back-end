package com.wegotoo.infra.security.redirect.staregy;

import static io.micrometer.common.util.StringUtils.isBlank;

import java.net.URI;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class AppRedirectStrategy implements RedirectStrategy {

    @Override
    public boolean supports(String uri) {
        return !isBlank(uri) && URI.create(uri).getPath().startsWith("/app");
    }

    @Override
    public String buildRedirectUri(String uri, String accessToken, String refreshToken) {
        return UriComponentsBuilder.fromUriString(uri)
                .queryParam("access_token", accessToken)
                .queryParam("refresh_token", refreshToken)
                .build()
                .toUriString();
    }

}
