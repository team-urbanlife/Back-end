package com.wegotoo.docs.schedule;

import static org.mockito.Mockito.mock;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
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

import com.wegotoo.api.schedule.DetailedPlanController;
import com.wegotoo.api.schedule.request.DetailedPlanCreateRequest;
import com.wegotoo.api.schedule.request.DetailedPlanMoveRequest;
import com.wegotoo.application.schedule.DetailedPlanService;
import com.wegotoo.application.schedule.request.DetailedPlanEditRequest;
import com.wegotoo.docs.RestDocsSupport;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.LongStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.payload.JsonFieldType;

public class DetailedPlanControllerDocs extends RestDocsSupport {

    private final DetailedPlanService detailedPlanService = mock(DetailedPlanService.class);

    @Override
    protected Object initController() {
        return  new DetailedPlanController(detailedPlanService);
    }

    @Test
    @DisplayName("세부 일정을 등록하는 API")
    void writeDetailedPlan() throws Exception {
        // given
        DetailedPlanCreateRequest request = DetailedPlanCreateRequest.builder()
                .name("장소 이름")
                .latitude(0.0)
                .longitude(0.0)
                .date(LocalDate.of(2024,9,1))
                .build();
        // when // then
        mockMvc.perform(post("/v1/schedule-details/{scheduleDetailsId}/detailed-plans", 1L)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("detailedPlan/create",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("scheduleDetailsId")
                                        .description("세부 일정 ID")
                        ),
                        requestFields(
                                fieldWithPath("name").type(STRING)
                                        .description("장소 이름"),
                                fieldWithPath("latitude").type(NUMBER)
                                        .description("위도"),
                                fieldWithPath("longitude").type(NUMBER)
                                        .description("경도"),
                                fieldWithPath("date").type(STRING)
                                        .description("여행 계획 일 (YYYY-MM-DD)")
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
    @DisplayName("세부 일정의 장소를 수정하는 API")
    void editDetailedPlan() throws Exception {
        // given
        DetailedPlanEditRequest request = DetailedPlanEditRequest.builder()
                .name("장소 이름")
                .latitude(0.0)
                .longitude(0.0)
                .build();
        // when // then
        mockMvc.perform(patch("/v1/detailed-plans/{detailedPlanId}", 1L)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("detailedPlan/edit",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("detailedPlanId")
                                        .description("세부 계획 ID")
                        ),
                        requestFields(
                                fieldWithPath("name").type(STRING)
                                        .description("장소 이름"),
                                fieldWithPath("latitude").type(NUMBER)
                                        .description("위도"),
                                fieldWithPath("longitude").type(NUMBER)
                                        .description("경도")
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
    @DisplayName("세부 일정의 순서를 변경하는 API")
    void movePlan() throws Exception {
        // given
        List<DetailedPlanMoveRequest> requests = LongStream.range(1, 4)
                .mapToObj(i -> DetailedPlanMoveRequest.builder()
                        .detailedPlanId(i)
                        .sequence(i)
                        .build()).toList();
        // when // then
        mockMvc.perform(patch("/v1/scheduleDetails/{scheduleDetailsId}/detailed-plans/move", 1L)
                        .content(objectMapper.writeValueAsString(requests))
                        .contentType(APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("detailedPlan/move",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("scheduleDetailsId")
                                        .description("세부 일정 ID")
                        ),
                        requestFields(
                                fieldWithPath("[].detailedPlanId").type(NUMBER)
                                        .description("세부 계획 ID"),
                                fieldWithPath("[].sequence").type(NUMBER)
                                        .description("세부 계획 순서")
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
    @DisplayName("세부 일정의 장소를 삭제하는 API")
    void deleteDetailedPlan() throws Exception {
        // when // then
        mockMvc.perform(delete("/v1/detailed-plans/{detailedPlanId}", 1L)
                        .contentType(APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("detailedPlan/delete",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("detailedPlanId")
                                        .description("세부 계획 ID")
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
