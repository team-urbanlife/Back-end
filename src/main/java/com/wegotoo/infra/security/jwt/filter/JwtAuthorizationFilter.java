package com.wegotoo.infra.security.jwt.filter;

import com.wegotoo.infra.security.jwt.provider.JwtTokenProvider;
import com.wegotoo.infra.security.util.ServletUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider tokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String bearerToken = ServletUtils.findAuthorizationHeaderToRequest();

        if (isNull(bearerToken) || !isValid(bearerToken)) {
            filterChain.doFilter(request, response);
            return;
        }

        Authentication authentication = tokenProvider.getAuthentication(removeBearer(bearerToken));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }

    private boolean isNull(String bearerToken) {
        return bearerToken == null;
    }

    private boolean isValid(String bearerToken) {
        return tokenProvider.isValid(bearerToken);
    }

    private String removeBearer(String bearerToken) {
        return bearerToken.replace("Bearer ", "");
    }

}
