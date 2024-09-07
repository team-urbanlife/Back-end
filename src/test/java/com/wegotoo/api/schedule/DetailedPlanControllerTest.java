package com.wegotoo.api.schedule;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wegotoo.api.schedule.request.DetailedPlanCreateRequest;
import com.wegotoo.application.schedule.DetailedPlanService;
import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = DetailedPlanController.class)
@AutoConfigureMockMvc(addFilters = false)
class DetailedPlanControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    DetailedPlanService detailedPlanService;

    final LocalDate DATE = LocalDate.of(2024, 9, 1);

    @Test
    @DisplayName("세부일정의 세부계획을 작성한다.")
    void writeDetailedPlan() throws Exception {
        // given
        DetailedPlanCreateRequest request = DetailedPlanCreateRequest.builder()
                .date(DATE)
                .type("TYPE_LOCATION")
                .name("제주공항")
                .latitude(11.1)
                .longitude(11.1)
                .build();

        Long scheduleId = 1L;

        // when // then
        mockMvc.perform(post("/v1/schedules/{scheduleId}/detailplans", scheduleId)
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
    @DisplayName("세부일정의 세부계획을 작성할 때 여행 일자를 입력하지 않으면 예외가 발생한다.")
    void validateDetailedPlanDate() throws Exception {
        // given
        DetailedPlanCreateRequest request = DetailedPlanCreateRequest.builder()
                .type("TYPE_LOCATION")
                .name("제주공항")
                .latitude(11.1)
                .longitude(11.1)
                .build();

        Long scheduleId = 1L;

        // when // then
        mockMvc.perform(post("/v1/schedules/{scheduleId}/detailplans", scheduleId)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("여행 일자는 필수입니다."));
    }

    @Test
    @DisplayName("세부일정의 세부계획을 작성할 때 타입을 입력하지 않으면 예외가 발생한다.")
    void validateDetailedPlanType() throws Exception {
        // given
        DetailedPlanCreateRequest request = DetailedPlanCreateRequest.builder()
                .date(DATE)
                .name("제주공항")
                .latitude(11.1)
                .longitude(11.1)
                .build();

        Long scheduleId = 1L;

        // when // then
        mockMvc.perform(post("/v1/schedules/{scheduleId}/detailplans", scheduleId)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("타입은 필수입니다."));
    }

    @Test
    @DisplayName("세부일정의 세부계획을 작성할 때 위도를 입력하지 않으면 예외가 발생한다.")
    void validateDetailedPlanLatitude() throws Exception {
        // given
        DetailedPlanCreateRequest request = DetailedPlanCreateRequest.builder()
                .date(DATE)
                .type("TYPE_LOCATION")
                .name("제주공항")
                .longitude(11.1)
                .build();

        Long scheduleId = 1L;

        // when // then
        mockMvc.perform(post("/v1/schedules/{scheduleId}/detailplans", scheduleId)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("위도는 필수입니다."));
    }

    @Test
    @DisplayName("세부일정의 세부계획을 작성할 때 경도를 입력하지 않으면 예외가 발생한다.")
    void validateDetailedPlanLongitude() throws Exception {
        // given
        DetailedPlanCreateRequest request = DetailedPlanCreateRequest.builder()
                .date(DATE)
                .type("TYPE_LOCATION")
                .name("제주공항")
                .latitude(11.1)
                .build();

        Long scheduleId = 1L;

        // when // then
        mockMvc.perform(post("/v1/schedules/{scheduleId}/detailplans", scheduleId)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("경도는 필수입니다."));
    }

}