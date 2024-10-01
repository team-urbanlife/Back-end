package com.wegotoo.application.s3;

import com.wegotoo.infra.s3.S3Manager;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Service
public class S3Service {

    private final S3Manager s3Manager;

    public List<String> upload(List<MultipartFile> multipartFiles, String dirName) {
        return multipartFiles.stream()
                .map(multipartFile -> s3Manager.upload(multipartFile, dirName))
                .collect(Collectors.toList());
    }

    public void removeFile(String fileName) {
        s3Manager.removeFile(fileName);
    }

}
