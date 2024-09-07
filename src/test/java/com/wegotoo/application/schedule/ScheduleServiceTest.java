package com.wegotoo.application.schedule;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.wegotoo.application.schedule.request.ScheduleCreateServiceRequest;
import com.wegotoo.domain.schedule.Schedule;
import com.wegotoo.domain.schedule.ScheduleGroup;
import com.wegotoo.domain.schedule.repository.ScheduleGroupRepository;
import com.wegotoo.domain.schedule.repository.ScheduleRepository;
import com.wegotoo.domain.user.Role;
import com.wegotoo.domain.user.User;
import com.wegotoo.domain.user.repository.UserRepository;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ScheduleServiceTest {

    @Autowired
    ScheduleRepository scheduleRepository;

    @Autowired
    ScheduleGroupRepository scheduleGroupRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ScheduleService scheduleService;

    @AfterEach
    void tearDown() {
        scheduleGroupRepository.deleteAllInBatch();
        scheduleRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("사용자가 여행갈 도시와 일정을 설정한다.")
    void createSchedule() throws Exception {
        // given
        User user = User.builder()
                .email("user@gmail.com")
                .name("user")
                .role(Role.USER)
                .build();
        userRepository.save(user);

        final LocalDate startDay = LocalDate.of(2024, 9, 1);
        final LocalDate endDay = LocalDate.of(2024, 9, 5);

        ScheduleCreateServiceRequest request = ScheduleCreateServiceRequest.builder()
                .city("제주도")
                .startDate(startDay)
                .endDate(endDay)
                .build();
        // when
        scheduleService.createSchedule(user.getId(), request);

        // then
        List<Schedule> schedules = scheduleRepository.findAll();
        List<ScheduleGroup> scheduleGroups = scheduleGroupRepository.findAll();

        assertThat(schedules.get(0))
                .extracting("id", "title", "startDate", "endDate", "city", "totalTravelDays")
                .contains(schedules.get(0).getId(), "제주도 여행", startDay, endDay, "제주도", 5L);

        assertThat(scheduleGroups.size()).isEqualTo(1);

    }

}