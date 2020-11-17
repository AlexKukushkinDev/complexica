package com.travelapp.travelplanner.models.entities;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@Setter
public class ForecastRecord {
    @Id
    private Long forecastId;

    @Column(name = "weatherDate")
    private LocalDate weatherDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "city", referencedColumnName = "cityId")
    private City city;

    @Column(name = "message")
    private String message;
}
