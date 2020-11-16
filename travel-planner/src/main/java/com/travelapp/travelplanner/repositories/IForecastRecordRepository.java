package com.travelapp.travelplanner.repositories;

import com.travelapp.travelplanner.models.entities.ForecastRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.sql.Date;

public interface IForecastRecordRepository extends JpaRepository<ForecastRecord, Long> {
    List<ForecastRecord> findAllByWeatherDate(Date weatherDate);
}
