package com.wegotoo.api.s3;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.wegotoo.api.ControllerTestSupport;
import com.wegotoo.application.s3.S3Service;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

class S3ControllerTest extends ControllerTestSupport {

    @Test
    @DisplayName("이미지 업로드하기")
    void uploadImage() throws Exception {
        // given
        String fileName = "image.jpg";
        byte[] imageByte = "image".getBytes();

        MockMultipartFile multipartFile = new MockMultipartFile("files", fileName, "image/jpg", imageByte);

        given(s3Service.upload(anyList(), anyString()))
                .willReturn(List.of("image.jpg"));

        // when // then
        mockMvc.perform(multipart("/v1/s3")
                        .file(multipartFile)
                        .param("dirName", "dirName")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("OK"));
    }

    @Test
    @DisplayName("이미지 제거하기")
    void removeImage() throws Exception {
        // given
        String fileName = "image.jpg";

        // when // then
        mockMvc.perform(delete("/v1/s3")
                        .param("fileName", fileName))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("삭제 성공"));

    }
}