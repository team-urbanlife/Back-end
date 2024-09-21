package com.wegotoo.domain.invitation;

import com.wegotoo.domain.schedule.Schedule;
import com.wegotoo.domain.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.util.Date;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Invitation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "invitation_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @Column(name = "invitation_inviter")
    private User inviter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id")
    private Schedule schedule;

    @Column(name = "invitation_token")
    private String token;

    @Column(name = "invitation_accepted")
    private boolean accepted;

    @Column(name = "invitation_expired")
    private boolean expired;

    @Temporal(TemporalType.TIMESTAMP)
    private Date expiredAt;

    @Builder
    private Invitation(User inviter, Schedule schedule, String token, boolean accepted, boolean expired,
                      Date expiredAt) {
        this.inviter = inviter;
        this.schedule = schedule;
        this.token = token;
        this.accepted = accepted;
        this.expired = expired;
        this.expiredAt = expiredAt;
    }

    public static Invitation create(User inviter, Schedule schedule, String token, Date expiryDate) {
        return Invitation.builder()
                .inviter(inviter)
                .schedule(schedule)
                .token(token)
                .accepted(false)
                .expired(false)
                .expiredAt(expiryDate)
                .build();
    }

}
