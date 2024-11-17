package com.wegotoo.api.chat;

import static com.wegotoo.support.security.MockAuthUtils.authorizationHeaderName;
import static com.wegotoo.support.security.MockAuthUtils.mockBearerToken;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.wegotoo.api.ControllerTestSupport;
import com.wegotoo.support.security.WithAuthUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ChatControllerTest extends ControllerTestSupport {

    @Test
    @WithAuthUser
    @DisplayName("채팅 전체 조회")
    public void findChats() throws Exception {
        // given
        Long chatRoomId = 1L;

        // when // then
        mockMvc.perform(get("/v1/chat-rooms/{chatRoomId}/chats", chatRoomId)
                        .param("size", "20")
                        .header(authorizationHeaderName(), mockBearerToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("OK"))
                .andDo(print());
    }

    @Test
    @WithAuthUser
    @DisplayName("마지막 채팅 조회")
    public void findLastReadMessages() throws Exception {
        // given
        Long chatRoomId = 1L;

        // when // then
        mockMvc.perform(get("/v1/chat-rooms/{chatRoomId}/chats/last-read", chatRoomId)
                .header(authorizationHeaderName(), mockBearerToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("OK"))
                .andDo(print());
    }

}
