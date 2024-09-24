package com.wegotoo.api.auth;

import static com.wegotoo.support.security.MockAuthUtils.authorizationHeaderName;
import static com.wegotoo.support.security.MockAuthUtils.authorizationRefreshHeaderName;
import static com.wegotoo.support.security.MockAuthUtils.mockRefreshToken;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.wegotoo.api.ControllerTestSupport;
import com.wegotoo.application.auth.response.TokenResponse;
import com.wegotoo.support.security.WithAuthUser;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class AuthControllerTest extends ControllerTestSupport {

    @Test
    @WithAuthUser
    @DisplayName("웹에서 로그아웃을 진행한다.")
    public void logout() throws Exception {
        // given
        Cookie cookie = new Cookie(authorizationRefreshHeaderName(), mockRefreshToken());

        // when // then
        mockMvc.perform(post("/v1/logout")
                        .cookie(cookie)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("OK"))
                .andDo(print());
    }

    @Test
    @WithAuthUser
    @DisplayName("앱에서 로그아웃을 진행한다.")
    public void logoutForApp() throws Exception {
        // when // then
        mockMvc.perform(post("/v1/app/logout")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("OK"))
                .andDo(print());
    }

    @Test
    @DisplayName("웹에서 토큰을 재발급 받는다.")
    public void reissueTokens() throws Exception {
        // given
        Cookie cookie = new Cookie(authorizationRefreshHeaderName(), mockRefreshToken());
        TokenResponse response = TokenResponse.builder()
                .accessToken("${NEW_ACCESS_TOKEN}")
                .refreshToken("${NEW_REFRESH_TOKEN}")
                .build();

        given(authService.validateAndReissueToken(anyString()))
                .willReturn(response);

        // when // then
        mockMvc.perform(post("/v1/reissue")
                        .cookie(cookie)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(header().stringValues(authorizationHeaderName(), "Bearer ${NEW_ACCESS_TOKEN}"))
                .andExpect(cookie().value(authorizationRefreshHeaderName(), "${NEW_REFRESH_TOKEN}"))
                .andDo(print());
    }

    @Test
    @DisplayName("앱에서 토큰을 재발급 받는다.")
    public void reissueTokensForApp() throws Exception {
        // given
        TokenResponse response = TokenResponse.builder()
                .accessToken("${NEW_ACCESS_TOKEN}")
                .refreshToken("${NEW_REFRESH_TOKEN}")
                .build();

        given(authService.validateAndReissueToken(anyString()))
                .willReturn(response);

        // when // then
        mockMvc.perform(post("/v1/app/reissue")
                        .header(authorizationRefreshHeaderName(), mockRefreshToken())
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(header().stringValues(authorizationHeaderName(), "Bearer ${NEW_ACCESS_TOKEN}"))
                .andExpect(header().stringValues(authorizationRefreshHeaderName(), "${NEW_REFRESH_TOKEN}"))
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("OK"))
                .andDo(print());
    }

}
