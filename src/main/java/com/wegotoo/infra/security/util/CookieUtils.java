package com.wegotoo.infra.security.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Base64;
import java.util.Optional;
import org.springframework.http.ResponseCookie;
import org.springframework.util.SerializationUtils;

public class CookieUtils {

    private static final int TOKEN_MAX_AGE = 604800;
    private static final String TOKEN_NAME = "Authorization-refresh";

    public static Optional<Cookie> getCookie(HttpServletRequest request, String name) {
        return Arrays.stream(request.getCookies())
                .filter(cookie -> cookie.getName().equals(name))
                .findFirst();
    }

    public static void addCookie(HttpServletResponse response, String name, String value, int maxAge) {
        ResponseCookie cookie = ResponseCookie.from(name, value)
                .path("/")
                .maxAge(maxAge)
                .secure(true)
                .sameSite("None")
                .build();

        response.addHeader("Set-Cookie", cookie.toString());
    }

    public static void addToken(HttpServletResponse response, String token) {
        ResponseCookie cookie = ResponseCookie.from(TOKEN_NAME, token)
                .path("/")
                .maxAge(TOKEN_MAX_AGE)
                .secure(true)
                .sameSite("None")
                .build();

        response.addHeader("Set-Cookie", cookie.toString());
    }

    public static void deleteCookie(HttpServletRequest request, HttpServletResponse response, String name) {
        Arrays.stream(request.getCookies())
                .filter(cookie -> cookie.getName().equals(name))
                .forEach(cookie -> {
                    ResponseCookie responseCookie = ResponseCookie.from(cookie.getName(), cookie.getValue())
                            .value("")
                            .path("/")
                            .secure(true)
                            .maxAge(0)
                            .build();

                    response.addHeader("Set-Cookie", responseCookie.toString());
                });
    }

    public static String serialize(Object object) {
        return Base64.getUrlEncoder().encodeToString(SerializationUtils.serialize(object));
    }

    public static <T> T deserialize(Cookie cookie, Class<T> cls) {
        return cls.cast(SerializationUtils.deserialize(Base64.getUrlDecoder().decode(cookie.getValue())));
    }

}
