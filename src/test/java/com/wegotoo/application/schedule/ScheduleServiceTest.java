package com.wegotoo.application.schedule;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.wegotoo.application.OffsetLimit;
import com.wegotoo.application.ServiceTestSupport;
import com.wegotoo.application.SliceResponse;
import com.wegotoo.application.schedule.request.ScheduleCreateServiceRequest;
import com.wegotoo.application.schedule.request.ScheduleEditServiceRequest;
import com.wegotoo.application.schedule.response.ScheduleFindAllResponse;
import com.wegotoo.domain.schedule.DetailedPlan;
import com.wegotoo.domain.schedule.Memo;
import com.wegotoo.domain.schedule.Schedule;
import com.wegotoo.domain.schedule.ScheduleDetails;
import com.wegotoo.domain.schedule.ScheduleGroup;
import com.wegotoo.domain.schedule.repository.DetailPlanRepository;
import com.wegotoo.domain.schedule.repository.MemoRepository;
import com.wegotoo.domain.schedule.repository.ScheduleDetailsRepository;
import com.wegotoo.domain.schedule.repository.ScheduleGroupRepository;
import com.wegotoo.domain.schedule.repository.ScheduleRepository;
import com.wegotoo.domain.user.Role;
import com.wegotoo.domain.user.User;
import com.wegotoo.domain.user.repository.UserRepository;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class ScheduleServiceTest extends ServiceTestSupport {

    @Autowired
    ScheduleRepository scheduleRepository;

    @Autowired
    ScheduleGroupRepository scheduleGroupRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ScheduleDetailsRepository scheduleDetailsRepository;

    @Autowired
    DetailPlanRepository detailPlanRepository;

    @Autowired
    MemoRepository memoRepository;

    @Autowired
    ScheduleService scheduleService;

    final LocalDate START_DATE = LocalDate.of(2024, 9, 1);
    final LocalDate END_DATE = LocalDate.of(2024, 9, 5);

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
    @DisplayName("사용자가 여행갈 도시와 일정을 설정한다.")
    void createSchedule() throws Exception {
        // given
        User user = User.builder()
                .email("user@gmail.com")
                .name("user")
                .role(Role.USER)
                .build();
        userRepository.save(user);

        ScheduleCreateServiceRequest request = ScheduleCreateServiceRequest.builder()
                .city("제주도")
                .startDate(START_DATE)
                .endDate(END_DATE)
                .build();
        // when
        scheduleService.createSchedule(user.getId(), request);

        // then
        List<Schedule> schedules = scheduleRepository.findAll();
        List<ScheduleGroup> scheduleGroups = scheduleGroupRepository.findAll();

        assertThat(schedules.get(0))
                .extracting("id", "title", "startDate", "endDate", "city", "totalTravelDays")
                .contains(schedules.get(0).getId(), "제주도 여행", START_DATE, END_DATE, "제주도", 5L);

        assertThat(scheduleGroups.size()).isEqualTo(1);

    }

    @Test
    @DisplayName("사용자가 여행 일정을 수정한다.")
    void editSchedule() throws Exception {
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

        ScheduleEditServiceRequest request = ScheduleEditServiceRequest.builder()
                .startDate(LocalDate.of(2024, 8, 1))
                .endDate(LocalDate.of(2024, 8, 3))
                .build();
        // when
        scheduleService.editSchedule(user.getId(), schedule.getId(), request);

        // then
        List<ScheduleDetails> response = scheduleDetailsRepository.findAll();
        assertThat(response.size()).isEqualTo(3);
    }

    @Test
    @DisplayName("세부 일정이 변경되면 세부 계획은 없어진다.")
    void shouldDeletePlansWhenScheduleDetailsChange() throws Exception {
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

        DetailedPlan detailedPlan = getDetailedPlan(scheduleDetailsList.get(0));
        detailPlanRepository.save(detailedPlan);

        ScheduleEditServiceRequest request = ScheduleEditServiceRequest.builder()
                .startDate(LocalDate.of(2024, 8, 1))
                .endDate(LocalDate.of(2024, 8, 3))
                .build();
        // when
        scheduleService.editSchedule(user.getId(), schedule.getId(), request);

        // then
        List<DetailedPlan> response = detailPlanRepository.findAll();
        assertThat(response.size()).isEqualTo(0);
    }

    @Test
    @DisplayName("사용자가 일정을 삭제하면 일정그룹과 세부 일정, 세부 계획이 전부 지워진다.")
    void deleteSchedule() throws Exception {
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

        DetailedPlan detailedPlan = getDetailedPlan(scheduleDetailsList.get(0));
        detailPlanRepository.save(detailedPlan);

        Memo memo = Memo.builder()
                .content("메모 추가")
                .detailedPlan(detailedPlan)
                .build();
        memoRepository.save(memo);
        // when
        scheduleService.deleteSchedule(user.getId(), schedule.getId());

        // then
        List<Schedule> schedules = scheduleRepository.findAll();
        List<ScheduleGroup> scheduleGroups = scheduleGroupRepository.findAll();
        List<ScheduleDetails> scheduleDetails = scheduleDetailsRepository.findAll();
        List<DetailedPlan> detailedPlans = detailPlanRepository.findAll();
        List<Memo> memos = memoRepository.findAll();

        assertThat(schedules.size()).isEqualTo(0);
        assertThat(scheduleGroups.size()).isEqualTo(0);
        assertThat(scheduleDetails.size()).isEqualTo(0);
        assertThat(detailedPlans.size()).isEqualTo(0);
        assertThat(memos.size()).isEqualTo(0);
    }

    @Test
    @DisplayName("유저가 본인이 작성한 일정들을 조회 할 수 있다.")
    void findAllSchedule() throws Exception {
        // given
        User user = getUser("user@gmail.com", "user");
        userRepository.save(user);

        List<Schedule> schedules = getScheduleList();
        scheduleRepository.saveAll(schedules);

        List<ScheduleGroup> scheduleGroups = getScheduleGroupList(user, schedules);
        scheduleGroupRepository.saveAll(scheduleGroups);

        OffsetLimit offsetLimit = OffsetLimit.builder()
                .offset(0)
                .limit(4)
                .build();
        // when
        SliceResponse<ScheduleFindAllResponse> responses = scheduleService.findAllSchedules(user.getId(),
                offsetLimit);
        // then
        assertThat(responses.getContent().size()).isEqualTo(4);
    }

    private static List<ScheduleGroup> getScheduleGroupList(User user, List<Schedule> schedules) {
        List<ScheduleGroup> scheduleGroups = IntStream.range(0, 4)
                .mapToObj(i -> ScheduleGroup.builder()
                        .user(user)
                        .schedule(schedules.get(i))
                        .build())
                .toList();
        return scheduleGroups;
    }

    private List<Schedule> getScheduleList() {
        List<Schedule> schedules = IntStream.range(1, 5)
                .mapToObj(i -> Schedule.builder()
                        .title("제주도 여행" + i)
                        .city("제주도")
                        .startDate(START_DATE)
                        .endDate(END_DATE)
                        .totalTravelDays(5)
                        .build())
                .toList();
        return schedules;
    }

    private DetailedPlan getDetailedPlan(ScheduleDetails scheduleDetails) {
        return DetailedPlan.builder()
                .name("제주공항")
                .latitude(11.1)
                .longitude(11.1)
                .sequence(1L)
                .scheduleDetails(scheduleDetails)
                .build();
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