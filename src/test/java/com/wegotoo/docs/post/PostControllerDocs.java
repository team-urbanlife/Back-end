package com.wegotoo.docs.post;

import static com.wegotoo.support.security.MockAuthUtils.authorizationHeaderName;
import static com.wegotoo.support.security.MockAuthUtils.mockBearerToken;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
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
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.wegotoo.api.post.request.ContentEditRequest;
import com.wegotoo.api.post.request.ContentWriteRequest;
import com.wegotoo.api.post.request.PostEditRequest;
import com.wegotoo.api.post.request.PostWriteRequest;
import com.wegotoo.application.post.response.ContentResponse;
import com.wegotoo.application.post.response.PostFindOneResponse;
import com.wegotoo.docs.RestDocsSupport;
import com.wegotoo.domain.post.ContentType;
import com.wegotoo.support.security.WithAuthUser;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.payload.JsonFieldType;

public class PostControllerDocs extends RestDocsSupport {

    @Test
    @WithAuthUser
    @DisplayName("여행 게시글을 생성하는 API")
    void createAccompany() throws Exception {
        // given
        ContentWriteRequest content1 = ContentWriteRequest.builder()
                .type(ContentType.T)
                .text("첫 문단 내용")
                .build();

        ContentWriteRequest content2 = ContentWriteRequest.builder()
                .type(ContentType.IMAGE)
                .text("이미지URL")
                .build();

        List<ContentWriteRequest> contents = new ArrayList<>();
        contents.add(content1);
        contents.add(content2);

        PostWriteRequest request = PostWriteRequest.builder()
                .title("제목")
                .contents(contents)
                .build();

        ContentResponse text = ContentResponse.builder()
                .id(1L)
                .type(ContentType.T)
                .text("내용")
                .build();

        ContentResponse image = ContentResponse.builder()
                .id(2L)
                .type(ContentType.IMAGE)
                .text("이미지 URL")
                .build();
        List<ContentResponse> content = new ArrayList<>();
        content.add(text);
        content.add(image);

        PostFindOneResponse response = PostFindOneResponse.builder()
                .id(1L)
                .title("제목")
                .userName("작성자 이름")
                .userProfileImage("작성자 프로필 이미지 URL")
                .views(0)
                .contents(content)
                .registeredDateTime(LocalDateTime.now().withNano(0))
                .build();

        given(postService.writePost(anyLong(), any(), any()))
                .willReturn(response);

        // when // then
        mockMvc.perform(post("/v1/posts")
                        .header(authorizationHeaderName(), mockBearerToken())
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("post/create",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization").description("어세스 토큰")
                        ),
                        requestFields(
                                fieldWithPath("title").type(STRING)
                                        .description("제목"),
                                fieldWithPath("contents").type(ARRAY)
                                        .description("Content Block"),
                                fieldWithPath("contents[].type").type(STRING)
                                        .description("타입 : T(텍스트), IMAGE(이미지)"),
                                fieldWithPath("contents[].text").type(STRING)
                                        .description("문단 별 내용")
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
                                fieldWithPath("data.id").type(NUMBER)
                                        .description("게시글 ID"),
                                fieldWithPath("data.title").type(STRING)
                                        .description("제목"),
                                fieldWithPath("data.userName").type(STRING)
                                        .description("작성자 이름"),
                                fieldWithPath("data.userProfileImage").type(STRING)
                                        .description("유저 프로필 이미지"),
                                fieldWithPath("data.views").type(NUMBER)
                                        .description("조회수 (10.2 아직 조회수 기능은 구현 X)"),
                                fieldWithPath("data.registeredDateTime").type(STRING)
                                        .description("작성 일자"),
                                fieldWithPath("data.contents").type(ARRAY)
                                        .description("문단 별 Content"),
                                fieldWithPath("data.contents[].id").type(NUMBER)
                                        .description("Content 순서"),
                                fieldWithPath("data.contents[].type").type(STRING)
                                        .description("타입"),
                                fieldWithPath("data.contents[].text").type(STRING)
                                        .description("내용")
                        )
                ));
    }

    @Test
    @WithAuthUser
    @DisplayName("여행 게시글을 수정하는 API")
    void editPost() throws Exception {
        // given
        ContentEditRequest content1 = ContentEditRequest.builder()
                .id(1L)
                .type(ContentType.T)
                .text("첫 문단 내용 수정")
                .build();

        ContentEditRequest content2 = ContentEditRequest.builder()
                .id(2L)
                .type(ContentType.IMAGE)
                .text("두 번째 문단 이미지 URL 수정")
                .build();

        PostEditRequest request = PostEditRequest.builder()
                .title("제목 수정")
                .contents(List.of(content1, content2))
                .build();

        // when // then
        mockMvc.perform(patch("/v1/posts/{postId}", 1L)
                        .header(authorizationHeaderName(), mockBearerToken())
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("post/edit",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization").description("어세스 토큰")
                        ),
                        pathParameters(
                                parameterWithName("postId")
                                        .description("Post ID")
                        ),
                        requestFields(
                                fieldWithPath("title").type(STRING)
                                        .description("제목"),
                                fieldWithPath("contents").type(ARRAY)
                                        .description("문단 별 Content"),
                                fieldWithPath("contents[].id").type(NUMBER)
                                        .description("문단 순서"),
                                fieldWithPath("contents[].type").type(STRING)
                                        .description("타입 : T(텍스트), IMAGE(이미지)"),
                                fieldWithPath("contents[].text").type(STRING)
                                        .description("내용")
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
