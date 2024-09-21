package com.wegotoo.docs.city;

import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.JsonFieldType.ARRAY;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.wegotoo.application.city.response.CityResponse;
import com.wegotoo.docs.RestDocsSupport;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.payload.JsonFieldType;

public class CityControllerDocs extends RestDocsSupport {

    @Test
    @DisplayName("도시 찾기")
    void findCity() throws Exception {
        // given
        CityResponse response = CityResponse.builder()
                .cityId(0L)
                .region("서울")
                .latitude(37.5665)
                .longitude(126.9780)
                .build();

        given(cityService.findAll())
                .willReturn(List.of(response));
        // when // then
        mockMvc.perform(get("/v1/cities")
                        .contentType(APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("city/find",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER)
                                        .description("코드"),
                                fieldWithPath("status").type(JsonFieldType.STRING)
                                        .description("상태"),
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                        .description("메시지"),
                                fieldWithPath("data").type(ARRAY)
                                        .description("응답 데이터"),
                                fieldWithPath("data[].cityId").type(NUMBER)
                                        .description("City ID"),
                                fieldWithPath("data[].region").type(STRING)
                                        .description("도시 이름"),
                                fieldWithPath("data[].latitude").type(NUMBER)
                                        .description("위도"),
                                fieldWithPath("data[].longitude").type(NUMBER)
                                        .description("경도")
                        )
                ));
    }
}
