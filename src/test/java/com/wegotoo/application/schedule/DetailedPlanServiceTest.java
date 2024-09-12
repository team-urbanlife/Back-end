package com.wegotoo.application.schedule;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.wegotoo.application.schedule.request.DetailedPlanCreateServiceRequest;
import com.wegotoo.application.schedule.request.DetailedPlanEditServiceRequest;
import com.wegotoo.domain.schedule.DetailedPlan;
import com.wegotoo.domain.schedule.Memo;
import com.wegotoo.domain.schedule.Schedule;
import com.wegotoo.domain.schedule.ScheduleDetails;
import com.wegotoo.domain.schedule.ScheduleGroup;
import com.wegotoo.domain.schedule.Type;
import com.wegotoo.domain.schedule.repository.DetailPlanRepository;
import com.wegotoo.domain.schedule.repository.MemoRepository;
import com.wegotoo.domain.schedule.repository.ScheduleDetailsRepository;
import com.wegotoo.domain.schedule.repository.ScheduleGroupRepository;
import com.wegotoo.domain.schedule.repository.ScheduleRepository;
import com.wegotoo.domain.user.User;
import com.wegotoo.domain.user.repository.UserRepository;
import com.wegotoo.exception.BusinessException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
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
    MemoRepository memoRepository;

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
                .extracting("id", "name", "latitude", "longitude", "sequence")
                .contains(1L, "제주공항", 11.1, 11.1, 1L);
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
    @DisplayName("사용자가 세부 계획을 수정한다.")
    void editDetailedPlan() throws Exception {
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

        DetailedPlanEditServiceRequest request = DetailedPlanEditServiceRequest.builder()
                .name("제주 공항 입구")
                .latitude(11.11)
                .longitude(11.11)
                .build();
        // when
        detailedPlanService.editDetailedPlan(detailedPlan.getId(), user.getId(), request);

        // then
        List<DetailedPlan> response = detailPlanRepository.findAll();
        assertThat(response.get(0))
                .extracting("id", "name", "latitude", "longitude", "sequence")
                .contains(response.get(0).getId(), "제주 공항 입구", 11.11, 11.11, 1L);
    }

    @Test
    @DisplayName("사용자가 세부 계획을 수정할 때 장소 이름을 입력하지 않으면, 이전에 저장된 장소 이름이 유지됩니다.")
    void updateDetailPlanKeepPreviousLocationIfEmpty() throws Exception {
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

        DetailedPlanEditServiceRequest request = DetailedPlanEditServiceRequest.builder()
                .latitude(11.11)
                .longitude(11.11)
                .build();
        // when
        detailedPlanService.editDetailedPlan(detailedPlan.getId(), user.getId(), request);

        // then
        List<DetailedPlan> response = detailPlanRepository.findAll();
        assertThat(response.get(0))
                .extracting("id", "name", "latitude", "longitude", "sequence")
                .contains(response.get(0).getId(), "제주공항", 11.11, 11.11, 1L);
    }

    @Test
    @DisplayName("사용자가 세부 계획을 수정할 때 위도을 입력받지 못한다면, 이전에 저장된 위도이 유지됩니다.")
    void updateDetailPlanKeepPreviousLatitudeIfEmpty() throws Exception {
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

        DetailedPlanEditServiceRequest request = DetailedPlanEditServiceRequest.builder()
                .name("제주 공항 입구")
                .longitude(11.11)
                .build();
        // when
        detailedPlanService.editDetailedPlan(detailedPlan.getId(), user.getId(), request);

        // then
        List<DetailedPlan> response = detailPlanRepository.findAll();
        assertThat(response.get(0))
                .extracting("id", "name", "latitude", "longitude", "sequence")
                .contains(response.get(0).getId(), "제주 공항 입구", 11.1, 11.11, 1L);
    }

    @Test
    @DisplayName("사용자가 세부 계획을 수정할 때 경도을 입력받지 못한다면, 이전에 저장된 경도이 유지됩니다.")
    void updateDetailPlanKeepPreviousLongitudeIfEmpty() throws Exception {
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

        DetailedPlanEditServiceRequest request = DetailedPlanEditServiceRequest.builder()
                .name("제주 공항 입구")
                .latitude(11.11)
                .build();
        // when
        detailedPlanService.editDetailedPlan(detailedPlan.getId(), user.getId(), request);

        // then
        List<DetailedPlan> response = detailPlanRepository.findAll();
        assertThat(response.get(0))
                .extracting("id", "name", "latitude", "longitude", "sequence")
                .contains(response.get(0).getId(), "제주 공항 입구", 11.11, 11.1, 1L);
    }

    @Test
    @DisplayName("사용자가 세부 계획의 순서를 변경할 수 있다. (UP)")
    void moveUpDetailPlan() throws Exception {
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

        List<DetailedPlan> detailedPlans = getDetailedPlanList(3, scheduleDetailsList.get(0));
        detailPlanRepository.saveAll(detailedPlans);

        // when
        detailedPlanService.movePlan(detailedPlans.get(0).getId(), user.getId(),  true);

        // then
        DetailedPlan response = detailPlanRepository.findById(detailedPlans.get(0).getId()).get();
        assertThat(response.getSequence()).isEqualTo(2L);
    }

    @Test
    @DisplayName("사용자가 세부 계획의 순서를 변경할 수 있다. (DOWN)")
    void moveDownDetailPlan() throws Exception {
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

        List<DetailedPlan> detailedPlans = getDetailedPlanList(3, scheduleDetailsList.get(0));
        detailPlanRepository.saveAll(detailedPlans);

        // when
        detailedPlanService.movePlan(detailedPlans.get(1).getId(), user.getId(),  false);

        // then
        DetailedPlan response = detailPlanRepository.findById(detailedPlans.get(1).getId()).get();
        assertThat(response.getSequence()).isEqualTo(1L);
    }

    @Test
    @DisplayName("사용자가 세부 계획을 삭제할 수 있다.")
    void deleteDetailedPlan() throws Exception {
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
                .content("메모 작성")
                .detailedPlan(detailedPlan)
                .build();
        memoRepository.save(memo);
        // when
        detailedPlanService.deleteDetailedPlan(detailedPlan.getId(), user.getId());

        // then
        List<DetailedPlan> detailedPlans = detailPlanRepository.findAll();
        List<Memo> memos = memoRepository.findAll();

        assertThat(detailedPlans.size()).isEqualTo(0);
        assertThat(memos.size()).isEqualTo(0);
    }

    private static List<DetailedPlan> getDetailedPlanList(int number, ScheduleDetails scheduleDetails) {
        List<DetailedPlan> detailedPlans = LongStream.range(1, number + 1)
                .mapToObj(i -> DetailedPlan.builder()
                        .name("제주 공항" + i)
                        .latitude(11.1)
                        .longitude(11.1)
                        .scheduleDetails(scheduleDetails)
                        .sequence(i)
                        .build()).toList();
        return detailedPlans;
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

    private DetailedPlanCreateServiceRequest getWriteDetailedPlanServiceRequest() {
        return DetailedPlanCreateServiceRequest.builder()
                .date(START_DATE)
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