package com.travelapp.travelplanner.services;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import com.travelapp.travelplanner.models.dto.ForecastRecordDto;
import com.travelapp.travelplanner.models.entities.ForecastRecord;
import com.travelapp.travelplanner.models.entities.Weather;
import org.json.JSONException;
import org.springframework.http.ResponseEntity;

public interface IForecastRecordService {

    void save(ForecastRecordDto forecastRecordDto);
    List<ForecastRecordDto> getAll();
    List<ForecastRecordDto> getAllByWeatherDate(String weatherDate);
    void deleteAll();
    ResponseEntity getCurrentWeather(String cityName, String weatherDate);
    ResponseEntity getGeneralSummary(String cityName, String weatherDate);
}