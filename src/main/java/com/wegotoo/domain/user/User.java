package com.wegotoo.domain.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "user_email")
    private String email;

    @Column(name = "user_name")
    private String name;

    @Column(name = "user_latitude")
    private double latitude;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_role")
    private Role role;

    @Column(name = "user_profile_image")
    private String profileImage;

    @Column(name = "user_refresh_token")
    private String refreshToken;

    @Builder
    private User(String email, String name, double latitude, Role role, String profileImage, String refreshToken) {
        this.email = email;
        this.name = name;
        this.latitude = latitude;
        this.role = role;
        this.profileImage = profileImage;
        this.refreshToken = refreshToken;
    }

    public User updateNameAndProfileImage(String name, String profileImage) {
        this.name = name;
        this.profileImage = profileImage;

        return this;
    }

    public User updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;

        return this;
    }

    public void deleteRefreshToken() {
        this.refreshToken = null;
    }

}
