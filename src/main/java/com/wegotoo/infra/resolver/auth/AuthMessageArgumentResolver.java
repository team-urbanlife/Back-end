package com.wegotoo.infra.resolver.auth;

import static com.wegotoo.exception.ErrorCode.NOT_VALID_USER;

import com.wegotoo.exception.BusinessException;
import com.wegotoo.infra.security.user.CustomUserDetails;
import org.springframework.core.MethodParameter;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.invocation.HandlerMethodArgumentResolver;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class AuthMessageArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(Auth.class) && parameter.getParameterType().equals(Long.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, Message<?> message) throws Exception {
        Authentication authentication = message.getHeaders().get("simpUser", Authentication.class);

        if (authentication == null) {
            throw new BusinessException(NOT_VALID_USER);
        }

        return ((CustomUserDetails) authentication.getPrincipal()).getId();
    }

}
