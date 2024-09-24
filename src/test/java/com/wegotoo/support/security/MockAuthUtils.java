package com.wegotoo.support.security;

public class MockAuthUtils {

    private static final String AUTHORIZATION_HEADER_NAME = "Authorization";
    private static final String AUTHORIZATION_REFRESH_HEADER_NAME = "Authorization-refresh";
    private static final String DEFAULT_ACCESS_TOKEN = "${ACCESS_TOKEN}";
    private static final String DEFAULT_REFRESH_TOKEN = "${REFRESH_TOKEN}";

    public static String authorizationHeaderName() {
        return AUTHORIZATION_HEADER_NAME;
    }

    public static String authorizationRefreshHeaderName() {
        return AUTHORIZATION_REFRESH_HEADER_NAME;
    }

    public static String mockBearerToken() {
        return "Bearer " + DEFAULT_ACCESS_TOKEN;
    }

    public static String mockRefreshToken() {
        return DEFAULT_REFRESH_TOKEN;
    }

}
