package com.wegotoo.api.user;

import static com.wegotoo.support.security.MockAuthUtils.authorizationHeaderName;
import static com.wegotoo.support.security.MockAuthUtils.mockBearerToken;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.wegotoo.api.ControllerTestSupport;
import com.wegotoo.support.security.WithAuthUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


public class UserControllerTest extends ControllerTestSupport {

    @Test
    @WithAuthUser
    @DisplayName("사용자 정보 조회 API")
    public void findUser() throws Exception {
        // when // then
        mockMvc.perform(get("/v1/users/me")
                        .header(authorizationHeaderName(), mockBearerToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("OK"))
                .andDo(print());
    }

}
