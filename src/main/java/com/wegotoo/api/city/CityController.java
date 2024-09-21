package com.wegotoo.api.city;

import com.wegotoo.api.ApiResponse;
import com.wegotoo.application.city.CityService;
import com.wegotoo.application.city.response.CityResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class CityController {

    private final CityService cityService;

    @GetMapping("/v1/cities")
    public ApiResponse<List<CityResponse>> getAllCities() {
        return ApiResponse.ok(cityService.findAll());
    }

}
