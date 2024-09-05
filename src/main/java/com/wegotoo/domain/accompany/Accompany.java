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
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Accompany {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "accompany_id")
    private Long id;

    @Column(name = "accompany_location")
    private String location;

    @Enumerated(value = EnumType.STRING)
    private Gender gender;

    @Column(name = "accompany_cost")
    private int cost;

    @Column(name = "accompany_content")
    private int content;

    @Enumerated(value = EnumType.STRING)
    private Status status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    private Accompany(String location, Gender gender, int cost, int content, Status status, User user) {
        this.location = location;
        this.gender = gender;
        this.cost = cost;
        this.content = content;
        this.status = status;
        this.user = user;
    }

}
