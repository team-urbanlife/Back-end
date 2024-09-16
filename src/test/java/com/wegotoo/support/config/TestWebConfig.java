package com.wegotoo.support.config;

import com.wegotoo.infra.resolver.auth.AuthArgumentResolver;
import com.wegotoo.infra.resolver.refresh.RefreshTokenArgumentResolver;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@TestConfiguration
@RequiredArgsConstructor
public class TestWebConfig implements WebMvcConfigurer {

    private final AuthArgumentResolver authArgumentResolver;
    private final RefreshTokenArgumentResolver refreshTokenArgumentResolver;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(authArgumentResolver);
        resolvers.add(refreshTokenArgumentResolver);
    }

}
