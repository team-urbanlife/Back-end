package com.wegotoo.api.schedule;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.wegotoo.api.ControllerTestSupport;
import com.wegotoo.support.security.WithAuthUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ScheduleDetailsControllerTest extends ControllerTestSupport {

    @Test
    @WithAuthUser
    @DisplayName("여행 세부 일정을 조회하는 API를 호출한다.")
    void findTravelPlans() throws Exception {
        // when // then
        mockMvc.perform(get("/v1/schedules/1/schedule-details")
                        .contentType(APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("OK"));
    }
}