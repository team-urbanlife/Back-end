package com.wegotoo.application.schedule;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.wegotoo.application.ServiceTestSupport;
import com.wegotoo.application.schedule.response.TravelPlanResponse;
import com.wegotoo.domain.schedule.DetailedPlan;
import com.wegotoo.domain.schedule.Schedule;
import com.wegotoo.domain.schedule.ScheduleDetails;
import com.wegotoo.domain.schedule.ScheduleGroup;
import com.wegotoo.domain.schedule.repository.DetailPlanRepository;
import com.wegotoo.domain.schedule.repository.MemoRepository;
import com.wegotoo.domain.schedule.repository.ScheduleDetailsRepository;
import com.wegotoo.domain.schedule.repository.ScheduleGroupRepository;
import com.wegotoo.domain.schedule.repository.ScheduleRepository;
import com.wegotoo.domain.user.User;
import com.wegotoo.domain.user.repository.UserRepository;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.LongStream;
import java.util.stream.Stream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class ScheduleDetailsServiceTest extends ServiceTestSupport {

    @Autowired
    ScheduleRepository scheduleRepository;

    @Autowired
    ScheduleGroupRepository scheduleGroupRepository;

    @Autowired
    ScheduleDetailsRepository scheduleDetailsRepository;

    @Autowired
    DetailPlanRepository detailPlanRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    MemoRepository memoRepository;

    @Autowired
    ScheduleDetailsService scheduleDetailsService;

    final LocalDate START_DATE = LocalDate.of(2024, 9, 1);
    final LocalDate END_DATE = LocalDate.of(2024, 9, 5);

    @AfterEach
    void tearDown() {
        scheduleGroupRepository.deleteAllInBatch();
        detailPlanRepository.deleteAllInBatch();
        ;
        scheduleDetailsRepository.deleteAllInBatch();
        scheduleRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("유저가 작성한 여행 계획을 조회하면, 여행 일자, 장소들을 조회할 수 있다.")
    void findSchedule() throws Exception {
        // given
        User user = getUser("user@gmail.com", "user");
        userRepository.save(user);

        Schedule schedule = getSchedule(START_DATE, END_DATE);
        scheduleRepository.save(schedule);

        ScheduleGroup scheduleGroup = getScheduleGroup(schedule, user);
        scheduleGroupRepository.save(scheduleGroup);

        List<ScheduleDetails> scheduleDetailsList = getDatesBetween(START_DATE, END_DATE)
                .stream().map(date -> ScheduleDetails.create(date, schedule))
                .toList();
        scheduleDetailsRepository.saveAll(scheduleDetailsList);

        List<DetailedPlan> detailedPlanList = getDetailedPlanList(4, scheduleDetailsList.get(0));
        detailPlanRepository.saveAll(detailedPlanList);
        // when
        List<TravelPlanResponse> travelPlans = scheduleDetailsService.findTravelPlans(user.getId(), schedule.getId());

        // then
        assertThat(travelPlans.get(0).getDetailedPlans().size()).isEqualTo(4);
    }

    private static List<DetailedPlan> getDetailedPlanList(int number, ScheduleDetails scheduleDetails) {
        return LongStream.range(1, number + 1)
                .mapToObj(i -> DetailedPlan.builder()
                        .name("제주 공항" + i)
                        .latitude(11.1)
                        .longitude(11.1)
                        .scheduleDetails(scheduleDetails)
                        .sequence(i)
                        .build()).toList();
    }

    private List<LocalDate> getDatesBetween(LocalDate startDate, LocalDate endDate) {
        return Stream.iterate(startDate, date -> !date.isAfter(endDate), date -> date.plusDays(1))
                .toList();
    }

    private static ScheduleGroup getScheduleGroup(Schedule schedule, User user) {
        return ScheduleGroup.builder()
                .schedule(schedule)
                .user(user)
                .build();
    }

    private static Schedule getSchedule(LocalDate start, LocalDate end) {
        return Schedule.builder()
                .title("제주도 여행")
                .city("제주도")
                .startDate(start)
                .endDate(end)
                .totalTravelDays(ChronoUnit.DAYS.between(start, end) + 1)
                .build();
    }

    private User getUser(String email, String name) {
        return User.builder()
                .email(email)
                .name(name)
                .build();
    }

}