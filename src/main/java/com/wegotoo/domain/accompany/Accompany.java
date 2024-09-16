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

    @Column(name = "accompany_content")
    private String content;

    @Enumerated(value = EnumType.STRING)
    private Gender gender;

    @Enumerated(value = EnumType.STRING)
    private Status status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    private Accompany(int cost, String location, String content, Gender gender, Status status, User user) {
        this.cost = cost;
        this.location = location;
        this.content = content;
        this.gender = gender;
        this.status = status;
        this.user = user;
    }

}
