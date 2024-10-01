package com.wegotoo.api.s3;

import com.wegotoo.api.ApiResponse;
import com.wegotoo.application.s3.S3Service;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class S3Controller {

    private final S3Service s3Service;

    @PostMapping("/v1/s3")
    public ApiResponse<List<String>> upload(@RequestPart List<MultipartFile> files,
                                            @RequestParam("dirName") String dirName) {
        return ApiResponse.ok(s3Service.upload(files, dirName));
    }

    @DeleteMapping("/v1/s3")
    public ApiResponse<Void> removeFile(@RequestParam("fileName") String fileName) {
        s3Service.removeFile(fileName);

        return ApiResponse.<Void>builder()
                .message("삭제 성공")
                .status(HttpStatus.OK)
                .build();
    }

}
