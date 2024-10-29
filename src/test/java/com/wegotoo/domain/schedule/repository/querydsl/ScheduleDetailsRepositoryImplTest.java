package com.wegotoo.domain.schedule.repository.querydsl;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.wegotoo.domain.DataJpaTestSupport;
import com.wegotoo.domain.schedule.Schedule;
import com.wegotoo.domain.schedule.ScheduleDetails;
import com.wegotoo.domain.schedule.repository.ScheduleDetailsRepository;
import com.wegotoo.domain.schedule.repository.ScheduleRepository;
import com.wegotoo.domain.schedule.repository.response.ScheduleDetailsQueryEntity;
import com.wegotoo.domain.user.User;
import com.wegotoo.domain.user.repository.UserRepository;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class ScheduleDetailsRepositoryImplTest extends DataJpaTestSupport {

    @Autowired
    ScheduleRepository scheduleRepository;

    @Autowired
    ScheduleDetailsRepository scheduleDetailsRepository;

    @Autowired
    UserRepository userRepository;

    @AfterEach
    void tearDown() {
        scheduleDetailsRepository.deleteAllInBatch();
        scheduleRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("세부 일정 조회 테스트")
    void scheduleDetails() throws Exception {
        // given
        User user = User.builder()
                .name("user")
                .email("user@gmail.com")
                .build();
        userRepository.save(user);

        Schedule schedule = Schedule.builder()
                .city("제주도")
                .title("제주도 여행")
                .startDate(LocalDate.of(2024, 9, 1))
                .endDate(LocalDate.of(2024, 9, 1))
                .totalTravelDays(5)
                .build();
        scheduleRepository.save(schedule);

        ScheduleDetails scheduleDetails = ScheduleDetails.builder()
                .date(LocalDate.of(2024, 9, 1))
                .schedule(schedule)
                .build();
        scheduleDetailsRepository.save(scheduleDetails);
        // when
        List<ScheduleDetailsQueryEntity> responses = scheduleDetailsRepository.findScheduleDetails(
                schedule.getId());
        // then
        assertThat(responses.size()).isEqualTo(1);
    }

}