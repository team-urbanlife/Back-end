package com.wegotoo.support.security;

public class MockAuthUtils {

    private static final String AUTHORIZATION_HEADER_NAME = "Authorization";
    private static final String DEFAULT_ACCESS_TOKEN = "${ACCESS_TOKEN}";

    public static String authorizationHeaderName() {
        return AUTHORIZATION_HEADER_NAME;
    }

    public static String mockBearerToken() {
        return "Bearer " + DEFAULT_ACCESS_TOKEN;
    }

}
