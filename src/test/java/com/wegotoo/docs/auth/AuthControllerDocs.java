package com.wegotoo.docs.auth;

import static com.wegotoo.support.security.MockAuthUtils.authorizationHeaderName;
import static com.wegotoo.support.security.MockAuthUtils.authorizationRefreshHeaderName;
import static com.wegotoo.support.security.MockAuthUtils.mockBearerToken;
import static com.wegotoo.support.security.MockAuthUtils.mockRefreshToken;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.cookies.CookieDocumentation.cookieWithName;
import static org.springframework.restdocs.cookies.CookieDocumentation.requestCookies;
import static org.springframework.restdocs.cookies.CookieDocumentation.responseCookies;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.JsonFieldType.NULL;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.wegotoo.application.auth.response.TokenResponse;
import com.wegotoo.docs.RestDocsSupport;
import com.wegotoo.support.security.WithAuthUser;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class AuthControllerDocs extends RestDocsSupport {

    @Test
    @WithAuthUser
    @DisplayName("웹 로그아웃 API")
    public void logout() throws Exception {
        // given
        Cookie cookie = new Cookie(authorizationRefreshHeaderName(), mockRefreshToken());

        // when // then
        mockMvc.perform(post("/v1/logout")
                        .cookie(cookie)
                        .header(authorizationHeaderName(), mockBearerToken())
                        .contentType(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("auth/logout_web",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName(authorizationHeaderName()).description("어세스 토큰")
                        ),
                        requestCookies(
                                cookieWithName(authorizationRefreshHeaderName()).description("리프레시 토큰")
                        ),
                        responseCookies(
                                cookieWithName(authorizationRefreshHeaderName()).description("리프레시 토큰 삭제")
                        ),
                        responseFields(
                                fieldWithPath("code").type(NUMBER).description("코드"),
                                fieldWithPath("status").type(STRING).description("상태"),
                                fieldWithPath("message").type(STRING).description("메세지"),
                                fieldWithPath("data").type(NULL).description("응답 데이터")
                        )
                ));
    }

    @Test
    @WithAuthUser
    @DisplayName("앱 로그아웃 API")
    public void logoutForApp() throws Exception {
        // when // then
        mockMvc.perform(post("/v1/app/logout")
                        .header(authorizationHeaderName(), mockBearerToken())
                        .contentType(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("auth/logout_app",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName(authorizationHeaderName()).description("어세스 토큰")
                        ),
                        responseFields(
                                fieldWithPath("code").type(NUMBER).description("코드"),
                                fieldWithPath("status").type(STRING).description("상태"),
                                fieldWithPath("message").type(STRING).description("메세지"),
                                fieldWithPath("data").type(NULL).description("응답 데이터")
                        )
                ));
    }

    @Test
    @DisplayName("웹 토큰 재발급 API")
    public void reissueTokens() throws Exception {
        // given
        Cookie cookie = new Cookie(authorizationRefreshHeaderName(), mockRefreshToken());
        TokenResponse response = TokenResponse.builder()
                .accessToken("${NEW_ACCESS_TOKEN}")
                .refreshToken("${NEW_REFRESH_TOKEN}")
                .build();

        given(authService.reissueToken(anyString()))
                .willReturn(response);

        // when // then
        mockMvc.perform(post("/v1/reissue")
                        .cookie(cookie)
                        .contentType(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("auth/reissue_web",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestCookies(
                                cookieWithName(authorizationRefreshHeaderName()).description("리프레시 토큰")
                        ),
                        responseHeaders(
                                headerWithName(authorizationHeaderName()).description("새로운 어세스 토큰")
                        ),
                        responseCookies(
                                cookieWithName(authorizationRefreshHeaderName()).description("새로운 리프레시 토큰")
                        ),
                        responseFields(
                                fieldWithPath("code").type(NUMBER).description("코드"),
                                fieldWithPath("status").type(STRING).description("상태"),
                                fieldWithPath("message").type(STRING).description("메세지"),
                                fieldWithPath("data").type(NULL).description("응답 데이터")
                        )
                ));
    }

    @Test
    @DisplayName("앱 토큰 재발급 API")
    public void reissueTokensForApp() throws Exception {
        // given
        TokenResponse response = TokenResponse.builder()
                .accessToken("${NEW_ACCESS_TOKEN}")
                .refreshToken("${NEW_REFRESH_TOKEN}")
                .build();

        given(authService.reissueToken(anyString()))
                .willReturn(response);

        // when // then
        mockMvc.perform(post("/v1/app/reissue")
                        .header(authorizationRefreshHeaderName(), mockRefreshToken())
                        .contentType(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("auth/reissue_app",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName(authorizationRefreshHeaderName()).description("리프레시 토큰")
                        ),
                        responseHeaders(
                                headerWithName(authorizationHeaderName()).description("새로운 어세스 토큰"),
                                headerWithName(authorizationRefreshHeaderName()).description("새로운 리프레시 토큰")
                        ),
                        responseFields(
                                fieldWithPath("code").type(NUMBER).description("코드"),
                                fieldWithPath("status").type(STRING).description("상태"),
                                fieldWithPath("message").type(STRING).description("메세지"),
                                fieldWithPath("data").type(NULL).description("응답 데이터")
                        )
                ));
    }

}
