package com.wegotoo.docs.chat;

import static com.wegotoo.support.security.MockAuthUtils.authorizationHeaderName;
import static com.wegotoo.support.security.MockAuthUtils.mockBearerToken;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.JsonFieldType.ARRAY;
import static org.springframework.restdocs.payload.JsonFieldType.BOOLEAN;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.OBJECT;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.wegotoo.application.CursorResponse;
import com.wegotoo.application.chat.response.ChatResponse;
import com.wegotoo.docs.RestDocsSupport;
import com.wegotoo.support.security.WithAuthUser;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.LongStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ChatControllerDocs extends RestDocsSupport {

    @Test
    @WithAuthUser
    @DisplayName("채팅 전체 조회")
    public void findChats() throws Exception {
        // given
        Long chatRoomId = 1L;

        given(chatService.findAllChats(anyLong(), anyLong(), anyString(), anyInt()))
                .willReturn(CursorResponse.<String, ChatResponse>builder()
                        .hasNext(true)
                        .nextCursor("_000X")
                        .size(10)
                        .content(createChatResponses())
                        .build());

        // when // then
        mockMvc.perform(get("/v1/chat-rooms/{chatRoomId}/chats", chatRoomId)
                        .param("cursor", "00011")
                        .param("size", "10")
                        .header(authorizationHeaderName(), mockBearerToken())
                        .contentType(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("chat/findAll",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization").description("어세스 토큰")
                        ),
                        pathParameters(
                                parameterWithName("chatRoomId").description("채팅방 아이디")
                        ),
                        queryParameters(
                                parameterWithName("cursor").description("마지막 채팅 아이디"),
                                parameterWithName("size").description("페이지 사이즈")
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
                                fieldWithPath("data.hasNext").type(BOOLEAN)
                                        .description("데이터 존재 여부"),
                                fieldWithPath("data.nextCursor").type(STRING)
                                        .description("다음 데이터 커서"),
                                fieldWithPath("data.size").type(NUMBER)
                                        .description("데이터 사이즈"),
                                fieldWithPath("data.content[]").type(ARRAY)
                                        .description("채팅 데이터"),
                                fieldWithPath("data.content[].id").type(STRING)
                                        .description("채팅 아이디"),
                                fieldWithPath("data.content[].chatRoomId").type(NUMBER)
                                        .description("채팅방 아이디"),
                                fieldWithPath("data.content[].senderId").type(NUMBER)
                                        .description("채팅 전송자 ID"),
                                fieldWithPath("data.content[].senderName").type(STRING)
                                        .description("채팅 전송자 이름"),
                                fieldWithPath("data.content[].senderProfileImage").type(STRING)
                                        .description("채팅 전송자 프로필 이미지"),
                                fieldWithPath("data.content[].message").type(STRING)
                                        .description("채팅 내용"),
                                fieldWithPath("data.content[].createAt").type(STRING)
                                        .description("채팅 전송 시간")
                        )
                ));
    }

    private ChatResponse createChatResponse(Long userId, Long number) {
        return ChatResponse.builder()
                .id("_000" + number)
                .chatRoomId(1L)
                .senderId(userId)
                .senderName("user" + userId)
                .senderProfileImage("profile_image.com/" + userId)
                .message("message" + number)
                .createAt(LocalDateTime.now())
                .build();
    }

    private List<ChatResponse> createChatResponses() {
        return LongStream.rangeClosed(1, 10)
                .mapToObj(i -> isOdd(i) ? createChatResponse(1L, i) : createChatResponse(2L, i))
                .sorted(Comparator.comparing(ChatResponse::getCreateAt).reversed())
                .toList();
    }

    private boolean isOdd(Long number) {
        return number % 2 == 1;
    }

}
