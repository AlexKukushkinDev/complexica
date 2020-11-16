package com.travelapp.travelplanner.services.impl;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import com.alicp.jetcache.anno.Cached;
import com.travelapp.travelplanner.models.dto.ForecastRecordDto;
import com.travelapp.travelplanner.models.dto.WeatherDTO;
import com.travelapp.travelplanner.models.dto.WeatherMapTimeDTO;
import com.travelapp.travelplanner.models.entities.ForecastRecord;
import com.travelapp.travelplanner.models.entities.Weather;
import com.travelapp.travelplanner.repositories.ICityRepository;
import com.travelapp.travelplanner.repositories.IForecastRecordRepository;
import com.travelapp.travelplanner.repositories.IWeatherRepository;
import com.travelapp.travelplanner.services.IForecastRecordService;
import com.travelapp.travelplanner.utils.ForecastConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import springfox.documentation.spring.web.json.Json;

@Service
public class ForecastRecordServiceImpl implements IForecastRecordService {

    private final String URI = "http://api.openweathermap.org/data/2.5/forecast";
    private String AppID = "10e5cc61b984ef74b5040ea3d3216b3c";
    private final RestTemplate restTemplate;

    @Autowired
    ICityRepository cityRepository;

    @Autowired
    IForecastRecordRepository forecastRecordRepository;

    @Autowired
    IWeatherRepository weatherRepository;

    @Override
    public void save(ForecastRecordDto forecastRecordDto) {
        Optional<ForecastRecord> verifyForecastRecord = forecastRecordRepository.findById(forecastRecordDto.getForecastId());

        if (verifyForecastRecord.isPresent()) {
            throw new IllegalArgumentException("ID is duplicated");
        }

        if (forecastRecordDto.getTemperature().length > 24) {
            throw new IllegalArgumentException("More than 24 temperatures");
        }

        ForecastRecord forecastRecord = ForecastConverter.convertForecastDtoToEntity(forecastRecordDto);
        cityRepository.save(forecastRecord.getCity());
        forecastRecordRepository.save(forecastRecord);

        List<Weather> weatherList = ForecastConverter.extractTemperatures(forecastRecord, forecastRecordDto.getTemperature());
        weatherRepository.saveAll(weatherList);
    }

    @Override
    public List<ForecastRecordDto> getAll() {
        List<ForecastRecord> forecastRecords = forecastRecordRepository.findAll();
        return fetchToDto(forecastRecords);
    }

    @Override
    public List<ForecastRecordDto> getAllByWeatherDate(String sDate) {
        Date date = Date.valueOf(sDate);
        List<ForecastRecord> forecastRecords = forecastRecordRepository.findAllByWeatherDate(date);
        return fetchToDto(forecastRecords);
    }

    @Override
    public void deleteAll() {
        weatherRepository.deleteAll();
        forecastRecordRepository .deleteAll();
    }

    private List<ForecastRecordDto> fetchToDto(List<ForecastRecord> forecastRecords) {
        return forecastRecords.stream().map(
                forecast -> ForecastConverter.convertForecastEntityToDto(forecast, getTemperatures(forecast))
        ).collect(Collectors.toList());
    }

    private List<Weather> getTemperatures(ForecastRecord forecastRecord) {
        return weatherRepository.findAllByForecast(forecastRecord);
    }

    public ForecastRecordServiceImpl(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    @Cached(expire = 60, timeUnit = TimeUnit.MINUTES)
    public ResponseEntity<?> getCurrentWeather(String cityName, String weatherDate) {
        return getResponseEntity(cityName, weatherDate);
    }

    @Cached(expire = 60, timeUnit = TimeUnit.MINUTES)
    public ResponseEntity<?> getGeneralSummary(String cityName, String weatherDate) {
        List<ForecastRecordDto> list = new ArrayList<>();
        ForecastRecordDto forecastRecordDto = new ForecastRecordDto();
        Boolean isRainy = true;

        for (ForecastRecordDto item : list) {
            if (Float.parseFloat(item.getTemperature()[0]) >= 5 && isRainy) {
                forecastRecordDto.setMessage("It's raining. You should take an umbrella!");
            } else if (Float.parseFloat(item.getTemperature()[0]) < 5) {
                forecastRecordDto.setMessage("It's cold. Please take a coat!");
            }
        }
        return getResponseEntity(cityName, weatherDate);
    }

    private ResponseEntity<?> getResponseEntity(String cityName, String weatherDate) {
        List<ForecastRecordDto> result = new ArrayList<>();
        try {
            WeatherDTO weatherMap = this.restTemplate.getForObject(this.url(cityName, weatherDate), WeatherDTO.class);

            for (LocalDate reference = LocalDate.now();
                 reference.isBefore(LocalDate.now().plusDays(2));
                 reference = reference.plusDays(1)) {
                final LocalDate ref = reference;
                List<WeatherMapTimeDTO> weatherMapTimeDTOList = weatherMap.getList().stream()
                        .filter(x -> x.getDt().toLocalDate().equals(ref)).collect(Collectors.toList());
                if (!CollectionUtils.isEmpty(weatherMapTimeDTOList)) {
                    result.add(this.collect(weatherMapTimeDTOList));
                }

            }
        } catch (HttpClientErrorException e) {
            return new ResponseEntity<>(new Json(e.getResponseBodyAsString()), e.getStatusCode());
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    private ForecastRecordDto collect(List<WeatherMapTimeDTO> list) {
        ForecastRecordDto result = new ForecastRecordDto();

        for (WeatherMapTimeDTO item : list) {
            result.setWeatherDate(item.getDt().toLocalDate().toString());
        }

        return result;
    }

    private String url(String cityName, String weatherDate) {
        return String.format(URI.concat("?q=%s").concat("&appid=%s").concat("&units=metric"), cityName, weatherDate, AppID);
    }
}