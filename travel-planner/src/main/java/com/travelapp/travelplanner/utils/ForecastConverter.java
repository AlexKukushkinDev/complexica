package com.travelapp.travelplanner.utils;

import com.travelapp.travelplanner.models.dto.CityDto;
import com.travelapp.travelplanner.models.dto.ForecastRecordDTO;
import com.travelapp.travelplanner.models.entities.City;
import com.travelapp.travelplanner.models.entities.ForecastRecord;
import com.travelapp.travelplanner.models.entities.Weather;
import org.modelmapper.ModelMapper;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

public abstract class ForecastConverter {
    private ForecastConverter() throws IllegalAccessException {
        throw new IllegalAccessException("Private Constructor");
    }

    public static ForecastRecord convertForecastDtoToEntity(ForecastRecordDTO forecastRecordDto) {
        ForecastRecord forecastRecord = new ForecastRecord();

        forecastRecord.setForecastId(forecastRecordDto.getForecastId());
        forecastRecord.setWeatherDate(forecastRecordDto.getDate());
        forecastRecord.getCity().setCityName(forecastRecordDto.getCityName());
        forecastRecord.getCity().setCountryCode(forecastRecordDto.getCountryCode());

        return forecastRecord;
    }

    public static ForecastRecordDTO convertForecastEntityToDto(ForecastRecord forecastRecord, List<Weather> weatherList) {
        ForecastRecordDTO forecastRecordDto = new ForecastRecordDTO();

        forecastRecordDto.setForecastId(forecastRecord.getForecastId());
        forecastRecordDto.setDate(forecastRecord.getWeatherDate());
        forecastRecordDto.setCityName(forecastRecord.getCity().getCityName());
        forecastRecordDto.setCountryCode(forecastRecord.getCity().getCountryCode());
        forecastRecordDto.setTemperature(fetchTemperaturesToArray(weatherList));

        return forecastRecordDto;
    }

    private static City fetchCityDtoToEntity(CityDto cityDto) {
        ModelMapper modelMapper = new ModelMapper();
        return  modelMapper.map(cityDto, City.class);
    }

    private static CityDto fetchCityEntityToDto(City city) {
        CityDto cityDto = new CityDto();
        cityDto.setName(city.getCityName());
        cityDto.setCountry(city.getCountryCode());

        return cityDto;
    }

    private static List<String> fetchTemperaturesToArray(List<Weather> weatherList) {
        NumberFormat formatter = new DecimalFormat("#0.0");
        return weatherList.stream()
                .map(Weather::getTemperature)
                .map(formatter::format)
                .collect(Collectors.toList());
    }

    private static BiFunction<ForecastRecord, Float, Weather> createWeather = Weather::new;

    public static List<Weather> extractTemperatures(ForecastRecord forecastRecord, List<String> temps) {

        return temps.stream().map(
                temp -> createWeather.apply(forecastRecord, Float.parseFloat(temp))
        ).collect(Collectors.toList());
    }
}
