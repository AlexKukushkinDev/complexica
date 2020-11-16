package com.travelapp.travelplanner.models.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@JacksonXmlRootElement(localName = "weather")
public class WeatherDTO implements Serializable {
	@NotNull
	private Long weatherId;

	@NotNull
	private String weatherDate;

	@NotNull
	private CityDto city;

	@NotNull
	private String cod;

	@NotNull
	private String[] temperature;

	@NotNull
	private String message;

	@JacksonXmlProperty(localName = "list")
	@JacksonXmlElementWrapper(localName = "list", useWrapping = true)
	private List<WeatherMapTimeDTO> list;
}
