package com.wegotoo.infra.resolver.refresh;

import static com.wegotoo.exception.ErrorCode.NOT_FOUND_REFRESH_TOKEN;
import static com.wegotoo.infra.security.util.ServletUtils.findAuthorizationRefreshHeaderToRequest;

import com.wegotoo.exception.BusinessException;
import java.util.Optional;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class AppRefreshTokenArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(AuthAppRefreshToken.class) && parameter.getParameterType()
                .equals(String.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        return Optional.ofNullable(findAuthorizationRefreshHeaderToRequest())
                .orElseThrow(() -> new BusinessException(NOT_FOUND_REFRESH_TOKEN));
    }

}
