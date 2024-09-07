package com.wegotoo.domain.schedule;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DetailedPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "detailed_plan_id")
    private Long Id;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "detailed_plan_type",  nullable = false)
    private Type type;

    @Column(name = "detailed_plan_name")
    private String name;

    @Column(name = "detailed_plan_latitude",  nullable = false)
    private Double latitude;

    @Column(name = "detailed_plan_longitude",  nullable = false)
    private Double longitude;

    @Column(name = "detailed_plan_memo")
    private String memo;

    @Column(name = "detailed_plan_sequence",  nullable = false)
    private Long sequence;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_details_id")
    private ScheduleDetails scheduleDetails;

    @Builder
    private DetailedPlan(Type type, String name, Double latitude, Double longitude, String memo, Long sequence,
                         ScheduleDetails scheduleDetails) {
        this.type = type;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.memo = memo;
        this.sequence = sequence;
        this.scheduleDetails = scheduleDetails;
    }

    public static DetailedPlan create(Type type, String name, String memo, Double latitude, Double longitude, Long sequence,
                                      ScheduleDetails scheduleDetails) {
        return DetailedPlan.builder()
                .type(type)
                .name(name)
                .memo(memo)
                .latitude(latitude)
                .longitude(longitude)
                .sequence(sequence + 1)
                .scheduleDetails(scheduleDetails)
                .build();
    }
}
