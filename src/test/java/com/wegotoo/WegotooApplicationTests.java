package com.wegotoo;

import com.wegotoo.infra.s3.S3Manager;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
class WegotooApplicationTests {

    @MockBean
    private S3Manager s3Manager;

    @Test
    void contextLoads() {
    }

}
