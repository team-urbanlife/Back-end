package com.wegotoo.application.schedule;

import static com.wegotoo.exception.ErrorCode.*;

import com.wegotoo.application.schedule.request.DetailedPlanCreateServiceRequest;
import com.wegotoo.application.schedule.request.DetailedPlanEditServiceRequest;
import com.wegotoo.domain.schedule.DetailedPlan;
import com.wegotoo.domain.schedule.Schedule;
import com.wegotoo.domain.schedule.ScheduleDetails;
import com.wegotoo.domain.schedule.repository.DetailPlanRepository;
import com.wegotoo.domain.schedule.repository.MemoRepository;
import com.wegotoo.domain.schedule.repository.ScheduleDetailsRepository;
import com.wegotoo.domain.schedule.repository.ScheduleGroupRepository;
import com.wegotoo.domain.user.User;
import com.wegotoo.domain.user.repository.UserRepository;
import com.wegotoo.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class DetailedPlanService {

    private final ScheduleDetailsRepository scheduleDetailsRepository;
    private final DetailPlanRepository detailPlanRepository;
    private final UserRepository userRepository;
    private final ScheduleGroupRepository scheduleGroupRepository;
    private final MemoRepository memoRepository;

    @Transactional
    public void writeDetailedPlan(Long scheduleDetailsId, Long userId, DetailedPlanCreateServiceRequest request) {
        User user = userRepository.findById(userId).orElseThrow(() -> new BusinessException(USER_NOT_FOUND));

        ScheduleDetails scheduleDetails = scheduleDetailsRepository.findById(scheduleDetailsId)
                .orElseThrow(() -> new BusinessException(SCHEDULE_DETAIL_NOT_FOUND));

        Schedule schedule = scheduleDetails.getSchedule();

        validateUserHasAccessToSchedule(user.getId(), schedule.getId());

        Long sequence = detailPlanRepository.dayIncludedPlanCount(scheduleDetails.getId(), request.getDate());

        DetailedPlan detailedPlan = DetailedPlan.create(request.getName(),
                request.getLatitude(), request.getLongitude(), sequence, scheduleDetails);

        detailPlanRepository.save(detailedPlan);
    }

    @Transactional
    public void editDetailedPlan(Long detailedPlanId, Long userId, DetailedPlanEditServiceRequest request) {
        User user = userRepository.findById(userId).orElseThrow(() -> new BusinessException(USER_NOT_FOUND));

        DetailedPlan detailedPlan = detailPlanRepository.findById(detailedPlanId)
                .orElseThrow(() -> new BusinessException(DETAILED_PLAN_NOT_FOUND));

        ScheduleDetails scheduleDetails = detailedPlan.getScheduleDetails();
        Schedule schedule = scheduleDetails.getSchedule();

        validateUserHasAccessToSchedule(user.getId(), schedule.getId());

        detailedPlan.edit(request.getName(), request.getLatitude(), request.getLongitude());
    }

    @Transactional
    public void movePlan(Long detailedPlanId, Long userId, boolean isMoveUp) {
        User user = userRepository.findById(userId).orElseThrow(() -> new BusinessException(USER_NOT_FOUND));

        DetailedPlan detailedPlan = detailPlanRepository.findById(detailedPlanId)
                .orElseThrow(() -> new BusinessException(DETAILED_PLAN_NOT_FOUND));

        ScheduleDetails scheduleDetails = detailedPlan.getScheduleDetails();
        Schedule schedule = scheduleDetails.getSchedule();

        validateUserHasAccessToSchedule(user.getId(), schedule.getId());

        DetailedPlan swapPlan = isMoveUp(detailedPlan, isMoveUp);

        Long tempSequence = detailedPlan.getSequence();
        detailedPlan.movePlan(swapPlan.getSequence());
        swapPlan.movePlan(tempSequence);
    }

    @Transactional
    public void deleteDetailedPlan(Long detailedPlanId, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(USER_NOT_FOUND));

        DetailedPlan detailedPlan = detailPlanRepository.findById(detailedPlanId)
                .orElseThrow(() -> new BusinessException(DETAILED_PLAN_NOT_FOUND));

        validateUserHasAccessToSchedule(user.getId(), detailedPlan.getId());

        memoRepository.deleteByDetailedPlan(detailedPlan);
        detailPlanRepository.delete(detailedPlan);
    }

    private DetailedPlan isMoveUp(DetailedPlan detailedPlan, boolean isMoveUp) {
        if (isMoveUp) {
            return detailPlanRepository.findTopBySequenceGreaterThanOrderBySequenceAsc(
                            detailedPlan.getSequence())
                    .orElseThrow(() -> new BusinessException(CANNOT_MOVE_SEQUENCE));
        }
        return detailPlanRepository.findTopBySequenceLessThanOrderBySequenceDesc(
                        detailedPlan.getSequence())
                .orElseThrow(() -> new BusinessException(CANNOT_MOVE_SEQUENCE));
    }

    private void validateUserHasAccessToSchedule(Long userId, Long scheduleId) {
        scheduleGroupRepository.findByUserIdAndScheduleId(userId, scheduleId)
                .orElseThrow(() -> new BusinessException(UNAUTHORIZED_REQUEST));
    }
}
