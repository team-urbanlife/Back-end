package com.wegotoo.domain.schedule;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "schedule_id")
    private Long id;

    @Column(name = "schedule_title",  nullable = false)
    private String title;

    @Column(name ="schedule_start_date_time",  nullable = false)
    private LocalDate startDate;

    @Column(name = "schedule_end_date_time",  nullable = false)
    private LocalDate endDate;

    @Column(name = "schedule_city",  nullable = false)
    private String city;

    @Column(name = "schedule_total_day",  nullable = false)
    private long totalTravelDays;

    @Builder
    private Schedule(String title, LocalDate startDate, LocalDate endDate, String city, long totalTravelDays) {
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.city = city;
        this.totalTravelDays = totalTravelDays;
    }

    public static Schedule create(String city, LocalDate startDay, LocalDate endDay, long totalTravelDays) {
        return Schedule.builder()
                .title(city + " 여행")
                .city(city)
                .startDate(startDay)
                .endDate(endDay)
                .totalTravelDays(totalTravelDays)
                .build();
    }

    public void edit(String title, String city, LocalDate startDate, LocalDate endDate, long totalTravelDays) {
        this.title = title != null ? title : this.title;
        this.city = city != null ? city : this.city;
        this.startDate = startDate != null ? startDate : this.startDate;
        this.endDate = endDate != null ? endDate : this.endDate;
        this.totalTravelDays = totalTravelDays;
    }
}
