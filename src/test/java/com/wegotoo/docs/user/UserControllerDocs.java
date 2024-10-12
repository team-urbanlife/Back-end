package com.wegotoo.docs.user;

import static com.wegotoo.support.security.MockAuthUtils.authorizationHeaderName;
import static com.wegotoo.support.security.MockAuthUtils.mockBearerToken;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.OBJECT;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.wegotoo.application.user.response.UserResponse;
import com.wegotoo.docs.RestDocsSupport;
import com.wegotoo.support.security.WithAuthUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.payload.JsonFieldType;

public class UserControllerDocs extends RestDocsSupport {

    @Test
    @WithAuthUser
    @DisplayName("사용자 정보 조회 API")
    void findUser() throws Exception {
        // given
        given(userService.findUser(anyLong())).willReturn(UserResponse.builder()
                .userId(1L)
                .username("테스트 사용자")
                .userProfileImage("https://test-profile.com")
                .build());

        // when // then
        mockMvc.perform(get("/v1/users/me")
                        .header(authorizationHeaderName(), mockBearerToken()))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("users/findOne",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName(authorizationHeaderName()).description("어세스 토큰")
                        ),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER)
                                        .description("코드"),
                                fieldWithPath("status").type(JsonFieldType.STRING)
                                        .description("상태"),
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                        .description("메시지"),
                                fieldWithPath("data").type(OBJECT)
                                        .description("응답 데이터"),
                                fieldWithPath("data.userId").type(NUMBER)
                                        .description("사용자 아이디"),
                                fieldWithPath("data.username").type(STRING)
                                        .description("사용자 이름"),
                                fieldWithPath("data.userProfileImage").type(STRING)
                                        .description("사용자 프로필 이미지")
                        )
                ));
    }


}
