package com.travelapp.travelplanner.models.entities;

import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;

@Entity
@Getter
@Setter
public class Weather {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long weatherId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "forecast", referencedColumnName = "forecastId")
    private ForecastRecord forecast;

    @Column
    private Float temperature;

    public Weather() {
    }

    public Weather(ForecastRecord forecastRecord, Float temperature) {
        this.forecast = forecastRecord;
        this.temperature = temperature;
    }
}
