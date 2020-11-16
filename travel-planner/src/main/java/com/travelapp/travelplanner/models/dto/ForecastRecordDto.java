package com.travelapp.travelplanner.models.dto;

import lombok.Getter;
import lombok.Setter;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
public class ForecastRecordDto implements Serializable {
    @NotNull
    private Long forecastId;

    @NotNull
    private String weatherDate;

    @NotNull
    private CityDto city;

    @NotNull
    private String[] temperature;

    @NotNull
    private String message;
}
