package com.wegotoo.application.invitation;

import static com.wegotoo.exception.ErrorCode.*;

import com.wegotoo.domain.invitation.Invitation;
import com.wegotoo.domain.invitation.repository.InvitationRepository;
import com.wegotoo.domain.schedule.Schedule;
import com.wegotoo.domain.schedule.repository.ScheduleRepository;
import com.wegotoo.domain.user.User;
import com.wegotoo.domain.user.repository.UserRepository;
import com.wegotoo.exception.BusinessException;
import com.wegotoo.exception.ErrorCode;
import java.util.Date;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class InvitationService {

    private final InvitationRepository invitationRepository;
    private final UserRepository userRepository;
    private final ScheduleRepository scheduleRepository;

    public void createInvitation(Long userId, Long scheduleId) {
        User inviter = userRepository.findById(userId).orElseThrow(() -> new BusinessException(USER_NOT_FOUND));

        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new BusinessException(SCHEDULE_NOT_FOUND));

        String token = UUID.randomUUID().toString();
        Date expiryDate = new Date(System.currentTimeMillis() + (1000 * 60 * 60 * 24));

        Invitation invitation = Invitation.create(inviter, schedule, token, expiryDate);

        invitationRepository.save(invitation);

//        return "https://wegotoo.net/invitations/accept?token=" + token;
    }
}
