package com.wegotoo.domain.schedule;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ScheduleDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "schedule_details_id")
    private Long id;

    @Column(name = "schedule_details_day",  nullable = false)
    private LocalDate date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id")
    private Schedule schedule;

    @Builder
    private ScheduleDetails(LocalDate date, Schedule schedule) {
        this.date = date;
        this.schedule = schedule;
    }

    public static ScheduleDetails create(LocalDate date, Schedule schedule) {
        return ScheduleDetails.builder()
                .date(date)
                .schedule(schedule)
                .build();
    }
}
