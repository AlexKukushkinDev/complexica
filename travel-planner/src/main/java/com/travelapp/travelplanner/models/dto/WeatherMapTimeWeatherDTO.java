package com.travelapp.travelplanner.models.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class WeatherMapTimeWeatherDTO {

	private Integer id;

	private String main;

	private String description;
}
