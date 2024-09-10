package com.wegotoo.infra.security.oauth.handler;

import static jakarta.servlet.http.HttpServletResponse.SC_BAD_REQUEST;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wegotoo.exception.ErrorResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OAuth2FailureHandler implements AuthenticationFailureHandler {

    private final ObjectMapper om;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        response.setStatus(SC_BAD_REQUEST);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(om.writeValueAsString(ErrorResponse.of(SC_BAD_REQUEST, "소셜 로그인에 실패하였습니다.")));
        response.getWriter().flush();
    }

}
