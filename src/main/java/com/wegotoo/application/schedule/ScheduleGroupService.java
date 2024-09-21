package com.wegotoo.application.schedule;

import static com.wegotoo.exception.ErrorCode.*;

import com.wegotoo.domain.invitation.Invitation;
import com.wegotoo.domain.invitation.repository.InvitationRepository;
import com.wegotoo.domain.schedule.Schedule;
import com.wegotoo.domain.schedule.ScheduleGroup;
import com.wegotoo.domain.schedule.repository.ScheduleGroupRepository;
import com.wegotoo.domain.schedule.repository.ScheduleRepository;
import com.wegotoo.domain.user.User;
import com.wegotoo.domain.user.repository.UserRepository;
import com.wegotoo.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class ScheduleGroupService {

    private final ScheduleGroupRepository scheduleGroupRepository;
    private final UserRepository userRepository;
    private final InvitationRepository invitationRepository;

    @Transactional
    public void addScheduleGroupToUser(Long userId, String token) {
        User user = userRepository.findById(userId).orElseThrow(() -> new BusinessException(USER_NOT_FOUND));

        Invitation invitation = invitationRepository.findByToken(token)
                .orElseThrow(() -> new BusinessException(INVITATION_INVALID));

        Schedule schedule = invitation.getSchedule();

        ScheduleGroup scheduleGroup = ScheduleGroup.create(user, schedule);
        scheduleGroupRepository.save(scheduleGroup);
        invitationRepository.delete(invitation);
    }
}
