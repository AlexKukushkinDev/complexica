package com.travelapp.travelplanner.models.dto;

import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;

@Getter
@Setter
public class CityDto implements Serializable {
    private String name;
    private String country;
}
