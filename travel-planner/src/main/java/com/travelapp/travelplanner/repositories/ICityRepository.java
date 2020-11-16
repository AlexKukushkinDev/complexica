package com.travelapp.travelplanner.repositories;

import com.travelapp.travelplanner.models.entities.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ICityRepository extends JpaRepository<City, Long> {

}