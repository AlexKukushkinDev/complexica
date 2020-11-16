package com.travelapp.travelplanner.utils;

import com.travelapp.travelplanner.models.dto.CityDto;
import com.travelapp.travelplanner.models.dto.ForecastRecordDto;
import com.travelapp.travelplanner.models.entities.City;
import com.travelapp.travelplanner.models.entities.ForecastRecord;
import com.travelapp.travelplanner.models.entities.Weather;
import org.modelmapper.ModelMapper;

import java.sql.Date;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

public abstract class ForecastConverter {
    private ForecastConverter() throws IllegalAccessException {
        throw new IllegalAccessException("Private Constructor");
    }

    public static ForecastRecord convertForecastDtoToEntity(ForecastRecordDto forecastRecordDto) {
        ForecastRecord forecastRecord = new ForecastRecord();

        forecastRecord.setForecastId(forecastRecordDto.getForecastId());
        forecastRecord.setWeatherDate(Date.valueOf(forecastRecordDto.getWeatherDate()));
        forecastRecord.setCity(fetchCityDtoToEntity(forecastRecordDto.getCity()));

        return forecastRecord;
    }

    public static ForecastRecordDto convertForecastEntityToDto(ForecastRecord forecastRecord, List<Weather> weatherList) {
        ForecastRecordDto forecastRecordDto = new ForecastRecordDto();

        forecastRecordDto.setForecastId(forecastRecord.getForecastId());
        forecastRecordDto.setWeatherDate(new SimpleDateFormat("yyyy-MM-dd").format(forecastRecord.getWeatherDate()));
        forecastRecordDto.setCity(fetchCityEntityToDto(forecastRecord.getCity()));
        forecastRecordDto.setTemperature(fetchTemperaturesToArray(weatherList));

        return forecastRecordDto;
    }

    private static City fetchCityDtoToEntity(CityDto cityDto) {
        ModelMapper modelMapper = new ModelMapper();
        return  modelMapper.map(cityDto, City.class);
    }

    private static CityDto fetchCityEntityToDto(City city) {
        CityDto cityDto = new CityDto();
        cityDto.setCityName(city.getCityName());
        cityDto.setCountryCode(city.getCountryCode());

        return cityDto;
    }

    private static String[] fetchTemperaturesToArray(List<Weather> weatherList) {
        NumberFormat formatter = new DecimalFormat("#0.0");
        return weatherList.stream()
                .map(Weather::getTemperature)
                .map(formatter::format)
                .collect(Collectors.toList()).toArray(new String[0]);
    }

    private static BiFunction<ForecastRecord, Float, Weather> createWeather = Weather::new;

    public static List<Weather> extractTemperatures(ForecastRecord forecastRecord, String[] temps) {
        return Arrays.stream(temps).map(
                temp -> createWeather.apply(forecastRecord, Float.parseFloat(temp))
        ).collect(Collectors.toList());
    }
}
