package com.wegotoo.application.schedule;

import static com.wegotoo.exception.ErrorCode.*;

import com.wegotoo.application.OffsetLimit;
import com.wegotoo.application.SliceResponse;
import com.wegotoo.application.schedule.request.ScheduleCreateServiceRequest;
import com.wegotoo.application.schedule.request.ScheduleEditServiceRequest;
import com.wegotoo.application.schedule.response.ScheduleFindAllResponse;
import com.wegotoo.application.schedule.response.ScheduleResponse;
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
import com.wegotoo.domain.schedule.repository.response.DetailedPlanQueryEntity;
import com.wegotoo.domain.schedule.repository.response.ScheduleDetailsQueryEntity;
import com.wegotoo.domain.schedule.repository.response.ScheduleQueryEntity;
import com.wegotoo.domain.user.User;
import com.wegotoo.domain.user.repository.UserRepository;
import com.wegotoo.exception.BusinessException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final UserRepository userRepository;
    private final ScheduleGroupRepository scheduleGroupRepository;
    private final ScheduleDetailsRepository scheduleDetailsRepository;
    private final DetailPlanRepository detailPlanRepository;
    private final MemoRepository memoRepository;

    @Transactional
    public ScheduleResponse createSchedule(Long userId, ScheduleCreateServiceRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(USER_NOT_FOUND));

        Schedule schedule = Schedule.create(request.getCity(), request.getStartDate(),
                request.getEndDate(), request.totalTravelDays());
        Schedule scheduleSave = scheduleRepository.save(schedule);

        ScheduleGroup scheduleGroup = ScheduleGroup.create(user, scheduleSave);
        scheduleGroupRepository.save(scheduleGroup);

        List<ScheduleDetails> scheduleDetailsList = getDatesBetween(request.getStartDate(), request.getEndDate())
                .stream().map(date -> ScheduleDetails.create(date, schedule))
                .toList();
        scheduleDetailsRepository.saveAll(scheduleDetailsList);

        List<ScheduleDetailsQueryEntity> scheduleDetails = scheduleDetailsRepository.findScheduleDetails(scheduleSave.getId());

        List<Long> scheduleDetailsIds = scheduleDetails.stream().map(ScheduleDetailsQueryEntity::getId).toList();
        List<DetailedPlanQueryEntity> detailedPlans = detailPlanRepository.findDetailedPlans(scheduleDetailsIds);

        return ScheduleResponse.of(schedule.getId(), TravelPlanResponse.toList(scheduleDetails, detailedPlans));
    }

    public SliceResponse<ScheduleFindAllResponse> findAllSchedules(Long userId, OffsetLimit offsetLimit) {
        User user = userRepository.findById(userId).orElseThrow(() -> new BusinessException(USER_NOT_FOUND));

        List<ScheduleQueryEntity> schedules = scheduleRepository.findAllSchedules(user.getId(), offsetLimit.getOffset(),
                offsetLimit.getLimit());

        return SliceResponse.of(ScheduleFindAllResponse.toList(schedules), offsetLimit.getOffset(), offsetLimit.getLimit());
    }

    @Transactional
    public void editSchedule(Long userId, Long scheduleId, ScheduleEditServiceRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(USER_NOT_FOUND));

        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new BusinessException(SCHEDULE_NOT_FOUND));

        validateUserHasAccessToSchedule(user.getId(), schedule.getId());

        List<ScheduleDetails> existingDetails = scheduleDetailsRepository.findBySchedule(schedule);

        List<LocalDate> existingDates = existingDetails.stream()
                .map(ScheduleDetails::getDate)
                .toList();

        List<LocalDate> newDates = getDatesBetween(request.getStartDate(), request.getEndDate());

        List<LocalDate> datesToAdd = newDates.stream()
                .filter(date -> isDateContain(date, existingDates))
                .toList();

        List<ScheduleDetails> detailsToRemove = existingDetails.stream()
                .filter(detail -> isDateContain(detail.getDate(), newDates))
                .toList();

        List<DetailedPlan> detailedPlans = detailPlanRepository.findByScheduleDetails(detailsToRemove);
        detailPlanRepository.deleteAll(detailedPlans);

        scheduleDetailsRepository.deleteAll(detailsToRemove);

        List<ScheduleDetails> detailsToAdd = datesToAdd.stream()
                .map(date -> ScheduleDetails.create(date, schedule))
                .toList();

        scheduleDetailsRepository.saveAll(detailsToAdd);

        schedule.edit(request.getTitle(), request.getCity(), request.getStartDate(), request.getEndDate(),
                request.totalTravelDays());
    }

    @Transactional
    public void deleteSchedule(Long userId, Long scheduleId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(USER_NOT_FOUND));

        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new BusinessException(SCHEDULE_NOT_FOUND));

        ScheduleGroup scheduleGroup = scheduleGroupRepository.findByUserIdAndScheduleId(user.getId(), scheduleId)
                .orElseThrow(() -> new BusinessException(UNAUTHORIZED_REQUEST));

        List<ScheduleDetails> scheduleDetails = scheduleDetailsRepository.findBySchedule(schedule);

        List<DetailedPlan> detailedPlans = detailPlanRepository.findByScheduleDetails(scheduleDetails);

        if (hasDetailedPlans(detailedPlans)) {
            detailedPlans.forEach(memoRepository::deleteByDetailedPlan);
            detailPlanRepository.deleteAll(detailedPlans);
        }

        scheduleDetailsRepository.deleteAll(scheduleDetails);
        scheduleGroupRepository.delete(scheduleGroup);
        scheduleRepository.delete(schedule);
    }

    private static boolean hasDetailedPlans(List<DetailedPlan> detailedPlan) {
        return !detailedPlan.isEmpty();
    }

    private static boolean isDateContain(LocalDate date, List<LocalDate> existingDates) {
        return !existingDates.contains(date);
    }

    private void validateUserHasAccessToSchedule(Long userId, Long scheduleId) {
        scheduleGroupRepository.findByUserIdAndScheduleId(userId, scheduleId)
                .orElseThrow(() -> new BusinessException(UNAUTHORIZED_REQUEST));
    }

    private List<LocalDate> getDatesBetween(LocalDate startDate, LocalDate endDate) {
        return Stream.iterate(startDate, date -> !date.isAfter(endDate), date -> date.plusDays(1))
                .toList();
    }

}
