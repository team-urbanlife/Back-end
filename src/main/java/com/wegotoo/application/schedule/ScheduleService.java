package com.wegotoo.application.schedule;

import static com.wegotoo.exception.ErrorCode.*;

import com.wegotoo.application.schedule.request.ScheduleCreateServiceRequest;
import com.wegotoo.application.schedule.request.ScheduleEditServiceRequest;
import com.wegotoo.domain.schedule.DetailedPlan;
import com.wegotoo.domain.schedule.Schedule;
import com.wegotoo.domain.schedule.ScheduleDetails;
import com.wegotoo.domain.schedule.ScheduleGroup;
import com.wegotoo.domain.schedule.repository.DetailPlanRepository;
import com.wegotoo.domain.schedule.repository.ScheduleDetailsRepository;
import com.wegotoo.domain.schedule.repository.ScheduleGroupRepository;
import com.wegotoo.domain.schedule.repository.ScheduleRepository;
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

    @Transactional
    public void createSchedule(Long userId, ScheduleCreateServiceRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(USER_NOT_FOUND));

        Schedule schedule = Schedule.create(request.getCity(), request.getStartDate(),
                request.getEndDate(), request.totalTravelDays());
        scheduleRepository.save(schedule);

        ScheduleGroup scheduleGroup = ScheduleGroup.create(user, schedule);
        scheduleGroupRepository.save(scheduleGroup);

        List<ScheduleDetails> scheduleDetailsList = getDatesBetween(request.getStartDate(), request.getEndDate())
                .stream().map(date -> ScheduleDetails.create(date, schedule))
                .toList();
        scheduleDetailsRepository.saveAll(scheduleDetailsList);
    }

    @Transactional
    public void editSchedule(Long userId, Long scheduleId, ScheduleEditServiceRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(USER_NOT_FOUND));

        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new BusinessException(SCHEDULE_NOT_FOUND));

        validateUserHasAccessToSchedule(user.getId(), schedule.getId());

        editScheduleDetails(schedule, request.getStartDate(), request.getEndDate());

        schedule.edit(request.getTitle(), request.getCity(), request.getStartDate(), request.getEndDate(),
                request.totalTravelDays());
    }

    private void editScheduleDetails(Schedule schedule, LocalDate newStartDate, LocalDate newEndDate) {
        List<ScheduleDetails> existingDetails = scheduleDetailsRepository.findBySchedule(schedule);

        List<LocalDate> existingDates = existingDetails.stream()
                .map(ScheduleDetails::getDate)
                .toList();

        List<LocalDate> newDates = getDatesBetween(newStartDate, newEndDate);

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
    }

    private static boolean isDateContain(LocalDate date, List<LocalDate> existingDates) {
        return !existingDates.contains(date);
    }

    private void validateUserHasAccessToSchedule(Long userId, Long scheduleId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(USER_NOT_FOUND));

        scheduleGroupRepository.findByUserIdAndScheduleId(user.getId(), scheduleId)
                .orElseThrow(() -> new BusinessException(UNAUTHORIZED_REQUEST));
    }

    private List<LocalDate> getDatesBetween(LocalDate startDate, LocalDate endDate) {
        return Stream.iterate(startDate, date -> !date.isAfter(endDate), date -> date.plusDays(1))
                .toList();
    }

}
