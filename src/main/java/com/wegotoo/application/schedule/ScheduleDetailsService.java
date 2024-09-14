package com.wegotoo.application.schedule;

import static com.wegotoo.exception.ErrorCode.*;

import com.wegotoo.application.schedule.response.TravelPlanResponse;
import com.wegotoo.domain.schedule.Schedule;
import com.wegotoo.domain.schedule.repository.DetailPlanRepository;
import com.wegotoo.domain.schedule.repository.ScheduleDetailsRepository;
import com.wegotoo.domain.schedule.repository.ScheduleGroupRepository;
import com.wegotoo.domain.schedule.repository.ScheduleRepository;
import com.wegotoo.domain.schedule.repository.response.DetailedPlanQueryEntity;
import com.wegotoo.domain.schedule.repository.response.ScheduleDetailsQueryEntity;
import com.wegotoo.domain.user.User;
import com.wegotoo.domain.user.repository.UserRepository;
import com.wegotoo.exception.BusinessException;
import com.wegotoo.exception.ErrorCode;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ScheduleDetailsService {

    private final ScheduleRepository scheduleRepository;
    private final ScheduleDetailsRepository scheduleDetailsRepository;
    private final DetailPlanRepository detailPlanRepository;
    private final UserRepository userRepository;
    private final ScheduleGroupRepository scheduleGroupRepository;

    public List<TravelPlanResponse> findTravelPlans(Long userId, Long scheduleId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new BusinessException(USER_NOT_FOUND));

        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new BusinessException(SCHEDULE_NOT_FOUND));

        validateUserHasAccessToSchedule(user.getId(), schedule.getId());

        List<ScheduleDetailsQueryEntity> scheduleDetails = scheduleDetailsRepository.findScheduleDetails(scheduleId);

        List<Long> scheduleDetailsIds = scheduleDetails.stream().map(ScheduleDetailsQueryEntity::getId).toList();
        List<DetailedPlanQueryEntity> detailedPlans = detailPlanRepository.findDetailedPlans(scheduleDetailsIds);

        return TravelPlanResponse.toList(scheduleDetails, detailedPlans);
    }

    private void validateUserHasAccessToSchedule(Long userId, Long scheduleId) {
        scheduleGroupRepository.findByUserIdAndScheduleId(userId, scheduleId)
                .orElseThrow(() -> new BusinessException(UNAUTHORIZED_REQUEST));
    }
}
