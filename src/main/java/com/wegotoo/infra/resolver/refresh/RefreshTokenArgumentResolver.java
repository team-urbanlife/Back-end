package com.wegotoo.infra.resolver.refresh;

import com.wegotoo.exception.BusinessException;
import com.wegotoo.exception.ErrorCode;
import com.wegotoo.infra.security.util.CookieUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Objects;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class RefreshTokenArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(RefreshToken.class) && parameter.getParameterType()
                .equals(String.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        Cookie cookie = CookieUtils.getToken(
                        Objects.requireNonNull(webRequest.getNativeRequest(HttpServletRequest.class)))
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_REFRESH_TOKEN));

        return cookie.getValue();
    }

}
