package com.travelapp.travelplanner.models.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@JacksonXmlRootElement(localName = "weather")
public class WeatherMapDTO implements Serializable {

	private static final long serialVersionUID = 1253320017739887653L;

	private String cod;

	private BigDecimal message;

	private Integer cnt;

	private CityDto city;

	@JacksonXmlProperty(localName = "list")
	@JacksonXmlElementWrapper(localName = "list", useWrapping = true)
	private List<WeatherMapTimeDTO> list;
}
