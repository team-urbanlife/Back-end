package com.wegotoo.api.chatroom;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.wegotoo.api.ControllerTestSupport;
import com.wegotoo.api.chatroom.request.ChatRoomCreateRequest;
import com.wegotoo.support.security.WithAuthUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ChatRoomControllerTest extends ControllerTestSupport {

    @Test
    @WithAuthUser
    @DisplayName("채팅방을 전체 조회한다.")
    public void findChatRooms() throws Exception {
        // when // then
        mockMvc.perform(get("/v1/chat-rooms")
                        .contentType(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("OK"));
    }

    @Test
    @WithAuthUser
    @DisplayName("채팅방을 생성한다.")
    public void createChatRoom() throws Exception {
        // given
        ChatRoomCreateRequest request = ChatRoomCreateRequest.builder()
                .accompanyId(1L)
                .build();

        // when // then
        mockMvc.perform(post("/v1/chat-rooms")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("OK"));
    }

    @Test
    @WithAuthUser
    @DisplayName("채팅방 생성 시 동행 게시글 아이디는 필수이다.")
    public void createChatRoomWithoutAccompanyId() throws Exception {
        // given
        ChatRoomCreateRequest request = ChatRoomCreateRequest.builder().build();

        // when // then
        mockMvc.perform(post("/v1/chat-rooms")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("동행 아이디 입력은 필수 입니다."));
    }

}
