package com.wegotoo.api.schedule;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wegotoo.application.schedule.MemoService;
import com.wegotoo.application.schedule.request.MemoWriteRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(MemoController.class)
@AutoConfigureMockMvc(addFilters = false)
class MemoControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    MemoService memoService;

    @Test
    @DisplayName("사용자가 세부 일정의 메모 작성 API를 호출한다.")
    void writeMemoAPI() throws Exception {
        // given
        MemoWriteRequest request = MemoWriteRequest.builder()
                .content("여행에 대한 메모 작성")
                .build();

        Long detailedPlanId = 1L;

        // when // then
        mockMvc.perform(post("/v1/detailed-plans/{detailedPlanId}/memos", detailedPlanId)
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
    @DisplayName("사용자가 세부 일정의 메모 작성 API를 호출할 때 content 값이 null이면 예외가 발생한다.")
    void validateContent() throws Exception {
        // given
        MemoWriteRequest request = MemoWriteRequest.builder()
                .build();

        Long detailedPlanId = 1L;

        // when // then
        mockMvc.perform(post("/v1/detailed-plans/{detailedPlanId}/memos", detailedPlanId)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("내용은 필수입니다."));
    }
}