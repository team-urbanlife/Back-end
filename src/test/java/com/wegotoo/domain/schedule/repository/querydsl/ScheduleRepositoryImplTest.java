package com.wegotoo.domain.schedule.repository.querydsl;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.wegotoo.application.schedule.response.ScheduleFindAllResponse;
import com.wegotoo.config.QueryDslConfig;
import com.wegotoo.domain.schedule.Schedule;
import com.wegotoo.domain.schedule.ScheduleGroup;
import com.wegotoo.domain.schedule.repository.ScheduleGroupRepository;
import com.wegotoo.domain.schedule.repository.ScheduleRepository;
import com.wegotoo.domain.schedule.repository.response.ScheduleQueryEntity;
import com.wegotoo.domain.user.User;
import com.wegotoo.domain.user.repository.UserRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.IntStream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import(QueryDslConfig.class)
class ScheduleRepositoryImplTest {

    @Autowired
    ScheduleRepository scheduleRepository;

    @Autowired
    ScheduleGroupRepository scheduleGroupRepository;

    @Autowired
    UserRepository userRepository;

    private final LocalDate START_DATE = LocalDate.of(2024, 9, 1);
    private final LocalDate END_DATE = LocalDate.of(2024, 9, 5);

    @AfterEach
    void tearDown() {
        scheduleGroupRepository.deleteAllInBatch();
        scheduleRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("유저가 본인이 생성한 일정을 조회하면, 일정은 ID, 제목, 시작 일자, 종료 일자, 참여인원이 객체에 담긴다.")
    void findAllSchedule() throws Exception {
        // given
        User user = User.builder()
                .email("user@gmail.com")
                .name("user")
                .build();
        userRepository.save(user);

        List<Schedule> schedules = IntStream.range(1, 5)
                .mapToObj(i -> Schedule.builder()
                        .title("제주도 여행 " + i)
                        .city("제주도")
                        .startDate(START_DATE)
                        .endDate(END_DATE)
                        .totalTravelDays(5)
                        .build())
                .toList();
        scheduleRepository.saveAll(schedules);

        List<ScheduleGroup> scheduleGroups = IntStream.range(0, 4)
                .mapToObj(i -> ScheduleGroup.builder()
                        .schedule(schedules.get(i))
                        .user(user)
                        .build())
                .toList();
        scheduleGroupRepository.saveAll(scheduleGroups);
        // when
        List<ScheduleQueryEntity> findAllSchedules = scheduleRepository.findAllSchedules(user.getId(), 0, 4);
        // then
        assertThat(findAllSchedules.size()).isEqualTo(4);
    }
}