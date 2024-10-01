package com.wegotoo.application.s3;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

import com.wegotoo.infra.s3.S3Manager;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

@ExtendWith(MockitoExtension.class)
class S3ServiceTest {

    @InjectMocks
    S3Service s3Service;

    @Mock
    S3Manager s3Manager;

    @Test
    @DisplayName("이미지를 업로드 테스트")
    void uploadImage() throws Exception {
        // given
        MockMultipartFile multipartFile = getMockMultipartFile();

        given(s3Manager.upload(any(MultipartFile.class), anyString()))
                .willReturn("image.jpg");
        // when
        List<String> images = s3Service.upload(List.of(multipartFile), "post");

        // then
        Assertions.assertThat("image.jpg").isEqualTo(images.get(0));
    }

    private static MockMultipartFile getMockMultipartFile() {
        String fileName = "image.jpg";
        byte[] imageByte = "image".getBytes();

        MockMultipartFile multipartFile = new MockMultipartFile("file", fileName, "image", imageByte);
        return multipartFile;
    }

}