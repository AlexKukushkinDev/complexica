package com.travelapp.travelplanner.services.impl;

import java.util.List;

import com.travelapp.travelplanner.models.entities.City;
import com.travelapp.travelplanner.repositories.ICityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CityServiceImpl {

    @Autowired
    private ICityRepository cityRepository;

    public void addCity(City city) {
        cityRepository.save(city);
    }

    public List<City> getAllCities() {
        return cityRepository.findAll();
    }
}