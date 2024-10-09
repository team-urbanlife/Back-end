package com.wegotoo.docs.like;

import static com.wegotoo.support.security.MockAuthUtils.authorizationHeaderName;
import static com.wegotoo.support.security.MockAuthUtils.mockBearerToken;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.JsonFieldType.ARRAY;
import static org.springframework.restdocs.payload.JsonFieldType.NULL;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.OBJECT;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.wegotoo.application.like.response.PostIds;
import com.wegotoo.docs.RestDocsSupport;
import com.wegotoo.support.security.WithAuthUser;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

public class PostLikeControllerDocs extends RestDocsSupport {

    @Test
    @WithAuthUser
    @DisplayName("여행 게시글을 좋아요하는 API")
    void likePost() throws Exception {
        mockMvc.perform(
                        post("/v1/likes/posts/{postId}", 1L)
                                .header(authorizationHeaderName(), mockBearerToken())
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("likepost/create",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("postId")
                                        .description("Post ID")
                        ),
                        requestHeaders(
                                headerWithName("Authorization").description("어세스 토큰")
                        ),
                        responseFields(
                                fieldWithPath("code").type(NUMBER)
                                        .description("코드"),
                                fieldWithPath("status").type(STRING)
                                        .description("상태"),
                                fieldWithPath("message").type(STRING)
                                        .description("메시지"),
                                fieldWithPath("data").type(NULL)
                                        .description("return data null")
                        )
                ));
    }

    @Test
    @WithAuthUser
    @DisplayName("여행 게시글 좋아요를 취소하는 API")
    void nuLikePost() throws Exception {
        mockMvc.perform(
                        delete("/v1/likes/posts/{postId}", 1L)
                                .header(authorizationHeaderName(), mockBearerToken())
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("likepost/delete",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("postId")
                                        .description("Post ID")
                        ),
                        requestHeaders(
                                headerWithName("Authorization").description("어세스 토큰")
                        ),
                        responseFields(
                                fieldWithPath("code").type(NUMBER)
                                        .description("코드"),
                                fieldWithPath("status").type(STRING)
                                        .description("상태"),
                                fieldWithPath("message").type(STRING)
                                        .description("메시지"),
                                fieldWithPath("data").type(NULL)
                                        .description("return data null")
                        )
                ));
    }

    @Test
    @WithAuthUser
    @DisplayName("좋아요한 여행 게시글 API")
    void findLikePostIds() throws Exception {
        // given
        given(postLikeService.findPostIds(anyLong()))
                .willReturn(PostIds.of(List.of(1L, 2L, 3L, 4L, 5L)));

        // when // then
        mockMvc.perform(get("/v1/likes/posts")
                        .header(authorizationHeaderName(), mockBearerToken())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("likepost/get-ids",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization").description("어세스 토큰")
                        ),
                        responseFields(
                                fieldWithPath("code").type(NUMBER)
                                        .description("코드"),
                                fieldWithPath("status").type(STRING)
                                        .description("상태"),
                                fieldWithPath("message").type(STRING)
                                        .description("메세지"),
                                fieldWithPath("data").type(OBJECT)
                                        .description("응답 데이터"),
                                fieldWithPath("data.postIds").type(ARRAY)
                                        .description("좋아요 누른 게시글 ID")
                        )
                ));
    }

}
