package com.wegotoo.api.schedule;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wegotoo.api.schedule.request.DetailedPlanCreateRequest;
import com.wegotoo.api.schedule.request.DetailedPlanMoveRequest;
import com.wegotoo.application.schedule.DetailedPlanService;
import com.wegotoo.application.schedule.request.DetailedPlanEditRequest;
import java.time.LocalDate;
import java.util.List;
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
                .name("제주공항")
                .latitude(11.1)
                .longitude(11.1)
                .build();

        Long scheduleId = 1L;

        // when // then
        mockMvc.perform(post("/v1/schedule-details/{scheduleId}/detailed-plans", scheduleId)
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
                .name("제주공항")
                .latitude(11.1)
                .longitude(11.1)
                .build();

        Long scheduleId = 1L;

        // when // then
        mockMvc.perform(post("/v1/schedule-details/{scheduleId}/detailed-plans", scheduleId)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("여행 일자는 필수입니다."));
    }

    @Test
    @DisplayName("세부일정의 세부계획을 작성할 때 장소를 입력하지 않으면 예외가 발생한다.")
    void validateDetailedPlanLocal() throws Exception {
        // given
        DetailedPlanCreateRequest request = DetailedPlanCreateRequest.builder()
                .date(DATE)
                .latitude(11.1)
                .longitude(11.1)
                .build();

        Long scheduleId = 1L;

        // when // then
        mockMvc.perform(post("/v1/schedule-details/{scheduleId}/detailed-plans", scheduleId)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("장소는 필수입니다."));
    }

    @Test
    @DisplayName("세부일정의 세부계획을 작성할 때 위도를 입력하지 않으면 예외가 발생한다.")
    void validateDetailedPlanLatitude() throws Exception {
        // given
        DetailedPlanCreateRequest request = DetailedPlanCreateRequest.builder()
                .date(DATE)
                .name("제주공항")
                .longitude(11.1)
                .build();

        Long scheduleId = 1L;

        // when // then
        mockMvc.perform(post("/v1/schedule-details/{scheduleId}/detailed-plans", scheduleId)
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
                .name("제주공항")
                .latitude(11.1)
                .build();

        Long scheduleId = 1L;

        // when // then
        mockMvc.perform(post("/v1/schedule-details/{scheduleId}/detailed-plans", scheduleId)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("경도는 필수입니다."));
    }

    @Test
    @DisplayName("세부계획의 내용을 수정하는 API를 호출한다.")
    void editDetailedPlan() throws Exception {
        // given
        DetailedPlanEditRequest request = DetailedPlanEditRequest.builder()
                .name("제주공항")
                .latitude(11.1)
                .longitude(11.1)
                .build();

        Long detailedPlanId = 1L;

        // when // then
        mockMvc.perform(patch("/v1/detailed-plans/{detailedPlanId}", detailedPlanId)
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
    @DisplayName("세부계획의 내용을 수정할 때 지역 이름을 넣지 않으면 예외가 발생한다.")
    void validateEditDetailedPlanLocal() throws Exception {
        // given
        DetailedPlanEditRequest request = DetailedPlanEditRequest.builder()
                .latitude(11.1)
                .longitude(11.1)
                .build();

        Long detailedPlanId = 1L;

        // when // then
        mockMvc.perform(patch("/v1/detailed-plans/{detailedPlanId}", detailedPlanId)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("지역은 필수입니다."));
    }

    @Test
    @DisplayName("세부계획의 내용을 수정할 때 지역 위도를 넣지 않으면 예외가 발생한다.")
    void validateEditDetailedPlanLatitude() throws Exception {
        // given
        DetailedPlanEditRequest request = DetailedPlanEditRequest.builder()
                .name("제주공항")
                .longitude(11.1)
                .build();

        Long detailedPlanId = 1L;

        // when // then
        mockMvc.perform(patch("/v1/detailed-plans/{detailedPlanId}", detailedPlanId)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("위도는 필수입니다."));
    }

    @Test
    @DisplayName("세부계획의 내용을 수정할 때 지역 경도를 넣지 않으면 예외가 발생한다.")
    void validateEditDetailedPlanLongitude() throws Exception {
        // given
        DetailedPlanEditRequest request = DetailedPlanEditRequest.builder()
                .name("제주공항")
                .latitude(11.1)
                .build();

        Long detailedPlanId = 1L;

        // when // then
        mockMvc.perform(patch("/v1/detailed-plans/{detailedPlanId}", detailedPlanId)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("경도는 필수입니다."));
    }

    @Test
    @DisplayName("세부계획의 순서를 변경하는 API를 호출한다.")
    void movePlan() throws Exception {
        // given
        DetailedPlanMoveRequest request1 = getMoveRequest(1L, 2L);
        DetailedPlanMoveRequest request2 = getMoveRequest(2L, 1L);
        DetailedPlanMoveRequest request3 = getMoveRequest(3L, 3L);

        List<DetailedPlanMoveRequest> requests = List.of(request1, request2, request3);

        Long scheduleDetailsId = 1L;

        // when // then
        mockMvc.perform(patch("/v1/scheduleDetails/{scheduleDetailsId}/detailed-plans/move", scheduleDetailsId)
                        .content(objectMapper.writeValueAsString(requests))
                        .contentType(APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("OK"));
    }

    @Test
    @DisplayName("세부계획을 삭제하는 API를 호출한다.")
    void deleteDetailedPlan() throws Exception {
        // given
        Long detailedPlanId = 1L;

        // when // then
        mockMvc.perform(delete("/v1/detailed-plans/{detailedPlanId}", detailedPlanId)
                        .contentType(APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("OK"));
    }

    private static DetailedPlanMoveRequest getMoveRequest(long detailedPlanId,
                                                          long sequence) {
        return DetailedPlanMoveRequest.builder()
                .detailedPlanId(detailedPlanId)
                .sequence(sequence)
                .build();
    }

}