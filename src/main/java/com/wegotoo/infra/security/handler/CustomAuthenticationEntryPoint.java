package com.wegotoo.infra.security.handler;

import static jakarta.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wegotoo.api.ApiResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper om;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        prepareResponse(response);
        writeResponse(response);
    }

    private void prepareResponse(HttpServletResponse response) {
        response.setStatus(SC_UNAUTHORIZED);
        response.setContentType(APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(UTF_8.name());
    }

    private void writeResponse(HttpServletResponse response) throws IOException {
        response.getWriter().write(om.writeValueAsString(createApiResponse()));
        response.getWriter().flush();
    }

    private ApiResponse<?> createApiResponse() {
        return ApiResponse.builder()
                .status(UNAUTHORIZED)
                .message("인증되지 않은 사용자입니다.")
                .build();
    }

}
