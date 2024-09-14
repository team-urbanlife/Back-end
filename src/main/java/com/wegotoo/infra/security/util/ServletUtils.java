package com.wegotoo.infra.security.util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class ServletUtils {

    public static String findAuthorizationHeaderToRequest() {
        HttpServletRequest request = findServletRequest();
        return request.getHeader("Authorization");
    }

    public static void addAuthorizationHeaderToResponse(String token) {
        HttpServletResponse response = findServletResponse();
        response.addHeader("Authorization", "Bearer " + token);
    }

    public static void addRefreshTokenCookieToResponse(String token) {
        HttpServletResponse response = findServletResponse();
        CookieUtils.addToken(response, token);
    }

    private static HttpServletRequest findServletRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        return (HttpServletRequest) requestAttributes.resolveReference(RequestAttributes.REFERENCE_REQUEST);
    }

    private static HttpServletResponse findServletResponse() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();

        return ((ServletRequestAttributes) requestAttributes).getResponse();
    }

}