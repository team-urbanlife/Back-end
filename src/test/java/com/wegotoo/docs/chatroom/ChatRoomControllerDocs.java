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
import static org.springframework.restdocs.payload.JsonFieldType.ARRAY;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.OBJECT;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.wegotoo.api.chatroom.request.ChatRoomCreateRequest;
import com.wegotoo.application.chatroom.request.ChatRoomCreateServiceRequest;
import com.wegotoo.application.chatroom.response.ChatRoomFindAllResponse;
import com.wegotoo.application.chatroom.response.ChatRoomFindOneResponse;
import com.wegotoo.application.chatroom.response.ChatRoomResponse;
import com.wegotoo.application.chatroom.response.ChatRoomUserResponse;
import com.wegotoo.docs.RestDocsSupport;
import com.wegotoo.support.security.WithAuthUser;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.LongStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ChatRoomControllerDocs extends RestDocsSupport {

    @Test
    @WithAuthUser
    @DisplayName("채팅방 단건 조회")
    public void findChatRoom() throws Exception {
        // given
        given(chatRoomService.findChatRoom(anyLong(), anyLong()))
                .willReturn(createFindOneResponse());

        // when // then
        mockMvc.perform(get("/v1/chat-rooms/{chatRoomId}", 1L)
                        .header(authorizationHeaderName(), mockBearerToken())
                        .contentType(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("chatRoom/findOne",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName(authorizationHeaderName()).description("어세스 토큰")
                        ),
                        responseFields(
                                fieldWithPath("code").type(NUMBER).description("코드"),
                                fieldWithPath("status").type(STRING).description("상태"),
                                fieldWithPath("message").type(STRING).description("메세지"),
                                fieldWithPath("data").type(OBJECT).description("응답 데이터"),
                                fieldWithPath("data.users[]").type(ARRAY).description("사용자 정보"),
                                fieldWithPath("data.users[].id").description("사용자 아이디"),
                                fieldWithPath("data.users[].name").description("사용자 이름"),
                                fieldWithPath("data.users[].profileImage").description("사용자 프로필 사진")
                        )
                ));
    }

    @Test
    @WithAuthUser
    @DisplayName("채팅방 전체 조회")
    public void findChatRooms() throws Exception {
        // given
        given(chatRoomService.findAllChatRooms(anyLong()))
                .willReturn(createFindAllResponse());

        // when // then
        mockMvc.perform(get("/v1/chat-rooms")
                        .header(authorizationHeaderName(), mockBearerToken())
                        .contentType(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("chatRoom/findAll",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName(authorizationHeaderName()).description("어세스 토큰")
                        ),
                        responseFields(
                                fieldWithPath("code").type(NUMBER)
                                        .description("코드"),
                                fieldWithPath("status").type(STRING)
                                        .description("상태"),
                                fieldWithPath("message").type(STRING)
                                        .description("메세지"),
                                fieldWithPath("data[]").type(ARRAY)
                                        .description("응답 데이터"),
                                fieldWithPath("data[].chatRoomId").type(NUMBER)
                                        .description("채팅방 아이디"),
                                fieldWithPath("data[].accompanyId").type(NUMBER)
                                        .description("동행 아이디"),
                                fieldWithPath("data[].accompanyTitle").type(STRING)
                                                .description("동행 제목"),
                                fieldWithPath("data[].otherUserProfileImage").type(STRING)
                                        .description("타 사용자 프로필 이미지"),
                                fieldWithPath("data[].lastMessage").type(STRING)
                                        .description("마지막 채팅"),
                                fieldWithPath("data[].lastMessageCreateAt").type(STRING)
                                        .description("마지막 채팅 전송 시간")
                        )
                ));
    }


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

    private ChatRoomFindOneResponse createFindOneResponse() {
        List<ChatRoomUserResponse> response = LongStream.rangeClosed(1, 2)
                .mapToObj(i -> ChatRoomUserResponse.builder()
                        .id(i)
                        .name("user" + i)
                        .profileImage("profile_image.com/" + i)
                        .build())
                .toList();

        return ChatRoomFindOneResponse.builder().users(response).build();
    }

    private List<ChatRoomFindAllResponse> createFindAllResponse() {
        return LongStream.rangeClosed(1, 4)
                .mapToObj(i -> ChatRoomFindAllResponse.builder()
                        .chatRoomId(i)
                        .accompanyId(i)
                        .accompanyTitle("같이 제주도 가실 분??")
                        .otherUserProfileImage("profile_image.com/" + i)
                        .lastMessage("message content" + i)
                        .lastMessageCreateAt(LocalDateTime.now())
                        .build())
                .sorted(Comparator.comparing(ChatRoomFindAllResponse::getLastMessageCreateAt).reversed())
                .toList();
    }

}
