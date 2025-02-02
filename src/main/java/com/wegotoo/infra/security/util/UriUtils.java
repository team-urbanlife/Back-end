package com.wegotoo.infra.security.util;

import java.net.URI;
import java.util.regex.Pattern;

public class UriUtils {

    private static final String URI_REGEX = "^(https?)://[^\\\\s/$.?#].[^\\\\s]*$";
    private static final Pattern URI_PATTERN = Pattern.compile(URI_REGEX);

    public static boolean isValid(String uriString) {
        return URI_PATTERN.matcher(uriString).matches();
    }

    public static boolean isStartWithApp(String uriString) {
        return URI.create(uriString).getPath().startsWith("/app");
    }

}
