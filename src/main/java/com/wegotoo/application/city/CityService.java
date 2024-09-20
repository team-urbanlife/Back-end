package com.wegotoo.application.city;

import com.wegotoo.application.city.response.CityResponse;
import com.wegotoo.domain.city.City;
import com.wegotoo.domain.city.repository.CityRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CityService {

    private final CityRepository cityRepository;

    public List<CityResponse> findAll() {
        List<City> cities = cityRepository.findAll();

        return CityResponse.toList(cities);
    }

}
