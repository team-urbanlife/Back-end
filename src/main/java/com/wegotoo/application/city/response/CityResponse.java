package com.wegotoo.application.city.response;

import com.wegotoo.domain.city.City;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CityResponse {

    private Long cityId;

    private String region;

    private Double latitude;

    private Double longitude;

    @Builder
    private CityResponse(Long cityId, String region, Double latitude, Double longitude) {
        this.cityId = cityId;
        this.region = region;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public static List<CityResponse> toList(List<City> cities) {
        return cities.stream()
                .map(CityResponse::of)
                .toList();
    }

    private static CityResponse of(City city) {
        return CityResponse.builder()
                .cityId(city.getId())
                .region(city.getDistrict().getRegion())
                .latitude(city.getDistrict().getLatitude())
                .longitude(city.getDistrict().getLongitude())
                .build();
    }
}
