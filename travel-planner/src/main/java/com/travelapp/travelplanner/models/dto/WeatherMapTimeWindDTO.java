package com.travelapp.travelplanner.models.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class WeatherMapTimeWindDTO {

	private BigDecimal speed;
	private BigDecimal deg;
}
