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
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DetailedPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "detailed_plan_id")
    private Long Id;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "detailed_plan_type")
    private Type type;

    @Column(name = "detailed_plan_name")
    private String name;

    @Column(name = "detailed_plan_latitude")
    private double latitude;

    @Column(name = "detailed_plan_longitude")
    private double longitude;

    @Column(name = "detailed_plan_memo")
    private String memo;

    @Column(name = "detailed_plan_order")
    private int order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_details_id")
    private ScheduleDetails scheduleDetails;

    @Builder
    private DetailedPlan(Type type, String name, double latitude, double longitude, String memo, int order,
                        ScheduleDetails scheduleDetails) {
        this.type = type;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.memo = memo;
        this.order = order;
        this.scheduleDetails = scheduleDetails;
    }

}
