package com.travelapp.travelplanner.services;

import java.util.List;

import com.travelapp.travelplanner.models.dto.ForecastRecordDTO;
import org.springframework.http.ResponseEntity;

public interface IForecastRecordService {

    void save(ForecastRecordDTO forecastRecordDto);
    List<ForecastRecordDTO> getAll();
    List<ForecastRecordDTO> getAllByWeatherDate(String weatherDate);
    void deleteAll();
    ResponseEntity getCurrentWeather(String cityName, String weatherDate);
    ResponseEntity getGeneralSummary(String cityName, String weatherDate);
}