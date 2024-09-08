package com.wegotoo.application.schedule;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.wegotoo.application.schedule.request.DetailedPlanCreateServiceRequest;
import com.wegotoo.domain.schedule.DetailedPlan;
import com.wegotoo.domain.schedule.Schedule;
import com.wegotoo.domain.schedule.ScheduleDetails;
import com.wegotoo.domain.schedule.ScheduleGroup;
import com.wegotoo.domain.schedule.Type;
import com.wegotoo.domain.schedule.repository.DetailPlanRepository;
import com.wegotoo.domain.schedule.repository.ScheduleDetailsRepository;
import com.wegotoo.domain.schedule.repository.ScheduleGroupRepository;
import com.wegotoo.domain.schedule.repository.ScheduleRepository;
import com.wegotoo.domain.user.User;
import com.wegotoo.domain.user.repository.UserRepository;
import com.wegotoo.exception.BusinessException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DetailedPlanServiceTest {

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
    DetailedPlanService detailedPlanService;

    final LocalDate START_DATE = LocalDate.of(2024, 9, 1);
    final LocalDate END_DATE = LocalDate.of(2024, 9, 5);

    @AfterEach
    void tearDown() {
        scheduleGroupRepository.deleteAllInBatch();
        detailPlanRepository.deleteAllInBatch();;
        scheduleDetailsRepository.deleteAllInBatch();
        scheduleRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("유저가 세부 계획을 작성한다.")
    void writeDetailedPlan() throws Exception {
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

        DetailedPlanCreateServiceRequest request = getWriteDetailedPlanServiceRequest();
        // when
        detailedPlanService.writeDetailedPlan(schedule.getId(), user.getId(), request);

        // then
        List<DetailedPlan> response = detailPlanRepository.findAll();
        assertThat(response.get(0))
                .extracting("id", "type", "name", "latitude", "longitude", "memo", "sequence")
                .contains(1L, Type.LOCATION, "제주공항", 11.1, 11.1, null, 1L);
    }

    @Test
    @DisplayName("다른 유저가 생성한 일정에 세부 계획을 생성하면 예외가 발생한다.")
    void validateUserOwnsSchedule() throws Exception {
        // given
        User userA = getUser("userA@gmail.com", "userA");
        User userB = getUser("userB@gmail.com", "userB");
        userRepository.save(userA);
        userRepository.save(userB);

        Schedule schedule = getSchedule(START_DATE, END_DATE);
        scheduleRepository.save(schedule);

        ScheduleGroup scheduleGroup = getScheduleGroup(schedule, userA);
        scheduleGroupRepository.save(scheduleGroup);

        List<ScheduleDetails> scheduleDetailsList = getDatesBetween(START_DATE, END_DATE)
                .stream().map(date -> ScheduleDetails.create(date, schedule))
                .toList();
        scheduleDetailsRepository.saveAll(scheduleDetailsList);

        DetailedPlanCreateServiceRequest request = getWriteDetailedPlanServiceRequest();
        // when // then
        assertThatThrownBy(() -> detailedPlanService.writeDetailedPlan(scheduleDetailsList.get(0).getId(), userB.getId(), request))
                .isInstanceOf(BusinessException.class)
                .hasMessage("권한이 없는 사용자입니다.");
    }

    @Test
    @DisplayName("유저가 세부 계획을 작성한다.")
    void writeDetailedPlan2() throws Exception {
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

        DetailedPlanCreateServiceRequest request = getWriteDetailedPlanServiceRequest();
        // when
        detailedPlanService.writeDetailedPlan(scheduleDetailsList.get(0).getId(), user.getId(), request);

        // then
        List<DetailedPlan> response = detailPlanRepository.findAll();
        assertThat(response.get(0))
                .extracting("id", "type", "name", "latitude", "longitude", "memo", "sequence")
                .contains(1L, Type.LOCATION, "제주공항", 11.1, 11.1, null, 1L);
    }

    private List<LocalDate> getDatesBetween(LocalDate startDate, LocalDate endDate) {
        return Stream.iterate(startDate, date -> !date.isAfter(endDate), date -> date.plusDays(1))
                .toList();
    }

    private DetailedPlanCreateServiceRequest getWriteDetailedPlanServiceRequest() {
        return DetailedPlanCreateServiceRequest.builder()
                .date(START_DATE)
                .type(Type.LOCATION)
                .name("제주공항")
                .latitude(11.1)
                .longitude(11.1)
                .build();
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