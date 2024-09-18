package com.wegotoo.domain.schedule.repository;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import com.wegotoo.config.QueryDslConfig;
import com.wegotoo.domain.schedule.DetailedPlan;
import com.wegotoo.domain.schedule.Memo;
import com.wegotoo.domain.schedule.Schedule;
import com.wegotoo.domain.schedule.ScheduleDetails;
import com.wegotoo.domain.schedule.ScheduleGroup;
import com.wegotoo.domain.schedule.Type;
import com.wegotoo.domain.schedule.repository.response.DetailedPlanQueryEntity;
import com.wegotoo.domain.user.User;
import com.wegotoo.domain.user.repository.UserRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.IntStream;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import(QueryDslConfig.class)
class DetailPlanRepositoryImplTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ScheduleRepository scheduleRepository;

    @Autowired
    ScheduleGroupRepository scheduleGroupRepository;

    @Autowired
    ScheduleDetailsRepository scheduleDetailsRepository;

    @Autowired
    DetailPlanRepository detailPlanRepository;

    @Autowired
    MemoRepository memoRepository;

    LocalDate startDay = LocalDate.of(2024, 9, 1);
    LocalDate endDay = LocalDate.of(2024, 9, 5);

    @AfterEach
    void tearDown() {
        memoRepository.deleteAllInBatch();
        scheduleGroupRepository.deleteAllInBatch();
        detailPlanRepository.deleteAllInBatch();
        scheduleDetailsRepository.deleteAllInBatch();
        scheduleRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("세부 일정의 세부 계획 개수를 구할 수 있다.")
    void dayIncludedPlanCount() throws Exception {
        // given
        User user = User.builder()
                .email("user@gmail.com")
                .name("user")
                .build();
        userRepository.save(user);

        Schedule schedule = Schedule.builder()
                .title("제주도 여행")
                .city("제주도")
                .startDate(startDay)
                .endDate(endDay)
                .totalTravelDays(5)
                .build();
        scheduleRepository.save(schedule);

        ScheduleGroup scheduleGroup = ScheduleGroup.builder()
                .user(user)
                .schedule(schedule)
                .build();
        scheduleGroupRepository.save(scheduleGroup);

        ScheduleDetails scheduleDetails = ScheduleDetails.builder()
                .date(startDay)
                .schedule(schedule)
                .build();
        scheduleDetailsRepository.save(scheduleDetails);

        List<DetailedPlan> plans = IntStream.range(1, 4)
                .mapToObj(i -> DetailedPlan.builder()
                        .name("제주도 " + i)
                        .scheduleDetails(scheduleDetails)
                        .latitude(11.1)
                        .longitude(11.1)
                        .sequence((long) i)
                        .build())
                .toList();

        detailPlanRepository.saveAll(plans);
        // when
        Long response = detailPlanRepository.dayIncludedPlanCount(scheduleDetails.getId(), startDay);

        // then
        assertThat(3L).isEqualTo(response);
    }

    @Test
    @DisplayName("여행 세부 계획 조회 테스트")
    void findDetailedPlan() throws Exception {
        // given
        User user = User.builder()
                .email("user@gmail.com")
                .name("user")
                .build();
        userRepository.save(user);

        Schedule schedule = Schedule.builder()
                .title("제주도 여행")
                .city("제주도")
                .startDate(startDay)
                .endDate(endDay)
                .totalTravelDays(5)
                .build();
        scheduleRepository.save(schedule);

        ScheduleGroup scheduleGroup = ScheduleGroup.builder()
                .user(user)
                .schedule(schedule)
                .build();
        scheduleGroupRepository.save(scheduleGroup);

        ScheduleDetails scheduleDetails = ScheduleDetails.builder()
                .date(startDay)
                .schedule(schedule)
                .build();
        scheduleDetailsRepository.save(scheduleDetails);

        List<DetailedPlan> plans = IntStream.range(1, 4)
                .mapToObj(i -> DetailedPlan.builder()
                        .name("제주도 " + i)
                        .scheduleDetails(scheduleDetails)
                        .latitude(11.1)
                        .longitude(11.1)
                        .sequence((long) i)
                        .build())
                .toList();

        detailPlanRepository.saveAll(plans);
        // when
        List<DetailedPlanQueryEntity> responses = detailPlanRepository.findDetailedPlans(
                List.of(scheduleDetails.getId()));
        // then
        assertThat(responses.size()).isEqualTo(3);
    }

    @Test
    @DisplayName("세부 계획 메모 조회 테스트")
    void memoTest() throws Exception {
        // given
        User user = User.builder()
                .email("user@gmail.com")
                .name("user")
                .build();
        userRepository.save(user);

        Schedule schedule = Schedule.builder()
                .title("제주도 여행")
                .city("제주도")
                .startDate(startDay)
                .endDate(endDay)
                .totalTravelDays(5)
                .build();
        scheduleRepository.save(schedule);

        ScheduleGroup scheduleGroup = ScheduleGroup.builder()
                .user(user)
                .schedule(schedule)
                .build();
        scheduleGroupRepository.save(scheduleGroup);

        ScheduleDetails scheduleDetails = ScheduleDetails.builder()
                .date(startDay)
                .schedule(schedule)
                .build();
        scheduleDetailsRepository.save(scheduleDetails);

        List<DetailedPlan> plans = IntStream.range(1, 4)
                .mapToObj(i -> DetailedPlan.builder()
                        .name("제주도 " + i)
                        .scheduleDetails(scheduleDetails)
                        .latitude(11.1)
                        .longitude(11.1)
                        .sequence((long) i)
                        .build())
                .toList();

        detailPlanRepository.saveAll(plans);

        Memo memo = Memo.builder()
                .detailedPlan(plans.get(0))
                .content("메모")
                .build();
        memoRepository.save(memo);

        // when
        List<DetailedPlanQueryEntity> responses = detailPlanRepository.findDetailedPlans(
                List.of(scheduleDetails.getId()));
        // then
        assertThat(responses.get(0).getMemo()).isEqualTo("메모");
        assertThat(responses.get(0).getMemoId()).isEqualTo(memo.getId());
    }
}