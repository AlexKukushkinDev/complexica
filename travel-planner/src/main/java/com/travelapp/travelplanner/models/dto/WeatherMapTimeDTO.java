package com.travelapp.travelplanner.models.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class WeatherMapTimeDTO {

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonProperty("localDateTime")
	private LocalDateTime dt;

	@JsonIgnore
	public Boolean isDaily() {
		return (this.dt.getHour() >= 12 && this.dt.getHour() < 18);
	}
}
