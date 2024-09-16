package com.wegotoo.docs.chatroom;

import static com.wegotoo.support.security.MockAuthUtils.authorizationHeaderName;
import static com.wegotoo.support.security.MockAuthUtils.mockBearerToken;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
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
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.wegotoo.api.chatroom.request.ChatRoomCreateRequest;
import com.wegotoo.application.chatroom.request.ChatRoomCreateServiceRequest;
import com.wegotoo.application.chatroom.response.ChatRoomResponse;
import com.wegotoo.docs.RestDocsSupport;
import com.wegotoo.support.security.WithAuthUser;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ChatRoomControllerDocs extends RestDocsSupport {

    @Test
    @WithAuthUser
    @DisplayName("채팅방 생성 API")
    public void createChatRoom() throws Exception {
        // given
        ChatRoomCreateRequest request = ChatRoomCreateRequest.builder()
                .accompanyId(1L)
                .build();

        given(chatRoomService.createChatRoom(any(ChatRoomCreateServiceRequest.class), anyLong()))
                .willReturn(ChatRoomResponse.of(1L, UUID.randomUUID().toString()));

        // when // then
        mockMvc.perform(post("/v1/chat-rooms")
                        .header(authorizationHeaderName(), mockBearerToken())
                        .accept(APPLICATION_JSON)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("chatRoom/create",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization").description("어세스 토큰")
                        ),
                        requestFields(
                                fieldWithPath("accompanyId").type(NUMBER).description("동행 게시글 ID")
                        ),
                        responseFields(
                                fieldWithPath("code").type(NUMBER).description("코드"),
                                fieldWithPath("status").type(STRING).description("상태"),
                                fieldWithPath("message").type(STRING).description("메세지"),
                                fieldWithPath("data").type(OBJECT).description("응답 데이터"),
                                fieldWithPath("data.id").type(NUMBER).description("채팅방 ID"),
                                fieldWithPath("data.code").type(STRING).description("채팅방 코드")
                        )
                ));
    }

}
