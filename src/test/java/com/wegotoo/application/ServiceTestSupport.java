package com.wegotoo.application;

import com.wegotoo.infra.s3.S3Manager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
public abstract class ServiceTestSupport {

    @MockBean
    protected S3Manager s3Manager;

}
