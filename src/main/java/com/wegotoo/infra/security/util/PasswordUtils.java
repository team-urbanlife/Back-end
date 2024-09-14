package com.wegotoo.infra.security.util;

import java.util.UUID;

public class PasswordUtils {

    public static String createRandomPassword() {
        return UUID.randomUUID().toString();
    }

}
