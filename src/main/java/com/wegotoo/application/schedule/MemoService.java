package com.wegotoo.application.schedule;

import static com.wegotoo.exception.ErrorCode.*;

import com.wegotoo.application.schedule.request.MemoWriteServiceRequest;
import com.wegotoo.domain.schedule.DetailedPlan;
import com.wegotoo.domain.schedule.Memo;
import com.wegotoo.domain.schedule.Schedule;
import com.wegotoo.domain.schedule.ScheduleDetails;
import com.wegotoo.domain.schedule.repository.DetailPlanRepository;
import com.wegotoo.domain.schedule.repository.MemoRepository;
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
public class MemoService {

    private final MemoRepository memoRepository;
    private final DetailPlanRepository detailPlanRepository;
    private final UserRepository userRepository;
    private final ScheduleGroupRepository scheduleGroupRepository;

    @Transactional
    public void writeMemo(Long userId, Long detailedPlanId, MemoWriteServiceRequest request) {
        User user = userRepository.findById(userId).orElseThrow(() -> new BusinessException(USER_NOT_FOUND));

        DetailedPlan detailedPlan = detailPlanRepository.findById(detailedPlanId)
                .orElseThrow(() -> new BusinessException(DETAILED_PLAN_NOT_FOUND));

        ScheduleDetails scheduleDetails = detailedPlan.getScheduleDetails();
        Schedule schedule = scheduleDetails.getSchedule();

        scheduleGroupRepository.findByUserIdAndScheduleId(user.getId(), schedule.getId())
                .orElseThrow(() -> new BusinessException(UNAUTHORIZED_REQUEST));

        Memo memo = Memo.write(request.getContent(), detailedPlan);
        memoRepository.save(memo);
    }

}

