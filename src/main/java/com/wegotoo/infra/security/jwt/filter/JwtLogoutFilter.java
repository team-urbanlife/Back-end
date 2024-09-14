package com.wegotoo.infra.security.jwt.filter;

import static com.wegotoo.exception.ErrorCode.INVALID_REFRESH_TOKEN;
import static com.wegotoo.exception.ErrorCode.NOT_FOUND_REFRESH_TOKEN;
import static org.springframework.http.HttpMethod.POST;

import com.wegotoo.application.auth.AuthService;
import com.wegotoo.exception.BusinessException;
import com.wegotoo.infra.security.jwt.validator.JwtTokenValidator;
import com.wegotoo.infra.security.util.CookieUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class JwtLogoutFilter extends OncePerRequestFilter {

    private final AuthService authService;
    private final JwtTokenValidator tokenValidator;
    private final LogoutSuccessHandler logoutSuccessHandler;
    private static final RequestMatcher REQUEST_MATCHER = new AntPathRequestMatcher("/logout", POST.name());


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        if (!REQUEST_MATCHER.matches(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        String refreshToken = findRefreshTokenByCookie(request);

        if (!tokenValidator.isValidToken(refreshToken)) {
            throw new BusinessException(INVALID_REFRESH_TOKEN);
        }

        performLogout(request, response, refreshToken);
        logoutSuccessHandler.onLogoutSuccess(request, response, null);
    }

    private String findRefreshTokenByCookie(HttpServletRequest request) {
        return CookieUtils.getToken(request)
                .map(Cookie::getValue)
                .orElseThrow(() -> new BusinessException(NOT_FOUND_REFRESH_TOKEN));
    }

    private void performLogout(HttpServletRequest request, HttpServletResponse response, String refreshToken) {
        authService.logout(refreshToken);
        CookieUtils.deleteToken(request, response);
    }

}
