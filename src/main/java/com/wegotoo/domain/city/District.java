package com.wegotoo.domain.city;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class District {

    private String region;

    private Double latitude;

    private Double longitude;

    @Builder
    private District(String region, Double latitude, Double longitude) {
        this.region = region;
        this.latitude = latitude;
        this.longitude = longitude;
    }

}
