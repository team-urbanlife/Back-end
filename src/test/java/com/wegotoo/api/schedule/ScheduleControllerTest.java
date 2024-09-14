package com.wegotoo.api.schedule;

import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wegotoo.api.schedule.request.ScheduleCreateRequest;
import com.wegotoo.api.schedule.request.ScheduleEditRequest;
import com.wegotoo.application.schedule.ScheduleService;
import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = ScheduleController.class)
@AutoConfigureMockMvc(addFilters = false)
class ScheduleControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    ScheduleService scheduleService;

    final LocalDate START_DATE = LocalDate.of(2024, 9, 1);
    final LocalDate END_DATE = LocalDate.of(2024, 9, 5);

    @Test
    @DisplayName("유저가 여행 일정을 생성한다.")
    void scheduleCreate() throws Exception {
        // given
        ScheduleCreateRequest request = ScheduleCreateRequest.builder()
                .city("제주도")
                .startDate(START_DATE)
                .endDate(END_DATE)
                .build();

        // when // then
        mockMvc.perform(post("/v1/schedules")
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
    @DisplayName("일정을 작성할 때 지역 이름을 입력하지 않으면 예외가 발생한다.")
    void validateCityName() throws Exception {
        // given
        ScheduleCreateRequest request = ScheduleCreateRequest.builder()
                .startDate(START_DATE)
                .endDate(END_DATE)
                .build();
        // when // then
        mockMvc.perform(post("/v1/schedules")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("도시 선택은 필수입니다."));
    }

    @Test
    @DisplayName("일정을 작성할 때 여행 시작 일을 입력하지 않으면 예외가 발생한다.")
    void validateStartDate() throws Exception {
        // given
        ScheduleCreateRequest request = ScheduleCreateRequest.builder()
                .city("제주도")
                .endDate(END_DATE)
                .build();

        // when // then
        mockMvc.perform(post("/v1/schedules")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("여행 시작 일은 필수입니다."));
    }

    @Test
    @DisplayName("일정을 작성할 때 여행 종료 일을 입력하지 않으면 예외가 발생한다.")
    void validateEndDate() throws Exception {
        // given
        ScheduleCreateRequest request = ScheduleCreateRequest.builder()
                .city("제주도")
                .startDate(START_DATE)
                .build();

        // when // then
        mockMvc.perform(post("/v1/schedules")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("여행 종료 일은 필수입니다."));
    }

    @Test
    @DisplayName("사용자가 일정 수정 API를 호출한다.")
    void editSchedule() throws Exception {
        // given
        ScheduleEditRequest request = ScheduleEditRequest.builder()
                .title("제주도 여행")
                .city("제주도")
                .startDate(START_DATE)
                .endDate(END_DATE)
                .build();

        Long scheduleId = 1L;

        // when // then
        mockMvc.perform(patch("/v1/schedules/{scheduleId}", scheduleId)
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
    @DisplayName("사용자가 수정할 때 여행 시작 일을 작성하지 않으면 예외가 발생한다.")
    void validateEditStartDate() throws Exception {
        // given
        ScheduleEditRequest request = ScheduleEditRequest.builder()
                .title("제주도 여행")
                .city("제주도")
                .endDate(END_DATE)
                .build();

        Long scheduleId = 1L;

        // when // then
        mockMvc.perform(patch("/v1/schedules/{scheduleId}", scheduleId)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("여행 시작 일은 필수입니다."));
    }

    @Test
    @DisplayName("사용자 수정할 때 여행 종료 일을 작성하지 않으면 예외가 발생한다.")
    void validateEditEndDate() throws Exception {
        // given
        ScheduleEditRequest request = ScheduleEditRequest.builder()
                .title("제주도 여행")
                .city("제주도")
                .startDate(START_DATE)
                .build();

        Long scheduleId = 1L;

        // when // then
        mockMvc.perform(patch("/v1/schedules/{scheduleId}", scheduleId)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("여행 종료 일은 필수입니다."));
    }

    @Test
    @DisplayName("사용자가 일정 삭제 API를 호출한다.")
    void deleteSchedule() throws Exception {
        // given
        Long scheduleId = 1L;

        // when // then
        mockMvc.perform(delete("/v1/schedules/{scheduleId}", scheduleId)
                        .contentType(APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("OK"));
    }

    @Test
    @DisplayName("사용자가 일정 조회 API를 호출한다.")
    void findAllSchedule() throws Exception {
        // when // then
        mockMvc.perform(get("/v1/schedules")
                .contentType(APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("OK"));
    }
}