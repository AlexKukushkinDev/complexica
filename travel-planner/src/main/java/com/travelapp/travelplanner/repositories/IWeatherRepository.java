package com.travelapp.travelplanner.repositories;

import com.travelapp.travelplanner.models.entities.ForecastRecord;
import com.travelapp.travelplanner.models.entities.Weather;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface IWeatherRepository extends JpaRepository<Weather, Long> {
    List<Weather> findAllByForecast(ForecastRecord forecast);
}
