package com.wegotoo.docs.schedule;

import static com.wegotoo.support.security.MockAuthUtils.authorizationHeaderName;
import static com.wegotoo.support.security.MockAuthUtils.mockBearerToken;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.JsonFieldType.ARRAY;
import static org.springframework.restdocs.payload.JsonFieldType.BOOLEAN;
import static org.springframework.restdocs.payload.JsonFieldType.NULL;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.OBJECT;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.wegotoo.api.schedule.request.ScheduleCreateRequest;
import com.wegotoo.api.schedule.request.ScheduleEditRequest;
import com.wegotoo.application.OffsetLimit;
import com.wegotoo.application.SliceResponse;
import com.wegotoo.application.schedule.response.ScheduleFindAllResponse;
import com.wegotoo.application.schedule.response.TravelPlanResponse;
import com.wegotoo.docs.RestDocsSupport;
import com.wegotoo.domain.schedule.repository.response.DetailedPlanQueryEntity;
import com.wegotoo.support.security.WithAuthUser;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.payload.JsonFieldType;

public class ScheduleControllerDocs extends RestDocsSupport {

    private final LocalDate START_DATE = LocalDate.of(2024, 9, 1);
    private final LocalDate END_DATE = LocalDate.of(2024, 9, 2);

    @Test
    @WithAuthUser
    @DisplayName("여행 일자를 생성하는 API")
    void createSchedule() throws Exception {
        // given
        DetailedPlanQueryEntity queryResponse = DetailedPlanQueryEntity.builder()
                .region("장소 이름")
                .sequence(1L)
                .latitude(0.0)
                .longitude(0.0)
                .ScheduleDetailsId(0L)
                .build();

        ScheduleCreateRequest request = ScheduleCreateRequest.builder()
                .city("여행 도시")
                .startDate(START_DATE)
                .endDate(END_DATE)
                .build();

        TravelPlanResponse response = TravelPlanResponse.builder()
                .id(0L)
                .travelDate(LocalDate.of(2024, 9, 1))
                .detailedPlans(List.of(queryResponse))
                .build();

        given(scheduleService.createSchedule(anyLong(), any()))
                .willReturn(List.of(response));

        // when // then
        mockMvc.perform(post("/v1/schedules")
                        .header(authorizationHeaderName(), mockBearerToken())
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("schedule/create",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization").description("어세스 토큰")
                        ),
                        requestFields(
                                fieldWithPath("city").type(STRING)
                                        .description("여행 도시"),
                                fieldWithPath("startDate").type(STRING)
                                        .description("여행 시작 날짜 (YYYY-MM-DD)"),
                                fieldWithPath("endDate").type(STRING)
                                        .description("여행 종료 일자 (YYYY-MM-DD")
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
                                fieldWithPath("data[].travelDate").type(STRING)
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
                                        .description("세부 일정 ID"),
                                fieldWithPath("data[].detailedPlans[].memo").type(STRING)
                                        .description("메모").optional(),
                                fieldWithPath("data[].detailedPlans[].memoId").type(STRING)
                                        .description("메모 ID").optional()
                        )
                ));
    }

    @Test
    @WithAuthUser
    @DisplayName("등록한 여행 일자들을 조회하는 API")
    void findAllSchedule() throws Exception {
        // given
        ScheduleFindAllResponse findAllResponse = ScheduleFindAllResponse.builder()
                .id(0L)
                .title("일정 제목")
                .startDate(START_DATE)
                .endDate(END_DATE)
                .participants(1L)
                .build();

        SliceResponse<ScheduleFindAllResponse> response = SliceResponse.<ScheduleFindAllResponse>builder()
                .content(List.of(findAllResponse))
                .hasContent(true)
                .number(1)
                .size(4)
                .first(true)
                .last(false)
                .build();
        // when
        given(scheduleService.findAllSchedules(anyLong(), any(OffsetLimit.class)))
                .willReturn(response);

        // then
        mockMvc.perform(get("/v1/schedules")
                        .param("page", "1")
                        .param("size", "4")
                        .contentType(APPLICATION_JSON)
                        .header(authorizationHeaderName(), mockBearerToken())
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("schedule/find",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        queryParameters(
                                parameterWithName("page").description("페이지")
                                        .optional(),
                                parameterWithName("size").description("페이지 사이즈")
                                        .optional()
                        ),
                        requestHeaders(
                                headerWithName("Authorization").description("어세스 토큰")
                        ),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER)
                                        .description("코드"),
                                fieldWithPath("status").type(JsonFieldType.STRING)
                                        .description("상태"),
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                        .description("메시지"),
                                fieldWithPath("data").type(OBJECT)
                                        .description("응답 데이터"),
                                fieldWithPath("data.hasContent").type(BOOLEAN)
                                        .description("데이터 존재 여부"),
                                fieldWithPath("data.isFirst").type(BOOLEAN)
                                        .description("첫 번째 페이지 여부"),
                                fieldWithPath("data.isLast").type(BOOLEAN)
                                        .description("마지막 페이지 여부"),
                                fieldWithPath("data.number").type(NUMBER)
                                        .description("현재 페이지 번호"),
                                fieldWithPath("data.size").type(NUMBER)
                                        .description("토론방 반환 사이즈"),
                                fieldWithPath("data.content[]").type(ARRAY)
                                        .description("여행 일정 데이터"),
                                fieldWithPath("data.content[].id").type(NUMBER)
                                        .description("Schedule ID"),
                                fieldWithPath("data.content[].title").type(STRING)
                                        .description("일정 제목"),
                                fieldWithPath("data.content[].startDate").type(STRING)
                                        .description("여행 시작 일자"),
                                fieldWithPath("data.content[].endDate").type(STRING)
                                        .description("여행 종료 일자"),
                                fieldWithPath("data.content[].participants").type(NUMBER)
                                        .description("참여인원")
                        )
                ));
    }

    @Test
    @WithAuthUser
    @DisplayName("등록한 여행 일자를 수정하는 API")
    void editSchedule() throws Exception {
        // given
        ScheduleEditRequest request = ScheduleEditRequest.builder()
                .city("여행 도시")
                .title("여행 제목")
                .startDate(START_DATE)
                .endDate(END_DATE)
                .build();

        // when // then
        mockMvc.perform(patch("/v1/schedules/{scheduleId}", 1L)
                        .header(authorizationHeaderName(), mockBearerToken())
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("schedule/edit",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("scheduleId")
                                        .description("Schedule ID")
                        ),
                        requestHeaders(
                                headerWithName("Authorization").description("어세스 토큰")
                        ),
                        requestFields(
                                fieldWithPath("city").type(STRING)
                                        .description("여행 도시"),
                                fieldWithPath("title").type(STRING)
                                        .description("여행 제목"),
                                fieldWithPath("startDate").type(STRING)
                                        .description("여행 시작 날짜 (YYYY-MM-DD)"),
                                fieldWithPath("endDate").type(STRING)
                                        .description("여행 종료 일자 (YYYY-MM-DD")
                        ),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER)
                                        .description("코드"),
                                fieldWithPath("status").type(JsonFieldType.STRING)
                                        .description("상태"),
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                        .description("메시지"),
                                fieldWithPath("data").type(NULL)
                                        .description("응답 데이터")
                        )
                ));
    }

    @Test
    @WithAuthUser
    @DisplayName("등록한 여행 일자를 삭제 하는 API")
    void deleteSchedule() throws Exception {
        // when // then
        mockMvc.perform(delete("/v1/schedules/{scheduleId}", 1L)
                        .header(authorizationHeaderName(), mockBearerToken())
                        .contentType(APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("schedule/delete",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("scheduleId")
                                        .description("Schedule ID")
                        ),
                        requestHeaders(
                                headerWithName("Authorization").description("어세스 토큰")
                        ),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER)
                                        .description("코드"),
                                fieldWithPath("status").type(JsonFieldType.STRING)
                                        .description("상태"),
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                        .description("메시지"),
                                fieldWithPath("data").type(NULL)
                                        .description("응답 데이터")
                        )
                ));
    }

}
