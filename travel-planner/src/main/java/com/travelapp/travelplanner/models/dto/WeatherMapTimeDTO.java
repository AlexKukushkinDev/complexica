package com.travelapp.travelplanner.models.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class WeatherMapTimeDTO {

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonProperty("dt_txt")
	private LocalDateTime dt;

	private WeatherMapTimeMainDTO main;

	@JacksonXmlProperty(localName = "weather")
	@JacksonXmlElementWrapper(localName = "weather", useWrapping = true)
	private List<WeatherMapTimeWeatherDTO> weather;

	private WeatherMapTimeCloudsDTO clouds;

	private WeatherMapTimeWindDTO wind;

	@JsonIgnore
	public Boolean isDaily() {
		return (this.dt.getHour() >= 12 && this.dt.getHour() < 18);
	}

}
