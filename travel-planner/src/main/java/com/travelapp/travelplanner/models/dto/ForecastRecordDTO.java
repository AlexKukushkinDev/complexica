package com.travelapp.travelplanner.models.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ForecastRecordDTO implements Serializable {

    @JsonIgnore
    private Long forecastId;

    @NotNull
    private List<String> temperature;

    @JsonIgnore
    private String message;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate date;

    private BigDecimal daily;

    private BigDecimal nightly;

    private BigDecimal pressure;

    private String weatherDescription;

    private String cityName;

    private String countryCode;

    @JsonIgnore
    private BigDecimal totalDaily;

    @JsonIgnore
    private Integer quantDaily;

    @JsonIgnore
    private BigDecimal totalNightly;

    @JsonIgnore
    private Integer quantNightly;

    @JsonIgnore
    private BigDecimal totalPressure;

    @JsonIgnore
    private Integer quantPressure;

    public ForecastRecordDTO() {
        this.totalDaily = BigDecimal.ZERO;
        this.totalNightly = BigDecimal.ZERO;
        this.totalPressure = BigDecimal.ZERO;
        this.quantDaily = 0;
        this.quantNightly = 0;
        this.quantPressure = 0;
    }

    public void plusMap(WeatherMapTimeDTO map) {
        for (WeatherMapTimeWeatherDTO weather: map.getWeather()) {
            if (!weather.getDescription().isEmpty()) {
                this.weatherDescription = weather.getDescription();
            }
        }
        if (map.isDaily()) {
            this.totalDaily = this.totalDaily.add(map.getMain().getTemp());
            this.quantDaily++;
        } else {
            this.totalNightly = this.totalNightly.add(map.getMain().getTemp());
            this.quantNightly++;
        }
        this.totalPressure = this.totalPressure.add(map.getMain().getTemp());
        this.quantPressure++;
    }

    public void totalize() {
        this.daily = (this.quantDaily > 0)
                ? this.totalDaily.divide(new BigDecimal(this.quantDaily.toString()), 2, RoundingMode.HALF_UP)
                : null;
        this.nightly = (this.quantNightly > 0)
                ? this.totalNightly.divide(new BigDecimal(this.quantNightly.toString()), 2, RoundingMode.HALF_UP)
                : null;
        this.pressure = (this.quantPressure > 0)
                ? this.totalPressure.divide(new BigDecimal(this.quantPressure.toString()), 2, RoundingMode.HALF_UP)
                : null;

        this.temperature = new ArrayList<>();
        String averageTemperature = String.valueOf(this.daily.add(this.nightly).divide(BigDecimal.valueOf(2)).intValue());
        this.temperature.add(averageTemperature);
    }
}
