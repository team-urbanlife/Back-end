package com.wegotoo.docs.schedule;

import static com.wegotoo.support.security.MockAuthUtils.authorizationHeaderName;
import static com.wegotoo.support.security.MockAuthUtils.mockBearerToken;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.JsonFieldType.NULL;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.wegotoo.api.schedule.request.MemoEditRequest;
import com.wegotoo.api.schedule.request.ScheduleCreateRequest;
import com.wegotoo.application.schedule.request.MemoWriteRequest;
import com.wegotoo.docs.RestDocsSupport;
import com.wegotoo.support.security.WithAuthUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.payload.JsonFieldType;

public class MemoControllerDocs extends RestDocsSupport {

    @Test
    @WithAuthUser
    @DisplayName("메모를 생성하는 API")
    void createMemo() throws Exception {
        // given
        MemoWriteRequest request = MemoWriteRequest.builder()
                .content("메모 내용")
                .build();

        // when // then
        mockMvc.perform(post("/v1/detailed-plans/{detailedPlanId}/memos", 1L)
                        .header(authorizationHeaderName(), mockBearerToken())
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("memo/create",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization").description("어세스 토큰")
                        ),
                        pathParameters(
                                parameterWithName("detailedPlanId")
                                        .description("DetailedPlan ID")
                        ),
                        requestFields(
                                fieldWithPath("content").type(STRING)
                                        .description("메모 내용")
                        ),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER)
                                        .description("코드"),
                                fieldWithPath("status").type(JsonFieldType.STRING)
                                        .description("상태"),
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                        .description("메시지"),
                                fieldWithPath("data").type(NULL)
                                        .description("응답 데이터")
                        )
                ));
    }

    @Test
    @WithAuthUser
    @DisplayName("메모를 수정하는 API")
    void editMemo() throws Exception {
        // given
        MemoEditRequest request = MemoEditRequest.builder()
                .content("메모 내용")
                .build();

        // when // then
        mockMvc.perform(patch("/v1/detailed-plans/{detailedPlanId}/memos/{memoId}", 1L, 1L)
                        .header(authorizationHeaderName(), mockBearerToken())
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("memo/edit",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization").description("어세스 토큰")
                        ),
                        pathParameters(
                                parameterWithName("detailedPlanId")
                                        .description("DetailedPlan ID"),
                                parameterWithName("memoId")
                                        .description("Memo ID")
                        ),
                        requestFields(
                                fieldWithPath("content").type(STRING)
                                        .description("메모 수정 내용")
                        ),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER)
                                        .description("코드"),
                                fieldWithPath("status").type(JsonFieldType.STRING)
                                        .description("상태"),
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                        .description("메시지"),
                                fieldWithPath("data").type(NULL)
                                        .description("응답 데이터")
                        )
                ));
    }

    @Test
    @WithAuthUser
    @DisplayName("메모를 수정하는 API")
    void deleteMemo() throws Exception {
        // when // then
        mockMvc.perform(delete("/v1/detailed-plans/{detailedPlanId}/memos/{memoId}", 1L, 1L)
                        .header(authorizationHeaderName(), mockBearerToken())
                        .contentType(APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("memo/delete",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization").description("어세스 토큰")
                        ),
                        pathParameters(
                                parameterWithName("detailedPlanId")
                                        .description("DetailedPlan ID"),
                                parameterWithName("memoId")
                                        .description("Memo ID")
                        ),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER)
                                        .description("코드"),
                                fieldWithPath("status").type(JsonFieldType.STRING)
                                        .description("상태"),
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                        .description("메시지"),
                                fieldWithPath("data").type(NULL)
                                        .description("응답 데이터")
                        )
                ));
    }
}
