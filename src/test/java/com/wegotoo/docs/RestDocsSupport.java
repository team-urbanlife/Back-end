package com.wegotoo.docs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wegotoo.application.accompany.AccompanyService;
import com.wegotoo.application.chatroom.ChatRoomService;
import com.wegotoo.application.city.CityService;
import com.wegotoo.application.schedule.DetailedPlanService;
import com.wegotoo.application.schedule.MemoService;
import com.wegotoo.application.schedule.ScheduleDetailsService;
import com.wegotoo.application.schedule.ScheduleService;
import com.wegotoo.support.ControllerWebMvcTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@ExtendWith(RestDocumentationExtension.class)
@ControllerWebMvcTest
public abstract class RestDocsSupport {

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

    @MockBean
    protected ChatRoomService chatRoomService;

    @MockBean
    protected AccompanyService accompanyService;

    @MockBean
    protected CityService cityService;

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(MockMvcRestDocumentation.documentationConfiguration(restDocumentation))
                .build();
    }

}
