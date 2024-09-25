package com.wegotoo.infra.security.redirect.staregy;

import static com.wegotoo.infra.security.util.ServletUtils.addRefreshTokenCookieToResponse;
import static io.micrometer.common.util.StringUtils.isBlank;

import com.wegotoo.infra.security.util.UriUtils;
import java.net.URI;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class WebRedirectStrategy implements RedirectStrategy {

    @Override
    public boolean supports(String uri) {
        return !isBlank(uri) && UriUtils.isValid(uri) && !URI.create(uri).getPath().startsWith("/app");
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
