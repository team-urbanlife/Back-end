package com.wegotoo.support.security;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import org.springframework.security.test.context.support.WithSecurityContext;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = TestSecurityContextFactory.class)
public @interface WithAuthUser {

    long id() default 1L;
    String username() default "user@email.com";
    String password() default "password";
    String role() default "ROLE_USER";
}
