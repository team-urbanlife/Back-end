package com.wegotoo.docs.schedule;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.JsonFieldType.ARRAY;
import static org.springframework.restdocs.payload.JsonFieldType.NULL;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.wegotoo.api.schedule.ScheduleDetailsController;
import com.wegotoo.application.schedule.ScheduleDetailsService;
import com.wegotoo.application.schedule.response.TravelPlanResponse;
import com.wegotoo.docs.RestDocsSupport;
import com.wegotoo.domain.schedule.repository.response.DetailedPlanQueryEntity;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.payload.JsonFieldType;

public class ScheduleDetailsControllerDocs extends RestDocsSupport {

    private final ScheduleDetailsService scheduleDetailsService = mock(ScheduleDetailsService.class);

    @Override
    protected Object initController() {
        return new ScheduleDetailsController(scheduleDetailsService);
    }

    @Test
    @DisplayName("여행 세부 계획을 조회하는 API")
    void findTravelPlans() throws Exception {
        // given
        DetailedPlanQueryEntity queryResponse = DetailedPlanQueryEntity.builder()
                .region("장소 이름")
                .sequence(1L)
                .latitude(0.0)
                .longitude(0.0)
                .ScheduleDetailsId(0L)
                .build();

        TravelPlanResponse response = TravelPlanResponse.builder()
                .id(0L)
                .travelDate(LocalDate.of(2024, 9, 1))
                .detailedPlans(List.of(queryResponse))
                .build();
        // when
        given(scheduleDetailsService.findTravelPlans(anyLong(), anyLong()))
                .willReturn(List.of(response));

        // then
        mockMvc.perform(get("/v1/schedules/{scheduleId}/schedule-details", 1L)
                        .contentType(APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("scheduleDetails/findTravelPlans",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("scheduleId")
                                        .description("일정 ID")
                        ),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER)
                                        .description("코드"),
                                fieldWithPath("status").type(JsonFieldType.STRING)
                                        .description("상태"),
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                        .description("메시지"),
                                fieldWithPath("data").type(ARRAY)
                                        .description("응답 데이터"),
                                fieldWithPath("data[].id").type(NUMBER)
                                        .description("세부 일정 ID"),
                                fieldWithPath("data[].travelDate").type(ARRAY)
                                        .description("여행 계획 일 (YYYY-MM-DD"),
                                fieldWithPath("data[].detailedPlans").type(ARRAY)
                                        .description("세부 계획 데이터"),
                                fieldWithPath("data[].detailedPlans[].region").type(STRING)
                                        .description("장소 이름"),
                                fieldWithPath("data[].detailedPlans[].sequence").type(NUMBER)
                                        .description("세부 계획 순서"),
                                fieldWithPath("data[].detailedPlans[].latitude").type(NUMBER)
                                        .description("위도"),
                                fieldWithPath("data[].detailedPlans[].longitude").type(NUMBER)
                                        .description("경도"),
                                fieldWithPath("data[].detailedPlans[].scheduleDetailsId").type(NUMBER)
                                        .description("세부 일정 ID")
                        )
                ));
    }
}