package com.wegotoo.domain.accompany;

import com.wegotoo.domain.user.User;
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
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Accompany {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "accompany_id")
    private Long id;

    @Column(name = "accompany_cost")
    private int cost;

    @Column(name = "accompany_location")
    private String location;

    @Column(name = "accompany_title")
    private String title;

    @Column(name = "accompany_content")
    private String content;

    @Enumerated(value = EnumType.STRING)
    private Gender gender;

    @Enumerated(value = EnumType.STRING)
    private Status status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "accompany_latitude")
    private Double latitude;

    @Column(name = "accompany_longitude")
    private Double longitude;

    @Column(name = "accompany_personnel")
    private int personnel;

    @Column(name = "accompany_start_age")
    private int startAge;

    @Column(name = "accompany_end_age")
    private int endAge;
    
    @Column(name = "accompany_start_date")
    private LocalDate startDate;
    
    @Column(name = "accompany_end_date")
    private LocalDate endDate;

    @Builder
    private Accompany(int cost, String location, String title, String content, Gender gender, Status status, User user,
                      Double latitude, Double longitude, int personnel, int startAge, int endAge, LocalDate startDate, LocalDate endDate) {
        this.cost = cost;
        this.location = location;
        this.title = title;
        this.content = content;
        this.gender = gender;
        this.status = status;
        this.user = user;
        this.latitude = latitude;
        this.longitude = longitude;
        this.personnel = personnel;
        this.startAge = startAge;
        this.endAge = endAge;
        this.startDate = startDate;
        this.endDate = endDate;
    }

}
