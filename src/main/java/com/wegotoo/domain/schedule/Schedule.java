package com.wegotoo.domain.schedule;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "schedule_id")
    private Long id;

    @Column(name = "schedule_title")
    private String title;

    @Column(name ="schedule_start_date_time")
    private LocalDateTime startDateTime;

    @Column(name = "schedule_end_date_time")
    private LocalDateTime endDateTime;

    @Column(name = "schedule_city")
    private String city;

    @Builder
    private Schedule(String title, LocalDateTime startDateTime, LocalDateTime endDateTime, String city) {
        this.title = title;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.city = city;
    }

}
