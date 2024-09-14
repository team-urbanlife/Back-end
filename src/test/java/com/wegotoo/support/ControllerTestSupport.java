package com.wegotoo.support;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wegotoo.api.schedule.DetailedPlanController;
import com.wegotoo.api.schedule.MemoController;
import com.wegotoo.api.schedule.ScheduleController;
import com.wegotoo.api.schedule.ScheduleDetailsController;
import com.wegotoo.application.schedule.DetailedPlanService;
import com.wegotoo.application.schedule.MemoService;
import com.wegotoo.application.schedule.ScheduleDetailsService;
import com.wegotoo.application.schedule.ScheduleService;
import com.wegotoo.config.SecurityConfig;
import jakarta.servlet.Filter;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.client.servlet.OAuth2ClientAutoConfiguration;
import org.springframework.boot.autoconfigure.security.oauth2.resource.servlet.OAuth2ResourceServerAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = {
        DetailedPlanController.class,
        MemoController.class,
        ScheduleController.class,
        ScheduleDetailsController.class
},
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,
                        classes = {
                                SecurityConfig.class,
                                Filter.class
                        })
        },
        excludeAutoConfiguration = {
                SecurityAutoConfiguration.class,
                SecurityFilterAutoConfiguration.class,
                OAuth2ClientAutoConfiguration.class,
                OAuth2ResourceServerAutoConfiguration.class
        }
)
public abstract class ControllerTestSupport {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @MockBean
    protected DetailedPlanService detailedPlanService;

    @MockBean
    protected MemoService memoService;

    @MockBean
    protected ScheduleService scheduleService;

    @MockBean
    protected ScheduleDetailsService scheduleDetailsService;

}